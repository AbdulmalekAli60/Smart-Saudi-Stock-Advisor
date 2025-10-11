import { mutationOptions, queryOptions } from "@tanstack/react-query";
import axios, { AxiosError, AxiosResponse } from "axios";
import { AddCompany } from "../Interfaces/AdminInterfaces";
import UserResponseInterface from "../Interfaces/UserResponseInterface";

const BASE_URL = "http://localhost:8080/admin";
const isAdminEndpoint = "/dashboard";
const createCompany = "/add-company";
const deleteCompany = "/delete-company";
const allUsers = "/all-users";
const deleteUser = "/delete-user";
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

export function AddCompanyMutationOptions(data: AddCompany) {
  return mutationOptions({
    mutationKey: ["add-company", data],
    mutationFn: () => addCompany(data),
    onSuccess: (response: AxiosResponse<AddCompany>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      throw error;
    },
  });
}

const addCompany = (data: AddCompany): Promise<AxiosResponse<AddCompany>> => {
  return axios.post(`${BASE_URL}${createCompany}`, data, {
    withCredentials: true,
  });
};

export function DeleteCompanyMutationOptions(id: number) {
  return mutationOptions({
    mutationKey: ["delete-company", id],
    mutationFn: () => deletCompany(id),
    onSuccess: (response: AxiosResponse<string>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      throw error;
    },
  });
}

const deletCompany = (id: number): Promise<AxiosResponse<string>> => {
  return axios.delete(`${BASE_URL}${deleteCompany}/${id}`, {
    withCredentials: true,
  });
};

export function AllUsersQueryOptions() {
  return queryOptions({
    queryKey: ["allUsers"],
    queryFn: getAllUsers,
  });
}

const getAllUsers = (): Promise<AxiosResponse<UserResponseInterface[]>> => {
  return axios.get(`${BASE_URL}${allUsers}`, {
    withCredentials: true,
  });
};

export function DeleteUserMutationOptions(id: number | undefined) {
  return mutationOptions({
    mutationKey: ["add-company", id],
    mutationFn: () => deletUser(id),
    onSuccess: (response: AxiosResponse<string>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      throw error;
    },
  });
}

const deletUser = (id: number | undefined): Promise<AxiosResponse<string>> => {
  return axios.delete(`${BASE_URL}${deleteUser}/${id}`, {
    withCredentials: true,
  });
};
