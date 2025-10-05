interface calculateROI {
  roiValeu: number;
  roiPercentage: number;
}

interface calculateROIProps {
  currentClose: number;
  predection: number;
  investAmount: number;
}

export default function calculateROI({
  currentClose,
  predection,
  investAmount,
}: calculateROIProps): calculateROI {
  const priceDiff = predection - currentClose;

  const roiPercentage = (priceDiff / currentClose) * 100;
  const roiValeu = (priceDiff / currentClose) * investAmount;

  return { roiValeu, roiPercentage };
}
