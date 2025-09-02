import Button from "./components/Button";

export default function LandingPage() {
  return (
    <>
      <nav className="h-14 p-2 shadow-xl flex rounded-bl-2xl rounded-br-2xl">
        {/* logo & name */}
        <div className="w-1/2 flex justify-start align-middle">
            <h1>Hi</h1>
            {/* logo component */}
        </div>
        {/* logo & name */}

        {/* sign-up & log in */}
        <div className=" w-1/2 flex justify-end gap-3.5">
            <Button color="primary">إنشاء حساب</Button>
            <Button color="primary">تسجيل الدخول</Button>
        </div>
        {/* sign-up & log in */}
      </nav>
    </>
  );
}
