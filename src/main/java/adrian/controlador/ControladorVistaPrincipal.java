package adrian.controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import adrian.App;
import adrian.modelo.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ControladorVistaPrincipal implements Initializable {
    /**
     * VBox que contiene la vista de clientes.
     */
    @FXML
    VBox clientesVBox;

    /**
     * HBox que contiene la vista de pedidos.
     */
    @FXML
    HBox pedidosHBox;

    /**
     * Botón para cambiar a la vista de clientes.
     */
    @FXML
    Button clientesBoton;

    /**
     * Botón para cambiar a la vista de pedidos.
     */
    @FXML
    Button pedidosBoton;

    /**
     * Campo de texto para introducir el NIF del cliente.
     */
    @FXML
    TextField nif;

    /**
     * Campo de texto para introducir el nombre del cliente.
     */
    @FXML
    TextField nombre;

    /**
     * Campo de texto para introducir los apellidos del cliente.
     */
    @FXML
    TextField apellidos;

    /**
     * Tabla de clientes.
     */
    @FXML
    TableView<Cliente> tablaClientes;

    /**
     * Columna de la tabla de clientes que muestra el nombre de cada cliente.
     */
    @FXML
    TableColumn<Cliente, String> nombreCol;

    /**
     * Columna de la tabla de clientes que muestra los apellidos de cada cliente.
     */
    @FXML
    TableColumn<Cliente, String> apellidosCol;

    /**
     * Columna de la tabla de clientes que muestra el NIF de cada cliente.
     */
    @FXML
    TableColumn<Cliente, String> nifCol;

    /**
     * Campo de texto para introducir el NIF del cliente al que se le va a realizar el pedido.
     */
    @FXML
    TextField nifPedido;

    /**
     * Grid de tarjetas con los productos disponibles.
     */
    @FXML
    GridPane gridTarjetas;

    /**
     * Tabla de pedidos.
     */
    @FXML
    TableView<LineaPedido> tablaPedidos;

    /**
     * Columna de la tabla de pedidos que muestra el producto de cada línea de pedido.
     */
    @FXML
    TableColumn<LineaPedido, String> productoCol;

    /**
     * Columna de la tabla de pedidos que muestra la cantidad de productos de cada línea de pedido.
     */
    @FXML
    TableColumn<LineaPedido, String> cantidadCol;

    /**
     * Columna de la tabla de pedidos que muestra el precio de cada línea de pedido.
     */
    @FXML
    TableColumn<LineaPedido, String> precioCol;

    /**
     * Label que muestra el precio total del pedido actual.
     */
    @FXML
    Label precioTotal;

    /**
     * Lista de clientes.
     */
    ObservableList<Cliente> clientes;

    /**
     * Pedido actual.
     */
    Pedido pedidoActual;

    /**
     * Lista de lineas de pedido del pedido actual.
     */
    ObservableList<LineaPedido> lineasPedido;

    /**
     * Lista de productos disponibles.
     */
    private static final List<Producto> PRODUCTOS = List.of(
            new Producto("Hot-N-Ready Pepperoni", 10.99),
            new Producto("Hot-N-Ready Cheese", 10.99),
            new Producto("Sweet N Spicy Chicken", 16.2),
            new Producto("BBQ Chicken", 16.2),
            new Producto("3 Meat Treat", 18.5),
            new Producto("Hula Hawaiian", 16.2),
            new Producto("Ultimate Supreme", 18.5),
            new Producto("Veggie", 16.2));

    /**
     * Lista de pedidos realizados.
     */
    private List<Pedido> pedidos;

    /** 
     * Lista de spinners que se utilizan para seleccionar la cantidad de productos.
     */
    private List<Spinner<Integer>> spinners;

    /**
     * Inicializa la vista principal. Se inicializan las columnas de la tabla de clientes y de la tabla de pedidos.
     * Se añaden los spinners a la lista de spinners. Se inicializan las listas de clientes y de pedidos.
     * Se crea el grid de tarjetas con los productos.
     * 
     * @param arg0 No se utiliza.
     * @param arg1 No se utiliza.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        tablaClientes.setEditable(true);

        nifCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreCol.setCellFactory(TextFieldTableCell.forTableColumn());
        apellidosCol.setCellFactory(TextFieldTableCell.forTableColumn());

        nifCol.setOnEditCommit(e -> {
            if (campoNifValido(e.getNewValue()))
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setNif(e.getNewValue());
            tablaClientes.refresh();
        });

        nombreCol.setOnEditCommit(e -> {
            if (campoNombreValido(e.getNewValue()))
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setNombre(e.getNewValue());
            tablaClientes.refresh();
        });

        apellidosCol.setOnEditCommit(e -> {
            if (campoApellidosValido(e.getNewValue()))
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setApellidos(e.getNewValue());
            tablaClientes.refresh();
        });

        clientes = FXCollections.observableArrayList();
        pedidos = new ArrayList<>();
        spinners = new ArrayList<>();

        tablaClientes.setItems(clientes);

        productoCol.setCellValueFactory(new PropertyValueFactory<>("producto"));
        cantidadCol.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precio"));

        lineasPedido = FXCollections.observableArrayList();
        pedidoActual = new Pedido(lineasPedido);

        tablaPedidos.setItems(lineasPedido);

        crearGridTarjetas();
    }

    /**
     * Cambia la vista entre la lista de clientes y la lista de pedidos.
     */
    @FXML
    private void changeVista() {
        clientesVBox.setVisible(!clientesVBox.isVisible());
        clientesBoton.setDisable(!clientesBoton.isDisable());
        pedidosHBox.setVisible(!pedidosHBox.isVisible());
        pedidosBoton.setDisable(!pedidosBoton.isDisable());
    }

    /**
     * Cierra la aplicación.
     */
    @FXML
    private void onClickSalir() {
        Platform.exit();
    }

    /**
     * Añade un cliente a la lista de clientes. Si los campos no son válidos, no se
     * añade el cliente. Se limpian los campos.
     */
    @FXML
    private void onClickAñadir() {
        if (!camposClienteValidos())
            return;

        clientes.add(new Cliente(nif.getText(), nombre.getText(), apellidos.getText()));
        nif.clear();
        nombre.clear();
        apellidos.clear();
    }

    /**
     * Realiza el pedido actual. Si el pedido es válido, se añade a la lista de
     * pedidos y se muestra un mensaje. Si el pedido no es válido, se muestra un
     * mensaje de error. Se limpian los campos del pedido y se reinician los
     * spinners.
     */
    @FXML
    private void onClickPagar() {
        if (!camposPedidoValidos())
            return;

        Cliente cliente = clientes.stream().filter(c -> c.getNif().equals(nifPedido.getText())).findFirst().get();
        Pedido pedido = new Pedido(cliente, lineasPedido);
        pedidos.add(pedido);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pedido realizado");
        alert.setHeaderText("El cliente " + cliente.getNombre() + " " + cliente.getApellidos()
                + " ha realizado el pedido. Total: " + pedido.getTotal() + " €");
        alert.showAndWait();

        lineasPedido.clear();
        pedidoActual = new Pedido(lineasPedido);
        spinners.forEach(s -> s.getValueFactory().setValue(0));
        precioTotal.setText("Total");
        nifPedido.clear();
    }

    /**
     * Crea un grid con las tarjetas de los productos. Cada tarjeta contiene el
     * nombre del producto,
     * una imagen, el precio, un spinner para seleccionar la cantidad y un botón
     * para añadir el producto
     * al pedido actual.
     */
    private void crearGridTarjetas() {
        int nFilas = (int) Math.ceil(PRODUCTOS.size() / 2.0);

        for (int j = 0; j < nFilas; j++)
            for (int i = 0; i < 2 && i * nFilas + j < PRODUCTOS.size(); i++)
                gridTarjetas.add(crearTarjeta(PRODUCTOS.get(j * 2 + i)), i, j);

        gridTarjetas.setHgap(10);
        gridTarjetas.setVgap(10);
    }

    /**
     * Crea una tarjeta con la información de un producto. La tarjeta contiene el
     * nombre del producto,
     * una imagen, el precio, un spinner para seleccionar la cantidad y un botón
     * para añadir el producto
     * al pedido actual.
     * 
     * @param producto El producto a mostrar en la tarjeta.
     * @return La tarjeta con la información del producto.
     */
    private Pane crearTarjeta(Producto producto) {
        Pane pane = new Pane();
        pane.setMinHeight(130);
        pane.prefHeightProperty().bind(pane.minHeightProperty());
        pane.getStyleClass().add("tarjeta");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefHeight(10.0);

        Label nombre = new Label(producto.getNombre());

        ImageView imagen = new ImageView(
                new Image(App.class.getResourceAsStream("img/" + producto.getId() + ".png")));
        imagen.setFitHeight(60.0);
        imagen.setPreserveRatio(true);
        imagen.setPickOnBounds(true);

        HBox hbox = new HBox();
        hbox.setPrefHeight(100.0);
        hbox.setPrefWidth(200.0);

        Label precio = new Label(producto.getPrecio() + " €");

        Spinner<Integer> spinner = new Spinner<>();
        spinners.add(spinner);
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 99, 0));
        spinner.setPrefHeight(25.0);
        spinner.setPrefWidth(54.0);

        Button button = new Button("+");
        button.setOnAction(e -> añadirProducto(producto, spinner.getValue()));
        button.getStyleClass().add("boton-anadir");

        hbox.getChildren().addAll(precio, spinner, button);
        hbox.setSpacing(10.0);
        hbox.setAlignment(Pos.CENTER);
        hbox.prefWidthProperty().bind(vbox.widthProperty());

        vbox.getChildren().addAll(nombre, imagen, hbox);
        vbox.prefWidthProperty().bind(pane.widthProperty());
        vbox.getStyleClass().add("tarjeta-vbox");

        pane.getChildren().add(vbox);

        return pane;
    }

    /**
     * Añade un producto al pedido actual. Si la cantidad es 0, no se añade el
     * producto. Actualiza la tabla de pedidos y el precio total. Comprueba si el
     * producto ya
     * está en el pedido y, en caso afirmativo, suma la cantidad.
     * 
     * @param producto El producto a añadir.
     * @param cantidad La cantidad del producto a añadir.
     */
    private void añadirProducto(Producto producto, int cantidad) {
        if (cantidad == 0)
            return;

        pedidoActual.addLineaPedido(producto, cantidad);

        tablaPedidos.refresh();
        precioTotal.setText("Total: " + pedidoActual.getTotal() + " €");
    }

    /**
     * Muestra un mensaje de error en una ventana emergente.
     * 
     * @param mensaje El mensaje de error a mostrar.
     * @return true si se ha mostrado un mensaje de error, false en caso contrario.
     */
    private boolean mostrarError(String mensaje) {
        if (mensaje.isEmpty())
            return false;

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(mensaje);
        alert.showAndWait();

        return true;
    }

    // ESTOS MÉTODOS DE VALIDACIÓN DEBERÍAN HACERSE EN EL MODELO

    /**
     * Comprueba si los campos del cliente son válidos.
     * 
     * @return true si los campos son válidos, false en caso contrario.
     */
    private boolean camposClienteValidos() {
        if (!campoNifValido(nif.getText()))
            return false;
        if (!campoNombreValido(nombre.getText()))
            return false;
        if (!campoApellidosValido(apellidos.getText()))
            return false;

        return true;
    }

    /**
     * Comprueba si los campos del pedido son válidos.
     * 
     * @return true si los campos son válidos, false en caso contrario.
     */
    private boolean camposPedidoValidos() {
        String mensajeError = "";

        if (nifPedido.getText().isEmpty())
            mensajeError = "Por favor, introduce el NIF.";
        if (!nifPedido.getText().matches("[0-9]{8}[A-Z]"))
            mensajeError = "El NIF no es válido.";
        if (clientes.stream().noneMatch(c -> c.getNif().equals(nifPedido.getText())))
            mensajeError = "El cliente no existe.";

        return !mostrarError(mensajeError);
    }

    /**
     * Comprueba si el NIF es válido.
     * 
     * @param nif El NIF a comprobar.
     * @return true si el NIF es válido, false en caso contrario.
     */
    private boolean campoNifValido(String nif) {
        String mensajeError = "";

        if (clientes.stream().anyMatch(c -> c.getNif().equals(nif)))
            mensajeError = "El cliente ya existe.";
        if (!nif.matches("[0-9]{8}[A-Z]") && !nif.isEmpty())
            mensajeError = "El NIF no es válido.";

        return !mostrarError(mensajeError);
    }

    /**
     * Comprueba si el nombre es válido.
     * 
     * @param nombre El nombre a comprobar.
     * @return true si el nombre es válido, false en caso contrario.
     */
    private boolean campoNombreValido(String nombre) {
        String mensajeError = "";

        if (!nombre.matches("[A-Za-z\\s]+") && !nombre.isEmpty())
            mensajeError = "El nombre no es válido.";

        return !mostrarError(mensajeError);
    }

    /**
     * Comprueba si los apellidos son válidos.
     * 
     * @param apellidos Los apellidos a comprobar.
     * @return true si los apellidos son válidos, false en caso contrario.
     */
    private boolean campoApellidosValido(String apellidos) {
        String mensajeError = "";

        if (!apellidos.matches("[A-Za-z\\s]+") && !apellidos.isEmpty())
            mensajeError = "Los apellidos no son válidos.";

        return !mostrarError(mensajeError);
    }
}
