import { useMutation, useQuery } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import {
  AllUsersQueryOptions,
  DeleteUserMutationOptions,
  isAdminQueryOptions,
} from "../services/AdminService";
import Loader from "../components/Loader";
import { useEffect } from "react";
import axios, { isAxiosError } from "axios";
import MainNav from "../components/MainNav";
import { useToast } from "../contexts/ToastContext";
import Footer from "../components/Footer";

export default function DashBoaredPage() {
  const navigaet = useNavigate();

  // check if user data in storgae or back no landing page
  useEffect(() => {
    if (!sessionStorage.getItem("user")) {
      navigaet("/");
    }
  }, [navigaet]);
  const { showToast } = useToast();
  const navigate = useNavigate();

  const { isLoading, error } = useQuery(isAdminQueryOptions());

  useEffect(() => {
    if (axios.isAxiosError(error) && error.response?.status === 401) {
      navigate("/home");
    }
  }, [error, navigate]);

  const usersQuery = useQuery(AllUsersQueryOptions());
  const users = usersQuery.data?.data;

  const deleteuserMutation = useMutation(DeleteUserMutationOptions());
  async function handelUserDelete(
    userId: number | undefined,
    userRole: string | undefined
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
      if (userId != undefined) {
        await deleteuserMutation.mutateAsync(userId);
      }

      showToast("success", `تم حذف المستخدم بنجاح`);
      usersQuery.refetch();
      deleteuserMutation.reset();
    } catch (error) {
      if (isAxiosError(error)) {
        // console.log(error.response?.data);
      } else {
        // console.log("error happend in catch block", error);
      }
    }
  }

  return (
    <>
      <title>صفحة التحكم</title>
      <MainNav />
      <div className="pt-14 h-screen w-full bg-background">
        <div className=" p-4">
          <h1 className="font-primary-bold text-2xl sm:text-3xl lg:text-4xl">
            كل المستخدمين
          </h1>

          <div className="p-4 rounded-3xl mt-2  bg-background shadow-lg overflow-x-auto md:overflow-y-auto">
            <table className="table-auto w-full border text-center border-collapse ">
              <thead>
                <tr className="bg-success font-primary-thin border border-black text-white">
                  <th className="p-2">الإسم</th>
                  <th>إسم المسخدم</th>
                  <th>الصلاحيات</th>
                  <th>تاريخ التسجيل</th>
                  <th>الإيميل</th>
                  <th>حذف الحساب</th>
                </tr>
              </thead>
              {users?.map((user) => {
                return (
                  <thead key={user.userId}>
                    <tr className="border even:bg-gray-400 font-primary-regular">
                      <td key={user.name} className="p-2 border">
                        {user.name}
                      </td>

                      <td key={user.username} className="border">
                        {user.username}
                      </td>

                      <td key={user.role} className=" border">
                        {user.role}
                      </td>

                      <td key={user.joinDate} className=" border">
                        {user.joinDate?.split("T")[0]}
                      </td>

                      <td key={user.email} className=" border">
                        {user.email}
                      </td>

                      <td className="p-2">
                        <button
                          onClick={() => {
                            handelUserDelete(user.userId, user.role);
                          }}
                          className="text-white whitespace-nowrap font-primary-bold hover:bg-red-700 cursor-pointer w-full bg-fail p-2 rounded-full"
                        >
                          {user.role === "ADMIN"
                            ? "لايمكن حذف حساب مسؤول"
                            : "حذف"}
                        </button>
                      </td>
                    </tr>
                  </thead>
                );
              })}
            </table>
          </div>
        </div>
      </div>

      <Footer />

      {(isLoading || usersQuery.isLoading, deleteuserMutation.isPending) && (
        <Loader />
      )}
    </>
  );
}
