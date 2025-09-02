interface ButtonProps {
  color: string;
  children: string;
}

export default function Button({ color, children }: ButtonProps) {
  return (
      <button
        className={
        `bg-${color}
        hover:bg-primary-light
         text-white
         font-primary-thin 
         cursor-pointer 
         rounded-3xl 
         p-1.5 
         w-28 
         border
         border-solid 
         border-border
         whitespace-nowrap`
        }
      >
        {children}
      </button>
  );
}
