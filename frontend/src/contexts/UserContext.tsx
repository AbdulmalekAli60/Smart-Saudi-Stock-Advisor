import React, { createContext, useContext, useEffect, useState } from "react";
import UserResponseInterface from "../Interfaces/UserResponseInterface";
import { useNavigate } from "react-router-dom";

interface ContextInterface {
  currentUserData: UserResponseInterface;
  setCurrentUserData: React.Dispatch<
    React.SetStateAction<UserResponseInterface>
  >;
}

const userContext = createContext<ContextInterface | undefined>(undefined);

export function UserContextProvider({
  children,
}: {
  children: React.ReactNode;
}) {
  const navigate = useNavigate();
  const [currentUserData, setCurrentUserData] = useState<UserResponseInterface>(
    {
      email: "",
      joinDate: "",
      message: "",
      name: "",
      role: "",
      userId: 0,
      username: "",
    }
  );

  useEffect(() => {
    const userFromSession: string | null = sessionStorage.getItem("user");

    if (userFromSession) {
      const user: UserResponseInterface = JSON.parse(userFromSession);

      setCurrentUserData(user);
    } else {
      navigate("/");
    }
  }, [navigate]);

  return (
    <userContext.Provider value={{ currentUserData, setCurrentUserData }}>
      {children}
    </userContext.Provider>
  );
}

// eslint-disable-next-line react-refresh/only-export-components
export function useUserInfo(): ContextInterface {
  const context = useContext(userContext);

  if (context == undefined) {
    throw new Error("Context In not found");
  }
  return context;
}
