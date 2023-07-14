package uz.pdp.userservice.service.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.userservice.model.Role;
import uz.pdp.userservice.model.User;
import uz.pdp.userservice.payload.RegisterDTO;
import uz.pdp.userservice.service.RoleService;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class RegisterDTOMapper implements Function<RegisterDTO, User> {
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public User apply(RegisterDTO registerDTO) {
        Role userRole = roleService.getRoleByName("ROLE_USER");
        return new User(
                registerDTO.firstName(),
                registerDTO.lastName(),
                registerDTO.email(),
                passwordEncoder.encode(registerDTO.password()),
                userRole,
                false
        );
    }
}
