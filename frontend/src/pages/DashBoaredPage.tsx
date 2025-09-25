import { useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { isAdminQueryOptions } from "../services/Admin";
import Loader from "../components/Loader";
import { useEffect } from "react";
import axios from "axios";

export default function DashBoaredPage() {
  const navigate = useNavigate();

  const { isLoading, error } = useQuery(isAdminQueryOptions());
  
  useEffect(() => {
    if (axios.isAxiosError(error) && error.response?.status === 401) {
      navigate("/home");
    }
  }, [error, navigate]);
  return (
    <>
      this is dash boared
      {isLoading && <Loader />}
    </>
  );
}
