import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js/auto";
import Zoom from "chartjs-plugin-zoom";
import { Bar} from "react-chartjs-2";
import PredictionInterface from "../Interfaces/PredictionInterface";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Zoom
);

ChartJS.defaults.font.family = "var(--font-primary-regular)";
interface PredictionsChartProps {
  predections: PredictionInterface[] | undefined;
}

export default function PredictionsChart(data: PredictionsChartProps) {
  return (
    // <div className="bg-gray-200 w-full h-full rounded-xl  overflow-y-hidden">
      <Bar
        className="overflow-x-scroll"
        options={{
          devicePixelRatio: 1,
          responsive: true,
          maintainAspectRatio: false,

          layout: {
            autoPadding: true,
          },
          scales: {
            y: {
              grid: { display: true, color: "rgba(0,0,0,0.1)" },
              beginAtZero: true,
              ticks: {
                font: { family: "var(--font-primary-bold)", size: 16 },
              },
            },
            x: {
              grid: { display: true, color: "rgba(0,0,0,0.1)" },
              min: 0,
              max: 20,
              ticks: {
                maxTicksLimit: 20,
                font: { family: "var(--font-primary-regular)", size: 16 },
              },
            },
          },
          plugins: {
            title: {
              display: true,
              text: "مقارنة التوقعات مع السعر الفعلي",
              font: {
                size: 12,
                family: "var(--font-primary-thin)",
              },
            },
            tooltip: { enabled: true, rtl: true },

            zoom: {
              pan: {
                enabled: true,
                mode: "x",
                scaleMode: "x",
                threshold: 40,
              },
              zoom: {
                drag: { enabled: false },
                wheel: { enabled: true, speed: 0.1 },
                pinch: { enabled: true },
                mode: "x",
                scaleMode: "x",
              },
            },
          },
          animation: {
            duration: 300,
          },
          interaction: {
            intersect: false,
            mode: "index",
          },
        }}
        data={{
          labels: data.predections?.map(
            (item) => item.predictionDate.split("T")[0]
          ), // x-axis

          datasets: [
            {
              label: "السعر الفعلي",
              data: data.predections?.map((item) => item.actualResult), // data = y-axis
              backgroundColor: "rgba(43,63,229,0.8)",
              barThickness: 10,
              borderRadius: 4,
            },
            {
              label: "التوقع",
              data: data.predections?.map((item) => item.prediction),
              backgroundColor: "rgba(250,192,19,0.8)",
              barThickness: 10,
              borderRadius: 4,
            },
          ],
        }}
      />
    // </div>
  );
}
