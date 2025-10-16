import axios, { AxiosError, AxiosResponse } from "axios";

export const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

let tokenInHeader: string[] | undefined = undefined;

axiosInstance.interceptors.response.use(function (response: AxiosResponse) {
  // take token and store it in varibale

  tokenInHeader = response.headers["set-cookie"]; // use substring
  console.log("Token in header: ", tokenInHeader);
  return response;

  //   if (tokenInHeader != undefined) return response;
});

axiosInstance.interceptors.response.use(
  function (response: AxiosResponse) {
    if (response.status != 401) {
      console.log("Response object: ", response);
      return response;
    }
    throw AxiosError.ERR_BAD_RESPONSE;
  },

  async function (error: AxiosError) {
    // const originalRequest = error.config;

    if (error.response?.status === 401) {
      console.log("Token expired : ");
      const req = axiosInstance.post("/refresh-token", undefined, {
        headers: { Authorization: `Bearer ${tokenInHeader}` },
      });
      console.log("Query result: ", (await req).data);
      //   originalRequest "make orginal request again"
    }
  }
);
