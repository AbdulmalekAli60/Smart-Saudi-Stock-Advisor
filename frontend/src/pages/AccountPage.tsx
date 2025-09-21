import { useState } from "react";
import Input from "../components/Input";
import { useQuery } from "@tanstack/react-query";
import { userDataQueryOptions } from "../services/UserService";
import Loader from "../components/Loader";
import { UpdateInfo } from "../Interfaces/UpdateInfoInterface";

export default function AccountPage() {
  const [updateData, setUpdateData] = useState<boolean>(false);
  const [updatedInfo, setUpdatedInfo] = useState<UpdateInfo>({
    email: null,
    name: null,
    password: null,
    username: null,
  });

  const isUpdateData = updateData ? false : true;

  const { data, isLoading } = useQuery(userDataQueryOptions());

  const formFields = [
    {
      id: "name-input",
      name: "name",
      label: "الاسم",
      type: "text",
      value: data?.data.name,
      placeholder: "",
    },
    {
      id: "username-input",
      name: "username",
      label: "إسم المستخدم",
      type: "text",
      value: data?.data.username,
      placeholder: "",
    },
    {
      id: "email-input",
      name: "email",
      label: "الايميل",
      type: "email",
      value: data?.data.email,
      placeholder: "",
    },
    {
      id: "amount-input",
      name: "investmentAmount",
      label: "مبلغ الإستثمار",
      type: "text",
      value: data?.data.investAmount,
      placeholder: "",
    },
    {
      id: "password-input",
      name: "password",
      label: "الرقم السري",
      type: "password",
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

  console.log({ ...updatedInfo });

  return (
    <main className="max-w-4xl mx-auto p-6 h-fit bg-white">
      {/* Header Section */}
      <div className="mb-8">
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
        </div>
      </div>

      {/* for erorr */}
      <div className="font-primary-bold text-fail mr-7 mb- h-fit ">
        <li>asd</li>
      </div>

      <div className="bg-white border border-gray-200 rounded-xl p-6 mb-8 shadow-sm">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {formFields.map((field) => (
            <div key={field.name} className="space-y-2">
              <label
                htmlFor={field.id}
                className="text-sm font-primary-bold text-gray-700 mb-1"
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
                value={field.value?.toString() || ""} // fix this
                placeholder={field.placeholder}
              />
            </div>
          ))}
        </div>
      </div>

      <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
        <button
          className="w-full sm:w-48 py-3 px-6 font-medium bg-primary hover:bg-primary-light text-white rounded-lg transition-colors duration-200 shadow-sm hover:shadow-md"
          onClick={() => setUpdateData(!updateData)}
        >
          {isUpdateData ? "تغيير البيانات" : "إلغاء"}
        </button>

        <button
          className={`${
            isUpdateData ? "hidden" : ""
          } w-full sm:w-48 py-3 px-6 font-medium bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors duration-200 shadow-sm hover:shadow-md`}
          onClick={() => setUpdateData(!updateData)}
        >
          حفظ التغييرات
        </button>
      </div>

      {/*       
      <section className="bg-green-400 w-full h-3/5 ">
        <div className="bg-accent max-w-4/5 h-full ">WatchList section</div>
      </section> */}

      {isLoading && <Loader />}
    </main>
  );
}
