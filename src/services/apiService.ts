import { UUID } from "crypto";

import axios, {
  AxiosError,
  AxiosInstance,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";

export interface AppWrapperResponse<T> {
  code: number;
  msg: string;
  data: T;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  expiresIn: number;
}

export interface AuthorResponse {
  authorId: UUID;
  authorName: string;
}

export interface Category {
  uuid: UUID;
  name: string;
  postCount: number;
}

export interface Tag {
  uuid: UUID;
  name: string;
  postCount?: number;
}

export interface Post {
  id: UUID;
  title: string;
  content: string;
  author?: {
    id: UUID;
    name: string;
  };
  category: Category;
  tags: Tag[];
  readingTime?: number;
  createdAt: string;
  updatedAt: string;
  status?: PostStatus;
}

export interface CreatePostRequest {
  title: string;
  content: string;
  categoryId: UUID;
  tagsIds: UUID[];
  status: PostStatus;
}

export interface UpdatePostRequest extends CreatePostRequest {
  id: string;
}

export enum PostStatus {
  DRAFT = "DRAFT",
  PUBLISHED = "PUBLISHED",
}
class ApiService {
  private api: AxiosInstance;
  private static instance: ApiService;

  private constructor() {
    this.api = axios.create({
      baseURL: "/api/v1",
      headers: {
        "Content-Type": "application/json",
      },
    });

    this.api.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem("token");

        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
      },
      (error: AxiosError) => {
        return Promise.reject(error);
      },
    );
    this.api.interceptors.response.use(
      (response: AxiosResponse) => response,
      (error: AxiosError) => {
        if (error.response?.status === 401) {
          localStorage.removeItem("token");
          window.location.href = "login";
        }

        return Promise.reject(this.handleError(error));
      },
    );
  }
  public static getInstance(): ApiService {
    if (!ApiService.instance) {
      ApiService.instance = new ApiService();
    }

    return ApiService.instance;
  }

  private handleError(error: AxiosError): AppWrapperResponse<any> {
    if (error.response?.data) {
      return error.response.data as AppWrapperResponse<any>;
    }

    return {
      code: 500,

      msg: "An unexpected error occurred",

      data: error,
    };
  }

  public async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response: AxiosResponse<AppWrapperResponse<AuthResponse>> =
      await this.api.post("/auth/login", credentials);

    localStorage.setItem("token", response.data.data.token);

    return response.data.data;
  }

  public logout(): void {
    localStorage.removeItem("token");
  }

  public async register(register: RegisterRequest): Promise<RegisterRequest> {
    const response: AxiosResponse<AppWrapperResponse<RegisterRequest>> = await this.api.post("/register", register);

    return response.data.data;
  }

  public async getPost(params: {
    categoryId?: UUID;
    tagId?: UUID;
  }): Promise<Post[]> {
    const response: AxiosResponse<AppWrapperResponse<Post[]>> =
      await this.api.get(`/posts`, { params });

    return response.data.data;
  }

  public async createPost(post: CreatePostRequest): Promise<Post> {
    const response: AxiosResponse<AppWrapperResponse<Post>> =
      await this.api.post(`/posts`, post);

    return response.data.data;
  }

  public async updatePost(post: CreatePostRequest): Promise<Post> {
    const response: AxiosResponse<AppWrapperResponse<Post>> =
      await this.api.post(`/posts`, post);

    return response.data.data;
  }

  public async deletePost(id: UUID): Promise<void> {
    await this.api.delete(`/posts/${id}`);
  }

  public async getDraft(params: {
    page?: number;
    size?: number;
    sort?: string;
  }): Promise<Post[]> {
    const response: AxiosResponse<AppWrapperResponse<Post[]>> =
      await this.api.get(`/posts/drafts`, { params });

    return response.data.data;
  }

  public async getCatogeries(): Promise<Category[]> {
    const response: AxiosResponse<AppWrapperResponse<Category[]>> =
      await this.api.get(`/categoires`);

    return response.data.data;
  }

  public async createCategory(name: string): Promise<Category> {
    const response: AxiosResponse<AppWrapperResponse<Category>> =
      await this.api.post(`/categories`, { name });

    return response.data.data;
  }

  public async updateCategory(id: UUID, name: string): Promise<Category> {
    const response: AxiosResponse<AppWrapperResponse<Category>> =
      await this.api.put(`/categories/${id}`, { id, name });

    return response.data.data;
  }

  public async deleteCategory(id: UUID): Promise<void> {
    const response: AxiosResponse<AppWrapperResponse<void>> =
      await this.api.delete(`/categories/${id}`);

    return response.data.data;
  }

  public async getTags(): Promise<Tag[]> {
    const response: AxiosResponse<AppWrapperResponse<Tag[]>> =
      await this.api.get(`/tags`);

    return response.data.data;
  }

  public async createTags(names: string[]): Promise<Tag[]> {
    const response: AxiosResponse<AppWrapperResponse<Tag[]>> =
      await this.api.post("/tags", { names });

    return response.data.data;
  }

  public async deleteTag(id: UUID): Promise<void> {
    const response: AxiosResponse<AppWrapperResponse<void>> = await this.api.delete(`/tags/${id}`);
    
    return response.data.data;
  }
}

export const apiService = ApiService.getInstance();
