export function waitForPio(timeout = 10000): Promise<any> {
  return new Promise((resolve, reject) => {
    let timeoutId: number | null = null

    const check = () => {
      if ((window as any).PioInstance) {
        if (timeoutId) clearTimeout(timeoutId)
        resolve((window as any).PioInstance)
      } else {
        requestAnimationFrame(check)
      }
    }

    timeoutId = window.setTimeout(() => {
      reject(new Error('PioInstance 加载超时'))
    }, timeout)

    check()
  })
}
