package adrian.modelo;

public class Producto {
    private final int id;
    private final String nombre;
    private final double precio;
    private static int nProductos = 0;

    public Producto(String nombre, double precio) {
        id = nProductos++;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
