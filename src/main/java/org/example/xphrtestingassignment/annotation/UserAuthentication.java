package org.example.xphrtestingassignment.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole(T(org.example.xphrtestingassignment.constant.UserRoles).ADMIN)" +
        " or hasRole(T(org.example.xphrtestingassignment.constant.UserRoles).EMPLOYEE)")
public @interface UserAuthentication {
}
