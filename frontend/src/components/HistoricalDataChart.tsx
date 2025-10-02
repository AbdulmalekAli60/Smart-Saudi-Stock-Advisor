import { Bar } from "react-chartjs-2";
import HistoricalDataInterface from "../Interfaces/HistoricalDataInterface";
import { SelectedValue } from "../Interfaces/SelectedValueInterface";
import { useEffect, useState } from "react";
import getNumberOfDataPointsBasedOnWidth from "../utils/GetNumberOfDataPointsBasedOnWidth";
import { ChartOptions } from "chart.js";

interface HistoricalDataChartProps {
  historicalData: HistoricalDataInterface[] | undefined;

  limits?: SelectedValue;
}
export default function HistoricalDataChart({
  historicalData,
  limits,
}: HistoricalDataChartProps) {
  const [dataPoints, setDataPoints] = useState<number>(0);

  const [filteredHistoricalData, setFilteredHistoricalData] = useState<
    HistoricalDataInterface[] | undefined
  >(undefined);

  useEffect(() => {
    const handleResize = () => {
      setDataPoints(getNumberOfDataPointsBasedOnWidth());
    };

    handleResize();

    window.addEventListener("resize", handleResize);

    return () => window.removeEventListener("resize", handleResize);
  }, []);

  useEffect(() => {
    const filteredDates = historicalData?.filter((data) => {
      const dataDate = data.dataDate.split("T")[0];

      if (!limits?.from.value || !limits?.to.value) return true;

      if (limits.from.value && !limits.to.value) {
        return dataDate >= limits.from.value;
      }

      if (!limits?.from.value && limits?.to.value) {
        return dataDate <= limits.to.value;
      }

      return dataDate >= limits.from.value && dataDate <= limits.to.value;
    });

    setFilteredHistoricalData(filteredDates);
  }, [historicalData, limits]);

  const optionsObj: ChartOptions<"bar"> = {
    devicePixelRatio: 1,
    responsive: true,
    maintainAspectRatio: false,

    scales: {
      y: {
        display: true,
        beginAtZero: false,
        ticks: {
          maxTicksLimit: getNumberOfDataPointsBasedOnWidth(),
          font: { family: "var(--font-primary-bold)", size: 16 },
        },
        // stacked: true,
      },
      x: {
        min: Math.max(0, (filteredHistoricalData?.length || 0) - dataPoints),
        max: (filteredHistoricalData?.length || 0) - 1,

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
        text: "البيانات التاريخية",
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
            style: "italic",
            weight: "bold",
          },
        },
      },

      tooltip: {
        rtl: true,
        callbacks: {
          title: () => "البيانات",
          afterTitle: function (toolTipItems) {
            const dataIndex = toolTipItems[0].dataIndex;
            const dataPointObject = filteredHistoricalData?.at(dataIndex);
            // console.log("All items:" , toolTipItems)
            // console.log("Data index: ", dataIndex)
            // console.log("Data point: ", dataPointObject)
            if (!dataPointObject) return "";

            return [
              `التاريخ: ${dataPointObject.dataDate.split("T")[0]}`,
              `الافتتاح : ${dataPointObject.open}`,
              `الأعلى: ${dataPointObject.high}`,
              `الأدنى : ${dataPointObject.low}`,
              `الإغلاق: ${dataPointObject.close}`,
              `الحجم: ${dataPointObject.volume}`,
            ];
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
    labels: filteredHistoricalData?.map((item) => item.dataDate.split("T")[0]), // x-axis

    datasets: [
      {
        label: "سعر الإغلاق",
        data: historicalData?.map((item) => item.close), // data = y-axis
        backgroundColor: "rgba(100,63,229,0.8)",

        // barThickness: 10,
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
