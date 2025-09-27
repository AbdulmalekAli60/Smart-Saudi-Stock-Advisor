import { LucideIcon } from "lucide-react";
import { ReactNode } from "react";

interface statCardProps {
  Icon: LucideIcon;
  title: string;
  body: string | undefined | ReactNode;
  color: string;
  bodyClassName?:string
}
export default function StatCard({ body, Icon, title, color, bodyClassName }: statCardProps) {
  return (
    <div className="flex items-center gap-2 md:gap-3 p-2 md:p-3 bg-gray-50 rounded-lg">
      <Icon className={`w-4 h-4 md:w-5 md:h-5 ${color} flex-shrink-0`} />
      <div className="min-w-0 flex-1">
        <p className="text-xs text-gray-500">{title}</p>
        <div className={`font-medium text-sm md:text-base text-gray-900 truncate ${bodyClassName}`}>
          {body}
        </div>
      </div>
    </div>
  );
}
