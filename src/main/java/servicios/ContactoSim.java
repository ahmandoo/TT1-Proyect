package servicios;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
import modelo.Punto;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * Implementación del servicio de comunicación con la simulación.
 * Utiliza los clientes de la API (SolicitudApi y ResultadosApi) para enviar 
 * peticiones y procesar respuestas.
 */
@Service
public class ContactoSim implements InterfazContactoSim {
    private final utilidades.api.SolicitudApi solicitudApi;
    private final utilidades.api.ResultadosApi resultadosApi;
    /**
     * Constructor que inyecta los clientes generados por OpenAPI.
     * * @param solicitudApi Cliente de la API para manejar solicitudes.
     * @param resultadosApi Cliente de la API para descargar resultados.
     */
    public ContactoSim(utilidades.api.SolicitudApi solicitudApi, utilidades.api.ResultadosApi resultadosApi) {
        this.solicitudApi = solicitudApi;
        this.resultadosApi = resultadosApi;
    }
    /**
     * {@inheritDoc}
     * Mapea internamente los modelos locales a los modelos generados por OpenAPI antes de hacer la petición HTTP.
     */
    @Override
    public int solicitarSimulation(DatosSolicitud sol, String usuario) {
        try {
            utilidades.model.Solicitud apiSolicitud = new utilidades.model.Solicitud();
            List<Integer> cantidades = new ArrayList<>();
            List<String> nombres = new ArrayList<>();

            List<Entidad> entidadesDisponibles = getEntities();
            sol.getNums().forEach((id, cantidad) -> {
                cantidades.add(cantidad);
                String nombreEntidad = entidadesDisponibles.stream()
                        .filter(e -> e.getId() == id)
                        .map(Entidad::getName)
                        .findFirst()
                        .orElse("Entidad Desconocida " + id);

                nombres.add(nombreEntidad);
            });
            apiSolicitud.setCantidadesIniciales(cantidades);
            apiSolicitud.setNombreEntidades(nombres);

            utilidades.model.SolicitudResponse response = solicitudApi.solicitudSolicitarPost(usuario, apiSolicitud);
            return (response != null) ? response.getTokenSolicitud() : -1;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public DatosSimulation descargarDatos(int ticket,String usuario) {
        DatosSimulation ds = new DatosSimulation();
        ds.setPuntos(new HashMap<>());
        ds.setAnchoTablero(10);
        ds.setMaxSegundos(0);

        try {
            utilidades.model.ResultsResponse response = resultadosApi.resultadosPost(usuario, ticket);
            Object dataObj = response.getData();
            if (dataObj instanceof String && !((String) dataObj).isEmpty()) {
                String rawData = ((String) dataObj).replace("\r", "");
                String[] lineas = rawData.trim().split("\n");

                if (lineas.length > 0) {
                    int ancho = Integer.parseInt(lineas[0].trim());
                    ds.setAnchoTablero(ancho);
                    Map<Integer, List<Punto>> puntosPorTiempo = new HashMap<>();
                    int maxT = 0;

                    for (int i = 1; i < lineas.length; i++) {
                        String lineaLimpia = lineas[i].trim();
                        if (lineaLimpia.isEmpty()) continue;

                        String[] partes = lineaLimpia.split(",");
                        if (partes.length == 4) {
                            int t = Integer.parseInt(partes[0].trim());
                            int y = Integer.parseInt(partes[1].trim());
                            int x = Integer.parseInt(partes[2].trim());
                            String color = partes[3].trim();

                            Punto p = new Punto();
                            p.setX(x);
                            p.setY(y);
                            p.setColor(color);

                            puntosPorTiempo.computeIfAbsent(t, k -> new ArrayList<>()).add(p);
                            if (t > maxT) maxT = t;
                        }
                    }

                    ds.setPuntos(puntosPorTiempo);
                    ds.setMaxSegundos(maxT + 1);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en descargarDatos: " + e.getMessage());
        }
        return ds;
    }

    @Override
    public List<Entidad> getEntities() {
        List<Entidad> entidades = new ArrayList<>();

        String[] nombres = {" Viviendas", " Coches", " Temperatura", " Combustible"};

        for (int i = 0; i < nombres.length; i++) {
            Entidad e = new Entidad();
            e.setId(i + 1);
            e.setName(nombres[i]);
            e.setDescripcion("Monitorización " + (i + 1));
            entidades.add(e);
        }
        return entidades;
    }

    @Override
    public boolean isValidEntityId(int id) {
        return id >= 1 && id <= 4;
    }
}


