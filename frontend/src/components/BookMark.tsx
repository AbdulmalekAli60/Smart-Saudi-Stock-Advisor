import { ChartCandlestick, Star } from "lucide-react";
import { useState } from "react";
import WatchListResponseInterface from "../Interfaces/WatchListResponseInterface";
import { useMutation } from "@tanstack/react-query";
import { deleteWatchListMutationOptions } from "../services/WatchListService";
import { useToast } from "../contexts/ToastContext";
import axios from "axios";
import Loader from "./Loader";
import { useNavigate } from "react-router-dom";

interface bookMarksProps {
  data: WatchListResponseInterface
  onBookmarkChaneg: () => void
}

export default function BookMark({data, onBookmarkChaneg}:bookMarksProps ) {
  const [isSavedBookMark, setIsSaveToBookmark] = useState<boolean>(true);
  const { showToast } = useToast();
  const navigate = useNavigate()
  const mutation = useMutation(deleteWatchListMutationOptions(data.watchListId));

  async function handelBookMarkClick() {
    try {
      if (isSavedBookMark) {
        const response = await mutation.mutateAsync();
        showToast("success", response.data);
        setIsSaveToBookmark(false);
        onBookmarkChaneg()
      }
    } catch (error) {
      if (axios.isAxiosError(error)) {
        // console.log(error)
        showToast("fail", error.message);
      }
    }
  }
  return (
    <>
      <div className=" bg-background h-fit mt-3 w-full flex justify-between items-center gap-0 pr-2 rounded-2xl shadow-lg">
        <img src={data.companyLogo} className="w-12 h-12" alt="logo" />
        <div onClick={() => navigate(`/companies/${data.companyId}`)} className="w-4/5  text-start mr-3 border-r-2 p-2 cursor-pointer">
          <div className="space-y-3">
            <h1 className="sm:text-sm md:text-xl lg:text-2xl">{data.companyName}</h1>
            <h3 className="sm:text-sm md:text-base lg:text-xl font-primary-thin text-black">
              {data.tickerName.split(".")[0]}{" "}
              <ChartCandlestick className="inline " />
            </h3>
          </div>
        </div>

        <div
          onClick={handelBookMarkClick}
          className="bg-background cursor-pointer w-1/5 h-full flex justify-center items-center"
        >
          <Star
            className="w-2/6 h-2/6"
            fill={isSavedBookMark ? "black" : "white"}
          />
        </div>

        {mutation.isPending && <Loader />}
      </div>
    </>
  );
}
