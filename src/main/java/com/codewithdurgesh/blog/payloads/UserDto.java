package com.codewithdurgesh.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {

    private int id;

    @NotEmpty(message = "Username is required !!")
    @Size(min = 4, message = "Username must be at least 4 characters !!")
    private String name;

    @Email(message = "Email address is not valid !!")
    @NotEmpty(message = "Email is required !!")
    private String email;

    @NotEmpty(message = "Password is required !!")
    @Size(min = 3, max = 10, message = "Password must be between 3 and 10 characters !!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotEmpty(message = "About section is required !!")
    private String about;

    private Set<RoleDto> roles = new HashSet<>();

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", about='" + about + '\'' +
                ", roles=" + roles +
                '}';
    }
} 

