import axios, { AxiosError, AxiosResponse } from "axios";
import { LogInState, SignUp } from "../Interfaces/AuthInterfaces";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { mutationOptions } from "@tanstack/react-query";

const BASE_URL = "http://localhost:8080/auth"
const LOGIN_URL = "/log-in"
const SIGNUP_URL = "/sign-up"

export function signUpMutationOptions() {
  return mutationOptions({
    mutationKey: ["sign-up"],
    mutationFn: signup,
    onSuccess: (response: AxiosResponse<SignUp>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      return error;
    },
  });
}

const signup = (signUpFormData: SignUp): Promise<AxiosResponse<SignUp>>  => {
  return axios.post(`${BASE_URL}${SIGNUP_URL}`, signUpFormData, {withCredentials: true})
}   

export function logInMutationOptions() {
  return mutationOptions({
    mutationKey: ["log-in"],
    mutationFn: logIn,
    onSuccess: (response: AxiosResponse<UserResponseInterface>) => {
      return response.data;
    },
    onError: (error: AxiosError) => {
      return error;
    },
  });
}


const logIn = (LogInFormData: LogInState): Promise<AxiosResponse<UserResponseInterface>>  => {
  return axios.post(`${BASE_URL}${LOGIN_URL}`, LogInFormData, {withCredentials: true})
}   


