import { formatMoney, formatDate } from '../utils/format';
import './TransactionHistory.css';

/** Table with the transaction history and an account filter. */
export default function TransactionHistory({ transactions, accounts, filter, onFilterChange, onSelect }) {
  return (
    <section className="card">
      <div className="history-header">
        <div>
          <h2>Histórico de transacciones</h2>
          <p className="muted">Movimientos registrados, del más reciente al más antiguo.</p>
        </div>
        <select value={filter} onChange={(e) => onFilterChange(e.target.value)} aria-label="Filtrar por cuenta">
          <option value="">Todas las cuentas</option>
          {accounts.map((a) => <option key={a.id} value={a.accountNumber}>{a.accountNumber} · {a.ownerName}</option>)}
        </select>
      </div>
      <div className="scroll">
        <table className="table">
          <thead>
            <tr><th>Fecha</th><th>Origen</th><th>Destino</th><th className="right">Monto</th><th>Estado</th><th>QR</th></tr>
          </thead>
          <tbody>
            {transactions.length === 0 && <tr><td colSpan="6" className="empty">Aún no hay transacciones.</td></tr>}
            {transactions.map((t) => (
              <tr key={t.id}>
                <td>{formatDate(t.createdAt)}</td>
                <td>{t.sourceOwnerName}<br /><span className="muted small">{t.sourceAccountNumber}</span></td>
                <td>{t.targetOwnerName}<br /><span className="muted small">{t.targetAccountNumber}</span></td>
                <td className="right">{formatMoney(t.amount)}</td>
                <td><span className="badge">{t.status}</span></td>
                <td><button className="btn secondary tiny" onClick={() => onSelect(t)}>Ver QR</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
