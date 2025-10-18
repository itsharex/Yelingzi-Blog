export interface Tags {
  id: number
  tagName: string
  articleCount: number
}

export interface Tag {
  id: number;
  tagName: string;
}

export interface Category {
  id: number;
  categoryName: string;
}

export interface ArticleCondition {
  id: number;
  articleCover: string;
  title: string;
  category: Category;
  tagList: Tag[];
  createTime: string;
  content: string
}
