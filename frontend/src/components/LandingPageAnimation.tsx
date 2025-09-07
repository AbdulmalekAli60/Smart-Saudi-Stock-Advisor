import { Player } from "@lottiefiles/react-lottie-player"
import animationData from "../assets/Money Investment.json"
const LandingPageAnimation = () => {
  return (
    <Player
      src={JSON.parse(JSON.stringify(animationData))} 
      loop
      autoplay
      style={{ height: '300px', width: '300px' }}
    />
  );
};

export default LandingPageAnimation

