import { useMutation } from "@tanstack/react-query";
import { Link, useNavigate } from "react-router-dom";
import { LogoutMutationOptions } from "../services/AuthService";
import { useToast } from "../contexts/ToastContext";

export default function DropDownMenu() {
  const { showToast } = useToast();
  const mutation = useMutation(LogoutMutationOptions());

  const navigate = useNavigate();

  async function handelLogoutClick() {
    const response = await mutation.mutateAsync();

    sessionStorage.removeItem("user");
    // console.log("the message is: ", response.data.message);
    showToast("success", response.data.message);

    setTimeout(() => {
      navigate("/");
    }, 500);
  }

  return (
    <div className="fixed top-12 left-2 sm:left-7 md:left-4 lg:left-5 xl:left-5 2xl:left-5 shadow-lg bg-gray-400 rounded-lg z-50">
      <ul className=" w-fit p-2 whitespace-nowrap  space-y-3 ">
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
    </div>
  );
}
