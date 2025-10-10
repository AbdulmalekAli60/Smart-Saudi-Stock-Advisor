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
import Input from "../components/Input";

export default function DashBoaredPage() {
  const [newCompnay, setNewCompany] = useState<AddCompany>({} as AddCompany);

  console.log("New company Data: ", { ...newCompnay });

  const [selectedUserId, setSelectedUserId] = useState<number>(0);
  const [selectedSection, setSelectedSection] = useState<
    "addCompany" | "users" | "deleteUser" | "deleteCompany" | null
  >();

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

  return (
    <>
      <MainNav />
      <div
        className="grid grid-cols-4  pt-14 h-screen"
        // style={{ height: "calc(100vh - 3.5rem)" }}
      >
        <div className="bg-gray-400 ">
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
        </div>

        <div className="col-span-3  flex-row w-full h-full items-center justify-center p-4">
          <div className=" h-1/2 w-full">
            <h1 className="font-primary-bold text-2xl sm:text-3xl lg:text-4xl">
              كل المستخدمين
            </h1>
            <div className="p-4 font-primary-bold">
              {users?.map((user) => {
                return (
                  <li key={user.userId} className="w-full list-none bg-background rounded-full mt-3 p-3  cursor-pointer">
                    {user.name}
                  </li>
                );
              })}
            </div>
          </div>

          {/* <div className="h-1/2 w-full">
            <h1 className="font-primary-bold text-2xl sm:text-3xl lg:text-4xl">حذف مستخدم</h1>

          </div> */}
        </div>
      </div>

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
