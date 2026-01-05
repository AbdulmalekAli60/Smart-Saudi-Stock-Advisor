interface BadgeProps {
  arabicName: string;
  sectorId: number;
  isSelected: boolean;
  onSelect: (sectorId: number | null) => void;
}

export default function Badge({
  arabicName,
  sectorId,
  isSelected,
  onSelect,
}: BadgeProps) {
  return (
    <button
      key={sectorId}
      onClick={() => onSelect(isSelected ? null : sectorId)}
      className={`${
        isSelected
          ? "bg-white border border-primary"
          : "bg-primary-light text-white hover:bg-primary"
      }
         whitespace-nowrap  cursor-pointer font-primary-bold rounded-full p-3 w-fit  hover:scale-105 transition-all`}
    >
      {arabicName}
    </button>
  );
}
