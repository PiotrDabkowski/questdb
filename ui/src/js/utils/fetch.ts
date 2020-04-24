type Error = Readonly<{
  error: true
  message: string
  status?: number
}>

type ResponseShape<T> = Readonly<{ data: T; error: false }>

const fetchApi = async <T>(
  input: RequestInfo,
  init?: RequestInit,
): Promise<ResponseShape<T> | Error> => {
  const response = await fetch(input, init)

  if (!response.ok) {
    return {
      error: true,
      message: response.statusText,
      status: response.status,
    }
  }

  try {
    return {
      data: (await response.json()) as T,
      error: false,
    }
  } catch (ex) {
    return {
      error: true,
      message: ex.message,
    }
  }
}

const encodeParams = (params: Record<string, string | number | boolean>) =>
  Object.keys(params)
    .map((k) => `${encodeURIComponent(k)}=${encodeURIComponent(params[k])}`)
    .join("&")

export { encodeParams, fetchApi }
