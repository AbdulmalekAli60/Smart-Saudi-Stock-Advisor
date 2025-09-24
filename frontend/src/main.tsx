import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.tsx";
import { BrowserRouter } from "react-router-dom";
import { scan } from "react-scan";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

if (process.env.NODE_ENV === 'development') {
  const script = document.createElement('script');
  script.src = 'http://localhost:8097';
  document.head.appendChild(script);
}
const queryClinet = new QueryClient();

scan({
  enabled: true,
});

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClinet}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </QueryClientProvider>
  </StrictMode>
);
