import { useQuery } from "@tanstack/react-query";
import Badge from "../components/Badge";
import HomePageNav from "../components/HomePageNav";
import { useUserInfo } from "../contexts/UserContext";
import { getSectorsQueryOptions } from "../services/SectorService";
import Loader from "../components/Loader";
<title>Home page</title>;
export default function HomePage() {
  // check if auth or back no landing page
  const { currentUserData } = useUserInfo();

  const { data, isLoading } = useQuery(getSectorsQueryOptions());
  return (
    <main>
      {/* nav */}
      <div className="h-12 md:h-14 lg:h-16">
        <HomePageNav />
      </div>
      {/* nav */}

      {/* welcome section */}
      <div className=" h-32 p-6">
        <h1 className="font-primary-thin sm:text-1xl md:text-2xl lg:text-3xl xl:text-4xl 2xl:text-5xl">
          اهلا {currentUserData.name}
        </h1>
      </div>
      {/* welcome section */}

      {/* sectors */}
      <section className=" h-56 p-6">
        <h1 className="font-primary-bold sm:text-1xl md:text-2xl lg:text-3xl xl:text-4xl 2xl:text-5xl">
          الأقسام
        </h1>
        <br />
        <div className="flex shrink-0 gap-3 flex-1 flex-wrap grow">
          {!isLoading &&
            data?.data.map(({ sectorId, sectorArabicName }) => {
              return <Badge key={sectorId} sectorId={sectorId} arabicName={sectorArabicName} />;
            })}
        </div>
      </section>
      {/* sectors */}

      {/* companies */}
      <section>

      </section>
      {/* companies */}

      {/* <Footer /> */}

      {isLoading && <Loader />}
    </main>
  );
}
