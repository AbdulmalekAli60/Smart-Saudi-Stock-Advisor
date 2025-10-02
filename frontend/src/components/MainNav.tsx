import { User, LoaderCircle } from "lucide-react";
import Logo from "./Logo";
import { useEffect, useState } from "react";
import DropDownMenu from "./DropDownMenu";
import { useQuery } from "@tanstack/react-query";
import { searchQueryOptions } from "../services/SearchService";
import { Link } from "react-router-dom";
import { useUserInfo } from "../contexts/UserContext";
import Select from "react-select";
import SearchResponse from "../Interfaces/SearchInterface";

export default function MainNav() {
  const [searchTerms, setSearchTerms] = useState<SearchResponse>({
    companyId: 0,
    companyArabicName: "",
  });

  const [isMenueActive, setIsMenueActive] = useState<boolean>(false);

  const { currentUserData } = useUserInfo();

  const { data, isLoading, refetch } = useQuery({
    ...searchQueryOptions(searchTerms.companyArabicName),
    enabled: searchTerms.companyArabicName.length > 0,
  });

  useEffect(() => {
    const timeToStartSearch = setTimeout(() => {
      if (searchTerms.companyArabicName.length > 0) refetch();
    }, 500);

    return () => clearTimeout(timeToStartSearch);
  }, [searchTerms, refetch]);

  return (
    <nav className="shadow-lg bg-background fixed w-full h-12 md:h-14 lg:h-16 px-3 md:px-4 lg:px-6 py-2 md:py-3 flex items-center justify-between z-50">
      <div className="flex-shrink-0 min-w-0">
        <Link to={"/home"}>
          <Logo />
        </Link>
      </div>

      <div className="flex-1 flex justify-center px-2 md:px-4 max-w-md mx-auto">
        <div id="search-container" className="relative w-full">
          <Select
            placeholder="إبحث عن الشركات"
            isSearchable={true}
            isClearable={true}
            isLoading={isLoading}
            menuIsOpen={
              searchTerms.companyArabicName.length > 0 ? undefined : false
            }
            value={
              searchTerms.companyId !== 0
                ? {
                    value: searchTerms.companyId,
                    label: searchTerms.companyArabicName,
                  }
                : null
            }
            formatOptionLabel={(option) => (
              <Link to={`/companies/${option.value}`} className="block w-full">
                {option.label}
              </Link>
            )}
            options={
              data?.data?.map((result) => ({
                value: result.companyId,
                label: result.companyArabicName,
              })) || []
            }
            onInputChange={(inputValue, actionMeta) => {
              if (actionMeta.action === "input-change") {
                setSearchTerms({
                  companyId: 0,
                  companyArabicName: inputValue,
                });
              }
            }}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                refetch();
              }
            }}
            onChange={(option) => {
              if (option) {
                setSearchTerms({
                  companyId: option.value,
                  companyArabicName: option.label,
                });
              } else {
                setSearchTerms({
                  companyId: 0,
                  companyArabicName: "",
                });
              }
            }}
            noOptionsMessage={() => "لا توجد نتائج"}
            loadingMessage={() => <LoaderCircle className="animate-spin" />}
            styles={{
              control: (base, state) => ({
                ...base,
                minWidth: "180px",
                borderRadius: "12px",
                border: state.isFocused
                  ? "2px solid #3b82f6"
                  : "1px solid #e5e7eb",
                boxShadow: "none",
                padding: "2px 4px",
                cursor: "pointer",
                transition: "all 0.2s",
                "&:hover": {
                  borderColor: "#3b82f6",
                },
              }),
              menu: (base, state) => ({
                ...base,
                borderRadius: "12px",
                overflow: "hidden",
                boxShadow: "0 4px 12px rgba(0,0,0,0.1)",
                border: "1px solid #e5e7eb",
                text: state.options.length === 0 ? "لايوجد نتائج" : "",
              }),
              menuList: (base) => ({
                ...base,
                padding: "4px",
              }),
              option: (base, state) => ({
                ...base,
                backgroundColor: state.isSelected
                  ? "#3b82f6"
                  : state.isFocused
                  ? "#eff6ff"
                  : "white",
                color: state.isSelected ? "white" : "#374151",
                cursor: "pointer",
                borderRadius: "8px",
                padding: "8px 12px",
                fontSize: "14px",
                transition: "all 0.15s",
              }),
              placeholder: (base) => ({
                ...base,
                color: "#9ca3af",
                fontSize: "14px",
              }),
              singleValue: (base) => ({
                ...base,
                color: "#1f2937",
                fontSize: "14px",
                fontWeight: "500",
              }),
            }}
          />
        </div>
      </div>

      <div className="flex-shrink-0">
        <div id="menu-container" className="relative space-x-2">
          {currentUserData.role === "ADMIN" && (
            <Link to={"/dashboared"}>
              <button className="rounded-full mb-2 cursor-pointer  bg-primary hover:bg-primary-light hover:scale-105 transition-all font-medium text-white h-7 px-2 text-xs md:h-8 md:px-3 md:text-xs lg:h-10 lg:px-4 lg:text-sm whitespace-nowrap">
                التحكم
              </button>
            </Link>
          )}
          <button
            onClick={() => {
              setIsMenueActive(!isMenueActive);
            }}
            className="rounded-full mb-2 cursor-pointer  bg-primary hover:bg-primary-light hover:scale-105 transition-all font-medium text-white h-7 px-2 text-xs md:h-8 md:px-3 md:text-xs lg:h-10 lg:px-4 lg:text-sm whitespace-nowrap"
          >
            <span className="hidden sm:inline">الحساب </span>
            <User className="inline w-3 h-3 md:w-4 md:h-4" />
          </button>
          {isMenueActive && <DropDownMenu />}
        </div>
      </div>
    </nav>
  );
}
