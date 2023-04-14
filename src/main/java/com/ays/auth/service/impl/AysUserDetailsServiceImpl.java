package com.ays.auth.service.impl;

import com.ays.admin_user.model.entity.AdminUserEntity;
import com.ays.admin_user.repository.AdminUserRepository;
import com.ays.auth.model.AysUserDetails;
import com.ays.auth.model.mapper.AdminUserEntityToAysUserMapper;
import com.ays.auth.model.mapper.UserEntityToAysUserMapper;
import com.ays.user.model.entity.UserEntity;
import com.ays.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class AysUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserEntityToAysUserMapper userEntityToAysUserMapper = UserEntityToAysUserMapper.initialize();
    private final AdminUserRepository adminUserRepository;
    private final AdminUserEntityToAysUserMapper adminUserEntityToAysUserMapper = AdminUserEntityToAysUserMapper.initialize();


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isPresent()) {
            return new AysUserDetails(userEntityToAysUserMapper.map(userEntity.get()));
        }

        Optional<AdminUserEntity> adminUserEntity = adminUserRepository.findByUsername(username);
        if (adminUserEntity.isPresent()) {
            return new AysUserDetails(adminUserEntityToAysUserMapper.map(adminUserEntity.get()));
        }

        throw new UsernameNotFoundException("Admin User or User not found with username: " + username);
    }
}
