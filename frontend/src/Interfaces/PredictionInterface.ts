export default interface PredictionInterface {
  predictionId: number;
  predictionDate: string;
  prediction: number;
  direction: boolean;
  expirationDate: string;
  actualResult: number;
  companyId: number;
}
