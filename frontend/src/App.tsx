import "./App.css";
import AppRoutes from "./components/AppRoutes";
import ErrorBoundary from "./components/ErrorBoundary";

function App() {
  return (
    <ErrorBoundary fallback={<div>Errrrror</div>}>
      <AppRoutes />
    </ErrorBoundary>
  );
}

export default App;
