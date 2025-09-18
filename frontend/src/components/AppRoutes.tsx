import { Routes, Route } from "react-router-dom";
import LandingPage from "../pages/LandingPage";
import SignUpPage from "../pages/SignUpPage";
import LogInPage from "../pages/LogInPage";
import HomePage from "../pages/HomePage";
import { UserContextProvider } from "../contexts/UserContext";
import AccountPage from "../pages/AccountPage";
import { ToastContextProvider } from "../contexts/ToastContext";
import DashBoaredPage from "../pages/DashBoaredPage";

export default function AppRoutes() {
  return (
    <ToastContextProvider>
      <UserContextProvider>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/sign-up" element={<SignUpPage />} />
          <Route path="/log-in" element={<LogInPage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/account" element={<AccountPage />} />
          <Route path="/dashboared" element={<DashBoaredPage/>}/>
        </Routes>
      </UserContextProvider>
    </ToastContextProvider>
  );
}
