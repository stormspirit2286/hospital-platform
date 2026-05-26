package com.duy.hospital.authservice.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.duy.hospital.authservice.dto.response.UserResponse;
import com.duy.hospital.authservice.entity.Role;
import com.duy.hospital.authservice.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "status", expression = "java(user.getStatus().name())")
    @Mapping(target = "roles", expression = "java(toRoleCodes(user.getRoles()))")
    UserResponse toResponse(User user);

    default List<String> toRoleCodes(Set<Role> roles) {
        return roles.stream()
                .map(Role::getCode)
                .sorted(Comparator.naturalOrder())
                .toList();
    }
}
