import { queryOptions } from "@tanstack/react-query";
import axios, { AxiosResponse } from "axios";
import { CompanyResponse } from "../Interfaces/CompanyResponseInterface";

const BASE_URL = "http://localhost:8080";
const ALL_COMPANIES_URL = "/companies/all";

export function getAllCompaniesQueryOptions() {
  return queryOptions({
    queryKey: ["all-companies"],
    queryFn: getAllCompanies,
  });
}

const getAllCompanies = (): Promise<AxiosResponse<CompanyResponse[]>> => {
  return axios.get(`${BASE_URL}${ALL_COMPANIES_URL}`, {
    withCredentials: true,
  });
};
