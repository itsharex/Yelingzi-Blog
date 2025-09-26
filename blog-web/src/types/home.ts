export interface MenuItem {
  name: string
  path: string
  icon: string
  colorClass: string
  children?: MenuItem[]
  external?: boolean
}

export interface Weather {
  sheng: string,
  shi: string,
  weather1: string,
  weather2: string,
  wd1: string,
  wd2: string,
  winddirection1: string,
  winddirection2: string,
  windleve1: string,
  windleve2: string,
  uptime: string,
  lat: string,
  lon: string,
  nowinfo: NewInfo
}

export interface NewInfo {
  precipitation: string,
  temperature: string,
  pressure: string,
  humidity: string,
  windDirection: string,
  windDirectiondegree: string,
  windSpeed: string,
  windScale: string,
  feelst: string,
  uptime: string
}

export interface WeatherView {
  icon: string,
  title: string,
  value: string
}
