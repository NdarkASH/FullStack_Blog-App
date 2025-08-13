package com.darknash.blog.mapper;

import com.darknash.blog.dto.CreateNewUser;
import com.darknash.blog.dto.NewUserResponse;
import com.darknash.blog.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "Spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    NewUserResponse toUserResponse(User user);
}
