package com.projeto.manager;

import com.projeto.manager.infra.webclient.WebClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.EnableWireMock;

@Slf4j
@EnableWireMock
@SpringBootTest(classes = MembroRestTest.AppConfiguration.class)
public class MembroRestTest {

    private static final String url = "localhost";



    @SpringBootApplication
    static class AppConfiguration{}
}
