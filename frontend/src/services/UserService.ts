import { mutationOptions, queryOptions } from "@tanstack/react-query";
import { AxiosError, AxiosResponse } from "axios";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { UpdateInfo } from "../Interfaces/UpdateInfoInterface";
import { axiosInstance } from "../utils/AxiosInstance";

// const BASE_URL = "http://localhost:8080/user";
const userData = "/personal";
const updateEndpoint = "/update";
const deleteAccountEndpoint = "/delete";
export function userDataQueryOptions() {
  return queryOptions({
    queryKey: ["fresh-data"],
    queryFn: getUserData,
  });
}

const getUserData = (): Promise<AxiosResponse<UserResponseInterface>> => {
  return axiosInstance.get(`/user${userData}`);
};

export function updateUserDataMutationOptions(updatedInfo: UpdateInfo) {
  return mutationOptions({
    mutationKey: ["update-user-info", updatedInfo],
    mutationFn: () => updateUserData(updatedInfo),

    onSuccess: (response: AxiosResponse<UserResponseInterface>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      return error;
    },
  });
}

const updateUserData = (
  newInfo: UpdateInfo
): Promise<AxiosResponse<UserResponseInterface>> => {
  return axiosInstance.patch(`/user${updateEndpoint}`, newInfo);
};

export function deleteAccountMutationOptions() {
  return mutationOptions({
    mutationKey: ["delete-account"],
    mutationFn: deleteAccount,
    onSuccess: (response: AxiosResponse<string>) => {
      return response;
    },
    onError: (error: AxiosError) => {
      throw error;
    },
  });
}

const deleteAccount = (): Promise<AxiosResponse<string>> => {
  return axiosInstance.delete(`/user${deleteAccountEndpoint}`);
};
