import { Menu, X } from "lucide-react";
import { useState } from "react";

export default function Side({ children }: { children: React.ReactNode }) {
  const [isAside, setIsAside] = useState<boolean>(true);
  return (
    <>
      {isAside && (
        <div
          className="fixed h-screen w-screen bg-gray-500 opacity-50 z-40 md:hidden "
          onClick={() => setIsAside(false)}
        />
      )}
      {isAside && (
        <aside
          className={`
            h-fu
              fixed z-50 md:z-0
              bg-red-300 
              w-80 md:w-72 lg:w-1/4 xl:w-1/5 
              border-r border-gray-200 
              sm:w-full
              mt-14
              p-3 md:p-4 lg:p-6
              flex flex-col 
              space-y-3
              overflow-y-auto
              transform transition-all duration-300
              translate-x-0
            `}
          style={{ height: "calc(100vh - 3.5rem)" }}
        >
          <div className=" flex justify-end items-center mb-2">
            <button
              onClick={() => setIsAside(false)}
              className=" md:block bg-gray-100 hover:bg-red-100 text-gray-600 hover:text-red-600 p-2 rounded-full transition-colors cursor-pointer"
            >
              <X size={20} />
            </button>
          </div>

          {children}
        </aside>
      )}

      {!isAside && (
        <div
          onClick={() => setIsAside(!isAside)}
          className="z-50 bg-gray-400 fixed top-18 right-7 h-12 w-12 flex items-center justify-center text-white cursor-pointer hover:bg-gray-300 rounded-full "
        >
          <Menu />
        </div>
      )}
    </>
  );
}
