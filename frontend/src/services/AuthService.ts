import { AxiosError, AxiosResponse } from "axios";
import {
  LogInState,
  logoutInterface,
  SignUp,
} from "../Interfaces/AuthInterfaces";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { mutationOptions } from "@tanstack/react-query";
import { axiosInstance } from "../utils/AxiosInstance";

// const BASE_URL = "http://localhost:8080/auth";
const LOGIN_URL = "/log-in";
const SIGNUP_URL = "/sign-up";
const LOGOUT_URL = "/logout";
// const REFRESH_TOKEN = "/refresh-token";

export function signUpMutationOptions(signUpFormData: SignUp) {
  return mutationOptions({
    mutationKey: ["sign-up", signUpFormData],
    mutationFn: () => signup(signUpFormData),
    onSuccess: (response: AxiosResponse<UserResponseInterface>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      return error;
    },
  });
}

const signup = (
  signUpFormData: SignUp
): Promise<AxiosResponse<UserResponseInterface>> => {
  return axiosInstance.post(`/auth${SIGNUP_URL}`, signUpFormData);
};

export function logInMutationOptions(logInFormData: LogInState) {
  return mutationOptions({
    mutationKey: ["log-in", logInFormData],
    mutationFn: () => logIn(logInFormData),
    onSuccess: (response: AxiosResponse<UserResponseInterface>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      return error;
    },
  });
}

const logIn = (
  LogInFormData: LogInState
): Promise<AxiosResponse<UserResponseInterface>> => {
  return axiosInstance.post(`/auth${LOGIN_URL}`, LogInFormData);
};

export function LogoutMutationOptions() {
  return mutationOptions({
    mutationKey: ["logout"],
    mutationFn: logout,
    onSuccess: (response: AxiosResponse<logoutInterface>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      return error;
    },
  });
}

const logout = (): Promise<AxiosResponse<logoutInterface>> => {
  return axiosInstance.post(`/auth${LOGOUT_URL}`);
};

// export function refreshTokenQueryOptions() {
//   return queryOptions({
//     queryKey: ["refresh-token"],
//     queryFn: refreshToken,
//   });
// }

// const refreshToken = (): Promise<AxiosResponse<AxiosHeaders>> => {
//   return axiosInstance.post(`/auth${REFRESH_TOKEN}`);
// };
