package com.darknash.blog.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "api.paths")
public class ApiPaths {
    private String auth;
    private String post;
    private String blog;
    private String tag;

}
