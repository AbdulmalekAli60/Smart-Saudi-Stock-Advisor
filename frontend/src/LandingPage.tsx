import Button from "./components/Button";
import Logo from "./components/Logo";

export default function LandingPage() {
  return (
    <>
      <nav className="h-14 p-2 shadow-xl flex rounded-bl-2xl rounded-br-2xl">
        {/* logo*/}
        <div className="w-1/2 flex justify-start align-middle">
          <Logo />
        </div>
        {/* logo*/}

        {/* sign-up & log in */}
        <div className=" w-1/2 flex justify-end gap-3.5">
          <Button color="primary">إنشاء حساب</Button>
          <Button color="primary">تسجيل الدخول</Button>
        </div>
        {/* sign-up & log in */}
      </nav>

      {/* hero */}
      <section
        className="h-dvh relative"
        style={{ background: "var(--gradient-hero)" }}
      >
        <div className="absolute top-1/4 text-center w-full">
          <h1 className="font-primary-bold text-white text-6xl mb-6">
            تنبؤات الأسهم بالذكاء الاصطناعي
          </h1>
          <p className="font-primary-regular text-accent">
            احصل على تنبؤات دقيقة لحركة الأسهم باستخدام أحدث تقنيات الذكاء
            الاصطناعي
          </p>
        </div>

        <div className="w-full absolute top-1/2 text-center sm:mt-3">

          <p className="font-primary-bold text-secondary text-4xl mb-6">إبدأ الان</p>

          <div className=" space-x-3.5">
            <Button color="text-primary">إنشاء حساب</Button>
            <Button color="text-primary">تسجيل الدخول</Button>
          </div>

        </div>
      </section>
      {/* hero */}
    </>
  );
}
