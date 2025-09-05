import LoadingAnimiation from "./LoadingAnimiation";

export default function Loader() {
  return (
    <div className="w-screen h-screen fixed flex items-center justify-center top-0 z-50 left-0 bg-black opacity-30">
      <div>
        <LoadingAnimiation />
      </div>
    </div>
  );
}
