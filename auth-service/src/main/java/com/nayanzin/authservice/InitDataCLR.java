package com.nayanzin.authservice;

import com.nayanzin.authservice.accounts.Account;
import com.nayanzin.authservice.accounts.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class InitDataCLR implements CommandLineRunner {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public void run(String... args) {
        Stream.of("sasha,sasha", "masha,masha")
                .map(s -> s.split(","))
                .forEach(tuple -> accountJpaRepository.save(new Account(tuple[0], tuple[1], true)));
    }
}
