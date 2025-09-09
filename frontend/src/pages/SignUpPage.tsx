import { Link } from "react-router-dom";
import SignUpAnimation from "../animations/SignUpAnimation";
import { UserPlus } from "lucide-react";
import Footer from "../components/Footer";
import Input from "../components/Input";
import { useState } from "react";
import { SignUp } from "../Interfaces/AuthInterfaces";

export default function SignUpPage() {
  const [signUpFormData, setSignUpFormData] = useState<SignUp>({
    email: "",
    name: "",
    password: "",
    username: "",
  });

  function handleInputChange(e: React.ChangeEvent<HTMLInputElement>) {
    const { name, value } = e.target;

    setSignUpFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  function handleSubmitClick(
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>
  ) {
    e.preventDefault();

    console.log(signUpFormData);
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
                  <UserPlus className="h-8 w-8 lg:h-10 lg:w-10 text-white" />
                </div>
                <h1 className="font-primary-bold text-xl lg:text-2xl">
                  إنشاء حساب جديد
                </h1>
              </div>

              {/* Form */}
              <form className="space-y-4">
                <div>
                  <label
                    htmlFor="name"
                    className="block text-base lg:text-lg font-primary-regular text-gray-700 mb-2"
                  >
                    الإسم
                  </label>
                  <Input
                    id="name"
                    name="name"
                    type="text"
                    placeholder="أدخل الإسم"
                    onChange={handleInputChange}
                  />
                  <span className="text-fail"></span>
                </div>

                <div>
                  <label
                    htmlFor="username"
                    className="block text-base lg:text-lg font-primary-regular text-gray-700 mb-2"
                  >
                    إسم المستخدم
                  </label>
                  <Input
                    id="username"
                    name="username"
                    type="text"
                    placeholder="أدخل إسم المستخدم"
                    onChange={handleInputChange}
                  />
                  <span className="text-fail"></span>
                </div>

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
                    type="password"
                    placeholder="أدخل الرقم السىري"
                    onChange={handleInputChange}
                  />
                  <span className="text-fail"></span>
                </div>

                <button
                  // type="submit"
                  onClick={(e) => handleSubmitClick(e)}
                  className="w-full p-3 lg:p-4 mt-6 bg-secondary text-white font-primary-bold rounded-lg cursor-pointer hover:bg-amber-400 transition-colors duration-200"
                >
                  إنشاء حساب
                </button>
              </form>

              <div className="text-center">
                <p className="font-primary-regular text-sm lg:text-base">
                  لديك حساب؟{" "}
                  <Link to={"/log-in"}>
                    <span className="text-primary font-primary-bold hover:text-primary-light transition-colors duration-200">
                      تسجيل الدخول
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
            <SignUpAnimation />
          </div>
        </section>
      </main>

      <Footer />
    </>
  );
}
