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
import { Bar } from "react-chartjs-2";
import PredictionInterface from "../Interfaces/PredictionInterface";
import { useEffect, useState } from "react";
import getNumberOfDataPointsBasedOnWidth from "../utils/GetNumberOfDataPointsBasedOnWidth";

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
  const [dataPoints, setDataPoints] = useState<number>(5);

  useEffect(() => {
    const handleResize = () => {
      setDataPoints(getNumberOfDataPointsBasedOnWidth());
    };

    handleResize();

    window.addEventListener("resize", handleResize);

    return () => window.removeEventListener("resize", handleResize);
  }, []);

  return (
    <div className="bg-gray-200 w-full h-full rounded-xl  p-4">
      <Bar
        options={{
          devicePixelRatio: 1,
          responsive: true,
          maintainAspectRatio: false,

          scales: {
            y: {
              beginAtZero: true,
              ticks: {
                maxTicksLimit: 20,
                font: { family: "var(--font-primary-bold)", size: 16 },
              },
              // stacked: true,
            },
            x: {
              min: Math.max(0, (data.predections?.length || 0) - dataPoints),
              max: (data.predections?.length || 0) - 1,
              ticks: {
                maxTicksLimit: 20,
                font: { family: "var(--font-primary-regular)", size: 16 },
              },
              // stacked: true,
            },
          },
          plugins: {
            title: {
              display: true,
              text: "مقارنة التوقعات مع السعر الفعلي",
              padding: { top: 0 },
              font: {
                size: 30,
                family: "Helvetica Neue",
              },
            },
            legend: {
              labels: {
                font: {
                  size: 30,
                  weight: "bold",
                },
              },
            },

            tooltip: { rtl: true },

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
              },
            },
          },
          animation: {
            duration: 300,
          },
          interaction: {
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
    </div>
  );
}
