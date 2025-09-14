import React, { createContext, useContext, useState } from "react";
import Toast from "../components/Toast";

const toastColors = {
  success: "var(--color-success)",
  fail: "var(--color-fail)",
  warning: "var(--color-warning)",
} as const;

interface ToastContextType {
  showToast: (color: keyof typeof toastColors, text: string) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export function ToastContextProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const [toast, setToast] = useState<{
    color: keyof typeof toastColors;
    text: string;
  } | null>(null);

  function showToast(color: keyof typeof toastColors, text: string) {
    setToast({ color, text });
  }

  setTimeout(() => {
    setToast(null);
  }, 4200);

  return (
    <ToastContext.Provider value={{ showToast }}>
      {children}
      {toast && <Toast color={toast.color} text={toast.text} />}
    </ToastContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export const useToast = () => {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error("useToast must be used within a ToastContextProvider");
  }
  return context;
};
