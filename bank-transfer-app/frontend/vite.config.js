import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Port 3000 matches the origin allowed by the backend CORS filter.
export default defineConfig({
  plugins: [react()],
  server: { port: 3000 },
});
