package ru.binarysimple.auth.model;

public enum Roles {
    USER("user"),
    MANAGER("manager"),
    ADMIN("admin"),
    SUPERADMIN("superadmin");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
