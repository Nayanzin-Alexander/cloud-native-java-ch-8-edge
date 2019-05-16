package com.nayanzin.authservice;

import com.nayanzin.authservice.accounts.Account;
import com.nayanzin.authservice.accounts.AccountJpaRepository;
import com.nayanzin.authservice.clients.Client;
import com.nayanzin.authservice.clients.ClientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class InitDataCLR implements CommandLineRunner {

    private final Log log = LogFactory.getLog(getClass());
    private final AccountJpaRepository accountJpaRepository;
    private final ClientJpaRepository clientJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Save accounts data.");
        Stream.of("sasha,spring", "masha,masha")
                .map(s -> s.split(","))
                .map(tuple -> new Account(tuple[0], passwordEncoder.encode(tuple[1]), true))
                .forEach(accountJpaRepository::save);

        log.info("Save clients data.");
        Stream.of("html5,password", "android,secret")
                .map(x -> x.split(","))
                .map(tuple -> new Client(tuple[0], passwordEncoder.encode(tuple[1])))
                .forEach(clientJpaRepository::save);
    }
}
