export class WebSocketService {
  private socket: WebSocket | null = null;
  private listeners: ((data: any) => void)[] = [];

  connect(url: string) {
    this.socket = new WebSocket(url);
    this.socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      this.listeners.forEach((cb) => cb(data));
    };
    this.socket.onclose = () => {
      setTimeout(() => this.connect(url), 2000); // Auto-reconnect
    };
  }

  subscribe(cb: (data: any) => void) {
    this.listeners.push(cb);
  }
}
