// src/config/menu.ts
export interface MenuItem {
  name: string
  path: string
  icon: string
  colorClass: string
  isTabBar: boolean
  external?: boolean          // 外链标记
  children?: MenuItem[]
}

/**
 * 返回菜单结构的工厂函数，方便后续动态传参
 */
export const getMenuItems = (): MenuItem[] => [
  {
    name: 'home',
    path: '/pages/index/index',
    icon: 'icon-zhuye',
    colorClass: 'home-link',
	isTabBar: true
  },
  {
    name: 'article',
    path: '/pages/archive/index',
    icon: 'icon-shu',
    colorClass: 'archive-link',
    children: [
      { name: 'archive', path: '/pages/archive/index', icon: 'icon-jishi', colorClass: 'clock-link', isTabBar: false },
      { name: 'category', path: '/pages/category/index', icon: 'icon-fenlei', colorClass: 'category-link', isTabBar: false },
      { name: 'tag', path: '/pages/tag/index', icon: 'icon-biaoqian', colorClass: 'tag-link', isTabBar: false }
    ],
	isTabBar: false
  },
  {
    name: 'talk',
    path: '/pages/talk/list',
    icon: 'icon-xiaoxi',
    colorClass: 'talk-link',
	isTabBar: true
  },
  {
    name: 'album',
    path: '/pages/album/index',
    icon: 'icon-zhaopian',
    colorClass: 'photos-link',
	isTabBar: false
  },
  {
    name: 'messageBorad',
    path: '/pages/message/index',
    icon: 'icon-xinfeng',
    colorClass: 'message-link',
    children: [
      { name: 'messageBorad', path: '/pages/message/index', icon: 'icon-xinfeng', colorClass: 'about-link-me', isTabBar: false },
      { name: 'linkMe', path: '/pages/chat/detail?chatType=linkme', icon: 'icon-fasong', colorClass: 'about-link-me', isTabBar: false },
      { name: 'chatRoom', path: '/pages/chat/detail?chatType=chatroom', icon: 'icon-liaotianshi', colorClass: 'home-link', isTabBar: false }
    ],
	isTabBar: false
  },
  {
    name: 'friend',
    path: '/pages/friend/index',
    icon: 'icon-tuandui',
    colorClass: 'home-link',
	isTabBar: false
  },
  {
    name: 'aboutBlog',
    path: '/pages/about/index',
    icon: 'icon-tishi1',
    colorClass: 'about-link',
    children: [
      { name: 'about', path: '/pages/about/index', icon: 'icon-geren', colorClass: 'about-me', isTabBar: false },
      { name: 'chatAI', path: '/pages/chatai/index', icon: 'icon-rengongzhineng', colorClass: 'aichat', isTabBar: true },
      { name: 'backManagement', path: 'https://blog.yeling.top/', icon: 'icon-diannao', colorClass: 'admin-link', external: true, isTabBar: false },
      { name: 'openSource', path: 'https://github.com/yelingzi/Yelingzi-Blog', icon: 'icon-github', colorClass: 'admin-link', external: true, isTabBar: false }
    ],
	isTabBar: false
  }
]
