import { mutationOptions, queryOptions } from "@tanstack/react-query";
import axios, { AxiosError, AxiosResponse } from "axios";
import WatchListResponseInterface from "../Interfaces/WatchListResponseInterface";

const BASE_URL = "http://localhost:8080/watch-list";
const all = "/all";
const deleteUrl = "/delete";
const add = "/add";

export function WatchListQueryOptions() {
  return queryOptions({
    queryKey: ["all-watch-list"],
    queryFn: getAllWatchLists,
  });
}

const getAllWatchLists = (): Promise<
  AxiosResponse<WatchListResponseInterface[]>
> => {
  return axios.get(`${BASE_URL}${all}`, { withCredentials: true });
};

export function AddWatchListMutationOptions(companyId: number) {
  return mutationOptions({
    mutationKey: ["ADD-watch-list", companyId],
    mutationFn: () => AddWatchList(companyId),
    onSuccess: (response: AxiosResponse<string>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      throw error;
    },
  });
}

const AddWatchList = (id: number): Promise<AxiosResponse<string>> => {
  return axios.post(`${BASE_URL}${add}/${id}`, {}, { withCredentials: true });
};

export function deleteWatchListMutationOptions(watchListId: number) {
  return mutationOptions({
    mutationKey: ["delete-watch-list", watchListId],
    mutationFn: () => deleteWatchList(watchListId),
    onSuccess: (response: AxiosResponse<string>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      throw error;
    },
  });
}

const deleteWatchList = (
  watchListId: number
): Promise<AxiosResponse<string>> => {
  return axios.delete(`${BASE_URL}${deleteUrl}/${watchListId}`, {
    withCredentials: true,
  });
};
