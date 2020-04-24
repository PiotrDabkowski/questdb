export type ColumnShape = Readonly<{ name: string; type: string }>

export type QuestDBExecShape<T> = {
  query: string
  columns: ColumnShape[]
  dataset: T[]
  count: number
}
