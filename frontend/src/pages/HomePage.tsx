import Badge from "../components/Badge";
import HomePageNav from "../components/HomePageNav";
import { useUserInfo } from "../contexts/UserContext";
<title>Home page</title>;
export default function HomePage() {
  // check if auth or back no landing page

  const { currentUserData } = useUserInfo();
  return (
    <main>
      {/* nav */}
      <div className="h-12 md:h-14 lg:h-16">
        <HomePageNav />
      </div>
      {/* nav */}

      {/* welcome section */}
      <div>اهلا {currentUserData.name}</div>
      {/* welcome section */}

      {/* sectors */}
      <Badge arabicName="Hi" />
      {/* sectors */}

      {/* <Footer /> */}
    </main>
  );
}
