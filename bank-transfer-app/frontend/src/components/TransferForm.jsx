import { useState } from 'react';
import BankService, { getErrorMessage } from '../services/BankService';
import './TransferForm.css';

const EMPTY = { sourceAccountNumber: '', targetAccountNumber: '', amount: '', description: '' };

/** Controlled form that sends a transfer. All fields except description are required. */
export default function TransferForm({ accounts, onSuccess, onError }) {
  const [form, setForm] = useState(EMPTY);
  const [sending, setSending] = useState(false);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.sourceAccountNumber === form.targetAccountNumber) {
      onError('La cuenta origen y destino deben ser diferentes.');
      return;
    }
    setSending(true);
    try {
      const transaction = await BankService.transfer({ ...form, amount: Number(form.amount) });
      setForm(EMPTY);
      onSuccess(transaction);
    } catch (err) {
      onError(getErrorMessage(err));
    } finally {
      setSending(false);
    }
  };

  const options = accounts.map((a) => (
    <option key={a.id} value={a.accountNumber}>{a.accountNumber} · {a.ownerName}</option>
  ));

  return (
    <section className="card">
      <h2>Nueva transferencia</h2>
      <p className="muted">Completa los campos para trasladar dinero entre cuentas.</p>
      <form onSubmit={handleSubmit}>
        <label>Cuenta origen
          <select value={form.sourceAccountNumber} onChange={update('sourceAccountNumber')} required>
            <option value="">Seleccione la cuenta origen</option>{options}
          </select>
        </label>
        <label>Cuenta destino
          <select value={form.targetAccountNumber} onChange={update('targetAccountNumber')} required>
            <option value="">Seleccione la cuenta destino</option>{options}
          </select>
        </label>
        <div className="row">
          <label>Monto
            <input type="number" min="0.01" step="0.01" placeholder="0.00" value={form.amount} onChange={update('amount')} required />
          </label>
          <label>Descripción (opcional)
            <input maxLength={140} placeholder="Ej: pago arriendo" value={form.description} onChange={update('description')} />
          </label>
        </div>
        <button type="submit" className="btn" disabled={sending}>{sending ? 'Enviando…' : 'Transferir'}</button>
      </form>
    </section>
  );
}
