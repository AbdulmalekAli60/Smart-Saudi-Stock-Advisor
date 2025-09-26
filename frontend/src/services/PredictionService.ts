import { queryOptions } from "@tanstack/react-query";
import axios, { AxiosResponse } from "axios";
import PredictionInterface from "../Interfaces/PredictionInterface";

const BASE_URL = "http://localhost:8080/predictions";
const all = "/all";
const latest = "/latest/";

export function allPredictionsQueryOptions(compnayId: string | undefined) {
  return queryOptions({
    queryKey: ["all", compnayId],
    queryFn: () => getAllPredictions(compnayId),
  });
}

const getAllPredictions = (
  compnayId: string | undefined
): Promise<AxiosResponse<PredictionInterface[]>> => {
    console.log(`${BASE_URL}/${compnayId}${all}`)
  return axios.get(`${BASE_URL}/${compnayId}${all}`, { withCredentials: true });
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
    console.log(`${BASE_URL}${latest}${compnayId}`)
  return axios.get(`${BASE_URL}${latest}${compnayId}`, {
    withCredentials: true,
  });
};
