import { useCallback, useEffect, useState } from 'react';
import BankService, { getErrorMessage } from './services/BankService';
import AccountList from './components/AccountList';
import TransferForm from './components/TransferForm';
import TransactionHistory from './components/TransactionHistory';
import TransactionResult from './components/TransactionResult';
import Toast from './components/Toast';

/** Root component: loads data and wires the child components together. */
export default function App() {
  const [accounts, setAccounts] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [filter, setFilter] = useState('');
  const [result, setResult] = useState(null);
  const [toast, setToast] = useState({ message: '', type: 'error' });

  const showError = (message) => setToast({ message, type: 'error' });
  const closeToast = useCallback(() => setToast((t) => ({ ...t, message: '' })), []);

  const loadAccounts = useCallback(async () => {
    try { setAccounts(await BankService.getAccounts()); } catch (e) { showError(getErrorMessage(e)); }
  }, []);

  const loadTransactions = useCallback(async () => {
    try { setTransactions(await BankService.getTransactions(filter)); } catch (e) { showError(getErrorMessage(e)); }
  }, [filter]);

  useEffect(() => { loadAccounts(); }, [loadAccounts]);
  useEffect(() => { loadTransactions(); }, [loadTransactions]);

  const handleTransferDone = (transaction) => {
    setResult(transaction);
    loadAccounts();
    loadTransactions();
  };

  return (
    <main className="container">
      <header>
        <h1>Banco TdeA · Transferencias</h1>
        <p className="muted">Traslada dinero entre cuentas y consulta el histórico.</p>
      </header>
      <div className="grid">
        <TransferForm accounts={accounts} onSuccess={handleTransferDone} onError={showError} />
        <AccountList accounts={accounts} />
      </div>
      <TransactionHistory transactions={transactions} accounts={accounts} filter={filter}
        onFilterChange={setFilter} onSelect={setResult} />
      <TransactionResult transaction={result} onClose={() => setResult(null)} />
      <Toast message={toast.message} type={toast.type} onClose={closeToast} />
    </main>
  );
}
