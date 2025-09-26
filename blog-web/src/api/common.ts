import request from '@/utils/request'

export const getWeatherService = (device: string) => request.get('/api/weather', {
  headers: { 'x-host': device }
})
