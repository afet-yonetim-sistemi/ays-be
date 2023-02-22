package com.ays.backend.user.service;

import java.util.HashSet;
import java.util.Set;

import com.ays.backend.base.BaseServiceTest;
import com.ays.backend.user.exception.RoleNotFoundException;
import com.ays.backend.user.model.entities.Role;
import com.ays.backend.user.repository.RoleRepository;
import com.ays.backend.user.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static com.ays.backend.user.model.enums.UserRole.ROLE_ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoleServiceTest extends BaseServiceTest {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void shouldThrowRoleNotFoundExceptionOnNonExistentRole() {
        // given
        var roles = new HashSet<String>();
        roles.add("Test_Role");

        // when && then
        assertThrows(RoleNotFoundException.class, () -> roleService.getUserRoles(roles));
    }

    @Test
    void shouldGetRoleCorrectlyOnExistentRole() {
        // given
        var inputRoles = new HashSet<String>();
        inputRoles.add(ROLE_ADMIN.getValue());

        // when
        Set<Role> roles = roleService.getUserRoles(inputRoles);

        // then
        roles.forEach(role -> assertEquals(role.getName().getValue(), ROLE_ADMIN.getValue()));
    }
}
