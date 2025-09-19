import { queryOptions } from "@tanstack/react-query";
import axios, { AxiosResponse } from "axios";
import { CompanyResponse } from "../Interfaces/CompanyResponseInterface";

const BASE_URL = "http://localhost:8080";
const ALL_COMPANIES_URL = "/companies/all";
const SPECIFIC_COMPANY = "/companies";

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

export function companyById(id: string | undefined) {
  return queryOptions({
    queryKey: ["get-company-by-id", id],
    queryFn: () => getCompanyById(id),
    enabled: !!id,
  });
}

const getCompanyById = (
  companyId: string | undefined
): Promise<AxiosResponse<CompanyResponse>> => {
  return axios.get(`${BASE_URL}${SPECIFIC_COMPANY}/${companyId}`, {
    withCredentials: true,
  });
};
