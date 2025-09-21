import { queryOptions } from "@tanstack/react-query";
import axios, { AxiosResponse } from "axios";
import UserResponseInterface from "../Interfaces/UserResponseInterface";

const BASE_URL = "http://localhost:8080";
const USERDATA = "/user/personal";
export function userDataQueryOptions() {
  return queryOptions({
    queryKey: ["fresh-data"],
    queryFn: getUserData,
  });
}

const getUserData = (): Promise<AxiosResponse<UserResponseInterface>> => {
  return axios.get(`${BASE_URL}${USERDATA}`, { withCredentials: true });
};
