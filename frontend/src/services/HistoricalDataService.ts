import { queryOptions } from "@tanstack/react-query";
import axios, { AxiosResponse } from "axios";
import HistoricalDataInterface from "../Interfaces/HistoricalDataInterface";

const BASE_URL = "http://localhost:8080";
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
  return axios.get(`${BASE_URL}${historicalData}/${id}`, {
    withCredentials: true,
  });
};
