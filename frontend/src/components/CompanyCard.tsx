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

  async function handleSaveClick() {
    console.log("the company id is: ", compnayData.companyId);

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
        console.log(error.message);
        showToast("fail", error.message);
      }
    }
  }

  return (
    <div className="w-full h-full p-6 bg-background text-center space-y-6 rounded-3xl shadow-lg">
      <div className="w-full flex justify-between">
        <div></div>
        <img
          className="w-1/2 h-1/2"
          src={compnayData.companyLogo}
          alt="company logo"
        />

        <div onClick={handleSaveClick}>
          <Star
            fill={isBookmarked ? "black" : "white"}
            className="cursor-pointer"
          />
        </div>
      </div>

      <div
        onClick={() => navigate(`/companies/${compnayData.companyId}`)}
        className=" text-start font-primary-regular p-2 cursor-pointer"
      >
        <div className=" flex justify-between">
          <h1 className="mb-3 sm:text-base md:text-3xl lg:text-4xl">
            {compnayData.companyArabicName}
          </h1>

          <div className="space-x-2 whitespace-nowrap">
            <span className="font-primary-bold sm:text-sm md:text-base lg:text-lg">
              {compnayData.tickerName.split(".")[0]}
            </span>
            <ChartCandlestick className="inline" />
          </div>
        </div>

        <div className="flex justify-between">
          <h2 className="font-primary-regular sm:text-sm md:text-base lg:text-lg">
            {compnayData.companyEnglishName}
          </h2>
          <h2 className="font-primary-bold sm:text-sm md:text-base lg:text-lg">
            {compnayData.companyArabicName}
          </h2>
        </div>
      </div>
    </div>
  );
}
