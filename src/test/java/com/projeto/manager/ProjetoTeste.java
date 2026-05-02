package com.projeto.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.manager.models.dtos.projeto.CadastroProjeto;
import com.projeto.manager.models.dtos.response.MockMembroResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(classes = ManagerApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjetoTeste {

    @Autowired
    private  MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockWebServer mockApi;

    @BeforeAll
    static void ligarMockServer() throws IOException{

        mockApi = new MockWebServer();
        mockApi.start();
    }

    @AfterAll
    static void desligarMockServer() throws IOException{
        mockApi.shutdown();
    }

    @DynamicPropertySource
    static void configurarUrlApi(DynamicPropertyRegistry registry){
        registry.add("baseUrl.teste", () -> mockApi.url("/").toString());
    }


    @Test
    public void createProjetoSucesso() throws Exception{
        disponibilizarMembro();

        CadastroProjeto dto = new CadastroProjeto();

        dto.setNome("Projeto teste1");
        dto.setDataInicio(LocalDate.now().minusMonths(1));
        dto.setPrevisaoTermino(LocalDate.now().plusMonths(1));
        dto.setOrcamentoTotal(BigDecimal.valueOf(101000));
        dto.setIdGerente(Long.valueOf(1L));

        String response = mockMvc.perform(post("/projetos")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info("Response: {}", response);


    }


    private void disponibilizarMembro () {

        mockApi.setDispatcher(new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) {
                if (recordedRequest.getPath().equals("/membro/1")){
                    MockMembroResponse response = new MockMembroResponse();
                    response.setId(1L);
                    response.setCargo("Gerente");
                    response.setNome("Gustavo");


                    try {
                        return new MockResponse()
                                .setResponseCode(200)
                                .addHeader("Content-Type", "application/json")
                                .setBody(objectMapper.writeValueAsString(response));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                }
                if (recordedRequest.getPath().equals("/membro/2")){
                    MockMembroResponse response = new MockMembroResponse();
                    response.setId(2L);
                    response.setCargo("Funcionario");
                    response.setNome("Ciclano");

                    try {
                        return new MockResponse()
                                .setResponseCode(200)
                                .addHeader("Content-Type", "application/json")
                                .setBody(objectMapper.writeValueAsString(response));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (recordedRequest.getPath().equals("/membro/3")){
                    MockMembroResponse response = new MockMembroResponse();
                    response.setId(3L);
                    response.setCargo("Funcionario");
                    response.setNome("Beltrano");

                    try {
                        return new MockResponse()
                                .setResponseCode(200)
                                .addHeader("Content-Type", "application/json")
                                .setBody(objectMapper.writeValueAsString(response));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }

                return new MockResponse()
                        .setResponseCode(404)
                        .setBody("Não encontrado");
            }
        });
    }

}
