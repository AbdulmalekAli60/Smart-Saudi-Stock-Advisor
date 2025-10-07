import { useState } from "react";
import Input from "../components/Input";
import { useMutation, useQuery } from "@tanstack/react-query";
import {
  deleteAccountMutationOptions,
  updateUserDataMutationOptions,
  userDataQueryOptions,
} from "../services/UserService";
import Loader from "../components/Loader";
import { UpdateInfo } from "../Interfaces/UpdateInfoInterface";
import { useToast } from "../contexts/ToastContext";
import axios, { isAxiosError } from "axios";
import errorResponse from "../Interfaces/ErrorInterface";
import MainNav from "../components/MainNav";
import BookMark from "../components/BookMark";
import { WatchListQueryOptions } from "../services/WatchListService";
import { BookmarkX } from "lucide-react";
import { useNavigate } from "react-router-dom";
import { useUserInfo } from "../contexts/UserContext";

export default function AccountPage() {
  const [updateData, setUpdateData] = useState<boolean>(false);
  const [updatedInfo, setUpdatedInfo] = useState<UpdateInfo>({
    email: null,
    name: null,
    password: null,
    username: null,
    investAmount: null,
  });

  const { setCurrentUserData } = useUserInfo();
  const navigate = useNavigate();
  const { showToast } = useToast();

  const isUpdateData = updateData ? false : true;

  const { data, isLoading, refetch } = useQuery(userDataQueryOptions());

  const {
    data: watchListData,
    isLoading: watchListLoading,
    refetch: watchListRefetch,
  } = useQuery(WatchListQueryOptions());

  const mutation = useMutation(updateUserDataMutationOptions(updatedInfo));

  const deleteMutation = useMutation(deleteAccountMutationOptions());

  const formFields = [
    {
      id: "name-input",
      name: "name",
      label: "الاسم",
      type: "text",
      value: updatedInfo.name ?? data?.data.name ?? "",
      placeholder: "",
    },
    {
      id: "username-input",
      name: "username",
      label: "إسم المستخدم",
      type: "text",
      value: updatedInfo.username ?? data?.data.username ?? "",
      placeholder: "",
    },
    {
      id: "email-input",
      name: "email",
      label: "الايميل",
      type: "email",
      value: updatedInfo.email ?? data?.data.email ?? "",
      placeholder: "",
    },
    {
      id: "amount-input",
      name: "investAmount",
      label: "مبلغ الإستثمار",
      type: "text",
      value: updatedInfo.investAmount ?? data?.data.investAmount ?? "",
      placeholder: "",
    },
    {
      id: "password-input",
      name: "password",
      label: "الرقم السري",
      type: "password",
      value: updatedInfo.password ?? "",
      placeholder: "أدخل الرقم السري الجديد",
    },
  ];

  function handleInputChange(e: React.ChangeEvent<HTMLInputElement>) {
    const { name, value } = e.target;

    setUpdatedInfo((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  async function handleUpdateClick() {
    try {
      const result = await mutation.mutateAsync();

      showToast("success", result.data.message as string);
      setCurrentUserData(result.data);
      refetch();

      if (updatedInfo.password !== null) {
        setUpdatedInfo({ ...updatedInfo, password: null });
      }
    } catch (error) {
      if (isAxiosError(error)) {
        console.log("The errro is: ", error.response?.data);
        showToast("fail", "حدث خطأ أثناء تحديث البيانات");
      } else {
        console.log("error happend in catch block", error);
      }
    }
  }

  function renderErrorMessages() {
    if (
      !mutation.isError ||
      !axios.isAxiosError(mutation.error) ||
      !mutation.error.response?.data
    ) {
      return null;
    }

    const errorData = mutation.error.response.data as errorResponse;

    if (errorData.errorMessage) {
      return <li>{errorData.errorMessage}</li>;
    }

    return Object.values(mutation.error.response.data).map((message, key) => (
      <li key={key}>{message}</li>
    ));
  }
  async function handleDeleteAccountClick() {
    try {
      const userChoice = confirm("هل أنت متأكد من الحذف؟ لايمكنك التراجع بعد الحذف")
      if(!userChoice){
        return
      }
      const response = await deleteMutation.mutateAsync();
      sessionStorage.removeItem("user");
      navigate("/");
      showToast("success", response.data);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        showToast("fail", error.response?.data);
        console.log(error);
        return;
      }
      console.log(error);
    }
  }

  return (
    <>
      <MainNav />

      <main className="max-w-4xl mx-auto p-6 bg-white ">
        {/* Header Section */}
        <div className="mb-8 mt-14">
          <h1 className="text-2xl font-bold text-gray-800 mb-2">
            معلومات الحساب
          </h1>
          <div className="flex items-center justify-between bg-gray-50 p-4 rounded-lg">
            <div className="flex items-center gap-2">
              <span className="text-sm font-medium text-gray-600">
                تاريخ التسجيل:
              </span>
              <span className="text-sm text-gray-800 font-primary-regular">
                {data?.data.joinDate?.split("T")[0] || "غير متوفر"}
              </span>
            </div>
            <button
              onClick={handleDeleteAccountClick}
              className="bg-fail text-white font-primary-bold p-2 rounded-full hover:bg-red-500 cursor-pointer"
            >
              حذف الحساب
            </button>
          </div>
        </div>

        {/* for erorr */}
        <div className="font-primary-bold text-fail mr-7 mb-3 h-fit ">
          {renderErrorMessages()}
        </div>

        <div className="bg-white border border-gray-200 rounded-xl p-6 mb-8 shadow-sm">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {formFields.map((field) => (
              <div key={field.name} className="space-y-2">
                <label
                  htmlFor={field.id}
                  className="text-sm font-primary-bold text-gray-700 mb-3"
                >
                  {field.label}
                </label>
                <Input
                  id={field.id}
                  isDisabled={isUpdateData}
                  isRequired={false}
                  name={field.name}
                  type={field.type}
                  onChange={handleInputChange}
                  value={field.value?.toString() || ""}
                  placeholder={field.placeholder}
                />
              </div>
            ))}
          </div>
        </div>

        <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
          <button
            className="w-full cursor-pointer sm:w-48 py-3 px-6 font-medium bg-primary hover:bg-primary-light text-white rounded-lg transition-colors duration-200 shadow-sm hover:shadow-md"
            onClick={() => setUpdateData(!updateData)}
          >
            {isUpdateData ? "تغيير البيانات" : "إلغاء"}
          </button>

          <button
            className={`${
              isUpdateData ? "hidden" : ""
            } w-full sm:w-48 py-3 px-6 font-medium cursor-pointer bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors duration-200 shadow-sm hover:shadow-md`}
            onClick={handleUpdateClick}
          >
            حفظ التغييرات
          </button>
        </div>

        <section className="bg-gray-400 w-full min-h-fit  rounded-3xl  shadow-lg mt-4  font-primary-bold p-4 ">
          <h1 className="w-full  sm:text-sm md:text-xl lg:text-2xl text-center text-black">
            الشركات المفضلة
          </h1>
          {(watchListData?.data?.length ?? 0) > 0 ? (
            watchListData?.data.map((item) => {
              return (
                <BookMark
                  key={item.companyId}
                  data={item}
                  onBookmarkChaneg={watchListRefetch}
                />
              );
            })
          ) : (
            <div className="text-center font-primary-regular mt-7 space-x-2">
              <h1 className="inline sm:text-sm md:text-xl lg:text-2xl">
                لايوجد
              </h1>
              <BookmarkX className="inline" />
            </div>
          )}
        </section>

        {(isLoading || mutation.isPending || watchListLoading) && <Loader />}
      </main>
    </>
  );
}
