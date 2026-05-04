package com.projeto.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.manager.models.dtos.request.CadastroProjeto;
import com.projeto.manager.models.dtos.request.ReqAlocarMembroProjeto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    public void createProjetoFalhaData() throws Exception {
        disponibilizarMembro();

        CadastroProjeto dto = new CadastroProjeto();
        dto.setNome("Projeto teste1");
        dto.setDataInicio(LocalDate.now().plusYears(1));
        dto.setPrevisaoTermino(LocalDate.now().plusMonths(1));
        dto.setOrcamentoTotal(BigDecimal.valueOf(101000));
        dto.setIdGerente(Long.valueOf(1L));

        String response = mockMvc.perform(post("/projetos")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString();


        log.info("Resposta: {}", response);
    }

    @Test
    public void createProjetoFalhaGerente() throws Exception {
        disponibilizarMembro();

        CadastroProjeto dto = new CadastroProjeto();
        dto.setNome("Projeto teste1");
        dto.setDataInicio(LocalDate.now().plusYears(1));
        dto.setPrevisaoTermino(LocalDate.now().plusMonths(1));
        dto.setOrcamentoTotal(BigDecimal.valueOf(101000));
        dto.setIdGerente(Long.valueOf(6L));

        String response = mockMvc.perform(post("/projetos")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString();


        log.info("Resposta: {}", response);
    }

    @Test
    public void buscarProjetosTest() throws Exception {
        String response = mockMvc.perform(get("/projetos"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info(response);
    }

    @Test
    public void buscarProjetosTestStatus() throws Exception {
        String response = mockMvc.perform(get("/projetos?statusProjeto=EM_ANDAMENTO")
                        )
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info(response);
    }

    @Test
    public void buscarProjetoGerente() throws Exception {
        String response = mockMvc.perform(get("/projetos?idGerenteList=1")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info(response);
    }

    @Test
    public void alocarMembroProjetoSucesso() throws Exception{
        disponibilizarMembro();
        ReqAlocarMembroProjeto body = new ReqAlocarMembroProjeto();
        body.setIdProjeto(2L);
        body.setIdMembro(2L);

        String response = mockMvc.perform(post("/projetos/membro")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info("Response: {}", response);


    }

    @Test
    public void alocamMebroFalha() throws Exception{

        disponibilizarMembro();
        ReqAlocarMembroProjeto body = new ReqAlocarMembroProjeto();
        body.setIdProjeto(2L);
        body.setIdMembro(10L);

        String response = mockMvc.perform(post("/projetos/membro")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().is4xxClientError())
                .andReturn()
                .getResponse()
                .getContentAsString();

        log.info("Response: {}", response);
    }

    @Test
    public void buscarRelatorio() throws Exception{

        String response = mockMvc.perform(get("/projetos/relatorio"))
                .andExpect(status().isOk())
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
                if (recordedRequest.getPath().equals("/membro/10")){
                        return new MockResponse()
                                .setResponseCode(404)
                                .addHeader("Content-Type", "application/json");
                }

                return new MockResponse()
                        .setResponseCode(404)
                        .setBody("Não encontrado");
            }
        });
    }

}
