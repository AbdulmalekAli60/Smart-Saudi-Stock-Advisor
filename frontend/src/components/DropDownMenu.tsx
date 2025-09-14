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
    console.log("the message is: ", response.data.message);
    showToast("success", response.data.message);

    setTimeout(() => {
      navigate("/");
    }, 500);
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
    </>
  );
}
