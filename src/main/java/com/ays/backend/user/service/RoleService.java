package com.ays.backend.user.service;

import com.ays.backend.user.exception.RoleNotFoundException;
import com.ays.backend.user.model.ERole;
import com.ays.backend.user.model.Role;
import com.ays.backend.user.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> findByName(ERole name) {
        return roleRepository.findByName(name);
    }

    public Set<Role> addRoleToUser(Set<String> strRoles ) {

        Set<Role> roles = new HashSet<>();

        if (strRoles  != null) {
            strRoles.forEach(role -> {

                switch (role) {
                    case "ROLE_ADMIN":

                        Role adminRole = null;

                        if(roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()){
                            adminRole = new Role(ERole.ROLE_ADMIN);
                        }else{
                            adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RoleNotFoundException("Error: Admin Role is not found."));
                        }

                        roles.add(adminRole);
                        break;

                    case "ROLE_WORKER":

                        Role workerRole = null;

                        if(roleRepository.findByName(ERole.ROLE_WORKER).isEmpty()){
                            workerRole = new Role(ERole.ROLE_WORKER);
                        }else{
                            workerRole = roleRepository.findByName(ERole.ROLE_WORKER)
                                    .orElseThrow(() -> new RoleNotFoundException("Error: Worker Role is not found."));
                        }

                        roles.add(workerRole);

                        break;

                    default:

                        Role userRole = null;

                        if(roleRepository.findByName(ERole.ROLE_VOLUNTARY).isEmpty()){
                            userRole = new Role(ERole.ROLE_VOLUNTARY);
                        }else{
                            userRole = roleRepository.findByName(ERole.ROLE_VOLUNTARY)
                                    .orElseThrow(() -> new RoleNotFoundException("Error: VOLUNTARY Role is not found."));
                        }

                        roles.add(userRole);

                }

            });
        }else{

            roleRepository.findByName(ERole.ROLE_VOLUNTARY).ifPresentOrElse(roles::add, () -> roles.add(new Role(ERole.ROLE_VOLUNTARY)));
        }


        saveRoles(roles);

        return roles;
    }

    public void saveRoles(Set<Role> roles){
        roleRepository.saveAll(roles);
    }
}
