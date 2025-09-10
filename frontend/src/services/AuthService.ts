import axios, { AxiosResponse } from "axios";
import { LogInState } from "../Interfaces/AuthInterfaces";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { mutationOptions } from "@tanstack/react-query";

export function logInMutationOptions() {
  return mutationOptions({
    mutationKey:['log-in'],
    mutationFn: logIn,
    onSuccess: (response) => {
        const userData:UserResponseInterface = response.data
    }
  });
}

const logIn = (
  LogInFormData: LogInState
): Promise<AxiosResponse<UserResponseInterface>> => {
  return axios.post("http://localhost:8080/auth/log-in", LogInFormData);
};
