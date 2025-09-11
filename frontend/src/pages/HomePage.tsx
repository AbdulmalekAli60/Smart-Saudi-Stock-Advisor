import Footer from "../components/Footer";
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
    </>
  );
}
