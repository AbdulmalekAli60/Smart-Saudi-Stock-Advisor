import { useQueries } from "@tanstack/react-query";
import Badge from "../components/Badge";
import MainNav from "../components/MainNav";
import { useUserInfo } from "../contexts/UserContext";
import { getSectorsQueryOptions } from "../services/SectorService";
import Loader from "../components/Loader";
import { useEffect, useState } from "react";
import CompanyCard from "../components/CompanyCard";
import Footer from "../components/Footer";
import { getAllCompaniesQueryOptions } from "../services/CompanyService";
import { useNavigate } from "react-router-dom";
import { WatchListQueryOptions } from "../services/WatchListService";

export default function HomePage() {
  const [selectedSector, setSelectedSector] = useState<number | null>(null);
  const navigaet = useNavigate();

  useEffect(() => {
    if (!sessionStorage.getItem("user")) {
      navigaet("/");
    }
  }, [navigaet]);

  const { currentUserData } = useUserInfo();
  const results = useQueries({
    queries: [
      getSectorsQueryOptions(),
      getAllCompaniesQueryOptions(),
      WatchListQueryOptions(),
    ],
  });
  const [sectorQuery, companiesQuery, watchlists] = results;

  const sectorData = sectorQuery.data;
  const companiesData = companiesQuery.data;
  const watchlistsData = watchlists.data;

  const isLoading = sectorQuery.isLoading || companiesQuery.isLoading || watchlists.isLoading;

  const filteredData = companiesData?.data.filter(({sectorId}) => {
    return selectedSector === sectorId || selectedSector === null;
  });

  const bookmarkedCompanies = new Map();

  watchlistsData?.data.forEach((item) => {
    bookmarkedCompanies.set(item.companyId, item.watchListId);
  });
  
  return (
    <>
      <title>الرئيسة</title>

      <main className="min-h-screen flex flex-col bg-background">
        
        <div className="bg-primary pb-12">
            <div className="h-12 md:h-14 lg:h-16">
              <MainNav />
            </div>

            <div className="container mx-auto px-6 mt-8 mb-4">
              <h1 className="font-primary-thin text-white text-3xl md:text-4xl lg:text-5xl">
                اهلا، <span className="font-primary-bold text-[var(--color-secondary)]">{currentUserData.name}</span>
              </h1>
              <p className="text-gray-300 mt-2 font-primary-thin">تابع أحدث تحليلات السوق السعودي بالذكاء الاصطناعي</p>
            </div>
        </div>

        <div className="container mx-auto px-6 -mt-8 flex-grow">
            
            <section className="bg-white rounded-xl shadow-sm border border-border p-6 mb-8 mt-12">
              <h1 className="font-primary-bold text-text-primary mb-6 text-xl md:text-2xl">
                الأقسام
              </h1>
              <div className="flex flex-wrap gap-3">
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

            {/* Companies */}
            <section className="h-fit pb-12">
              <h1 className="font-primary-bold text-text-primary mb-6 text-xl md:text-2xl">
                الشركات
              </h1>

              <div className="grid gap-6 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                {!isLoading &&
                  filteredData?.map((company) => {
                    const isBookmarked = bookmarkedCompanies.has(company.companyId);
                    const watchListId = bookmarkedCompanies.get(company.companyId);
                    return (
                      <CompanyCard
                        key={company.companyId}
                        compnayData={company}
                        isBookmarked={isBookmarked}
                        watchListId={watchListId}
                        onBookMarkChange={watchlists.refetch}
                      />
                    );
                  })}
              </div>
            </section>
        </div>

        <Footer />
        {isLoading && <Loader />}
      </main>
    </>
  );
}