import { Frown } from "lucide-react";

export default function ErrorComponent() {
  return (
    <div className="bg-gray-200 h-screen w-screen flex items-center justify-center">

      <div className="bg-gray-300 w-9/12 md:w-fit p-4 rounded-2xl">
      
        <div className="flex items-center justify-center gap-4 mb-4">
          <h1 className="font-primary-bold  text-sm sm:text-base md:text-2xl lg:text-3xl">
            حصل خطأ
          </h1>
          <Frown className="text-fail w-12 h-12" />
        </div>

        <h2 className="font-primary-thin text-accent">
          الرجاء العودة الى الصفحة الرئيسة و تسجيل الدخول مجددا
        </h2>

        <button
          onClick={() => window.location.href = "/home"}
          className="bg-primary-light mt-4 text-white font-primary-bold hover:bg-primary cursor-pointer p-2 rounded-full w-full"
        >
          العودة الى الصفحة الرئيسة
        </button>
      </div>

    </div>
  );
}
