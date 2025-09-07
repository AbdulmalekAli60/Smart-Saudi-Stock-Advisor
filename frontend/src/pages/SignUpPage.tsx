import SignUpAnimation from "../animations/SignUpAnimation";

export default function SignUpPage() {
  return (
    <main className="grid grid-cols-3 place-items-center">
      <section className="col-span-2">
        <form></form>
      </section>
      <section
        className="h-screen w-full grid place-content-center"
        style={{ background: "var(--gradient-hero)" }}
      >
        <SignUpAnimation />
      </section>
    </main>
  );
}
