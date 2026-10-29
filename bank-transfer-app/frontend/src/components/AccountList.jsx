import { formatMoney } from '../utils/format';

/** Table with the accounts and their current balance. */
export default function AccountList({ accounts }) {
  return (
    <section className="card">
      <h2>Cuentas</h2>
      <p className="muted">Cuentas disponibles y saldo actual.</p>
      <table className="table">
        <thead>
          <tr><th>Número</th><th>Titular</th><th className="right">Saldo</th></tr>
        </thead>
        <tbody>
          {accounts.map((a) => (
            <tr key={a.id}>
              <td>{a.accountNumber}</td><td>{a.ownerName}</td><td className="right">{formatMoney(a.balance)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
