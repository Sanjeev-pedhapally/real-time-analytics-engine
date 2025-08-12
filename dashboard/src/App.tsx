import React, { useEffect, useState } from 'react';
import { 
  AppBar, 
  Box, 
  Container, 
  Paper, 
  ThemeProvider, 
  Toolbar, 
  Typography,
  createTheme 
} from '@mui/material';
import { Grid } from '@mui/material';
import { styled } from '@mui/material/styles';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { RealTimeChart } from './components/RealTimeChart';
import { WebSocketService } from './WebSocketService';

const wsService = new WebSocketService();

interface Metrics {
  eventsPerSecond: number[];
  pageViews: number[];
  cartActions: number[];
  purchases: number[];
}

// Use MuiGrid directly instead of styled wrapper
const theme = createTheme();

const Dashboard: React.FC = () => {
  const [metrics, setMetrics] = useState<Metrics>({
    eventsPerSecond: [],
    pageViews: [],
    cartActions: [],
    purchases: []
  });
  const [labels, setLabels] = useState<string[]>([]);

  useEffect(() => {
    const connectWs = async () => {
      try {
        await wsService.connect('ws://localhost:8080/ws');
        wsService.subscribe((data) => {
          setMetrics(prev => ({
            eventsPerSecond: [...prev.eventsPerSecond.slice(-49), data.eventsPerSecond || 0],
            pageViews: [...prev.pageViews.slice(-49), data.pageViews || 0],
            cartActions: [...prev.cartActions.slice(-49), data.cartActions || 0],
            purchases: [...prev.purchases.slice(-49), data.purchases || 0]
          }));
          setLabels(prev => [...prev.slice(-49), new Date().toLocaleTimeString()]);
        });
      } catch (error) {
        console.error('WebSocket connection failed:', error);
      }
    };
    connectWs();
    return () => wsService.disconnect();
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <Box sx={{ padding: '20px' }}>
        <Typography variant="h4" gutterBottom>
          Real-Time Analytics Dashboard
        </Typography>
        <Box sx={{ display: 'grid', gap: 3, gridTemplateColumns: { xs: '1fr', md: 'repeat(2, 1fr)' } }}>
          <Paper elevation={3} sx={{ p: 2, height: '300px' }}>
            <RealTimeChart 
              data={metrics.eventsPerSecond} 
              labels={labels} 
              title="Total Events/sec" 
            />
          </Paper>
          <Paper elevation={3} sx={{ p: 2, height: '300px' }}>
            <RealTimeChart 
              data={metrics.pageViews} 
              labels={labels} 
              title="Page Views/sec" 
            />
          </Paper>
          <Paper elevation={3} sx={{ p: 2, height: '300px' }}>
            <RealTimeChart 
              data={metrics.cartActions} 
              labels={labels} 
              title="Cart Actions/sec" 
            />
          </Paper>
          <Paper elevation={3} sx={{ p: 2, height: '300px' }}>
            <RealTimeChart 
              data={metrics.purchases} 
              labels={labels} 
              title="Purchases/sec" 
            />
          </Paper>
        </Box>
      </Box>
    </ThemeProvider>
  );
};

const App: React.FC = () => {
  return (
    <Router>
      <div>
        <nav style={{ 
          background: '#333', 
          padding: '1rem',
          marginBottom: '2rem'
        }}>
          <Link to="/" style={{ color: 'white', marginRight: '1rem', textDecoration: 'none' }}>
            Dashboard
          </Link>
        </nav>
        <Routes>
          <Route path="/" element={<Dashboard />} />
        </Routes>
      </div>
    </Router>
  );
};

export default App;
