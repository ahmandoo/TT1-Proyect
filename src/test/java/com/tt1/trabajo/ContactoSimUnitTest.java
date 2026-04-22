package com.tt1.trabajo;

import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import servicios.ContactoSim;
import utilidades.api.ResultadosApi;
import utilidades.api.SolicitudApi;
import utilidades.model.ResultsResponse;
import utilidades.model.SolicitudResponse;
import utilidades.model.Solicitud;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ContactoSimUnitTest {

    private ContactoSim contactoSim;
    private SolicitudApi solicitudApiMock;
    private ResultadosApi resultadosApiMock;

    @BeforeEach
    void setUp() {
        solicitudApiMock = Mockito.mock(SolicitudApi.class);
        resultadosApiMock = Mockito.mock(ResultadosApi.class);
        contactoSim = new ContactoSim(solicitudApiMock, resultadosApiMock);
    }

    @Test
    void testSolicitarSimulationConExito() throws Exception {
        SolicitudResponse mockResponse = new SolicitudResponse();
        mockResponse.setTokenSolicitud(12345);

        when(solicitudApiMock.solicitudSolicitarPost(anyString(), any(Solicitud.class)))
                .thenReturn(mockResponse);

        DatosSolicitud sol = new DatosSolicitud(Map.of(1, 10));
        int result = contactoSim.solicitarSimulation(sol, "usuarioTest");

        assertEquals(12345, result, "Debería devolver el token 12345");
    }

    @Test
    void testSolicitarSimulationConError() throws Exception {
        when(solicitudApiMock.solicitudSolicitarPost(anyString(), any()))
                .thenThrow(new RuntimeException("Error de red simulado"));

        DatosSolicitud sol = new DatosSolicitud(Map.of(1, 10));
        int result = contactoSim.solicitarSimulation(sol, "usuarioTest");

        assertEquals(-1, result, "Debería devolver -1 cuando hay una excepción");
    }

    @Test
    void testDescargarDatosSimulados() throws Exception {
        ResultsResponse mockResponse = new ResultsResponse();
        String datosFicticios = "12\n0,5,5,red\n1,6,6,red";
        mockResponse.setData(datosFicticios);
        when(resultadosApiMock.resultadosPost(anyString(), anyInt())).thenReturn(mockResponse);

        DatosSimulation resultado = contactoSim.descargarDatos(123);

        assertNotNull(resultado);
        assertEquals(12, resultado.getAnchoTablero());
        assertEquals(2, resultado.getMaxSegundos());
        assertTrue(resultado.getPuntos().containsKey(0));
        assertTrue(resultado.getPuntos().containsKey(1));
    }

    @Test
    void testGetEntities() {
        List<Entidad> entities = contactoSim.getEntities();
        assertNotNull(entities);
        assertEquals(4, entities.size());
    }

    @Test
    void testIsValidEntityId() {
        assertTrue(contactoSim.isValidEntityId(1));
        assertTrue(contactoSim.isValidEntityId(4));
        assertFalse(contactoSim.isValidEntityId(0));
        assertFalse(contactoSim.isValidEntityId(5));
    }
}