import { Star, ChartCandlestick } from "lucide-react";
import { CompanyResponse } from "../Interfaces/CompanyResponseInterface";
import { useNavigate } from "react-router-dom";

export default function CompanyCard({
  companyArabicName,
  companyEnglishName,
  companyId,
  companyLogo,
  sectorArabicName,
  sectorId,
  tickerName,
}: CompanyResponse) {
  const navigate = useNavigate();
  return (
    <div className="w-full h-full p-6 bg-background text-center space-y-6 rounded-3xl shadow-lg">
      <div className="w-full flex justify-between">
        <div></div>
        <img className="w-1/2 h-1/2" src={companyLogo} alt="company logo" />

        <Star className="cursor-pointer" />
      </div>

      <div
        onClick={() => navigate(`/companies/${companyId}`)}
        className=" text-start font-primary-regular p-2 cursor-pointer"
      >
        <div className=" flex justify-between">
          <h1 className="mb-3 sm:text-base md:text-3xl lg:text-4xl">
            {companyArabicName}
          </h1>

          <div className="space-x-2 whitespace-nowrap">
            <span className="font-primary-bold sm:text-sm md:text-base lg:text-lg">
              {tickerName.split(".")[0]}
            </span>
            <ChartCandlestick className="inline" />
          </div>
        </div>

        <div className="flex justify-between">
          <h2 className="font-primary-regular sm:text-sm md:text-base lg:text-lg">
            {companyEnglishName}
          </h2>
          <h2 className="font-primary-bold sm:text-sm md:text-base lg:text-lg">
            {sectorArabicName}
          </h2>
        </div>
      </div>
    </div>
  );
}
