package adrian.modelo;

public class Producto {
    private int id;
    private String nombre;
    private double precio;
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

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
