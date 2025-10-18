import { queryOptions } from "@tanstack/react-query";
import { AxiosResponse } from "axios";
import { CompanyResponse } from "../Interfaces/CompanyResponseInterface";
import { axiosInstance } from "../utils/AxiosInstance";

const ALL_COMPANIES_URL = "/companies/all";
const SPECIFIC_COMPANY = "/companies";

export function getAllCompaniesQueryOptions() {
  return queryOptions({
    queryKey: ["all-companies"],
    queryFn: getAllCompanies,
  });
}

const getAllCompanies = (): Promise<AxiosResponse<CompanyResponse[]>> => {
  return axiosInstance.get(`${ALL_COMPANIES_URL}`);
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
  return axiosInstance.get(`${SPECIFIC_COMPANY}/${companyId}`);
};
