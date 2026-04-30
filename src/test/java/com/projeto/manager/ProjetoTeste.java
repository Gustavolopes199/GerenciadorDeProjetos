package com.projeto.manager;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.projeto.manager.models.dtos.projeto.CadastroProjeto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@SpringBootTest(classes = ManagerApplication.class)
public class ProjetoTeste {

    private WireMockServer wireMockServer;

    @BeforeEach
    public void configurar(){
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();


    }

    @Test
    public void createProjeto(){

        WebClient webClient =  WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();

        CadastroProjeto dto = new CadastroProjeto();

        dto.setNome("Projeto teste1");
        dto.setDataInicio(LocalDate.now().minusMonths(3));
        dto.setPrevisaoTermino(LocalDate.now().plusMonths(3));
        dto.setOrcamentoTotal(BigDecimal.valueOf(500001));
        dto.setIdGerente(Long.valueOf(1L));

        try{

            String response = webClient.post()
                    .uri("/projeto")
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("Projeto criado: {}", response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @AfterEach
    public void parar(){
        wireMockServer.stop();
    }
}
