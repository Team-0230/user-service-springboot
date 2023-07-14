package uz.pdp.userservice.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PermissionEnum implements GrantedAuthority {
    USER_CRUD,
    USER_PROFILE,
    USER_PROFILE_UPDATE,
    GET_PERMISSIONS,
    ROLE_CRUD;

    @Override
    public String getAuthority() {
        return name();
    }
}
