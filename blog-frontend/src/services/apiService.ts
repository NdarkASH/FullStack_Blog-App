import axios, { AxiosInstance, AxiosResponse, InternalAxiosRequestConfig, AxiosError } from 'axios';

interface ApiWrapperResponse<T> {
    code: number;
    msg: string;
    data: T;
}
// Types
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
  authorId: string;
  authorName: string;
}
export interface Category {
  uuid: string;
  name: string;
  postCount?: number;
}

export interface Tag {
  uuid: string;
  name: string;
  postCount?: number;
}


export interface CreateUser {
  auth: AuthResponse;
  user: AuthorResponse;
}

export interface Post {
  uuid: string;
  title: string;
  content: string;
  author?: AuthorResponse;
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
  categoryId: string;
  tags: string[];
  status: PostStatus;
}

export interface UpdatePostRequest extends CreatePostRequest {
  id: string;
}


export interface ApiError {
  status: number;
  message: string;
  errors?: Array<{
    field: string;
    message: string;
  }>;
}

export enum PostStatus {
  DRAFT = 'DRAFT',
  PUBLISHED = 'PUBLISHED'
}

class ApiService {
  private api: AxiosInstance;
  private static instance: ApiService;

  private constructor() {
    this.api = axios.create({
      baseURL: '/api/v1',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    // Add request interceptor for authentication
    this.api.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('token');
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error: AxiosError) => {
        return Promise.reject(error);
      }
    );

    // Add response interceptor for error handling
    this.api.interceptors.response.use(
      (response: AxiosResponse) => response,
      (error: AxiosError) => {
        if (error.response?.status === 401) {
          localStorage.removeItem('token');
          window.location.href = '/login';
        }
        return Promise.reject(this.handleError(error));
      }
    );
  }

  public static getInstance(): ApiService {
    if (!ApiService.instance) {
      ApiService.instance = new ApiService();
    }
    return ApiService.instance;
  }

  private handleError(error: AxiosError): ApiError {
    if (error.response?.data) {
      return error.response.data as ApiError;
    }
    return {
      status: 500,
      message: 'An unexpected error occurred'
    };
  }

  // Auth endpoints
  public async login(credentials: LoginRequest): Promise<AuthResponse> {
    const response: AxiosResponse<ApiWrapperResponse<AuthResponse>> = await this.api.post('/login', credentials);
    localStorage.setItem('token', response.data.data.token);
    return response.data.data;
  }

  public logout(): void {
    localStorage.removeItem('token');
  }

  public async createUser(register: RegisterRequest): Promise<AuthResponse> {
      // localStorage.removeItem('token')
      const response: AxiosResponse<ApiWrapperResponse<AuthResponse>> = await this.api.post('/register', register)
      localStorage.setItem('token', response.data.data.token);
      return response.data.data;
  }

  // Posts endpoints
  public async getPosts(params: {
    categoryId?: string;
    tagId?: string;
  }): Promise<Post[]> {
    const response: AxiosResponse<ApiWrapperResponse<Post[]>> = await this.api.get('/posts', { params });
    return response.data.data;
  }

  public async getPost(id: string): Promise<Post> {
    const response: AxiosResponse<ApiWrapperResponse<Post>> = await this.api.get(`/posts/${id}`);
    return response.data.data;
  }

  public async createPost(post: CreatePostRequest): Promise<Post> {
    const response: AxiosResponse<ApiWrapperResponse<Post>> = await this.api.post('/posts', post);
    return response.data.data;
  }

  public async updatePost(id: string, post: UpdatePostRequest): Promise<Post> {
    const response: AxiosResponse<ApiWrapperResponse<Post>> = await this.api.put(`/posts/${id}/edit`, post);
    return response.data.data;
  }

  public async deletePost(id: string): Promise<void> {
    await this.api.delete(`/posts/${id}`);
  }

  public async getDrafts(params: {
    page?: number;
    size?: number;
    sort?: string;
  }): Promise<Post[]> {
    const response: AxiosResponse<ApiWrapperResponse<Post[]>> = await this.api.get('/posts/drafts', { params });
    return response.data.data;
  }

  // Categories endpoints
  public async getCategories(): Promise<Category[]> {
    const response: AxiosResponse<ApiWrapperResponse<Category[]>> = await this.api.get('/categories');
    return response.data.data;
  }

  public async createCategory(name: string): Promise<Category> {
    const response: AxiosResponse<ApiWrapperResponse<Category>> = await this.api.post('/categories', { name });
    return response.data.data;
  }

  public async updateCategory(id: string, name: string): Promise<Category> {
    const response: AxiosResponse<ApiWrapperResponse<Category>> = await this.api.put(`/categories/${id}`, { id, name });
    return response.data.data;
  }

  public async deleteCategory(id: string): Promise<void> {
    const response: AxiosResponse<ApiWrapperResponse<void>> = await this.api.delete(`/categories/${id}`);
    return response.data.data;
  }

  // Tags endpoints
  public async getTags(): Promise<Tag[]> {
    const response: AxiosResponse<ApiWrapperResponse<Tag[]>> = await this.api.get('/tags');
    return response.data.data;

  }

  public async createTags(names: string[]): Promise<Tag[]> {
    const response: AxiosResponse<ApiWrapperResponse<Tag[]>> = await this.api.post('/tags', { names });
    return response.data.data;
  }

  public async deleteTag(id: string): Promise<void> {
    const response: AxiosResponse<ApiWrapperResponse<void>> = await this.api.delete(`/tags/${id}`);
    return response.data.data;
  }
}

// Export a singleton instance
export const apiService = ApiService.getInstance();