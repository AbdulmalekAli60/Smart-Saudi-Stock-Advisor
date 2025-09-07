import { Player } from "@lottiefiles/react-lottie-player";
import animationData from "../assets/Login .json";
export default function LogInAnimation() {
  return (
    <Player
      src={JSON.parse(JSON.stringify(animationData))}
      loop
      autoplay
      style={{ height: "400px", width: "400px" }}
    />
  );
}
