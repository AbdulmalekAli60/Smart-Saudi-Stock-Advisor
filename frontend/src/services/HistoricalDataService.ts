import { queryOptions } from "@tanstack/react-query";
import  { AxiosResponse } from "axios";
import HistoricalDataInterface from "../Interfaces/HistoricalDataInterface";
import { axiosInstance } from "../utils/AxiosInstance";

// const BASE_URL = "http://localhost:8080";
const historicalData = "/historical-data";

export function getHistoricalDataQueryOptions(id: string | undefined) {
  return queryOptions({
    queryKey: ["historical-data", id],
    queryFn: () => getAllHistoricalData(id),
  });
}

const getAllHistoricalData = (
  id: string | undefined
): Promise<AxiosResponse<HistoricalDataInterface[]>> => {
  return axiosInstance.get(`${historicalData}/${id}`);
};
