import { useQueries } from "@tanstack/react-query";
import Badge from "../components/Badge";
import HomePageNav from "../components/HomePageNav";
import { useUserInfo } from "../contexts/UserContext";
import { getSectorsQueryOptions } from "../services/SectorService";
import Loader from "../components/Loader";
import { useEffect, useState } from "react";
import CompanyCard from "../components/CompanyCard";
import Footer from "../components/Footer";
import { getAllCompaniesQueryOptions } from "../services/CompanyService";
import { useNavigate } from "react-router-dom";
export default function HomePage() {
  const [selectedSector, setSelectedSector] = useState<number | null>(null);
  const navigaet = useNavigate();

  // check if user data in storgae or back no landing page
  useEffect(() => {
    if (!sessionStorage.getItem("user")) {
      navigaet("/");
    }
  }, []);

  const { currentUserData } = useUserInfo();
  const results = useQueries({
    queries: [getSectorsQueryOptions(), getAllCompaniesQueryOptions()],
  });
  const [sectorQuery, companiesQuery] = results;
  const sectorData = sectorQuery.data;
  const companiesData = companiesQuery.data;

  const isLoading = sectorQuery.isLoading || companiesQuery.isLoading;

  return (
    <main>
      {/* nav */}
      <div className="h-12 md:h-14 lg:h-16">
        <HomePageNav />
      </div>
      {/* nav */}

      {/* welcome section */}
      <div className=" h-32 p-6 mt-4">
        <h1 className="font-primary-thin sm:text-1xl md:text-2xl lg:text-3xl xl:text-4xl 2xl:text-5xl">
          اهلا {currentUserData.name}
        </h1>
      </div>
      {/* welcome section */}

      {/* sectors */}
      <section className="min-h-56 max-h-fit p-6">
        <h1 className="font-primary-bold mb-8 sm:text-1xl md:text-2xl lg:text-3xl xl:text-4xl 2xl:text-5xl">
          الأقسام
        </h1>
        <div className="flex shrink-0 gap-3 flex-1 flex-wrap grow">
          <Badge
            key="all"
            sectorId={0}
            arabicName="الكل"
            isSelected={selectedSector === null}
            onSelect={() => setSelectedSector(null)}
          />
          {!isLoading &&
            sectorData?.data.map(({ sectorId, sectorArabicName }) => {
              return (
                <Badge
                  key={sectorId}
                  sectorId={sectorId}
                  arabicName={sectorArabicName}
                  isSelected={selectedSector === sectorId}
                  onSelect={setSelectedSector}
                />
              );
            })}
        </div>
      </section>
      {/* sectors */}

      {/* companies */}
      <section className="h-fit p-6 pb-8">
        <h1 className="font-primary-bold mb-6 sm:text-1xl md:text-2xl lg:text-3xl xl:text-4xl 2xl:text-5xl">
          الشركات
        </h1>

        <div className="grid gap-7 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-4">
          {!isLoading &&
            companiesData?.data.map(
              ({
                companyId,
                companyLogo,
                tickerName,
                sectorId,
                sectorArabicName,
                companyArabicName,
                companyEnglishName,
                sectorEnglishName,
              }) => {
                return (
                  <CompanyCard
                    key={companyId}
                    companyArabicName={companyArabicName}
                    companyId={companyId}
                    companyLogo={companyLogo}
                    companyEnglishName={companyEnglishName}
                    sectorArabicName={sectorArabicName}
                    tickerName={tickerName}
                    sectorId={sectorId}
                    sectorEnglishName={sectorEnglishName}
                  />
                );
              }
            )}
        </div>
      </section>
      {/* companies */}

      <Footer />

      {isLoading && <Loader />}
    </main>
  );
}
