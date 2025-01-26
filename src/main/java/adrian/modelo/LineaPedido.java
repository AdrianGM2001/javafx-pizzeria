package adrian.modelo;

public class LineaPedido {
    private final Producto producto;
    private int cantidad;
    private double precio;

    public LineaPedido(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio =  Math.round(producto.getPrecio() * cantidad * 100.0) / 100.0;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void addCantidad(int cantidad) {
        this.cantidad += cantidad;
        precio = producto.getPrecio() * this.cantidad;
        precio = Math.round(precio * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "LineaPedido{" +
                "producto=" + producto +
                ", cantidad=" + cantidad +
                ", precio=" + precio +
                '}';
    }
}
