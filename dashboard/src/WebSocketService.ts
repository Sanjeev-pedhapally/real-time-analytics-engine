interface WebSocketMessage {
  type: string;
  payload: any;
}

export class WebSocketService {
  private socket: WebSocket | null = null;
  private listeners: ((data: any) => void)[] = [];
  private url: string = '';
  private reconnectAttempts: number = 0;
  private maxReconnectAttempts: number = 5;
  private reconnectInterval: number = 2000;
  private isReconnecting: boolean = false;
  private heartbeatInterval: number | null = null;

  private startHeartbeat(): void {
    if (this.heartbeatInterval !== null) {
      this.stopHeartbeat();
    }
    this.heartbeatInterval = window.setInterval(() => {
      if (this.socket?.readyState === WebSocket.OPEN) {
        this.socket.send(JSON.stringify({ type: 'heartbeat' }));
      }
    }, 30000); // Send heartbeat every 30 seconds
  }

  private stopHeartbeat(): void {
    if (this.heartbeatInterval !== null) {
      clearInterval(this.heartbeatInterval);
      this.heartbeatInterval = null;
    }
  }

  async connect(url: string): Promise<void> {
    this.url = url;
    this.reconnectAttempts = 0;
    return this.establishConnection();
  }

  private async establishConnection(): Promise<void> {
    return new Promise((resolve, reject) => {
      try {
        this.socket = new WebSocket(this.url);
        this.socket.onopen = () => {
          console.log('WebSocket connection established');
          this.reconnectAttempts = 0;
          this.isReconnecting = false;
          this.startHeartbeat();
          resolve();
        };

        this.socket.onerror = (error) => {
          console.error('WebSocket error:', error);
          if (!this.isReconnecting) {
            reject(error);
          }
        };

        this.socket.onmessage = (event) => {
          try {
            const message: WebSocketMessage = JSON.parse(event.data);
            if (message.type === 'heartbeat') {
              // Handle heartbeat response
              return;
            }
            this.listeners.forEach((cb) => cb(message.payload));
          } catch (error) {
            console.error('Failed to parse WebSocket message:', error);
          }
        };

        this.socket.onclose = () => {
          console.log('WebSocket connection closed');
          this.stopHeartbeat();
          if (this.reconnectAttempts < this.maxReconnectAttempts) {
            this.isReconnecting = true;
            this.reconnectAttempts++;
            console.log(`Attempting to reconnect (${this.reconnectAttempts}/${this.maxReconnectAttempts})`);
            setTimeout(() => this.establishConnection(), this.reconnectInterval);
          } else {
            console.error('Max reconnection attempts reached');
          }
        };
      } catch (error) {
        reject(error);
      }
    });
  }

  disconnect() {
    this.stopHeartbeat();
    if (this.socket) {
      this.socket.close();
      this.socket = null;
      this.listeners = [];
      this.reconnectAttempts = this.maxReconnectAttempts; // Prevent auto-reconnect
    }
  }

  subscribe(cb: (data: any) => void) {
    this.listeners.push(cb);
    return () => {
      this.listeners = this.listeners.filter(listener => listener !== cb);
    };
  }

  isConnected(): boolean {
    return this.socket?.readyState === WebSocket.OPEN;
  }

  send(data: any): void {
    if (!this.isConnected()) {
      throw new Error('WebSocket is not connected');
    }
    this.socket!.send(JSON.stringify({
      type: 'message',
      payload: data
    }));
  }
}
