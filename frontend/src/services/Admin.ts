import { queryOptions } from "@tanstack/react-query";
import axios, { AxiosResponse } from "axios";

const BASE_URL = "http://localhost:8080/admin";
const isAdminEndpoint = "/dashboard";

export function isAdminQueryOptions() {
  return queryOptions({
    queryKey: ["isAdmin"],
    queryFn: isAdmin,
    retry: false,
  });
}

const isAdmin = (): Promise<AxiosResponse> => {
  return axios.get(`${BASE_URL}${isAdminEndpoint}`, { withCredentials: true });
};
