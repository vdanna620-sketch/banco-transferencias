import { useEffect } from 'react';
import './Toast.css';

/** Floating message that closes itself after 5 seconds. type: 'error' | 'success' */
export default function Toast({ message, type = 'error', onClose }) {
  useEffect(() => {
    if (!message) return undefined;
    const timer = setTimeout(onClose, 5000);
    return () => clearTimeout(timer);
  }, [message, onClose]);

  if (!message) return null;
  return (
    <div className={`toast toast-${type}`} role="alert">
      <span>{message}</span>
      <button onClick={onClose} aria-label="Cerrar">×</button>
    </div>
  );
}
