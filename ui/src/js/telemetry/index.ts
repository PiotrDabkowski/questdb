// import { fetchApi } from "../utils"
import ProcessWorker from "./process.worker.ts"

type TelemetryResponse = Readonly<{ active: boolean; interval: number }>

// eslint-disable-next-line
const start = async () => {
  // const response = await fetchApi<TelemetryResponse>("/telemetry")
  const response = { error: false, data: { active: true, interval: 1e4 } }

  if (!response.error && response.data.active) {
    const startTelemetryWorker = () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const worker: Worker = new ProcessWorker()
    }

    setInterval(startTelemetryWorker, response.data.interval)
  }
}

export default start
