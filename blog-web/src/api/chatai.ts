import request from '@/utils/request'
import { useUserStore } from '@/stores'
import { fetchWithTokenRefresh } from '@/utils/fetchWithTokenRefresh'

const userStore = useUserStore()

//聊天列表
export const getSessionListService = (device: string) => {
  return request.get('/api/ai/session', {
    headers: { 'x-host': device }
  })
}


export const getHistoryBySessionService = (parmas: { device: string, sessionId: string }) => {
  return request.get('/api/ai/history', {
    headers: { 'x-host': parmas.device },
    params: { sessionId: parmas.sessionId }
  })
}

export const addSessionIdService = (parmas: { device: string, prompt: string }) => {
  return request.get('/api/ai/session/add', {
    headers: { 'x-host': parmas.device },
    params: { prompt: parmas.prompt }
  })
}

export const sendAIChatServiceService = (parmas: { device: string, sessionId: string, prompt: string }) => {
  return request.post('/api/ai/app/chat', {
    sessionId: parmas.sessionId,
    prompt: parmas.prompt
  }, { headers: { 'x-host': parmas.device } })
}

export const sendAIChatsServiceService = (
  parmas: { options: { platform: string, model: string, temperature: number }, sessionId: string, prompt: string }, device: string
) => {
  return request.post('/api/ai/app/chats', parmas, { headers: { 'x-host': device } })
}

// /**
//  * 发送流式请求
//  */
// export async function sendAIChatStream({
//   device,
//   sessionId,
//   prompt,
//   token,
// }: {
//   device: string;
//   sessionId: string;
//   prompt: string;
//   token: string;
// }) {
//   const fetchFn = () => {
//     const params = new URLSearchParams();
//     params.append('prompt', prompt);
//     params.append('sessionId', sessionId);

//     return fetch(new URL('/api/ai/chat', import.meta.env.VITE_AI_URL).toString(), {
//       method: 'POST',
//       headers: {
//         'Authorization': token ? `${token}` : '',
//         'x-host': device,
//         'Accept': 'text/event-stream',
//       },
//       body: params,
//     });
//   }

//   const response = await fetchWithTokenRefresh(fetchFn)

//   if (!response.ok) {
//     throw new Error(`Server error: ${response.status} ${response.statusText}`);
//   }

//   if (!response.body) {
//     throw new Error('Response body is null');
//   }

//   return response.body
// }

