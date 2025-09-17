import { queryOptions } from "@tanstack/react-query";
import axios, { AxiosResponse } from "axios";
import SectorInterface from "../Interfaces/SectorInterface";

const BASE_URL = "http://localhost:8080";
const SECTORS_URL = "/sector/all";

export function getSectorsQueryOptions(){
    return queryOptions({
        queryKey:["all-sectors"],
        queryFn:getSectors,
    })
}

const getSectors = (): Promise<AxiosResponse<SectorInterface[]>> => {
  return axios.get(`${BASE_URL}${SECTORS_URL}`, {withCredentials: true,});
};
