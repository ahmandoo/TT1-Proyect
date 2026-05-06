package servicios;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de gestionar la lógica principal de la simulación.
 * Calcula las generaciones futuras del tablero basándose en las reglas de las entidades.
 */
@Service
public class GameService {

    // Entidades:
    // 0: Casilla infectada
    // 1: Casilla sana
    // 2: Casilla curativa
    /**
     * Inicializa el tablero colocando las entidades base: 4 hospitales (valor 2) y 5 pacientes infectados (valor 0)
     * en posiciones aleatorias.
     *
     * @param grid Matriz bidimensional vacía (o con celdas sanas).
     * @return La matriz modificada con las entidades iniciales plantadas.
     */
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

    /**
     * Procesa la simulación completa del juego durante un número determinado de generaciones.
     *
     * @param gridInicial  Matriz bidimensional que representa el estado inicial del tablero.
     * @param generaciones Número total de generaciones (segundos) a simular.
     * @return Una lista de matrices bidimensionales, donde cada matriz es el estado del tablero en un instante de tiempo.
     */
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

    /**
     * Calcula el estado de todas las celdas para el siguiente turno aplicando las reglas de infección,
     * curación e inmunidad de rebaño.
     *
     * @param gridActual Matriz con el estado actual del tablero.
     * @return Una nueva matriz representando el estado del tablero en la siguiente generación.
     */
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

    /**
     * Cuenta cuántas celdas adyacentes (arriba, abajo, izquierda, derecha) coinciden con un tipo de entidad específico.
     *
     * @param grid        Matriz del tablero.
     * @param fila        Posición Y de la celda a evaluar.
     * @param col         Posición X de la celda a evaluar.
     * @param tipoEntidad Valor entero que representa el tipo de entidad a buscar.
     * @return El número de vecinos adyacentes que coinciden con el tipo solicitado.
     */
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

    /**
     * Verifica de forma rápida si existe al menos una celda adyacente de un tipo específico.
     * Útil para comprobar si hay curas (hospitales) cerca.
     *
     * @param grid        Matriz del tablero.
     * @param fila        Posición Y de la celda a evaluar.
     * @param col         Posición X de la celda a evaluar.
     * @param tipoEntidad Valor entero de la entidad a buscar.
     * @return true si hay al menos un vecino del tipo indicado, false en caso contrario.
     */
    private boolean comprobarVecinos(int[][] grid, int fila, int col, int tipoEntidad) {
        int filas = grid.length;
        int columnas = grid[0].length;

        if (fila > 0 && grid[fila - 1][col] == tipoEntidad) return true;
        if (fila < filas - 1 && grid[fila + 1][col] == tipoEntidad) return true;
        if (col > 0 && grid[fila][col - 1] == tipoEntidad) return true;
        if (col < columnas - 1 && grid[fila][col + 1] == tipoEntidad) return true;

        return false;
    }

    /**
     * Mueve aleatoriamente todos los hospitales (entidad 2) a una celda adyacente que esté sana (distinta de 2).
     *
     * @param grid Matriz del tablero cuyo estado se modificará in-place.
     */
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
