import { nextTick, type App } from "vue";

export default function install(app: App) {
  app.directive('slide-in', {
    mounted(el: any) {
      const selector = `.${el.className.split(' ').join('.')}`;

      // 创建小程序 IntersectionObserver
      const observer = wx.createIntersectionObserver(el);
      observer.relativeToViewport({ bottom: 100 }).observe(selector, (res) => {
        if (res.intersectionRatio > 0) {
          // 触发动画
          const animation = wx.createAnimation({
            duration: 500,
            timingFunction: 'ease-out',
          });
          animation.translateY(0).opacity(1).step();

          el.animation = animation.export();
          observer.disconnect(); // 只触发一次
        }
      });

      // 初始状态：下移 100px，透明
      const initial = wx.createAnimation({
        duration: 0,
      });
      initial.translateY(100).opacity(0).step();
      el.animation = initial.export();
    },
  });
}