import { queryOptions } from "@tanstack/react-query";
import axios, { AxiosResponse } from "axios";
import SearchResponse from "../Interfaces/SearchInterface";

const BASE_URL = "http://localhost:8080";
const SEARCH_URL = "/companies/search-company?";

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
  return axios.get(`${BASE_URL}${SEARCH_URL}term=${searchTerm}`, {
    withCredentials: true,
  });
};
