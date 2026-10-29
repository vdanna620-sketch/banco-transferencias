import axios from 'axios';

// Backend URL comes from an environment variable (see .env).
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' },
});

/** Extracts a readable message from an axios error. */
export function getErrorMessage(error) {
  return error.response?.data?.message ?? 'No se pudo conectar con el servidor.';
}

/** Service that wraps every call to the Spring Boot API. */
const BankService = {
  /** @returns {Promise<Array>} all accounts */
  getAccounts: () => api.get('/accounts').then((r) => r.data),

  /** @param {{sourceAccountNumber: string, targetAccountNumber: string, amount: number, description: string}} data */
  transfer: (data) => api.post('/transfers', data).then((r) => r.data),

  /** @param {string} [accountNumber] optional filter @returns {Promise<Array>} history */
  getTransactions: (accountNumber) =>
    api.get('/transactions', { params: accountNumber ? { accountNumber } : {} }).then((r) => r.data),
};

export default BankService;
