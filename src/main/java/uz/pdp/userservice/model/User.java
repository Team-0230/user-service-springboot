package uz.pdp.userservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends AbsUUIDEntity implements UserDetails {
    @Column(nullable = false)
    String firstName;
    @Column(nullable = false)
    String lastName;
    @Column(unique = true, nullable = false)
    String email;
    @Column(nullable = false)
    String password;
    @ManyToOne(optional = false)
    Role role;
    @OneToOne(fetch = FetchType.LAZY, targetEntity = VerificationCode.class, mappedBy = "parentTask", cascade = CascadeType.ALL)
    VerificationCode verificationCode;
    boolean verifiedEmail;
    boolean accountNonExpired;
    boolean accountNonLocked;
    boolean credentialsNonExpired;
    boolean enabled;

    public User(String firstName,
                String lastName,
                String email,
                String password,
                Role role,
                boolean verifiedEmail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.verifiedEmail = verifiedEmail;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getPermissions();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
