import { User, Search } from "lucide-react";
import Logo from "./Logo";
import { useState } from "react";

export default function HomePageNav() {
  const [searchTerms, setSearchTerms] = useState<string>("")

  function handelSearch(e:React.ChangeEvent<HTMLInputElement>){
    setSearchTerms(e.target.value)

    console.log(searchTerms)

  }

  return (
    <nav className="shadow-lg bg-background fixed w-full h-12 md:h-14 lg:h-16 px-3 md:px-4 lg:px-6 py-2 md:py-3 flex items-center justify-between z-50">
      
      <div className="flex-shrink-0 min-w-0">
        <Logo />
      </div>

      <div className="flex-1 flex justify-center px-2 md:px-4 max-w-md mx-auto">
        <div className="relative w-full">
          <input
            type="text"
            alt="search"
            placeholder="إبحث عن الشركات"
            className="w-full h-8 md:h-9 lg:h-10 px-3 md:px-4 pr-10 bg-gray-100 border border-gray-300 rounded-full text-xs md:text-sm lg:text-base focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            onChange={(e) => handelSearch(e)}
          />
          <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 w-3 h-3 md:w-4 md:h-4 text-gray-500" />
        </div>
      </div>

      <div className="flex-shrink-0">
        <button className="rounded-full cursor-pointer  bg-primary hover:bg-primary-light hover:scale-105 transition-all font-medium text-white h-7 px-2 text-xs md:h-8 md:px-3 md:text-xs lg:h-10 lg:px-4 lg:text-sm whitespace-nowrap">
          <span className="hidden sm:inline">الحساب </span>
          <User className="inline w-3 h-3 md:w-4 md:h-4" />
        </button>
      </div>
    </nav>
  );
}