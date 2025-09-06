interface CardProps {
  title: string;
  desc: string;
}

export default function Card({title, desc}: CardProps) {
  return (
    <div className="w-full min-h-[250px] md:min-h-[280px] lg:min-h-[320px] rounded-2xl md:rounded-3xl bg-success shadow-xl/30 mt-3 md:mt-4 lg:mt-5 p-6 md:p-7 lg:p-8">
      <div className="flex flex-col h-full justify-center gap-6 md:gap-7 lg:gap-8">
        <h1 className="font-primary-bold text-white break-words text-lg md:text-xl lg:text-2xl xl:text-3xl text-center leading-tight">
          {title}
        </h1>
                
        <p className="font-primary-thin text-white/90 text-base md:text-lg lg:text-xl text-center break-words leading-relaxed flex-1 flex items-center justify-center">
          {desc}
        </p>
      </div>
    </div>
  );
}