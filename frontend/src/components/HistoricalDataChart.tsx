import { Bar } from "react-chartjs-2";
import HistoricalDataInterface from "../Interfaces/HistoricalDataInterface";
import { SelectedValue } from "../Interfaces/SelectedValueInterface";
import { useEffect, useState, useMemo } from "react";
import getNumberOfDataPointsBasedOnWidth from "../utils/GetNumberOfDataPointsBasedOnWidth";
import { ChartOptions } from "chart.js";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import zoomPlugin from "chartjs-plugin-zoom"; 


ChartJS.register(
  CategoryScale,  
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
  zoomPlugin
);

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
      
      if (!limits?.from.value && !limits?.to.value) return true;

      if (limits.from.value && !limits.to.value)
        return dataDate >= limits.from.value;

      if (!limits?.from.value && limits?.to.value)
        return dataDate <= limits.to.value;

      return dataDate >= limits.from.value && dataDate <= limits.to.value;
    });
    setFilteredHistoricalData(filteredDates);
  }, [historicalData, limits]);

  const optionsObj: ChartOptions<"bar"> = useMemo(() => ({
    devicePixelRatio: 1,
    responsive: true,
    maintainAspectRatio: false,
    interaction: {
        mode: "index",
        intersect: false,
    },
    scales: {
      y: {
        display: true,
        beginAtZero: false,
        ticks: {
          maxTicksLimit: 6,
          font: { family: "var(--font-primary-bold)", size: 12 },
        },
      },
      x: {
        min: Math.max(0, (filteredHistoricalData?.length || 0) - dataPoints),
        max: (filteredHistoricalData?.length || 0) - 1,
        ticks: {
            maxTicksLimit: dataPoints,
            font: { family: "var(--font-primary-regular)", size: 12 },
        },
      },
    },
    plugins: {
      title: {
        display: true,
        text: "البيانات التاريخية",
        font: { size: 18, family: "var(--font-primary-bold)" },
        padding: { bottom: 20 }
      },
      legend: { display: false },
      tooltip: {
        rtl: true,
        backgroundColor: "rgba(0, 0, 0, 0.8)",
        titleFont: { family: "var(--font-primary-bold)", size: 14 },
        bodyFont: { family: "var(--font-primary-regular)", size: 13 },
        padding: 10,
        cornerRadius: 8,
        displayColors: false, 
        callbacks: {
          title: () => "تفاصيل السهم", 
          label: function (context) {
             const dataIndex = context.dataIndex;
             const rawData = filteredHistoricalData?.at(dataIndex);

             if (!rawData) return "";
             
             return [
               ` التاريخ: ${rawData.dataDate.split("T")[0]}`,
               ` الإغلاق: ${rawData.close}`,
               ` الافتتاح: ${rawData.open}`,
               ` الأعلى: ${rawData.high}`,
               ` الأدنى: ${rawData.low}`,
               ` الحجم: ${rawData.volume.toLocaleString()}`,
             ];
          },
          afterTitle: () => "" 
        },
      },
      zoom: {
        pan: {
            enabled: true,
            mode: "x",
            modifierKey: "ctrl", 
        },
        zoom: {
            wheel: {
                enabled: true,
            },
            pinch: {
                enabled: true
            },
            mode: "x",
        }
      }
    },
  }), [filteredHistoricalData, dataPoints]);



  const dataObj = {
    labels: filteredHistoricalData?.map((item) => item.dataDate.split("T")[0]),
    datasets: [
      {
        label: "سعر الإغلاق",
        data: filteredHistoricalData?.map((item) => item.close), 
        backgroundColor: "rgba(27, 67, 50, 0.8)", 
        hoverBackgroundColor: "rgba(212, 175, 55, 1)",
        borderRadius: 4,
        barPercentage: 0.6,
        categoryPercentage: 0.8,
      },
    ],
  };

  return (
    <div className="w-full h-full bg-white rounded-xl shadow-sm border border-border p-4">
      <div className="relative w-full h-full">
         <Bar options={optionsObj} data={dataObj} />
      </div>
    </div>
  );
}