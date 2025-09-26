import { useQueries } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import { companyById } from "../services/CompanyService";
import Loader from "../components/Loader";
import {
  allPredictionsQueryOptions,
  latestPredictionsQueryOptions,
} from "../services/PredictionService";
import { getHistoricalDataQueryOptions } from "../services/HistoricalDataService";
import HomePageNav from "../components/HomePageNav";
import {
  CandlestickChart,
  Database,
  Factory,
  SaudiRiyal,
  TrendingUpDown,
  X,
} from "lucide-react";
import { WatchListQueryOptions } from "../services/WatchListService";
import { useUserInfo } from "../contexts/UserContext";
import { useState } from "react";
import PredictionsChart from "../components/PredictionsChart";

export default function CompanyPage() {
  const { companyId } = useParams();
  const { currentUserData } = useUserInfo();
  const [isHistoricalData, setIsHistricalData] = useState<boolean>(false);
  const [isAside, setIsAside] = useState<boolean>(true);
  const result = useQueries({
    queries: [
      companyById(companyId),
      allPredictionsQueryOptions(companyId),
      latestPredictionsQueryOptions(companyId),
      getHistoricalDataQueryOptions(companyId),
      WatchListQueryOptions(),
    ],
  });

  const [
    companyQuery,
    predictionsQuery,
    latestPredictionQuery,
    historicalDataQuery,
    watchListQuery,
  ] = result;

  const company = companyQuery.data?.data;
  // const watchListData = watchListQuery.data?.data;
  const latestPredction = latestPredictionQuery.data?.data;
  const historical = historicalDataQuery.data?.data;
  console.log(isHistoricalData);
  return (
    <>
      <HomePageNav />

      <main className="w-full h-screen flex pt-14">
        {/* side */}

        {isAside && (
          <div
            className="fixed  h-screen w-screen bg-gray-500 opacity-50 z-40 md:hidden lg:hidden"
            onClick={() => setIsAside(false)}
          />
        )}
        {isAside && (
          <aside
            className={`
              fixed md:relative top-14 md:top-0 m-auto z-50 md:z-0
              bg-white 
              w-80 md:w-72 lg:w-1/4 xl:w-1/5 
              h-full border-r border-gray-200 
              sm:w-full
              p-3 md:p-4 lg:p-6
              flex flex-col 
              overflow-y-auto
              transform transition-all duration-300
              translate-x-0

            `}
          >
            <div className="flex justify-between items-start mb-4 md:mb-6">
              <button
                onClick={() => setIsAside(false)}
                className="md:hidden bg-gray-100 cursor-pointer hover:bg-red-100 text-gray-600 hover:text-red-600 p-2 rounded-full transition-colors"
              >
                <X size={20} />
              </button>

              <div className="w-20 h-20 md:w-24 md:h-24 lg:w-32 lg:h-32 bg-gray-50 rounded-lg flex items-center justify-center border p-2 mx-auto">
                <img
                  className="max-w-full max-h-full object-contain"
                  src={company?.companyLogo}
                  alt="company logo"
                />
              </div>

              <button
                onClick={() => setIsAside(false)}
                className="hidden md:block bg-gray-100 hover:bg-red-100 text-gray-600 hover:text-red-600 p-2 rounded-full transition-colors"
              >
                <X size={20} />
              </button>
            </div>

            <div className="space-y-3 md:space-y-4">
              <div className="text-center border-b border-gray-100 pb-3 md:pb-4">
                <h1 className="font-bold text-base md:text-lg lg:text-xl text-gray-900 mb-1 md:mb-2">
                  {company?.companyArabicName}
                </h1>
                <h2 className="text-xs md:text-sm text-gray-600">
                  {company?.companyEnglishName}
                </h2>
              </div>

              <div className="space-y-2 md:space-y-3">
                <div className="flex items-center gap-2 md:gap-3 p-2 md:p-3 bg-gray-50 rounded-lg">
                  <CandlestickChart className="w-4 h-4 md:w-5 md:h-5 text-blue-600 flex-shrink-0" />
                  <div className="min-w-0 flex-1">
                    <p className="text-xs text-gray-500">رمز التداول</p>
                    <p className="font-medium text-sm md:text-base text-gray-900 truncate">
                      {company?.tickerName.split(".")[0]}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-2 md:gap-3 p-2 md:p-3 bg-gray-50 rounded-lg">
                  <Factory className="w-4 h-4 md:w-5 md:h-5 text-green-600 flex-shrink-0" />
                  <div className="min-w-0 flex-1">
                    <p className="text-xs text-gray-500">القطاع</p>
                    <p className="font-medium text-sm md:text-base text-gray-900 truncate">
                      {company?.sectorArabicName}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-2 md:gap-3 p-2 md:p-3 bg-gray-50 rounded-lg">
                  <TrendingUpDown className="w-4 h-4 md:w-5 md:h-5 text-blue-600 flex-shrink-0" />
                  <div className="min-w-0 flex-1">
                    <p className="text-xs text-gray-500">
                      توقع سعر تاريخ:{" "}
                      {latestPredction?.expirationDate.split("T")[0]}
                    </p>
                    <p className="font-medium text-sm md:text-base text-gray-900">
                      {latestPredction?.prediction.toFixed(2)}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-2 md:gap-3 p-2 md:p-3 bg-gray-50 rounded-lg">
                  <Database className="w-4 h-4 md:w-5 md:h-5 text-gray-600 flex-shrink-0" />
                  <div className="min-w-0 flex-1">
                    <p className="text-xs text-gray-500">
                      عدد البيانات التي تم إستخدامها للتحليل
                    </p>
                    <p className="font-medium text-sm md:text-base text-gray-900">
                      {historical?.length}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-2 md:gap-3 p-2 md:p-3 bg-gray-50 rounded-lg">
                  <SaudiRiyal className="w-4 h-4 md:w-5 md:h-5 text-gray-600 flex-shrink-0" />
                  <div className="min-w-0 flex-1">
                    <p className="text-xs text-gray-500">
                      الأرباح المتوقعة بناءا على مبلغ الإستثمار
                    </p>
                    <p className="font-medium text-sm md:text-base text-gray-900">
                      {latestPredction?.prediction
                        ? (
                            currentUserData.investAmount *
                            latestPredction.prediction
                          ).toFixed(2)
                        : "لايوجد"}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </aside>
        )}

        <section
          className={`
          bg-gray-50 h-screen p-3 md:p-4 lg:p-6 transition-all duration-300
          ${isAside ? "w-full md:w-3/4 lg:w-3/4 xl:w-4/5" : "w-full"}
        `}
        >
          <div className="flex justify-between items-center">
            <button
              onClick={() => setIsAside(!isAside)}
              className="bg-primary text-white sm:px-3 whitespace-nowrap px-4 py-2 cursor-pointer rounded-lg hover:bg-primary-dark transition-colors duration-200 font-medium"
            >
              {isAside ? "إخفاء المعلومات" : "إظهار المعلومات"}
            </button>

            <div className="gap-1 text-center flex w-fit p-1 bg-gray-100 rounded-full mb-3 border border-gray-200">
              <button
                onClick={() => setIsHistricalData(true)}
                className={`px-6 py-2 rounded-full transition-all duration-200 font-medium cursor-pointer ${
                  isHistoricalData
                    ? "bg-blue-600 text-white shadow-md"
                    : "bg-transparent text-gray-600 hover:text-blue-600 hover:bg-gray-50"
                }`}
              >
                البيانات التاريخية
              </button>
              <button
                onClick={() => setIsHistricalData(false)}
                className={`sm:px-2 sm:py-1  lg:px-6 lg:py-2 rounded-full transition-all duration-200 font-medium cursor-pointer ${
                  !isHistoricalData
                    ? "bg-blue-600 text-white shadow-md"
                    : "bg-transparent text-gray-600 hover:text-blue-600 hover:bg-gray-50"
                }`}
              >
                التوقعات
              </button>
            </div>
            <div></div>
          </div>

          <div className="bg-white w-full h-screen rounded-xl shadow-sm border border-gray-200 flex items-center justify-center ">
            <p className="text-gray-500">
              {isHistoricalData ? (
                "محتوى البيانات سيظهر هنا"
              ) : (
                <PredictionsChart />
              )}
            </p>
          </div>
        </section>

        {(companyQuery.isLoading ||
          predictionsQuery.isLoading ||
          latestPredictionQuery.isLoading ||
          historicalDataQuery.isLoading ||
          watchListQuery.isLoading) && <Loader />}
      </main>
    </>
  );
}
