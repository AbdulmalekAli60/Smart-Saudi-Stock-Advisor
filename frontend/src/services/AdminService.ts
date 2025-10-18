import { mutationOptions, queryOptions } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { axiosInstance } from "../utils/AxiosInstance";

const isAdminEndpoint = "/dashboard";
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
