import Logo from "./Logo";

export default function Footer() {
  return (
    <footer className="bg-primary h-40 w-full flex items-center justify-around mt-auto">
      <span className="text-white font-primary-regular">
        © {new Date().getFullYear()} جميع الحقوق محفوظة
      </span>

      <Logo />
    </footer>
  );
}