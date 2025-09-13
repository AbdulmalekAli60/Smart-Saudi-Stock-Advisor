import { useState } from "react";
import { Link } from "react-router-dom";
import Toast from "./Toast";

export default function DropDownMenu() {
  const [showToast, setShowToast] = useState<boolean>(false);

  function handelLogoutClick() {
    setShowToast(true);

    setTimeout(() => setShowToast(false), 3000);
  }

  return (
    <>
      <ul className="bg-gray-400  w-fit p-2 whitespace-nowrap rounded-lg space-y-3 shadow-lg absolute top-full  sm:left-10 md:left-10 lg:left-0 xl:left-0 2xl:left-0 ">
        <Link to={"/account"}>
          <li className="font-primary-regular p-3 cursor-pointer hover:bg-gray-500  hover:shadow-lg hover:rounded-full text-white">
            الحساب
          </li>
        </Link>

        <li
          onClick={handelLogoutClick}
          className="font-primary-regular p-3 cursor-pointer hover:bg-gray-500  hover:shadow-lg hover:rounded-full text-white"
        >
          تسجيل الخروج
        </li>
      </ul>

      {showToast && <Toast color="success" text="تم تسجيل الخروج بنجاح" />}
    </>
  );
}