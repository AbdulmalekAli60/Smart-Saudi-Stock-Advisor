import { Link, redirect } from "react-router-dom";
import LogInAnimation from "../animations/LogInAnimation";
import Footer from "../components/Footer";
import { Loader, LogIn } from "lucide-react";
import React, { useState } from "react";
import { LogInState } from "../Interfaces/AuthInterfaces";
import Input from "../components/Input";
import { useMutation } from "@tanstack/react-query";
import { logInMutationOptions } from "../services/AuthService";

export default function LogInPage() {
  const [logInFormData, setLogInFormData] = useState<LogInState>({
    email: "",
    password: "",
  });

  const mutation = useMutation(logInMutationOptions());

  async function handleSubmitClick(e: React.FormEvent<HTMLButtonElement>) {
    e.preventDefault();
    console.log("Log in Data", logInFormData);

    try {
      const response = await mutation.mutateAsync(logInFormData);
      setLogInFormData({ email: "", password: "" });

      sessionStorage.setItem("user", JSON.stringify(response.data));

      redirect("/home")
    } catch (error) {
      console.log(error);
    }
  }

  function handleInputChange(e: React.ChangeEvent<HTMLInputElement>) {
    const { name, value } = e.target;

    setLogInFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  return (
    <>
      <main className="flex flex-col lg:flex-row min-h-screen">
        {/* Form Section */}
        <section className="w-full lg:w-3/5 flex items-center justify-center p-4 lg:p-8">
          <div className="w-full max-w-md lg:max-w-lg bg-gray-100 rounded-3xl shadow-lg p-6 lg:p-8">
            <div className="space-y-4 lg:space-y-6">
              {/* Logo */}
              <div className="text-center">
                <div className="w-16 h-16 lg:w-20 lg:h-20 bg-accent shadow-lg inline-flex items-center justify-center rounded-full mb-3">
                  <LogIn className="h-8 w-8 lg:h-10 lg:w-10 text-white" />
                </div>
                <h1 className="font-primary-bold text-xl lg:text-2xl">
                  تسجيل الدخول
                </h1>
              </div>

              {/* Form */}
              <form className="space-y-4">
                <div>
                  <label
                    htmlFor="email"
                    className="block text-base lg:text-lg font-primary-regular text-gray-700 mb-2"
                  >
                    الإيميل
                  </label>
                  <Input
                    id="email"
                    name="email"
                    type="email"
                    placeholder="أدخل الإيميل"
                    isRequired={true}
                    onChange={handleInputChange}
                  />
                  <span className="text-fail"></span>
                </div>

                <div>
                  <label
                    htmlFor="password"
                    className="block text-base lg:text-lg font-primary-regular text-gray-700 mb-2"
                  >
                    الرقم السري
                  </label>
                  <Input
                    id="password"
                    name="password"
                    type="text"
                    placeholder="أدخل الرقم السىري"
                    isRequired={true}
                    onChange={handleInputChange}
                  />
                  <span className="text-fail"></span>
                </div>

                <button
                  type="button"
                  onClick={(e) => handleSubmitClick(e)}
                  className="w-full p-3 lg:p-4 mt-6 bg-secondary text-white font-primary-bold rounded-lg cursor-pointer hover:bg-amber-400 transition-colors duration-200"
                >
                  {mutation.isPending ? <Loader className="flex items-center justify-center" /> : "إنشاء حساب"}
                </button>
              </form>

              <div className="text-center">
                <p className="font-primary-regular text-sm lg:text-base">
                  ليس لديك حساب؟{" "}
                  <Link to={"/log-in"}>
                    <span className="text-primary font-primary-bold hover:text-primary-light transition-colors duration-200">
                      إنشاء حساب
                    </span>
                  </Link>
                </p>
              </div>
            </div>
          </div>
        </section>

        <section
          className="hidden lg:flex lg:w-2/5 items-center justify-center"
          style={{ background: "var(--gradient-hero)" }}
        >
          <div className="w-full h-full flex items-center justify-center p-8">
            <LogInAnimation />
          </div>
        </section>
      </main>

      <Footer />
    </>
  );
}
