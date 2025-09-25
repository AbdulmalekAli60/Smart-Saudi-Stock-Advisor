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
import { CandlestickChart, Factory } from "lucide-react";
import { WatchListQueryOptions } from "../services/WatchListService";

export default function CompanyPage() {
  const { companyId } = useParams();

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

  return (
    <>
      <HomePageNav />

      <main className="w-screen h-screen flex pt-14">
        <section className="bg-white w-1/5 border-l border-gray-200 p-4 flex flex-col">
          <div className="flex justify-center mb-6">
            <div className="w-32 h-32 bg-gray-50 rounded-lg flex items-center justify-center border p-2">
              <img
                className="max-w-full max-h-full object-contain"
                src={company?.companyLogo}
                alt="company logo"
              />
            </div>
          </div>

          <div className="space-y-4">
            <div className="text-center border-b border-gray-100 pb-4">
              <h1 className="font-bold text-lg text-gray-900 mb-2">
                {company?.companyArabicName}
              </h1>
              <h2 className="text-sm text-gray-600">
                {company?.companyEnglishName}
              </h2>
            </div>

            <div className="space-y-3">
              <div className="flex items-center gap-3 p-2 bg-gray-50 rounded-lg">
                <CandlestickChart className="w-5 h-5 text-blue-600 flex-shrink-0" />
                <div>
                  <p className="text-xs text-gray-500">رمز التداول</p>
                  <p className="font-medium text-gray-900">
                    {company?.tickerName.split(".")[0]}
                  </p>
                </div>
              </div>

              <div className="flex items-center gap-3 p-2 bg-gray-50 rounded-lg">
                <Factory className="w-5 h-5 text-green-600 flex-shrink-0" />
                <div>
                  <p className="text-xs text-gray-500">القطاع</p>
                  <p className="font-medium text-gray-900">
                    {company?.sectorArabicName}
                  </p>
                </div>
              </div>

              <div className="flex items-center gap-3 p-2 bg-gray-50 rounded-lg">
                <Factory className="w-5 h-5 text-green-600 flex-shrink-0" />
                <div>
                  <p className="text-xs text-gray-500">القطاع</p>
                  <p className="font-medium text-gray-900">
                    {company?.sectorArabicName}
                  </p>
                </div>
              </div>

              <div className="flex items-center gap-3 p-2 bg-gray-50 rounded-lg">
                <Factory className="w-5 h-5 text-green-600 flex-shrink-0" />
                <div>
                  <p className="text-xs text-gray-500">القطاع</p>
                  <p className="font-medium text-gray-900">
                    {company?.sectorArabicName}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section className="bg-gray-50 w-4/5 p-6">
          <div className="bg-white w-full h-full rounded-xl shadow-sm border border-gray-200 flex items-center justify-center">
            <p className="text-gray-500">محتوى البيانات سيظهر هنا</p>
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
