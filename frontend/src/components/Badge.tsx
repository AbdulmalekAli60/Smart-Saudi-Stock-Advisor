interface BadgeProps {
  arabicName: string;
}

export default function Badge({ arabicName }: BadgeProps) {
  return (
    <div className="">
      <button className="font-primary-bold text-2xl">{arabicName}</button>
    </div>
  );
}
