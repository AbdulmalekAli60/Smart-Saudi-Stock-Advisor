import { queryOptions } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import SectorInterface from "../Interfaces/SectorInterface";
import { axiosInstance } from "../utils/AxiosInstance";

// const BASE_URL = "http://localhost:8080";
const SECTORS_URL = "/sector/all";

export function getSectorsQueryOptions() {
  return queryOptions({
    queryKey: ["all-sectors"],
    queryFn: getSectors,
  });
}

const getSectors = (): Promise<AxiosResponse<SectorInterface[]>> => {
  return axiosInstance.get(`${SECTORS_URL}`);
};
