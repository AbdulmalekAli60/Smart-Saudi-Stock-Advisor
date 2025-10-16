import { queryOptions } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import SearchResponse from "../Interfaces/SearchInterface";
import { axiosInstance } from "../utils/AxiosInstance";

// const BASE_URL = "http://localhost:8080";
const SEARCH_URL = "/companies/search-company";

export function searchQueryOptions(searchTerm: string) {
  return queryOptions({
    queryKey: ["search", searchTerm],
    queryFn: () => search(searchTerm),
    enabled: searchTerm.length > 0,
  });
}

const search = (
  searchTerm: string
): Promise<AxiosResponse<SearchResponse[]>> => {
  return axiosInstance.get(`${SEARCH_URL}?term=${searchTerm}`);
};
