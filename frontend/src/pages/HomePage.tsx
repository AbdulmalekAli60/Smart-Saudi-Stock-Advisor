import Footer from "../components/Footer";
import Toast from "../components/Toast";
import { useUserInfo } from "../contexts/UserContext";

export default function HomePage() {

  // check if auth or back no landing page

  const {currentUserData} = useUserInfo()
  return (
    <>
      {/* home page nav component */}
      <div className="flex items-center justify-center text-5xl">
        this is home Page
        {currentUserData.name}
      </div>

      <Footer />
      <br />
      <br />
      <br />

      <Toast text="تم تسجيل الدخول بنجاح" color="success" />

    </>
  );
}
