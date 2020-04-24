import { encodeParams, fetchApi } from "../utils"
import { QuestDBExecShape } from "../types"

type TelemetryRow = [string, number]

const selectFrom = (table: string) => `SELECT * FROM ${table}`

const start = async () => {
  const queryData = {
    query: selectFrom("bids"),
  }
  const response = await fetchApi<QuestDBExecShape<TelemetryRow>>(
    `/exec?${encodeParams(queryData)}`,
  )

  if (!response.error) {
    // Send to lambda function
  }
}
start()
