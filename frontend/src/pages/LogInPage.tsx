import LogInAnimation from "../animations/LogInAnimation";

export default function LogInPage() {
  return (
    <main className="grid grid-cols-3">
      <section className="col-span-2">
        <form></form>
      </section>
      <section
        className="h-screen w-full grid place-content-center"
        style={{ background: "var(--gradient-hero)" }}
      >
        <LogInAnimation />
      </section>
    </main>
  );
}
