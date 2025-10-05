import { Component, ErrorInfo } from "react";

interface ErrorBoundaryProps {
  children: React.ReactNode;
  fallback: React.ReactNode;
}

interface ErrorBoundaryState {
  hasError: boolean;
}

class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = { hasError: false };
  }

  static getDerivedStateFromError(error: Error): ErrorBoundaryState {
    // any childeren of this component throw an error it will update the state and rerender
    if (error) {
      return { hasError: true };
    }
    return { hasError: false };
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    // Log the error in console and we can send it to error reporting service on backend
    console.error("Error caught in ErrorBoundary:", error, errorInfo);
  }

  render() {
    if (this.state.hasError) {
      return this.props.fallback || <h1>Error happend</h1>; // show fallback
    }

    return this.props.children; // no error, render childeren normaly
  }
}

export default ErrorBoundary;
