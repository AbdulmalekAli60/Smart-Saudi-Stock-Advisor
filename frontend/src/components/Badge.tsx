interface BadgeProps {
  arabicName: string;
  sectorId: number;
}

export default function Badge({ arabicName, sectorId }: BadgeProps) {
  function handleSectorClick(
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) {
    console.log(sectorId)
  }
  return (
    <button
      key={sectorId}
      onClick={(e) => handleSectorClick(e)}
      className={`bg-primary-light whitespace-nowrap hover:bg-primary cursor-pointer font-primary-bold text-white border rounded-full p-3 w-fit`}
    >
      {arabicName}
    </button>
  );
}
