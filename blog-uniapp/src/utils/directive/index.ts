// directive/index.ts
import slideIn from "./slideIn/slideIn";
import type { App } from "vue";

export default (App: App) => {
  slideIn(App)
}
