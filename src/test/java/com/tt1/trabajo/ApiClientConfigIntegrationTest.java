package com.tt1.trabajo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utilidades.ApiClient;
import utilidades.api.EmailApi;
import utilidades.api.ResultadosApi;
import utilidades.api.SolicitudApi;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ApiClientConfigIntegrationTest {

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private SolicitudApi solicitudApi;

    @Autowired
    private ResultadosApi resultadosApi;

    @Autowired
    private EmailApi emailApi;

    @Test
    public void testApiBeansAreInjected() {
        assertNotNull(apiClient, "El bean ApiClient debería haberse cargado");
        assertNotNull(solicitudApi, "El bean SolicitudApi debería haberse cargado");
        assertNotNull(resultadosApi, "El bean ResultadosApi debería haberse cargado");
        assertNotNull(emailApi, "El bean EmailApi debería haberse cargado");
    }
}
