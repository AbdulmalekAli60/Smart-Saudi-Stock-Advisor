import { User, Search, LoaderCircle } from "lucide-react";
import Logo from "./Logo";
import { useEffect, useState } from "react";
import DropDownMenu from "./DropDownMenu";
import { useQuery } from "@tanstack/react-query";
import { searchQueryOptions } from "../services/SearchService";
import { Link } from "react-router-dom";
import { useUserInfo } from "../contexts/UserContext";

export default function MainNav() {
  const [searchTerms, setSearchTerms] = useState<string>("");
  const [isMenueActive, setIsMenueActive] = useState<boolean>(false);
  const [isSearchResultActive, setIsSearchResultActive] =
    useState<boolean>(false);

  const { currentUserData } = useUserInfo();

  const { data, isLoading, isFetched, refetch } = useQuery(
    searchQueryOptions(searchTerms)
  );

  useEffect(() => {
    if (isFetched && (data?.data?.length ?? 0) > 0) {
      setIsSearchResultActive(true);
    }
  }, [isFetched, data]);

  useEffect(() => {
    function handleClickOutside(event: MouseEvent) {
      const searchContainer = document.getElementById("search-container");
      if (
        searchContainer &&
        !searchContainer.contains(event.target as Element)
      ) {
        setIsSearchResultActive(false);
      }

      const menuContainer = document.getElementById("menu-container");
      if (menuContainer && !menuContainer.contains(event.target as Element)) {
        setIsMenueActive(false);
      }
    }

    document.addEventListener("click", handleClickOutside);
    return () => document.removeEventListener("click", handleClickOutside);
  }, []);

  useEffect(() => {
    if (searchTerms.length === 0 || searchTerms === "") {
      setIsSearchResultActive(false);
    }
  }, [searchTerms]);

  return (
    <nav className="shadow-lg bg-background fixed w-full h-12 md:h-14 lg:h-16 px-3 md:px-4 lg:px-6 py-2 md:py-3 flex items-center justify-between z-50">
      <div className="flex-shrink-0 min-w-0">
        <Link to={"/home"}>
          <Logo />
        </Link>
      </div>

      <div className="flex-1 flex justify-center px-2 md:px-4 max-w-md mx-auto">
        <div id="search-container" className="relative w-full">
          <input
            type="text"
            alt="search"
            value={searchTerms}
            placeholder="إبحث عن الشركات"
            className="w-full h-8 md:h-9 lg:h-10 px-3 md:px-4 pr-10 bg-gray-100 border border-gray-300 rounded-full text-xs md:text-sm lg:text-base focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            onChange={(e) => setSearchTerms(e.target.value)}
            onClick={() => {
              if (searchTerms.length > 0) refetch();
            }}
            onKeyDown={(e) => {
              if (e.key == "Enter") {
                e.preventDefault();
                refetch();
              }
            }}
          />
          <Search
            onClick={() => {
              if (searchTerms.length > 0) refetch();
            }}
            className="absolute left-4 top-1/2 cursor-pointer transform -translate-y-1/2 w-3 h-3 md:w-4 md:h-4 text-gray-500"
          />

          <div className="absolute top-full  w-full h-fit z-50">
            {isLoading && <LoaderCircle />}
            {isSearchResultActive &&
              data?.data?.map(({ companyId, companyArabicName }) => {
                return (
                  <Link to={`/companies/${companyId}`}>
                    <span
                      key={companyId}
                      className="p-2 border rounded-2xl block bg-gray-400 cursor-pointer"
                    >
                      {companyArabicName}
                    </span>
                  </Link>
                );
              })}

            {isFetched &&
              !isLoading &&
              (!data?.data || data.data.length === 0) && (
                <div className="p-2">لاتوجد نتيجة</div>
              )}
          </div>
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
