import "./App.css";
import AppRoutes from "./components/AppRoutes";
import ErrorBoundary from "./components/ErrorBoundary";
import ErrorComponent from "./components/ErrorComponent";

function App() {
  return (
    <ErrorBoundary fallback={<ErrorComponent />}>
      <AppRoutes />
    </ErrorBoundary>
  );
}

export default App;
