interface ButtonProps {
  color: string;
  children: string;
}

export default function Button({ color, children }: ButtonProps) {
  return (
    <button
      className={`
        hover:bg-primary-light
         text-white
         font-p
         cursor-pointer 
         rounded-3xl 
         p-1.5 
         w-28 
         whitespace-nowrap`}
      style={{ background: `var(--color-${color})` }}
    >
      {children}
    </button>
  );
}

