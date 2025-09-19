import { useQuery } from "@tanstack/react-query";
import { useParams } from "react-router-dom";
import { companyById } from "../services/CompanyService";
import Loader from "../components/Loader";

export default function CompanyPage() {
  const { companyId } = useParams();

  const { data, isLoading } = useQuery(companyById(companyId));
  console.log("the data is: " , {...data?.data});
  return (
    <div className="bg-red-700">
      <p>this is specific company page {companyId}</p>

      {isLoading && <Loader />}
    </div>
  );
}
