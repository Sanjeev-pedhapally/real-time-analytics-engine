import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import { Paper } from '@mui/material';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

interface RealTimeChartProps {
  data: number[];
  labels: string[];
  title: string;
}

export const RealTimeChart: React.FC<RealTimeChartProps> = ({ data, labels, title }) => {
  const options = {
    responsive: true,
    maintainAspectRatio: false,
    animation: {
      duration: 750,
      easing: 'easeInOutQuart'
    } as const,
    scales: {
      y: {
        beginAtZero: true,
        grid: {
          color: 'rgba(0,0,0,0.1)',
          drawBorder: false
        },
        ticks: {
          color: '#666'
        }
      },
      x: {
        grid: {
          display: false
        },
        ticks: {
          color: '#666',
          maxRotation: 45,
          minRotation: 45
        }
      }
    },
    plugins: {
      legend: {
        display: true,
        position: 'top' as const,
      },
    },
  };

  const chartData = {
    labels,
    datasets: [
      {
        label: title,
        data,
        borderColor: 'rgba(75,192,192,1)',
        backgroundColor: 'rgba(75,192,192,0.2)',
        borderWidth: 2,
        fill: true,
        tension: 0.4,
      },
    ],
  };

  return (
    <div style={{ width: '100%', height: '400px', padding: '20px' }}>
      <Line options={options} data={chartData} />
    </div>
  );
};
