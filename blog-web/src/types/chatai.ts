export interface ChatHistory {
  id: number,
  role: string,
  content: string
}

export interface SessionHistory {
  sessionId: string,
  description: string
}

export interface ChatHistoryView {
  id: number,
  role: string,
  content: string
}

export interface SessionHistoryView {
  sessionId: string,
  description: string
}
