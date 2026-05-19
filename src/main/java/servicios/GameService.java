package servicios;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio encargado de gestionar la lógica principal de la simulación.
 * Calcula las generaciones futuras del tablero basándose en las reglas de las entidades
 * (infecciones, recuperaciones, muertes y vacunaciones).
 */
@Service
public class GameService {

    // ESTADOS:
    // -1: Hueco en blanco (nuevo)
    // 0: Casilla infectada
    // 1: Casilla sana
    // 2: Casilla muerta
    // 3: Casilla vacunada
    // 4: Casilla recuperada

    // MOVILIDAD:
    // 0: Local (se mueve poco)
    // 1: Viajero (se mueve mucho)

    private int infectadosParam = 5;
    private int vacunadosParam = 4;
    private int viajerosPorcentajeParam = 20;
    private double porcentajeInfeccionParam = 0.50;

    public void configurarSimulacion(int infectados, int vacunados, int viajerosPorcentaje, double porcentajeInfeccion) {
        this.infectadosParam = infectados;
        this.vacunadosParam = vacunados;
        this.viajerosPorcentajeParam = viajerosPorcentaje;
        this.porcentajeInfeccionParam = porcentajeInfeccion;
    }

    /**
     * Inicializa el tablero colocando las entidades base: 4 hospitales (valor 3) y
     * 5 pacientes infectados (valor 0) en posiciones aleatorias.
     *
     * @param grid Matriz bidimensional vacía (o con celdas sanas).
     * @return La matriz modificada con las entidades iniciales plantadas.
     */

    private int[][] plantarEntidadesIniciales(int[][] grid, int[][] movilidad) {
        int filas = grid.length;
        int columnas = grid[0].length;

        // 1. Inicializamos el tablero con espacios en blanco (-1)
        for(int i = 0; i < filas; i++) {
            for(int j = 0; j < columnas; j++) {
                grid[i][j] = -1;
                movilidad[i][j] = 0;
            }
        }

        // Plantamos el 70% de la población como Sanos (1) en los huecos en blanco (-1)
        int totalPoblacion = (int) (filas * columnas * 0.70);
        for (int i = 0; i < totalPoblacion; i++) {
            plantarEntidadEnEspecifico(grid, movilidad, 1, -1);
        }

        for(int i = 0; i < this.infectadosParam; i++) {
            plantarEntidadEnEspecifico(grid, movilidad, 0, 1);
        }

        for(int i = 0; i < this.vacunadosParam; i++) {
            plantarEntidadEnEspecifico(grid, movilidad, 3, 1);
        }

        return grid;
    }

    private void plantarEntidadEnEspecifico(int[][] grid, int[][] movilidad, int estadoDeseado, int estadoSustituible) {
        int filas = grid.length;
        int columnas = grid[0].length;
        int f, c;
        do {
            f = (int)(Math.random() * filas);
            c = (int)(Math.random() * columnas);

        } while (grid[f][c] != estadoSustituible);

        grid[f][c] = estadoDeseado;

        movilidad[f][c] = (Math.random() < (this.viajerosPorcentajeParam / 100.0)) ? 1 : 0;
    }

    /**
     * Procesa la simulación completa del juego durante un número determinado de generaciones.
     *
     * @param gridInicial  Matriz bidimensional que representa el estado inicial del tablero.
     * @param generaciones Número total de generaciones (segundos) a simular.
     * @return Una lista de matrices bidimensionales, donde cada matriz es el estado del tablero en un instante de tiempo.
     */
    public synchronized List<int[][]> procesarSimulacionCompleta(int[][] gridInicial, int generaciones) {
        List<int[][]> historial = new ArrayList<>();

        int filas = gridInicial.length;
        int columnas = gridInicial[0].length;
        int[][] movilidad = new int[filas][columnas];

        int[][] gridActual = plantarEntidadesIniciales(gridInicial, movilidad);
        historial.add(clonarMatriz(gridActual));

        for (int i = 0; i < generaciones; i++) {
            faseDeMovimiento(gridActual, movilidad);
            gridActual = calcularSiguienteGeneracion(gridActual);
            historial.add(clonarMatriz(gridActual));
        }

        return historial;
    }

    private void faseDeMovimiento(int[][] grid, int[][] movilidad) {
        int filas = grid.length;
        int columnas = grid[0].length;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                // Solo se mueven los vivos (1, 0, 3, 4)
                int estado = grid[i][j];
                if (estado != -1 && estado != 2) {
                    boolean esViajero = (movilidad[i][j] == 1);
                    double probMovimiento = esViajero ? 0.80 : 0.10;

                    if (Math.random() < probMovimiento) {
                        int[] hueco = buscarHuecoVacio(grid, i, j);
                        if (hueco != null) {
                            // Intercambiamos posiciones
                            int fHueco = hueco[0];
                            int cHueco = hueco[1];

                            grid[fHueco][cHueco] = estado;
                            movilidad[fHueco][cHueco] = movilidad[i][j];

                            grid[i][j] = -1; // Se queda vacío
                            movilidad[i][j] = 0;
                        }
                    }
                }
            }
        }
    }

    private int[] buscarHuecoVacio(int[][] grid, int fila, int col) {
        int filas = grid.length;
        int columnas = grid[0].length;
        List<int[]> huecos = new ArrayList<>();

        if (fila > 0 && grid[fila - 1][col] == -1) huecos.add(new int[]{fila - 1, col});
        if (fila < filas - 1 && grid[fila + 1][col] == -1) huecos.add(new int[]{fila + 1, col});
        if (col > 0 && grid[fila][col - 1] == -1) huecos.add(new int[]{fila, col - 1});
        if (col < columnas - 1 && grid[fila][col + 1] == -1) huecos.add(new int[]{fila, col + 1});

        if (!huecos.isEmpty()) {
            return huecos.get((int) (Math.random() * huecos.size()));
        }
        return null;
    }

    /**
     * Calcula el estado de todas las celdas para el siguiente turno aplicando las reglas de infección,
     * curación, inmunidad de rebaño y muerte.
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
                int estado = gridActual[i][j];
                int infectadosCerca = contarVecinos(gridActual, i, j, 0);

                if (estado == 1) {
                    // Lógica sano (1)
                    if (infectadosCerca > 0) {
                        gridFuturo[i][j] = (Math.random() < this.porcentajeInfeccionParam) ? 0 : 1;
                    } else {
                        gridFuturo[i][j] = (Math.random() < 0.001) ? 3 : 1;
                    }

                } else if (estado == 0) {
                    // Lógica infectados (0)
                    double dado = Math.random();
                    if (dado < 0.02) {         // 2% de morir por turno
                        gridFuturo[i][j] = 2;
                    } else if (dado < 0.05) {  // 3% de recuperar por turno (0.05 - 0.02)
                        gridFuturo[i][j] = 4;
                    } else {
                        gridFuturo[i][j] = 0;  // 95% de seguir infectado
                    }

                } else if (estado == 4) {
                    // Lógica recuperado (4)
                    if (infectadosCerca > 0) {
                        gridFuturo[i][j] = (Math.random() < 0.05) ? 0 : 4;
                    } else {
                        gridFuturo[i][j] = 4;
                    }

                } else {
                    // Huecos (-1), muertos (2), vacunados (3)
                    gridFuturo[i][j] = estado;
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

    private int[][] clonarMatriz(int[][] original) {
        int[][] clon = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, clon[i], 0, original[i].length);
        }
        return clon;
    }


}
