import request, { buildQuery } from '@/utils/request'

//聊天列表
export const getSessionListService = (device: string) => {
  return request.get('/api/ai/session', null, { 'x-host': device })
}


export const getHistoryBySessionService = (parmas: { device: string, sessionId: string }) => {
  return request.get(buildQuery('/api/ai/history', { sessionId: parmas.sessionId }), null, { 'x-host': parmas.device })
}

export const addSessionIdService = (parmas: { device: string, prompt: string }) => {
  return request.get(buildQuery('/api/ai/session/add',{ prompt: parmas.prompt }), null, { 'x-host': parmas.device })
}

export const sendAIChatServiceService = (parmas: { device: string, sessionId: string, prompt: string }) => {
  return request.post('/api/ai/app/chat', 
    {
      sessionId: parmas.sessionId,
      prompt: parmas.prompt
    }
  , { 'x-host': parmas.device })
}

export const sendAIChatsServiceService = (
  parmas: { options: { platform: string, model: string, temperature: number }, sessionId: string, prompt: string }, device: string
) => {
  return request.post('/api/ai/app/chats', parmas, { headers: { 'x-host': device } })
}
