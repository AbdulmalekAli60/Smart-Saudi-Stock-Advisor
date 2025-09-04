interface CardProps {
    title: string;
    desc: string;
}

export default function Card({title, desc}: CardProps) {
  return (
    <div className="w-full min-h-[250px] md:min-h-[280px] lg:min-h-[320px] rounded-2xl md:rounded-3xl bg-success shadow-xl/30 mt-3 md:mt-4 lg:mt-5 p-4 md:p-5 lg:p-6">
      <div className="flex flex-col items-center h-full justify-around gap-3 md:gap-4 lg:gap-5">
        <h1 className="font-primary-bold text-white break-words line-clamp-2 text-base md:text-lg lg:text-xl xl:text-2xl text-center leading-tight">
          {title}
        </h1>
        
        <p className="font-primary-thin text-white/90 text-sm md:text-base lg:text-lg text-center break-words line-clamp-4 md:line-clamp-5 leading-relaxed">
          {desc}
        </p>
      </div>
    </div>
  );
}