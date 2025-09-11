import { useGSAP } from "@gsap/react";
import { gsap } from "gsap/gsap-core";
import { useRef } from "react";

const toastColors = {
  success: "var(--color-success)",
  fail: "var(--color-fail)",
  warning: "var(--color-warning)",
} as const;

interface ToastProps {
  color: keyof typeof toastColors;
  text: string;
}

export default function Toast({ color, text }: ToastProps) {
  const toastContainerRef = useRef<HTMLDivElement>(null);

  useGSAP(() => {
    const tl = gsap.timeline();

    tl.fromTo(
      toastContainerRef.current,
      { x: -40, opacity: 0.5 },
      { x: 0, opacity: 1, duration: 0.8, ease: "back.out(1.7)" }
    ).to(toastContainerRef.current, {
      x: -40,
      opacity: 0,
      duration: 0.4,
      ease: "power2.in(1.7)",
      delay: 3,
    });

  }, []);

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
