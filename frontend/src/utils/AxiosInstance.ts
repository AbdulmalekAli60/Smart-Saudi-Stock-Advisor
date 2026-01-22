import axios, { AxiosError, AxiosRequestConfig, AxiosResponse } from "axios";

const prodcution_url = "http://72.62.133.36:8080"
// const development_url  = "http://localhost:8080"

export const axiosInstance = axios.create({
  baseURL: prodcution_url,
  withCredentials: true,
});


let storedToken: string | undefined = undefined;

axiosInstance.interceptors.response.use(
  function (response: AxiosResponse) {
    const token = response.headers["x-access-token"];

    if (token) {
      storedToken = token;
    }
    return response;
  },

  async function (error: AxiosError) {
    try {
      const originalRequest: AxiosRequestConfig =
        error.config as AxiosRequestConfig & { _retry?: boolean };

      if (error.response?.status === 401 && storedToken) {

        const req = await axiosInstance.post("/auth/refresh-token", {
          token: storedToken,
        });

        const newToken = req.headers["x-access-token"];

        if (newToken) {
          console.log("==== New token block: ", newToken);
          storedToken = newToken;
        }

        return axiosInstance(originalRequest);
      }
    } catch (err) {
      storedToken = undefined;
      window.location.href = "/";
      console.log(err);
      return Promise.reject(err);
    }

    return Promise.reject(error);
  }
);
