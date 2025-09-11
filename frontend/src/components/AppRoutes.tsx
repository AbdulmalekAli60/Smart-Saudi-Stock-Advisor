import { Routes, Route } from "react-router-dom";
import LandingPage from "../pages/LandingPage";
import SignUpPage from "../pages/SignUpPage";
import LogInPage from "../pages/LogInPage";
import HomePage from "../pages/HomePage";
import { UserContextProvider } from "../contexts/UserContext";

export default function AppRoutes() {
  return (
    <UserContextProvider>
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/sign-up" element={<SignUpPage />} />
        <Route path="/log-in" element={<LogInPage />} />
        <Route path="/home" element={<HomePage />} />
      </Routes>
    </UserContextProvider>
  );
}
