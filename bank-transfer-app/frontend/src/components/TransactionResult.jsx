import { QRCodeSVG } from 'qrcode.react';
import { formatMoney, formatDate } from '../utils/format';
import './TransactionResult.css';

/** Builds the compact JSON text that is encoded inside the QR code. */
export function buildQrPayload(tx) {
  return JSON.stringify({
    ref: tx.reference,
    from: tx.sourceAccountNumber,
    to: tx.targetAccountNumber,
    amount: tx.amount,
    status: tx.status,
    date: tx.createdAt,
  });
}

/** Modal that shows the result of a transfer together with its QR code. */
export default function TransactionResult({ transaction, onClose }) {
  if (!transaction) return null;
  return (
    <div className="overlay" onClick={onClose}>
      <div className="modal" onClick={(e) => e.stopPropagation()}>
        <div className="check">✓</div>
        <h2>Transferencia exitosa</h2>
        <p className="amount">{formatMoney(transaction.amount)}</p>
        <div className="qr"><QRCodeSVG value={buildQrPayload(transaction)} size={168} level="M" /></div>
        <p className="muted small">Escanea el código para ver los datos de la transacción.</p>
        <dl>
          <dt>Referencia</dt><dd className="mono">{transaction.reference}</dd>
          <dt>Origen</dt><dd>{transaction.sourceOwnerName} · {transaction.sourceAccountNumber}</dd>
          <dt>Destino</dt><dd>{transaction.targetOwnerName} · {transaction.targetAccountNumber}</dd>
          <dt>Fecha</dt><dd>{formatDate(transaction.createdAt)}</dd>
        </dl>
        <button className="btn" onClick={onClose}>Cerrar</button>
      </div>
    </div>
  );
}
