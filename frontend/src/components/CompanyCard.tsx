import { Star, ChartCandlestick } from "lucide-react";
import { CompanyResponse } from "../Interfaces/CompanyResponseInterface";
import { useNavigate } from "react-router-dom";
import { useMutation } from "@tanstack/react-query";
import {
  AddWatchListMutationOptions,
  deleteWatchListMutationOptions,
} from "../services/WatchListService";
import axios from "axios";
import { useToast } from "../contexts/ToastContext";

interface compnayCardProps {
  compnayData: CompanyResponse;
  isBookmarked: boolean;
  watchListId: number;
  onBookMarkChange: () => void;
}

export default function CompanyCard({
  compnayData,
  isBookmarked,
  watchListId,
  onBookMarkChange,
}: compnayCardProps) {
  const navigate = useNavigate();
  const { showToast } = useToast();

  const mutation = useMutation(
    AddWatchListMutationOptions(compnayData.companyId)
  );
  const deleteMutation = useMutation(
    deleteWatchListMutationOptions(watchListId)
  );

  async function handleSaveClick(e: React.MouseEvent) {
    e.stopPropagation(); 
    try {
      if (isBookmarked && watchListId) {
        const response = await deleteMutation.mutateAsync();
        showToast("success", response.data);
      } else {
        const response = await mutation.mutateAsync();
        showToast("success", response.data);
      }
      onBookMarkChange();
    } catch (error) {
      if (axios.isAxiosError(error)) {
        showToast("fail", error.message);
      }
    }
  }

  return (
    <div
      onClick={() => navigate(`/companies/${compnayData.companyId}`)}
      className="group relative w-full bg-white rounded-xl border border-border p-5 
                 transition-all duration-300 hover:shadow-lg hover:-translate-y-1 cursor-pointer flex flex-col justify-between"
    >
      <div className="flex justify-between items-start mb-4">
        <div className="h-14 w-14 p-1 bg-gray-50 rounded-lg border border-gray-100 flex items-center justify-center">
          <img
            className="w-full h-full object-contain"
            src={compnayData.companyLogo}
            alt="company logo"
          />
        </div>

        <button
          onClick={handleSaveClick}
          className="p-2 rounded-full hover:bg-gray-50 cursor-pointer transition-colors"
        >
          <Star
            size={22}
            className={
              isBookmarked ? "fill-secondary text-secondary" : "text-gray-400"
            }
          />
        </button>
      </div>

      <div className="text-start font-primary-regular mb-4">
        <h1 className="font-primary-bold text-text-primary text-lg line-clamp-1 mb-1">
          {compnayData.companyArabicName}
        </h1>
        <h2 className="text-text-secondary text-sm font-primary-thin mb-3">
          {compnayData.companyEnglishName}
        </h2>

        <span className="inline-block bg-gray-100 text-text-secondary text-xs px-2 py-1 rounded-md">
          {compnayData.sectorArabicName}
        </span>
      </div>

      <div className="pt-4 border-t border-border flex justify-between items-center mt-auto">
        <div className="flex items-center gap-2 text-secondary">
          <ChartCandlestick size={16} />
          <span className="text-sm font-primary-bold">
            {compnayData.tickerName.split(".")[0]}
          </span>
        </div>
        <span className="text-xs text-gray-400">تداول</span>
      </div>
    </div>
  );
}
