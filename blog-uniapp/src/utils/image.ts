import { APP_BASE_URL } from "./config"

/**
 * 给相对路径图片补全域名（支持强制HTTPS）
 * @param src 图片路径：可能值如 '/upload/a.jpg'、'https://cdn.com/b.png'、null/undefined
 * @param ssl 是否强制使用HTTPS（默认true，会将http转为https）
 * @returns 处理后的有效路径（空值兜底为''）
 */
export const getImageUrl = (src?: string | null, ssl: boolean = true): string => {
  // 1. 空值兜底
  if (!src) return '';

  // 2. 强制HTTPS：如果是http开头且ssl为true，替换为https
  let processedSrc = src;
  if (processedSrc.startsWith('http://') && ssl) {
    processedSrc = processedSrc.replace('http://', 'https://');
  }

  // 3. 已有有效域名（https或/static）直接返回
  if (processedSrc.startsWith('https://') || processedSrc.startsWith('/static')) {
    return processedSrc;
  }

  // 4. 补全域名：去掉开头的多余斜杠，拼接基础域名
  const cleanPath = processedSrc.replace(/^\/+/, ''); 
  return `${APP_BASE_URL}/${cleanPath}`;
};

/**
 * 批量处理图片路径数组，返回补全域名后的路径数组
 * @param srcList 图片路径数组（元素支持字符串、null、undefined）
 * @param ssl 是否强制HTTPS（默认true，透传给getImageUrl）
 * @returns 处理后的路径数组（空值转为空字符串）
 */
export const getImageUrls = (
  srcList?: Array<string | null | undefined>,
  ssl: boolean = true
): string[] => {
  // 兜底：空数组或未传参数时返回空数组
  if (!srcList || srcList.length === 0) return [];

  // 遍历每个元素，调用getImageUrl处理
  return srcList.map((src) => getImageUrl(src, ssl));
};

/**
 * 批量处理（接口返回数组最常见）
 * @param list 任意含 url 或 src 字段的数组
 * @param field 要替换的字段名，默认 'url'
 */
export const fillImageDomain = <T extends Record<string, any>>(
  list: T[],
  field: string = 'url' || 'src'
): T[] => {
  return list.map(item => ({
    ...item,
    [field]: getImageUrl(item[field])
  }))
}