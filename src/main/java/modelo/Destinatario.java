package modelo;

/**
 * Clase que representa al receptor de una notificación o mensaje por correo electrónico.
 */
public class Destinatario {

    /**
     * Dirección de correo electrónico del destinatario.
     */
    private String email;

    /**
     * Constructor por defecto.
     */
    public Destinatario() {
    }

    /**
     * Constructor que inicializa el destinatario con una dirección de correo.
     * @param email La dirección de correo electrónico.
     */
    public Destinatario(String email) {
        this.email = email;
    }

    /**
     * Obtiene la dirección de correo electrónico del destinatario.
     * @return El email del destinatario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece la dirección de correo electrónico del destinatario.
     * @param email La nueva dirección de correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}