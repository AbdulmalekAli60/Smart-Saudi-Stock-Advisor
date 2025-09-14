import axios, { AxiosError, AxiosResponse } from "axios";
import { LogInState, SignUp } from "../Interfaces/AuthInterfaces";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { mutationOptions } from "@tanstack/react-query";

const BASE_URL = "http://localhost:8080/auth";
const LOGIN_URL = "/log-in";
const SIGNUP_URL = "/sign-up";
const LOGOUT_URL = "/logout";

export function signUpMutationOptions(signUpFormData: SignUp) {
  return mutationOptions({
    mutationKey: ["sign-up"],
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
  return axios.post(`${BASE_URL}${SIGNUP_URL}`, signUpFormData, {
    withCredentials: true,
  });
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
  return axios.post(`${BASE_URL}${LOGIN_URL}`, LogInFormData, {
    withCredentials: true,
  });
};

export function LogoutMutationOptions() {
  return mutationOptions({
    mutationKey: ["logout"],
    mutationFn: logout,
    onSuccess: (response: AxiosResponse<string>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      return error;
    },
  });
}

const logout = (): Promise<AxiosResponse<string>> => {
  return axios.post(`${BASE_URL}${LOGOUT_URL}`, undefined, {
    withCredentials: true,
  });
};
