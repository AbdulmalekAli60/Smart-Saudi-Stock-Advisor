import { useQueries } from "@tanstack/react-query";
import { useNavigate, useParams } from "react-router-dom";
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
import { useUserInfo } from "../contexts/UserContext";
import { useEffect, useState } from "react";
import PredictionsChart from "../components/PredictionsChart";
import StatCard from "../components/StatCard";
import { SelectedValue } from "../Interfaces/SelectedValueInterface";
import SelectComponent from "../components/SelectComponent";
import HistoricalDataChart from "../components/HistoricalDataChart";
import Footer from "../components/Footer";
import calculateROI from "../utils/CalculateROI";

export default function CompanyPage() {

  const navigaet = useNavigate();

  useEffect(() => {
    if (!sessionStorage.getItem("user")) {
      navigaet("/");
    }
  }, [navigaet]);

  const { companyId } = useParams();
  const { currentUserData } = useUserInfo();
  const [roiResult, setRoiResult] = useState<{
    value: number;
    percentage: number;
  }>({
    percentage: 0,
    value: 0,
  });
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
    ],
  });

  const [
    companyQuery,
    predictionsQuery,
    latestPredictionQuery,
    historicalDataQuery,
  ] = result;

  const company = companyQuery.data?.data;
  const latestPredction = latestPredictionQuery.data?.data;
  const predctions = predictionsQuery.data?.data;
  const historical = historicalDataQuery.data?.data;

  const options = predctions?.map((item) => {
    const date = item.predictionDate.split("T")[0];
    return { value: date, label: date };
  });

  useEffect(() => {
    if (historical != null && latestPredction != null) {
      const { roiPercentage, roiValue } = calculateROI({
        currentClose: historical[historical.length - 1].close,
        predection: latestPredction?.prediction,
        investAmount: currentUserData.investAmount,
      });
      setRoiResult({ value: roiValue, percentage: roiPercentage });
    }
  }, [historical, latestPredction, currentUserData.investAmount]);

  return (
    <>
      <title>{company?.companyArabicName}</title>
      <HomePageNav />

      <main className="w-full flex pt-14 h-screen overflow-hidden bg-background">
        {/* small size bg */}
        {isAside && (
          <div
            className="fixed inset-0 bg-gray-900/50 z-40 md:hidden"
            onClick={() => setIsAside(false)}
          />
        )}

        {/* sidbar */}
        <aside
          className={`
              fixed md:relative top-14 md:top-0 bottom-0 z-50 md:z-0
              bg-white 
              w-80 md:w-72 lg:w-1/4 xl:w-1/5 
              border-l border-[var(--color-border)]
              p-4 md:p-6
              flex flex-col 
              overflow-y-auto
              transition-transform duration-300 ease-in-out
              ${isAside ? "translate-x-0" : "translate-x-full md:hidden"}
            `}
        >
          <div className="flex justify-between items-start mb-6">
            <button
              onClick={() => setIsAside(false)}
              className="md:hidden bg-gray-100 p-2 rounded-full"
            >
              <X size={20} />
            </button>

            <div className="w-24 h-24 bg-gray-50 rounded-xl border p-2 mx-auto flex items-center justify-center">
              <img
                className="max-w-full max-h-full object-contain"
                src={company?.companyLogo}
                alt="company logo"
              />
            </div>

            <button
              onClick={() => setIsAside(false)}
              className="hidden md:block p-2 rounded-full bg-gray-200 hover:bg-gray-300 transition-colors"
            >
              <X size={20} className="text-gray-500 cursor-pointer" />
            </button>
          </div>

          <div className="space-y-4">
            <div className="text-center border-b border-gray-100 pb-4">
              <h1 className="font-bold text-lg text-text-primary">
                {company?.companyArabicName}
              </h1>
              <h2 className="text-sm text-text-secondary">
                {company?.companyEnglishName}
              </h2>
            </div>

            <div className="space-y-3">
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
                title={`توقع سعر الغد`}
                color="text-blue-600"
              />
              <StatCard
                Icon={Database}
                body={historical?.length.toString()}
                title="بيانات التحليل"
                color="text-black"
              />
              <StatCard
                Icon={SaudiRiyal}
                body={
                  latestPredction?.prediction
                    ? roiResult.value.toFixed(2)
                    : "لايوجد"
                }
                title="الأرباح المتوقعة"
                color="text-green-700"
              />
              <StatCard
                Icon={Clock}
                body={latestPredction?.expirationDate.split("T")[0]}
                title="تاريخ الصلاحية"
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

        <section
          className={`
             flex-1 flex flex-col h-full overflow-y-auto overflow-x-hidden relative
             transition-all duration-300
          `}
        >
          <div className="p-4 md:p-6 flex-grow flex flex-col ">
            <div className="flex flex-wrap justify-between items-center gap-4 mb-6">
              {!isAside && (
                <button
                  onClick={() => setIsAside(true)}
                  className="bg-primary text-white px-4 cursor-pointer py-2 rounded-lg hover:bg-primary-light transition-colors"
                >
                  إظهار المعلومات
                </button>
              )}

              <div className="flex  gap-1 bg-white p-1 rounded-lg border border-border">
                <button
                  onClick={() => setIsHistricalData(true)}
                  className={`px-4 py-2 rounded-md text-sm cursor-pointer font-medium transition-all ${
                    isHistoricalData
                      ? "bg-blue-600 text-white shadow-sm"
                      : "text-gray-500 hover:bg-gray-50"
                  }`}
                >
                  البيانات التاريخية
                </button>
                <button
                  onClick={() => setIsHistricalData(false)}
                  className={`px-4 py-2 rounded-md text-sm font-medium cursor-pointer transition-all ${
                    !isHistoricalData
                      ? "bg-blue-600 text-white shadow-sm"
                      : "text-gray-500 hover:bg-gray-50"
                  }`}
                >
                  التوقعات
                </button>
              </div>
            </div>

            <div className="bg-white border border-border rounded-xl p-4 mb-4 flex flex-wrap items-center gap-4">
              <div className="flex items-center gap-2 border-l pl-4 ml-2">
                <div className="w-1 h-6 bg-blue-500 rounded-full"></div>
                <h1 className="font-bold text-gray-800">التاريخ</h1>
              </div>

              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-500 font-bold">من</span>
                <SelectComponent
                  options={options}
                  placeholder="البداية"
                  field="from"
                  setValeu={setSelectedValue}
                  value={selectedValue.from}
                />
              </div>

              <div className="flex items-center gap-2">
                <span className="text-sm text-gray-500 font-bold">إلى</span>
                <SelectComponent
                  options={options}
                  placeholder="النهاية"
                  field="to"
                  setValeu={setSelectedValue}
                  value={selectedValue.to}
                />
              </div>

              <button
                className="mr-auto p-2 bg-gray-100 rounded-lg hover:text-blue-600 hover:rotate-180 transition-all duration-300"
                onClick={() =>
                  setSelectedValue({
                    from: { value: "", label: "" },
                    to: { value: "", label: "" },
                  })
                }
              >
                <RotateCcw size={18} />
              </button>
            </div>

            <div className="w-full h-[500px] mb-8">
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
          </div>

          <div className="mt-auto">
            <Footer />
          </div>
        </section>

        {(companyQuery.isLoading ||
          predictionsQuery.isLoading ||
          latestPredictionQuery.isLoading ||
          historicalDataQuery.isLoading) && <Loader />}
      </main>
    </>
  );
}
