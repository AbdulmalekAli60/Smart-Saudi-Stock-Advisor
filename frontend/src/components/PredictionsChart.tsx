import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  ChartOptions,
} from "chart.js/auto";
import Zoom from "chartjs-plugin-zoom";
import { Bar } from "react-chartjs-2";
import PredictionInterface from "../Interfaces/PredictionInterface";
import { useEffect, useState } from "react";
import getNumberOfDataPointsBasedOnWidth from "../utils/GetNumberOfDataPointsBasedOnWidth";
import { SelectedValue } from "../Interfaces/SelectedValueInterface";
import zoomPlugin from "chartjs-plugin-zoom";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  Zoom,
  zoomPlugin
);

ChartJS.defaults.font.family = "var(--font-primary-regular)";
interface PredictionsChartProps {
  predections: PredictionInterface[] | undefined;

  limits?: SelectedValue;
}

export default function PredictionsChart({
  predections,
  limits,
}: PredictionsChartProps) {
  const [dataPoints, setDataPoints] = useState<number>(0);
  
  const [filteredPredections, setFilteredPredections] = useState<
    PredictionInterface[] | undefined
  >(undefined);

  useEffect(() => {
    const handleResize = () => {
      setDataPoints(getNumberOfDataPointsBasedOnWidth());
    };

    handleResize();

    window.addEventListener("resize", handleResize);

    return () => window.removeEventListener("resize", handleResize);
  }, [dataPoints]);

  useEffect(() => {
    const filteredDates = predections?.filter((predecion) => {
      const predDate = predecion.predictionDate.split("T")[0];

      if (!limits?.from.value && !limits?.to.value) return true;

      if (limits.from.value && !limits.to.value) {
        return predDate >= limits.from.value;
      }

      if (!limits?.from.value && limits?.to.value) {
        return predDate <= limits.to.value;
      }

      return predDate >= limits.from.value && predDate <= limits.to.value;
    });

    setFilteredPredections(filteredDates);
  }, [predections, limits]);

  const optionsObj: ChartOptions<"bar"> = {
    devicePixelRatio: 1,
    responsive: true,
    maintainAspectRatio: false,

    scales: {
      y: {
        beginAtZero: false,
        ticks: {
          maxTicksLimit: getNumberOfDataPointsBasedOnWidth(),
          font: { family: "var(--font-primary-bold)", size: 16 },
        },
        // stacked: true,
      },
      x: {
        min: Math.max(0, (filteredPredections?.length || 0) - dataPoints),
        max: (filteredPredections?.length || 0) - 1,
        ticks: {
          maxTicksLimit: getNumberOfDataPointsBasedOnWidth(),
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
            size: 40,
            weight: "bold",
          },
        },
      },

      tooltip: {
        rtl: true,
        callbacks: {
          label: function (context) {
            // console.log(context);
            const label = context.dataset.label || "";
            const value = context.parsed.y;

            if (value === 0) {
              return `${label}:  لايوجد حتى الان`;
            }
            return `${label}: ${value}`;
          },
        },
      },

      zoom: {
        pan: {
          enabled: true,
          mode: "x",
          scaleMode: "x",
          threshold: 10,
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
      axis: "x",
    },
  };

  const dataObj = {
    labels: filteredPredections?.map(
      (item) => item.predictionDate.split("T")[0]
    ), // x-axis

    datasets: [
      {
        label: "السعر الفعلي",
        data: filteredPredections?.map((item) => item.actualResult || []), // data = y-axis
        backgroundColor: "rgba(100,63,229,0.8)",
        barThickness: 20,
        borderRadius: 4,
      },
      {
        label: "التوقع",
        data: filteredPredections?.map((item) =>
          parseInt(item.prediction.toFixed(2))
        ),
        backgroundColor: "rgba(250,192,15,0.8)",
        barThickness: 10,
        borderRadius: 4,
      },
    ],
  };

  return (
    <div className="bg-gray-50 w-full h-full rounded-xl  p-4 border">
      <Bar options={optionsObj} data={dataObj} />
    </div>
  );
}
