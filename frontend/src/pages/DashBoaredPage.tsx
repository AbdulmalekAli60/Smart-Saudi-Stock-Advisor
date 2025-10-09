import { useMutation, useQueries, useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import {
  AddCompanyMutationOptions,
  AllUsersQueryOptions,
  DeleteCompanyMutationOptions,
  DeleteUserMutationOptions,
  isAdminQueryOptions,
} from "../services/AdminService";
import Loader from "../components/Loader";
import { useEffect, useState } from "react";
import axios from "axios";
import { getSectorsQueryOptions } from "../services/SectorService";
import { AddCompany } from "../Interfaces/AdminInterfaces";
import MainNav from "../components/MainNav";
import { getAllCompaniesQueryOptions } from "../services/CompanyService";
import Side from "../components/Side";
import StatCard from "../components/StatCard";
import { CirclePlus } from "lucide-react";

export default function DashBoaredPage() {
  const [newCompnay, setNewCompany] = useState<AddCompany>({} as AddCompany);
  const [selectedUserId, setSelectedUserId] = useState<number>(0);
  const [selectedSection, setSelectedSection] = useState<"addCompany" | "users" | "deleteUser" | "deleteCompany" | null>();

  const navigate = useNavigate();

  const { isLoading, error } = useQuery(isAdminQueryOptions());

  useEffect(() => {
    if (axios.isAxiosError(error) && error.response?.status === 401) {
      navigate("/home");
    }
  }, [error, navigate]);

  const queries = useQueries({
    queries: [
      getSectorsQueryOptions(),
      AllUsersQueryOptions(),
      getAllCompaniesQueryOptions(),
    ],
  });
  const [sectorsQuery, usersQuery, companiesQuery] = queries;

  const sectors = sectorsQuery.data?.data;
  const users = usersQuery.data?.data;
  const companies = companiesQuery.data?.data;

  const addCompanyMutation = useMutation(AddCompanyMutationOptions(newCompnay));
  const deleteCompanyMutation = useMutation(
    DeleteCompanyMutationOptions(selectedUserId)
  );
  const deleteuserMutation = useMutation(
    DeleteUserMutationOptions(selectedUserId)
  );
  console.log(selectedSection);
  return (
    <>
      <MainNav />
      {/* <div
        className="grid grid-cols-2 pt-14"
        style={{ height: "calc(100vh - 3.5rem)" }}
      >
        <div className="bg-red-300 flex flex-col">
          <div className="bg-amber-300 h-1/2">create companies</div>
          <div className="bg-blue-200 h-1/2">sada</div>
        </div>

        <div className="bg-yellow-300">all users</div>
      </div> */}

      <Side>
        <StatCard
          handleClick={setSelectedSection}
          Icon={CirclePlus}
          body="إضافة شركة جديدة"
          title="إضافة شركة"
          color="text-green-400"
          bodyClassName="cursor-pointer"
        />

        <StatCard
        handleClick={setSelectedSection}
          Icon={CirclePlus}
          body="حذف شركة "
          title="حذف شركة"
          color="text-green-400"
          bodyClassName="cursor-pointer"
        />
      </Side>

      {/* <Footer /> */}

      {(isLoading ||
        sectorsQuery.isLoading ||
        usersQuery.isLoading ||
        addCompanyMutation.isPending ||
        deleteCompanyMutation.isPending ||
        deleteuserMutation.isPending ||
        companiesQuery.isLoading) && <Loader />}
    </>
  );
}
