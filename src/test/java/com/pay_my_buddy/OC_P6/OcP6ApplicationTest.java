package com.pay_my_buddy.OC_P6;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OcP6ApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void testMain() {
        String[] args = {};

        try (MockedStatic<SpringApplication> mockedSpringApp = Mockito.mockStatic(SpringApplication.class)) {
            OcP6Application.main(args);
            mockedSpringApp.verify(() -> SpringApplication.run(OcP6Application.class, args), Mockito.times(1));
        }
    }
}
