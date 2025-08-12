package com.darknash.blog.constant;

public class ApiPaths {

    // Base API version
    public static final String API_V1 = "/api/v1";

    // Authentication endpoints
    public static final String AUTH = API_V1 + "/auth";

    public static final String CATEGORIES = API_V1 + "/categories";

    // Post endpoints
    public static final String POSTS = API_V1 + "/posts";


    public static final String TAGS = API_V1 + "/tags";


    // Private constructor to prevent instantiation
    private ApiPaths() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}