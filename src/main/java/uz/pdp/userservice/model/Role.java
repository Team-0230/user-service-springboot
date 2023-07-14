package uz.pdp.userservice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.pdp.userservice.model.enums.PermissionEnum;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends AbsEntity {
    @Column(unique = true, nullable = false)
    String name;

    @ElementCollection
    @CollectionTable(
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"role_id", "permissions"})})
    @Enumerated(EnumType.STRING)
    Set<PermissionEnum> permissions;

    public Role(String name, Set<PermissionEnum> permissions) {
        this.name = name;
        this.permissions = permissions;
    }
}
