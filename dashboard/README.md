# React TypeScript Dashboard

## Overview
Real-time analytics dashboard with WebSocket live updates, interactive charts (Chart.js), and responsive routing.

## Setup
1. Install dependencies:
   ```sh
   npm install
   ```
2. Start development server:
   ```sh
   npm start
   ```
3. Build for production:
   ```sh
   npm run build
   ```
4. Build Docker image:
   ```sh
   docker build -t analytics-dashboard .
   docker run -p 80:80 analytics-dashboard
   ```

## Features
- Real-time charts (events/sec, revenue, top products)
- WebSocket live updates
- Responsive design, routing
- FAANG-grade performance

## Performance Notes
- Sub-second chart updates
- Handles 10K+ events/sec
- Ready for interview demos
