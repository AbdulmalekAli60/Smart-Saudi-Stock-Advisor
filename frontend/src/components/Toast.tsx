import { useRef } from "react";
import useAnimations from "../hooks/useAnimation";

const toastColors = {
  success: "var(--color-success)",
  fail: "var(--color-fail)",
  warning: "var(--color-warning)",
} as const;

interface ToastProps {
  color: keyof typeof toastColors;
  text?: string;
}

export default function Toast({ color, text }: ToastProps) {


  const toastContainerRef = useRef<HTMLDivElement>(null);

  useAnimations({toastContainerRef})

  return (
    <div
      ref={toastContainerRef}
      style={{ background: toastColors[color] }}
      className="w-fit min-w-1/7 rounded-lg p-2 fixed bottom-16 left-6 transform -translate-x-2 z-50"
    >
      <span className="font-primary-bold text-white whitespace-nowrap">
        {text}
      </span>
    </div>
  );
}
