package com.nayanzin.authservice.accounts;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class AccountConfig {

    @Bean
    UserDetailsService userDetailsService(AccountJpaRepository accountJpaRepository) {
        return username -> accountJpaRepository.findByUsername(username)
                .map(account -> User.builder()
                        .username(account.getUsername())
                        .password(account.getPassword())
                        .disabled(!account.isActive())
                        .accountExpired(!account.isActive())
                        .credentialsExpired(!account.isActive())
                        .accountLocked(!account.isActive())
                        .authorities("ROLE_ADMIN", "ROLE_USER")
                        .build())
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Username %s not found!", username)));
    }
}
