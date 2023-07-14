package uz.pdp.userservice.components;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.userservice.model.Role;
import uz.pdp.userservice.model.User;
import uz.pdp.userservice.model.enums.PermissionEnum;
import uz.pdp.userservice.repository.RoleRepository;
import uz.pdp.userservice.repository.UserRepository;

import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class    DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlMode;

    @Override
    public void run(String... args) {
        if (Objects.equals(ddlMode, "create") || Objects.equals(ddlMode, "create-drop")) {
            Role superAdmin = roleRepository.save(new Role(
                    "SUPER_ADMIN",
                    Set.of(PermissionEnum.values())
            ));
            roleRepository.save(new Role(
                    "ROLE_USER",
                    Set.of(PermissionEnum.USER_PROFILE,
                            PermissionEnum.USER_PROFILE_UPDATE)
            ));

            userRepository.save(
                    new User(
                            "admin",
                            "admin",
                            "admin@gmail.com",
                            passwordEncoder.encode("13524"),
                            superAdmin,
                            true
                    )
            );
        }
    }
}
