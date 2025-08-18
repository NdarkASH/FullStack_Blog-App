import React, { StrictMode } from "react";
import ReactDOM from "react-dom/client";

import App from "./App.tsx";
import "@/styles/globals.css";
import { NextUIProvider } from "@nextui-org/react";

ReactDOM.createRoot(document.getElementById("root")!).render(
  <StrictMode>
      <NextUIProvider>
        <App />
      </NextUIProvider>
  </StrictMode>,
);
