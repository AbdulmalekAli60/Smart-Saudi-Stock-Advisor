import Logo from "./Logo";

export default function Footer() {
  return (
    <footer className="bg-text-primary h-40 flex items-center flex-1 justify-around">
      <span className="whitespace-nowrap text-white">
        {new Date().getFullYear()}
      </span>

      <Logo />
    </footer>
  );
}
