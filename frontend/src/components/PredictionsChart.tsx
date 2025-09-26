import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js/auto";
import { Bar, Chart } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

export default function PredictionsChart() {
  return (
    <div className="bg-red-400">
      <Bar
        data={{
          labels: ["A", "B", "C"],
          datasets: [{ label: "Hi", data: [6, 7, 8] }],
        }}
      />
    </div>
  );
}
