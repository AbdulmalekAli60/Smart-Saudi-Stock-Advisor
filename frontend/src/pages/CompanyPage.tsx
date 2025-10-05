import { useQueries } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import { companyById } from "../services/CompanyService";
import Loader from "../components/Loader";
import {
  allPredictionsQueryOptions,
  latestPredictionsQueryOptions,
} from "../services/PredictionService";
import { getHistoricalDataQueryOptions } from "../services/HistoricalDataService";
import HomePageNav from "../components/MainNav";
import {
  CandlestickChart,
  Clock,
  Database,
  Factory,
  RotateCcw,
  SaudiRiyal,
  SignpostBig,
  TrendingDown,
  TrendingUp,
  TrendingUpDown,
  X,
} from "lucide-react";
import { WatchListQueryOptions } from "../services/WatchListService";
import { useUserInfo } from "../contexts/UserContext";
import { useState } from "react";
import PredictionsChart from "../components/PredictionsChart";
import StatCard from "../components/StatCard";
import { SelectedValue } from "../Interfaces/SelectedValueInterface";
import SelectComponent from "../components/SelectComponent";
import HistoricalDataChart from "../components/HistoricalDataChart";
import Footer from "../components/Footer";
import calculateROI from "../utils/CalculateROI";

export default function CompanyPage() {
  const { companyId } = useParams();
  const { currentUserData } = useUserInfo();
  const [isHistoricalData, setIsHistricalData] = useState<boolean>(false);
  const [isAside, setIsAside] = useState<boolean>(true);
  const [selectedValue, setSelectedValue] = useState<SelectedValue>({
    from: { value: "", label: "" },
    to: { value: "", label: "" },
  });

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
  const latestPredction = latestPredictionQuery.data?.data;
  const predctions = predictionsQuery.data?.data;
  const historical = historicalDataQuery.data?.data;

  const options = predctions?.map((item) => {
    const date = item.predictionDate.split("T")[0];
    return { value: date, label: date };
  });

    const {roiValeu} = calculateROI(historical[historical!.length - 1].close, latestPredction.prediction, currentUserData.investAmount)

  return (
    <>
      <HomePageNav />

      <main
        className="w-full flex pt-14"
        style={{ height: "calc(100vh - 3.5rem)" }}
      >
        {/* side */}

        {isAside && (
          <div
            className="fixed h-screen w-screen bg-gray-500 opacity-50 z-40 md:hidden lg:hidden"
            onClick={() => setIsAside(false)}
          />
        )}
        {isAside && (
          <aside
            className={`
              fixed md:relative top-14 md:top-0 m-auto z-50 md:z-0
              bg-white 
              w-80 md:w-72 lg:w-1/4 xl:w-1/5 
              border-r border-gray-200 
              sm:w-full
              p-3 md:p-4 lg:p-6
              flex flex-col 
              overflow-y-auto
              transform transition-all duration-300
              translate-x-0
            `}
            style={{ height: "calc(100vh - 3.5rem)" }}
          >
            <div className="flex justify-between items-start mb-4 md:mb-6">
              <button
                onClick={() => setIsAside(false)}
                className="md:hidden bg-gray-100 cursor-pointer hover:bg-red-100 text-gray-600 hover:text-red-600 p-2 rounded-full transition-colors "
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
                className="hidden md:block bg-gray-100 hover:bg-red-100 text-gray-600 hover:text-red-600 p-2 rounded-full transition-colors cursor-pointer"
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
                <StatCard
                  Icon={CandlestickChart}
                  body={company?.tickerName?.split(".")[0]}
                  title="رمز التداول"
                  color="text-blue-600"
                />

                <StatCard
                  Icon={Factory}
                  body={company?.sectorArabicName}
                  title="القطاع"
                  color="text-green-600"
                />

                <StatCard
                  Icon={TrendingUpDown}
                  body={latestPredction?.prediction.toFixed(2)}
                  title={`توقع سعر اليوم التالي ${
                    latestPredction?.expirationDate.split("T")[0]
                  }`}
                  color="text-blue-600"
                />

                <StatCard
                  Icon={Database}
                  body={historical?.length.toString()}
                  title="عدد البيانات التي تم استخدامها للتحليل"
                  color="text-black"
                />

                <StatCard
                  Icon={Database}
                  body={historical?.length.toString()}
                  title="عدد البيانات التي تم استخدامها للتحليل"
                  color="text-black"
                />
                {/* ROI = [(Total Return - Cost of Investment) / Cost of Investment] x 100. */}
                <StatCard
                  Icon={SaudiRiyal}
                  body={
                    // fix
                    latestPredction?.prediction
                      ? roiValeu
                      : "لايوجد"
                  }
                  title="الأرباح المتوقعة بناءا على مبلغ الإستثمار"
                  color="text-green-700"
                />

                <StatCard
                  Icon={Clock}
                  body={latestPredction?.expirationDate.split("T")[0]}
                  title="نهاية تاريخ الصلاحية"
                  color="text-black"
                />

                <StatCard
                  Icon={SignpostBig}
                  body={
                    latestPredction?.direction ? (
                      <TrendingUp className="text-success" />
                    ) : (
                      <TrendingDown className="text-fail" />
                    )
                  }
                  title="الإتجاة"
                  color="text-amber-400"
                />
              </div>
            </div>
          </aside>
        )}

        <section
          className={`
          bg-gray-50 p-3 md:p-4 lg:p-6 transition-all duration-300 overflow-y-auto
          ${isAside ? "w-full md:w-3/4 lg:w-3/4 xl:w-4/5" : "w-full"}
        `}
          style={{ height: "calc(100vh - 3.5rem)" }}
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

          <div className="w-full flex flex-wrap items-center gap-4 bg-white border border-gray-200 rounded-2xl shadow-sm p-4 mb-4">
            <div className="flex items-center gap-2">
              <div className="w-1 h-8 bg-blue-500 rounded-full"></div>
              <h1 className="font-primary-bold text-lg text-gray-800">
                إختر التاريخ
              </h1>
            </div>

            <div className="flex items-center gap-2">
              <label className="font-primary-bold text-gray-700 text-sm">
                من
              </label>
              <SelectComponent
                options={options}
                placeholder="حدد تاريخ البداية"
                field="from"
                setValeu={setSelectedValue}
                value={selectedValue.from}
              />
            </div>

            <div className="flex items-center gap-2">
              <label className="font-primary-bold text-gray-700 text-sm">
                إلى
              </label>
              <SelectComponent
                options={options}
                field="to"
                placeholder="حدد تاريخ النهاية"
                setValeu={setSelectedValue}
                value={selectedValue.to}
              />
            </div>

            <button
              className="mr-auto bg-gray-100 rounded-xl cursor-pointer hover:bg-blue-50 hover:text-blue-600 p-2.5 transition-all duration-200 group"
              onClick={() =>
                setSelectedValue({
                  from: { value: "", label: "" },
                  to: { value: "", label: "" },
                })
              }
            >
              <RotateCcw className="w-5 h-5 hover:rotate-180 duration-300" />
            </button>
          </div>

          <div className="bg-white w-full h-full rounded-xl shadow-sm border border-gray-200 p-4">
            {isHistoricalData ? (
              <HistoricalDataChart
                historicalData={historical}
                limits={selectedValue}
              />
            ) : (
              <PredictionsChart
                predections={predctions}
                limits={selectedValue}
              />
            )}
          </div>
        </section>

        {(companyQuery.isLoading ||
          predictionsQuery.isLoading ||
          latestPredictionQuery.isLoading ||
          historicalDataQuery.isLoading ||
          watchListQuery.isLoading) && <Loader />}
      </main>
      <Footer />
    </>
  );
}
