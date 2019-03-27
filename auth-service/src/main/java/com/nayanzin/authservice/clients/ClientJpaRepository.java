package com.nayanzin.authservice.clients;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientJpaRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByClientId(String clientId);
}
