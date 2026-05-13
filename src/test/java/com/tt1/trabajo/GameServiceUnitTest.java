package com.tt1.trabajo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servicios.GameService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceUnitTest {

    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    @Test
    public void testProcesarSimulacionCompletaDimensionesYGeneraciones() {
        int ancho = 10;
        int[][] gridInicial = new int[ancho][ancho];
        for(int i = 0; i < ancho; i++) {
            for(int j = 0; j < ancho; j++) {
                gridInicial[i][j] = 1;
            }
        }
        int generaciones = 5;
        List<int[][]> historial = gameService.procesarSimulacionCompleta(gridInicial, generaciones);
        assertNotNull(historial, "El historial generado no debe ser nulo");
        assertEquals(generaciones + 1, historial.size(), "Debe devolver N generaciones + el estado inicial");
        int[][] ultimoGrid = historial.get(historial.size() - 1);
        assertEquals(ancho, ultimoGrid.length, "El número de filas debe mantenerse intacto");
        assertEquals(ancho, ultimoGrid[0].length, "El número de columnas debe mantenerse intacto");
    }

    @Test
    public void testEntidadesValidasGeneradas() {
        int ancho = 5;
        int[][] gridInicial = new int[ancho][ancho];
        for(int i = 0; i < ancho; i++) {
            for(int j = 0; j < ancho; j++) {
                gridInicial[i][j] = 1;
            }
        }

        List<int[][]> historial = gameService.procesarSimulacionCompleta(gridInicial, 3);
        int[][] ultimoGrid = historial.get(historial.size() - 1);

        for(int i = 0; i < ancho; i++) {
            for(int j = 0; j < ancho; j++) {
                int estado = ultimoGrid[i][j];
                assertTrue(estado >= 0 && estado <= 4, 
                    "El estado de la celda (" + i + "," + j + ") es " + estado + " y no es un valor válido (0-4)");
            }
        }
    }
}