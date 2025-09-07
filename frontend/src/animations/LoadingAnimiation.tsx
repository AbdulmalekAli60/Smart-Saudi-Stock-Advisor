import { Player } from "@lottiefiles/react-lottie-player"
import animationData from "../assets/Stock candle loading.json"
export default function LoadingAnimiation() {
  return (
    <Player
    src={JSON.parse(JSON.stringify(animationData))}
    autoplay
    loop
    style={{width:"150px", height:"150px"}}
    />
  )
}
