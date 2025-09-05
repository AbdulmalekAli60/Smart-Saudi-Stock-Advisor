import Button from "./components/Button";
import Card from "./components/Card";
import LandingPageAnimation from "./components/LandingPageAnimation";
import Logo from "./components/Logo";

export default function LandingPage() {
  const cardsContent = [
    {
      id: 1,
      title: "تحليل 16 شركة مختارة",
      desc: "نركز على تحليل أهم 16 شركة في السوق السعودي بعمق ودقة عالية لضمان أفضل النتائج للمستثمرين",
    },
    {
      id: 2,
      title: "دقة تنبؤات 85%",
      desc: "نحقق معدل دقة يصل إلى 85% في تنبؤات حركة الأسهم باستخدام نماذج الذكاء الاصطناعي المتقدمة",
    },
    {
      id: 3,
      title: "تحليل يومي محدث",
      desc: "نقدم تحليلات يومية محدثة لكل شركة من الشركات الـ16 مع توقعات واضحة لاتجاه السعر",
    },
  ];

  return (
    <>
      {/* Navigation */}
      <nav className="h-12 md:h-14 lg:h-16 px-3 md:px-4 lg:px-6 py-2 md:py-3 shadow-xl flex items-center justify-between rounded-bl-xl rounded-br-xl md:rounded-bl-2xl md:rounded-br-2xl bg-white">
        {/* Logo */}
        <div className="flex items-center">
          <Logo />
        </div>

        {/* Navigation Buttons */}
        <div className="flex items-center gap-2 md:gap-3 lg:gap-4">
          <Button color="primary">إنشاء حساب</Button>
          <Button color="primary">تسجيل الدخول</Button>
        </div>
      </nav>

      {/* Hero Section */}
      <section
        className="grid grid-cols-3"
        style={{ background: "var(--gradient-hero)" }}
      >
        <div className="min-h-[400px] col-span-2 md:min-h-[500px] lg:min-h-[600px] xl:min-h-[700px] relative flex flex-col justify-center items-center px-4 md:px-6 lg:px-8">
          <div className="text-center max-w-4xl mx-auto space-y-4 md:space-y-6 lg:space-y-8">
            <h1 className="font-primary-bold text-white text-3xl md:text-4xl lg:text-5xl xl:text-6xl leading-tight">
              تنبؤات الأسهم بالذكاء الاصطناعي
            </h1>
            <p className="font-primary-regular text-accent text-base md:text-lg lg:text-xl max-w-2xl mx-auto leading-relaxed">
              احصل على تنبؤات دقيقة لحركة الأسهم باستخدام أحدث تقنيات الذكاء
              الاصطناعي
            </p>
          </div>
        </div>

        <div className="flex items-center justify-around h-full">
          <LandingPageAnimation />
        </div>

        <div className="col-span-3 text-center -mt-4 md:-mt-6 lg:-mt-8 pb-6 md:pb-8 lg:pb-10 space-y-4 md:space-y-5">
          <p className="font-primary-bold text-secondary text-2xl md:text-3xl lg:text-4xl">
            إبدأ الان
          </p>
          <div className="flex flex-col sm:flex-row items-center justify-center gap-3 md:gap-4 lg:gap-6">
            <Button color="text-primary">إنشاء حساب</Button>
            <Button color="text-primary">تسجيل الدخول</Button>
          </div>
        </div>
      </section>

      {/* About Us Section */}
      <section className=" py-12 md:py-16 lg:py-20 px-4 md:px-6 lg:px-8 bg-white min-h-[400px] md:min-h-[500px] flex items-center">
        <div className="max-w-4xl mx-auto text-center space-y-6 md:space-y-8 lg:space-y-10">
          <h2 className="text-2xl md:text-3xl lg:text-4xl xl:text-5xl font-primary-bold text-gray-800">
            كيف نعمل؟
          </h2>
          <div className="bg-accent shadow-xl/30 rounded-xl md:rounded-2xl lg:rounded-3xl p-4 md:p-6 lg:p-8">
            <p className="text-white text-justify text-sm md:text-base lg:text-lg xl:text-xl leading-relaxed">
              نقوم بجمع البيانات التاريخية الشاملة للشركات المختارة من مصادر
              موثوقة, ثم نستخدم نموذج XGBoost المتقدم في التعلم الآلي لتحليل هذه
              البيانات وإنتاج تنبؤات دقيقة لحركة الأسهم. يعتمد نموذجنا على
              خوارزميات معقدة تدرس الأنماط والاتجاهات السابقة لتقديم توقعات
              مبنية على أسس علمية قوية، مما يساعد المستثمرين على اتخاذ قرارات
              استثمارية مدروسة ومبنية على البيانات.
            </p>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="py-8 md:py-12 lg:py-16 px-4 md:px-6 lg:px-8 bg-amber-500 min-h-[600px]">
        <div className="max-w-7xl mx-auto">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 md:gap-6 lg:gap-8">
            {cardsContent.map((cardContent) => (
              <div key={cardContent.id} className="flex">
                <Card title={cardContent.title} desc={cardContent.desc} />
              </div>
            ))}
          </div>
        </div>
      </section>

      <footer className="bg-text-primary h-40 flex items-center flex-1 justify-around">
        <span className="whitespace-nowrap text-white">
          {new Date().getFullYear()}
        </span>

        <Logo />
      </footer>
    </>
  );
}
