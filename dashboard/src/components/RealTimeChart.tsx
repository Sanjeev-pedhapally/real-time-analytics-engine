import React, { useEffect, useRef } from 'react';
import { Chart, ChartConfiguration } from 'chart.js';

interface RealTimeChartProps {
  data: number[];
  labels: string[];
  title: string;
}

export const RealTimeChart: React.FC<RealTimeChartProps> = ({ data, labels, title }) => {
  const chartRef = useRef<HTMLCanvasElement>(null);
  const chartInstance = useRef<Chart | null>(null);

  useEffect(() => {
    if (chartRef.current) {
      if (!chartInstance.current) {
        const config: ChartConfiguration = {
          type: 'line',
          data: {
            labels,
            datasets: [{
              label: title,
              data,
              borderColor: 'rgba(75,192,192,1)',
              backgroundColor: 'rgba(75,192,192,0.2)',
              fill: true,
            }],
          },
          options: {
            responsive: true,
            animation: false,
            scales: { x: { display: true }, y: { display: true } },
          },
        };
        chartInstance.current = new Chart(chartRef.current, config);
      } else {
        chartInstance.current.data.labels = labels;
        chartInstance.current.data.datasets[0].data = data;
        chartInstance.current.update();
      }
    }
  }, [data, labels, title]);

  return <canvas ref={chartRef} />;
};
