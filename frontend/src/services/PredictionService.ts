import { queryOptions } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import PredictionInterface from "../Interfaces/PredictionInterface";
import { axiosInstance } from "../utils/AxiosInstance";

const all = "/all";
const latest = "latest/";

export function allPredictionsQueryOptions(compnayId: string | undefined) {
  return queryOptions({
    queryKey: ["all", compnayId],
    queryFn: () => getAllPredictions(compnayId),
  });
}

const getAllPredictions = (
  compnayId: string | undefined
): Promise<AxiosResponse<PredictionInterface[]>> => {
  return axiosInstance.get(`/predictions/${compnayId}${all}`);
};

export function latestPredictionsQueryOptions(compnayId: string | undefined) {
  return queryOptions({
    queryKey: ["latest", compnayId],
    queryFn: () => getLatestPredictions(compnayId),
  });
}

const getLatestPredictions = (
  compnayId: string | undefined
): Promise<AxiosResponse<PredictionInterface>> => {
  return axiosInstance.get(`/predictions/${latest}${compnayId}`);
};
