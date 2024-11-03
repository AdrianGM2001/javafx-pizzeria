package adrian.modelo;

import java.util.List;

public class Pedido {
    private Cliente cliente;
    private List<LineaPedido> lineasPedido;
    private double total;

    public Pedido(Cliente cliente, List<LineaPedido> lineasPedido) {
        this.cliente = cliente;
        this.lineasPedido = lineasPedido;
        this.total = lineasPedido.stream().mapToDouble(LineaPedido::getPrecio).sum();
    }

    public Pedido(List<LineaPedido> lineasPedido) {
        this(null, lineasPedido);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<LineaPedido> getLineasPedido() {
        return lineasPedido;
    }

    public double getTotal() {
        return total;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setLineasPedido(List<LineaPedido> lineasPedido) {
        this.lineasPedido = lineasPedido;
        total = Math.round(lineasPedido.stream().mapToDouble(LineaPedido::getPrecio).sum()  * 100.0) / 100.0;
    }

    public void addLineaPedido(Producto producto, int cantidad) {
        lineasPedido.stream()
                .filter(lineaPedido -> lineaPedido.getProducto().equals(producto))
                .findFirst()
                .ifPresentOrElse(
                        lineaPedido -> lineaPedido.addCantidad(cantidad),
                        () -> lineasPedido.add(new LineaPedido(producto, cantidad))
                );
        
        total = Math.round(lineasPedido.stream().mapToDouble(LineaPedido::getPrecio).sum()  * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "cliente=" + cliente +
                ", lineasPedido=" + lineasPedido +
                ", total=" + total +
                '}';
    }
}
