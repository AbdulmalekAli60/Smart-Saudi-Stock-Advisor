import { Player } from "@lottiefiles/react-lottie-player";
import animationData from "../assets/Money Investment.json";
const LandingPageAnimation = () => {
  return (
    <Player
      src={JSON.parse(JSON.stringify(animationData))}
      loop
      autoplay
      style={{
        height: "100%",
        width: "100%",
        maxHeight: "400px",
        maxWidth: "400px",
      }}
    />
  );
};

export default LandingPageAnimation;
