package com.projeto.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.manager.infra.exceptions.CargoNotFoundException;
import com.projeto.manager.infra.exceptions.MembroNotFoundException;
import com.projeto.manager.models.dtos.response.MockMembroResponse;
import com.projeto.manager.models.entity.Membro;
import com.projeto.manager.services.MembroService;
import lombok.RequiredArgsConstructor;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;


@SpringBootTest(classes = ManagerApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public class MembroTeste {


    @Autowired
    private ObjectMapper objectMapper;

    private static MockWebServer mockApi;

    @Autowired
    private MembroService membroService;

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
    public void buscarMembroSucesso(){
        disponibilizarMembro();

        Membro membro = membroService.buscarMembro(1L);

        assertThat(membro).isNotNull();

    }

    @Test
    public void buscarMembroNotFound(){
        disponibilizarMembro();

        assertThrows(MembroNotFoundException.class, () -> membroService.buscarMembro(999L));
    }

    @Test
    public void BuscarMembroCargoErrado(){
        disponibilizarMembro();

        assertThrows(CargoNotFoundException.class, () -> membroService.buscarMembro(4L));
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
                if (recordedRequest.getPath().equals("/membro/4")){
                    MockMembroResponse response = new MockMembroResponse();
                    response.setId(4L);
                    response.setCargo("Func");
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
