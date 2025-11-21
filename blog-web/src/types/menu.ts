// src/config/menu.ts
export interface MenuItem {
  name: string
  path: string
  icon: string
  colorClass: string
  external?: boolean          // 外链标记
  children?: MenuItem[]
}

/**
 * 返回菜单结构的工厂函数，方便后续动态传参
 */
export const getMenuItems = (): MenuItem[] => [
  {
    name: 'home',
    path: '/',
    icon: 'icon-zhuye',
    colorClass: 'home-link'
  },
  {
    name: 'article',
    path: '/archive',
    icon: 'icon-shu',
    colorClass: 'archive-link',
    children: [
      { name: 'archive', path: '/archive', icon: 'icon-jishi', colorClass: 'clock-link' },
      { name: 'category', path: '/category', icon: 'icon-fenlei', colorClass: 'category-link' },
      { name: 'tag', path: '/tag', icon: 'icon-biaoqian', colorClass: 'tag-link' }
    ]
  },
  {
    name: 'talk',
    path: '/talks',
    icon: 'icon-xiaoxi',
    colorClass: 'talk-link'
  },
  {
    name: 'album',
    path: '/album',
    icon: 'icon-zhaopian',
    colorClass: 'photos-link'
  },
  {
    name: 'messageBorad',
    path: '/message',
    icon: 'icon-xinfeng',
    colorClass: 'message-link',
    children: [
      { name: 'messageBorad', path: '/message', icon: 'icon-xinfeng', colorClass: 'about-link-me' },
      { name: 'linkMe', path: '/link/message', icon: 'icon-fasong', colorClass: 'about-link-me' },
      { name: 'chatRoom', path: '/chat/chatroom', icon: 'icon-liaotianshi', colorClass: 'home-link' }
    ]
  },
  {
    name: 'friend',
    path: '/friend',
    icon: 'icon-tuandui',
    colorClass: 'home-link'
  },
  {
    name: 'aboutBlog',
    path: '/about',
    icon: 'icon-tishi1',
    colorClass: 'about-link',
    children: [
      { name: 'about', path: '/about', icon: 'icon-geren', colorClass: 'about-me' },
      { name: 'chatAI', path: '/chatai', icon: 'icon-rengongzhineng', colorClass: 'aichat' },
      { name: 'backManagement', path: 'https://blog.yeling.top/', icon: 'icon-diannao', colorClass: 'admin-link', external: true },
      { name: 'openSource', path: 'https://github.com/yelingzi/Yelingzi-Blog', icon: 'icon-github', colorClass: 'admin-link', external: true }
    ]
  }
]
