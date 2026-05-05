package servicios;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    // Entidades:
    // 0: Casilla infectada
    // 1: Casilla sana
    // 2: Casilla curativa

    private int[][] plantarEntidadesIniciales(int[][] grid) {
        // Generación inicial que contiene 4 hospitales y 5 pacientes (casillas infectadas)
        for(int i = 0; i < 4; i++) {
            grid[(int)(Math.random() * grid.length)][(int)(Math.random() * grid[0].length)] = 2;
        }
        for(int i = 0; i < 5; i++) {
            grid[(int)(Math.random() * grid.length)][(int)(Math.random() * grid[0].length)] = 0;
        }
        return grid;
    }

    public List<int[][]> procesarSimulacionCompleta(int[][] gridInicial, int generaciones) {
        List<int[][]> historial = new ArrayList<>();

        int[][] gridActual = plantarEntidadesIniciales(gridInicial);
        historial.add(gridActual);

        for (int i = 0; i < generaciones; i++) {
            gridActual = calcularSiguienteGeneracion(gridActual);
            moverHospitales(gridActual);
            historial.add(gridActual);
        }

        return historial;
    }

    private int[][] calcularSiguienteGeneracion(int[][] gridActual) {
        int filas = gridActual.length;
        int columnas = gridActual[0].length;
        int[][] gridFuturo = new int[filas][columnas];

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int estadoActual = gridActual[i][j];
                boolean curaCerca = comprobarVecinos(gridActual, i, j, 2);

                if (estadoActual == 1) {
                    // Lógica para una célula sana
                    int zombisCerca = contarVecinos(gridActual, i, j, 0);
                    int sanosCerca = contarVecinos(gridActual, i, j, 1);

                    if (zombisCerca > 0) {
                        // Inmunidad de manada: si está rodeado de 3 o 4 sanos, la infección cae al 5%
                        double probabilidadInfeccion = (sanosCerca >= 3) ? 0.05 : 0.30;
                        gridFuturo[i][j] = (Math.random() < probabilidadInfeccion) ? 0 : 1;
                    } else {
                        gridFuturo[i][j] = 1; // Sigue sano
                    }
                } else if (estadoActual == 0) {
                    // Lógica para una celula infectada
                    int sanosCerca = contarVecinos(gridActual, i, j, 1);

                    if (curaCerca) {
                        gridFuturo[i][j] = 1;
                    } else if (sanosCerca == 0) {
                        // Si no tiene sanos cerca, 25% de probabilidad de morir de inanición
                        gridFuturo[i][j] = (Math.random() < 0.25) ? 1 : 0;
                    } else {
                        gridFuturo[i][j] = 0; // Sigue infectado
                    }
                } else {
                    gridFuturo[i][j] = estadoActual;
                }
            }
        }
        return gridFuturo;
    }

    private int contarVecinos(int[][] grid, int fila, int col, int tipoEntidad) {
        int filas = grid.length;
        int columnas = grid[0].length;
        int count = 0;

        if (fila > 0 && grid[fila - 1][col] == tipoEntidad) count++;
        if (fila < filas - 1 && grid[fila + 1][col] == tipoEntidad) count++;
        if (col > 0 && grid[fila][col - 1] == tipoEntidad) count++;
        if (col < columnas - 1 && grid[fila][col + 1] == tipoEntidad) count++;

        return count;
    }

    private boolean comprobarVecinos(int[][] grid, int fila, int col, int tipoEntidad) {
        int filas = grid.length;
        int columnas = grid[0].length;

        if (fila > 0 && grid[fila - 1][col] == tipoEntidad) return true;
        if (fila < filas - 1 && grid[fila + 1][col] == tipoEntidad) return true;
        if (col > 0 && grid[fila][col - 1] == tipoEntidad) return true;
        if (col < columnas - 1 && grid[fila][col + 1] == tipoEntidad) return true;

        return false;
    }

    private void moverHospitales(int[][] grid) {
        int filas = grid.length;
        int columnas = grid[0].length;
        List<int[]> posicionesHospitales = new ArrayList<>();

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (grid[i][j] == 2) {
                    posicionesHospitales.add(new int[]{i, j});
                }
            }
        }

        for (int[] pos : posicionesHospitales) {
            int f = pos[0];
            int c = pos[1];

            List<int[]> movimientosPosibles = new ArrayList<>();
            if (f > 0 && grid[f - 1][c] != 2) movimientosPosibles.add(new int[]{f - 1, c});
            if (f < filas - 1 && grid[f + 1][c] != 2) movimientosPosibles.add(new int[]{f + 1, c});
            if (c > 0 && grid[f][c - 1] != 2) movimientosPosibles.add(new int[]{f, c - 1});
            if (c < columnas - 1 && grid[f][c + 1] != 2) movimientosPosibles.add(new int[]{f, c + 1});

            if (!movimientosPosibles.isEmpty()) {

                int indiceAleatorio = (int) (Math.random() * movimientosPosibles.size());
                int[] nuevoDestino = movimientosPosibles.get(indiceAleatorio);

                grid[f][c] = 1;
                grid[nuevoDestino[0]][nuevoDestino[1]] = 2;
            }
        }
    }


}
