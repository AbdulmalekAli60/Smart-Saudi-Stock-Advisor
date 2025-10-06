interface calculateROI {
  roiValue: number;
  roiPercentage: number;
}

interface calculateROIProps {
  currentClose: number ;
  predection: number;
  investAmount: number;
}
// {/* ROI = [(prediction - Cost of Investment) / Cost of Investment] x 100. */}

export default function calculateROI({
  currentClose,
  predection,
  investAmount,
}: calculateROIProps): calculateROI {
  const priceDiff = predection - currentClose;

  const roiPercentage = (priceDiff / currentClose) * 100;
  const roiValue = (priceDiff / currentClose) * investAmount;

  return { roiValue, roiPercentage };
}
