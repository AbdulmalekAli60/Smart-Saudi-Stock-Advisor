import { useMutation, useQueries, useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import {
  AllUsersQueryOptions,
  DeleteUserMutationOptions,
  isAdminQueryOptions,
} from "../services/AdminService";
import Loader from "../components/Loader";
import { useEffect, useState } from "react";
import axios, { isAxiosError } from "axios";
import { getSectorsQueryOptions } from "../services/SectorService";
import MainNav from "../components/MainNav";
import { getAllCompaniesQueryOptions } from "../services/CompanyService";
import Side from "../components/Side";
import StatCard from "../components/StatCard";
import { CirclePlus } from "lucide-react";
import { useToast } from "../contexts/ToastContext";

export default function DashBoaredPage() {
  const [selectedUserId, setSelectedUserId] = useState<number | undefined>(undefined);

  const [selectedSection, setSelectedSection] = useState<
    "addCompany" | "users" | "deleteUser" | "deleteCompany" | null
  >();

  const { showToast } = useToast();
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

  // const sectors = sectorsQuery.data?.data;
  const users = usersQuery.data?.data;
  // const companies = companiesQuery.data?.data;

  // const addCompanyMutation = useMutation(AddCompanyMutationOptions(newCompnay));
  // const deleteCompanyMutation = useMutation(
  //   DeleteCompanyMutationOptions(selectedUserId)
  // );
  const deleteuserMutation = useMutation(
    DeleteUserMutationOptions(selectedUserId)
  );

  async function handleDeleteUserClick(
    userRole: string | undefined,
  ) {
    
    if (userRole != undefined && userRole === "ADMIN") {
      alert("لايمكن حذف حساب مسؤول");
      return;
    }

    const adminConfirm = confirm(
      "هل أنت متأكد من الحذف؟ هذه عملية لايمكن التراجع عنها"
    );

    if (!adminConfirm) return;

    try {
      const deleteUser = await deleteuserMutation.mutateAsync();

      showToast("success", deleteUser.data);
    } catch (error) {
      if (isAxiosError(error)) {
        console.log(error.response?.data);
      } else {
        console.log("error happend in catch block", error);
      }
    }
  }
console.log("Selected id: ", selectedUserId)
console.log(selectedSection)
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
              value="add"
              Icon={CirclePlus}
              body="إضافة شركة جديدة"
              title="إضافة شركة"
              color="text-green-400"
              bodyClassName="cursor-pointer"
            />

            <StatCard
              handleClick={setSelectedSection}
              value="delete"
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

            <div className="p-4 rounded-3xl mt-2  bg-background shadow-lg overflow-y-auto">
              <table className="table-auto w-full border text-center border-collapse">
                <tr className="bg-success font-primary-thin border border-black text-white">
                  <th className="p-2">الإسم</th>
                  <th>إسم المسخدم</th>
                  <th>الصلاحيات</th>
                  <th>تاريخ التسجيل</th>
                  <th>الإيميل</th>
                  <th>حذف الحساب</th>
                </tr>
                {users?.map((user) => {
                  return (
                    <tr className="border even:bg-gray-400 font-primary-regular" key={user.userId}>
                      <td className="p-2 border" >
                        {user.name}
                      </td>

                      <td className="border" >
                        {user.username}
                      </td>

                      <td className=" border" >
                        {user.role}
                      </td>

                      <td className=" border" >
                        {user.joinDate?.split("T")[0]}
                      </td>

                      <td className=" border">
                        {user.email}
                      </td>

                      <td className="p-2">
                        <button
                          onClick={() => {
                            setSelectedUserId(user.userId)
                            handleDeleteUserClick(user.role)
                          }
                            
                          }
                          className="text-white font-primary-bold hover:bg-red-700 cursor-pointer w-full bg-fail p-2 rounded-full"
                        >
                          {user.role === "ADMIN"
                            ? "لايمكن حذف حساب مسؤول"
                            : "حذف"}
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </table>
            </div>
          </div>
        </div>
      </div>

      {/* <Footer /> */}

      {(isLoading ||
        sectorsQuery.isLoading ||
        usersQuery.isLoading ||
        deleteuserMutation.isPending ||
        companiesQuery.isLoading) && <Loader />}
    </>
  );
}
