interface ButtonProps {
  color: string;
  children: string;
}

export default function Button({ color, children }: ButtonProps) {
  return (
    <button
      className={`bg-${color}
        hover:bg-primary-light
         text-white
         font-p
         cursor-pointer 
         rounded-3xl 
         p-1.5 
         w-28 
         whitespace-nowrap`}
    >
      {children}
    </button>
  );
}
//  border
//  border-solid
//  border-border
