import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { RealTimeChart } from './components/RealTimeChart';
import { WebSocketService } from './WebSocketService';

const wsService = new WebSocketService();

const Dashboard: React.FC = () => {
  const [metrics, setMetrics] = useState<number[]>([]);
  const [labels, setLabels] = useState<string[]>([]);

  useEffect(() => {
    wsService.connect('ws://localhost:8080/ws');
    wsService.subscribe((data) => {
      setMetrics((prev) => [...prev.slice(-49), data.metric]);
      setLabels((prev) => [...prev.slice(-49), new Date().toLocaleTimeString()]);
    });
  }, []);

  return (
    <div>
      <h2>Real-Time Metrics</h2>
      <RealTimeChart data={metrics} labels={labels} title="Events/sec" />
    </div>
  );
};

const TopProducts: React.FC = () => (
  <div>
    <h2>Top Products</h2>
    {/* Add chart or table for top products */}
  </div>
);

const Revenue: React.FC = () => (
  <div>
    <h2>Revenue</h2>
    {/* Add chart for revenue */}
  </div>
);

const App: React.FC = () => (
  <Router>
    <nav>
      <Link to="/">Dashboard</Link> | <Link to="/top-products">Top Products</Link> | <Link to="/revenue">Revenue</Link>
    </nav>
    <Routes>
      <Route path="/" element={<Dashboard />} />
      <Route path="/top-products" element={<TopProducts />} />
      <Route path="/revenue" element={<Revenue />} />
    </Routes>
  </Router>
);

export default App;
