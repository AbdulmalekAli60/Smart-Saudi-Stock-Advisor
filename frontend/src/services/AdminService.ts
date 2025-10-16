import { mutationOptions, queryOptions } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { axiosInstance } from "../utils/AxiosInstance";

// const BASE_URL = "http://localhost:8080/admin";
const isAdminEndpoint = "/dashboard";
// const createCompany = "/add-company";
// const deleteCompany = "/delete-company";
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
  return axiosInstance.get(`/admin${isAdminEndpoint}`);
};

// export function AddCompanyMutationOptions(data: AddCompany) {
//   return mutationOptions({
//     mutationKey: ["add-company", data],
//     mutationFn: () => addCompany(data),
//     onSuccess: (response: AxiosResponse<AddCompany>) => {
//       return response.data;
//     },
//     onError: (error: AxiosError) => {
//       throw error;
//     },
//   });
// }

// const addCompany = (data: AddCompany): Promise<AxiosResponse<AddCompany>> => {
//   return axios.post(`${BASE_URL}${createCompany}`, data, {
//     withCredentials: true,
//   });
// };

// export function DeleteCompanyMutationOptions(id: number) {
//   return mutationOptions({
//     mutationKey: ["delete-company", id],
//     mutationFn: () => deletCompany(id),
//     onSuccess: (response: AxiosResponse<string>) => {
//       return response.data;
//     },
//     onError: (error: AxiosError) => {
//       throw error;
//     },
//   });
// }

// const deletCompany = (id: number): Promise<AxiosResponse<string>> => {
//   return axios.delete(`${BASE_URL}${deleteCompany}/${id}`, {
//     withCredentials: true,
//   });
// };

export function AllUsersQueryOptions() {
  return queryOptions({
    queryKey: ["allUsers"],
    queryFn: getAllUsers,
  });
}

const getAllUsers = (): Promise<AxiosResponse<UserResponseInterface[]>> => {
  return axiosInstance.get(`/admin${allUsers}`);
};

export function DeleteUserMutationOptions() {
  return mutationOptions({
    mutationKey: ["add-company"],
    mutationFn: (userId: number | undefined) => {
      return axiosInstance.delete(`/admin${deleteUser}/${userId}`);
    },
    onSuccess: (response: AxiosResponse<string>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      throw error;
    },
  });
}
