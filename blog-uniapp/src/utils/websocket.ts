import { useUserStore } from '@/stores'


/* ---------- 类型声明 ---------- */
type MessageHandler = (data: any) => void

/* ---------- 核心数据结构 ---------- */
const keyHandlerMap = new Map<string, Set<MessageHandler>>()

/* ---------- 注册 / 注销 ---------- */
/** 注册某个 key 的处理器 */
export function addWsMessageHandler(key: string, handler: MessageHandler) {
  if (!keyHandlerMap.has(key)) keyHandlerMap.set(key, new Set())
  keyHandlerMap.get(key)!.add(handler)
}

/** 注销某个 key 的处理器 */
export function removeWsMessageHandler(key: string, handler: MessageHandler) {
  const set = keyHandlerMap.get(key)
  if (!set) return
  set.delete(handler)
  if (set.size === 0) keyHandlerMap.delete(key)
}

/* ---------- 消息派发 ---------- */
function handleWsMessage(msg: {messageType: string, data: any}) {
  // 约定消息必有 key 字段
  if (!msg || typeof msg.messageType !== 'string') {
    console.warn('[WS] 消息缺少 key，无法派发', msg)
    return
  }
  const { messageType, data } = msg
  const handlers = keyHandlerMap.get(messageType)
  if (!handlers || handlers.size === 0) {
    console.warn(`[WS] 未注册 key="${messageType}" 的处理器`)
    return
  }
  handlers.forEach(fn => fn(data))
}

let ws: UniApp.SocketTask | null = null
let pingTimer: number | null = null


// 打开 WebSocket 连接
export async function openWs() {
  if (ws) return
  
  const userStore = useUserStore()
  const token = userStore.refreshToken || userStore.deviceId
  if (!token) {
    console.error('无法建立 WebSocket 连接：缺少 token')
    return
  }

  try {
    const socketTask = uni.connectSocket({
		url: `wss://www.yeling.top/ws?token=${token}`,
		// url: `ws://localhost:12360/ws?token=${token}`,
		complete: () => { },
	}) as UniApp.SocketTask
    
    ws = socketTask

    ws.onOpen(() => {
      console.log('WebSocket 连接已建立')
      // 心跳机制
      pingTimer = setInterval(() => {
        if (ws) {
          ws.send({ data: 'ping' })
        }
      }, 25_000) as unknown as number
    })

    ws.onClose(() => {
      console.log('WebSocket 连接已关闭')
      if (pingTimer) {
        clearInterval(pingTimer)
        pingTimer = null
      }
      ws = null
      // 自动重连
      setTimeout(openWs, 3000)
    })

    ws.onMessage((res) => {
      try {
        const body = JSON.parse(res.data as string)
		console.log(body)
            try {
              handleWsMessage(body);   // 这里可能抛错
            } catch (e) {
              console.error('[WS] 消息派发失败', e);
            }
      } catch (e) {
        console.error('WS 消息格式错误', res.data)
      }
    })
    
    ws.onError((err) => {
      console.error('WebSocket 错误:', err)
      if (pingTimer) {
        clearInterval(pingTimer)
        pingTimer = null
      }
      ws = null
      // 自动重连
      setTimeout(openWs, 5000)
    })
  } catch (error) {
    console.error('WebSocket 连接失败:', error)
    // 自动重连
    setTimeout(openWs, 5000)
  }
}

// 关闭 WebSocket 连接
export function closeWs() {
  if (ws) {
    ws.close({})
    ws = null
  }
  if (pingTimer) {
    clearInterval(pingTimer)
    pingTimer = null
  }
}