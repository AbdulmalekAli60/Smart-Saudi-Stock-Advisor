import { mutationOptions, queryOptions } from "@tanstack/react-query";
import axios, { AxiosError, AxiosResponse } from "axios";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { UpdateInfo } from "../Interfaces/UpdateInfoInterface";

const BASE_URL = "http://localhost:8080/user";
const userData = "/personal";
const updateEndpoint = "/update";
export function userDataQueryOptions() {
  return queryOptions({
    queryKey: ["fresh-data"],
    queryFn: getUserData,
  });
}

const getUserData = (): Promise<AxiosResponse<UserResponseInterface>> => {
  return axios.get(`${BASE_URL}${userData}`, { withCredentials: true });
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
  return axios.patch(`${BASE_URL}${updateEndpoint}`, newInfo, {
    withCredentials: true,
  });
};
