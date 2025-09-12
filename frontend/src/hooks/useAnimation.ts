import { gsap } from "gsap/gsap-core";
import { useGSAP } from "@gsap/react";
import { ScrollTrigger } from "gsap/all";

interface AnimationRefs {
  heroSectionRef?: React.RefObject<HTMLDivElement | null>;
  aboutUsSectionRef?: React.RefObject<HTMLDivElement | null>;
  cardsSectionRef?: React.RefObject<HTMLDivElement | null>;

  toastContainerRef?: React.RefObject<HTMLDivElement | null>;
}

gsap.registerPlugin(ScrollTrigger);

function useAnimations({
  heroSectionRef,
  aboutUsSectionRef,
  cardsSectionRef,
  toastContainerRef,
}: AnimationRefs): void {
  useGSAP(() => {
    if (heroSectionRef?.current) {
      gsap.fromTo(
        heroSectionRef.current,
        {
          x: 360,
          opacity: 0.5,
        },
        {
          x: 0,
          opacity: 1,
          duration: 1,
          scrollTrigger: {
            trigger: heroSectionRef.current,
            toggleActions: "play none restart none",
            start: "top 80%",
          },
        }
      );
    }

    if (aboutUsSectionRef?.current) {
      gsap.fromTo(
        aboutUsSectionRef.current,
        {
          y: 60,
          opacity: 0.5,
        },
        {
          y: 0,
          opacity: 1,
          duration: 0.7,
          scrollTrigger: {
            trigger: aboutUsSectionRef.current,
            toggleActions: "restart none restart none",
            start: "top 60%",
          },
        }
      );
    }

    if (cardsSectionRef?.current) {
      gsap.fromTo(
        cardsSectionRef.current,
        { y: 60, opacity: 0, rotationX: 15 },
        {
          y: 0,
          opacity: 1,
          rotationX: 0,
          duration: 0.7,
          stagger: 0.2,
          scrollTrigger: {
            trigger: cardsSectionRef.current,
            toggleActions: "restart none restart none",
            start: "top 70%",
          },
        }
      );
    }

    if (toastContainerRef?.current) {
      const tl = gsap.timeline();

      tl.fromTo(
        toastContainerRef.current,
        { x: -40, opacity: 0.5 },
        { x: 0, opacity: 1, duration: 0.8, ease: "back.out(1.7)" }
      ).to(toastContainerRef.current, {
        x: -40,
        opacity: 0,
        duration: 0.4,
        ease: "power2.in(1.7)",
        delay: 3,
      });
    }
  }, []);
}

export default useAnimations;
