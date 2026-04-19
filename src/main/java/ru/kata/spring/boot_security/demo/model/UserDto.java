package ru.kata.spring.boot_security.demo.model;

import java.util.List;

public class UserDto {
    private Long id;

    private String username;

    private String password;

    private String email;


    private List<Long> roleIds;

    public UserDto() {
    }

    public UserDto(Long id, String username, String password, String email, List<Long> roleIds) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roleIds = roleIds;
    }

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}

