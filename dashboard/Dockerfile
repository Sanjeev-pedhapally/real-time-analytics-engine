# React Dashboard Dockerfile
FROM node:20-alpine as build

WORKDIR /app

COPY package.json tsconfig.json ./
RUN npm install
RUN npm install react-scripts -g
COPY public ./public
COPY src ./src
RUN npm run build

FROM nginx:1.25-alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
