export default function getNumberOfDataPointsBasedOnWidth(): number {
  const screenWidth = window.innerWidth;

  if (screenWidth < 640) {
    return 3;
  } else if (screenWidth < 768) {
    return 4;
  } else if (screenWidth < 1024) {
    return 6;
  } else if (screenWidth < 1280) {
    return 8;
  } else if (screenWidth < 1536) {
    return 10;
  } else {
    return 12;
  }
}
