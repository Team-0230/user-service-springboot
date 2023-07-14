package uz.pdp.userservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.userservice.model.Role;
import uz.pdp.userservice.repository.RoleRepository;
import uz.pdp.userservice.exception.DataNotLoadedException;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new DataNotLoadedException("Role not found"));
    }
}
