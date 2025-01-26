package adrian.utilidades;

import adrian.modelo.Cliente;

import java.util.List;

/**
 * Clase que contiene métodos para validar los campos de un cliente, devuelven un mensaje de error si no son válidos.
 */
public class Errores {

    /**
     * Comprueba si los campos de un cliente son válidos.
     * @param nif NIF del cliente.
     * @param nombre Nombre del cliente.
     * @param apellidos Apellidos del cliente.
     * @param clientes Lista de clientes.
     * @return Mensaje de error si los campos no son válidos, si no, una cadena vacía.
     */
    public static String camposClienteValidos(String nif, String nombre, String apellidos, List<Cliente> clientes) {
        return String.format("%s%s%s", campoNifValido(nif, clientes), campoNombreValido(nombre), campoApellidosValido(apellidos)) ;
    }

    /**
     * Comprueba si el NIF de un cliente es válido.
     * @param nif NIF del cliente.
     * @param clientes Lista de clientes.
     * @return Mensaje de error si el NIF no es válido, si no, una cadena vacía.
     */
    public static String campoNifValido(String nif, List<Cliente> clientes) {
        String mensajeError = "";

        if (clientes.stream().anyMatch(c -> c.getNif().equals(nif)))
            mensajeError += "El cliente ya existe.\n";
        if (!nif.matches("[0-9]{8}[A-Z]") || nif.isEmpty())
            mensajeError += "El NIF no es válido.\n";

        return mensajeError;
    }

    /**
     * Comprueba si el nombre de un cliente es válido.
     * @param nombre Nombre del cliente.
     * @return Mensaje de error si el nombre no es válido, si no, una cadena vacía.
     */
    public static String campoNombreValido(String nombre) {
        String mensajeError = "";

        if (!nombre.matches("[A-Za-z\\s]+") || nombre.isEmpty())
            mensajeError = "El nombre no es válido.\n";

        return mensajeError;
    }

    /**
     * Comprueba si los apellidos de un cliente son válidos.
     * @param apellidos Apellidos del cliente.
     * @return Mensaje de error si los apellidos no son válidos, si no, una cadena vacía.
     */
    public static String campoApellidosValido(String apellidos) {
        String mensajeError = "";

        if (!apellidos.matches("[A-Za-z\\s]+") || apellidos.isEmpty())
            mensajeError = "Los apellidos no son válidos.\n";

        return mensajeError;
    }
}
