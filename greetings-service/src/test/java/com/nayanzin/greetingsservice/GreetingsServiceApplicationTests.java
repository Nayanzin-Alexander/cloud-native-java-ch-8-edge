package com.nayanzin.greetingsservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("default")
public class GreetingsServiceApplicationTests {

    @Test
    public void contextLoads() {
        // just test if context loads
    }

}
