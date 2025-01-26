package adrian.controlador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import adrian.App;
import adrian.modelo.*;
import adrian.utilidades.CrearFactura;
import adrian.utilidades.PdfToImage;
import adrian.utilidades.Errores;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controlador de la vista principal de la aplicación.
 * @author Adrián González
 */
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
     * Botón para añadir un cliente.
     */
    @FXML
    Button botonAnyadir;

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
     * Botón para pagar el pedido actual.
     */
    @FXML
    Button pagarBoton;

    /**
     * Botón para mostrar la gráfica de los productos del pedido actual.
     */
    @FXML
    Button graficaBoton;

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
    public static final List<Producto> PRODUCTOS = List.of(
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
    public List<Pedido> pedidos;

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
        // Asignar las propiedades de los clientes a las columnas de la tabla
        nifCol.setCellValueFactory(new PropertyValueFactory<>("nif"));
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        // Permitir editar los campos de la tabla de clientes
        nifCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreCol.setCellFactory(TextFieldTableCell.forTableColumn());
        apellidosCol.setCellFactory(TextFieldTableCell.forTableColumn());

        tablaClientes.setEditable(true);

        // Actualizar los valores de los clientes al editar las celdas
        nifCol.setOnEditCommit(e -> {
            if (!mostrarError(Errores.campoNifValido(e.getNewValue(), clientes)))
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setNif(e.getNewValue());
            tablaClientes.refresh();
        });

        nombreCol.setOnEditCommit(e -> {
            if (!mostrarError(Errores.campoNombreValido(e.getNewValue())))
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setNombre(e.getNewValue());
            tablaClientes.refresh();
        });

        apellidosCol.setOnEditCommit(e -> {
            if (!mostrarError(Errores.campoApellidosValido(e.getNewValue())))
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setApellidos(e.getNewValue());
            tablaClientes.refresh();
        });

        // Inicializar las listas de clientes y pedidos
        clientes = FXCollections.observableArrayList();
        pedidos = new ArrayList<>();

        // Añadir la lista de clientes a la tabla de clientes
        tablaClientes.setItems(clientes);

        // Asignar las propiedades de las lineas de pedido a las columnas de la tabla
        productoCol.setCellValueFactory(new PropertyValueFactory<>("producto"));
        cantidadCol.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        precioCol.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // Inicializar la lista de lineas de pedido y el pedido actual
        lineasPedido = FXCollections.observableArrayList();
        pedidoActual = new Pedido(lineasPedido);

        // Añadir la lista de lineas de pedido a la tabla de pedidos
        tablaPedidos.setItems(lineasPedido);

        // Deshabilitar los botones de la gráfica y de pagar al inicio
        graficaBoton.setDisable(true);
        pagarBoton.setDisable(true);

        // Habilitar los botones de la gráfica y de pagar si hay lineas de pedido
        lineasPedido.addListener((ListChangeListener.Change<? extends LineaPedido> c) -> {
            graficaBoton.setDisable(lineasPedido.isEmpty());
            pagarBoton.setDisable(lineasPedido.isEmpty());
        });

        // Crear el grid de tarjetas con los productos
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
    private void onClickAnyadir() {
        if (mostrarError(Errores.camposClienteValidos(nif.getText(), nombre.getText(), apellidos.getText(), clientes)))
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
        // Buscar el cliente con el NIF introducido
        Cliente cliente = clientes.stream().filter(c -> c.getNif().equals(nifPedido.getText())).findFirst().orElse(null);

        // Si el cliente no existe, mostrar un mensaje de error
        if (cliente == null) {
            mostrarError("El NIF no es válido o el cliente no está registrado.");
            return;
        }

        // Crear el pedido y añadirlo a la lista de pedidos
        Pedido pedido = new Pedido(cliente, lineasPedido);
        pedidos.add(pedido);

        // Previsualizar la factura
        mostrarFactura(pedido);

        // Limpiar los campos del pedido
        lineasPedido.clear();
        pedidoActual = new Pedido(lineasPedido);
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
        // Calcular el número de filas del grid en función del número de productos y de las columnas
        int nColumnas = 2;
        int nFilas = (int) Math.ceil((double) PRODUCTOS.size() / nColumnas);

        // Añadir las tarjetas al grid
        for (int j = 0; j < nFilas; j++)
            for (int i = 0; i < nColumnas && i * nFilas + j < PRODUCTOS.size(); i++)
                gridTarjetas.add(crearTarjeta(PRODUCTOS.get(j * nColumnas + i)), i, j);

        // Ajustar el espaciado entre las tarjetas
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
        // Crear un Pane para la tarjeta
        Pane pane = new Pane();
        // Añadir la clase tarjeta al estilo de la tarjeta
        pane.getStyleClass().add("tarjeta");

        // Crear VBox para el contenido de la tarjeta
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefHeight(10.0);

        // Añadir el nombre del producto a la tarjeta
        Label nombre = new Label(producto.getNombre());

        // Añadir la imagen del producto a la tarjeta y ajustar el tamaño (el nombre de la imagen es el id del producto)
        ImageView imagen = new ImageView(
                new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/" + producto.getId() + ".png"))));
        imagen.setFitHeight(60.0);
        imagen.setPreserveRatio(true); // Mantener la relación de aspecto de la imagen

        // Crear HBox para el precio, el spinner y el botón
        HBox hbox = new HBox();
        hbox.setPrefHeight(100.0);
        hbox.setPrefWidth(200.0);

        // Añadir el precio del producto a la tarjeta
        Label precio = new Label(String.format("%.2f€", producto.getPrecio()));

        // Crear Spinner para seleccionar la cantidad del producto y ajustar el tamaño
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setId("spinner-" + producto.getId()); // Se usa en los tests
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
        spinner.setPrefHeight(25.0);
        spinner.setPrefWidth(54.0);

        // Añadir un botón para añadir el producto al pedido actual
        Button button = new Button("+");
        button.setId("boton-" + producto.getId()); // Se usa en los tests
        button.setOnAction(e -> {
            anyadirProducto(producto, spinner.getValue());
            spinner.getValueFactory().setValue(1);
        });
        button.getStyleClass().add("boton-anyadir"); // Añadir la clase boton-anyadir al estilo del botón

        // Añadir el precio, el spinner y el botón al HBox y ajustar el espaciado y la alineación
        hbox.getChildren().addAll(precio, spinner, button);
        hbox.setSpacing(10.0);
        hbox.setAlignment(Pos.CENTER);

        // Añadir el nombre, la imagen y el HBox al VBox y ajustar el ancho del VBox al ancho del Pane
        vbox.getChildren().addAll(nombre, imagen, hbox);
        vbox.prefWidthProperty().bind(pane.widthProperty());
        vbox.getStyleClass().add("tarjeta-vbox"); // Añadir la clase tarjeta-vbox al estilo del VBox

        // Añadir el VBox al Pane
        pane.getChildren().add(vbox);

        return pane;
    }

    /**
     * Añade un producto al pedido actual.
     * Actualiza la tabla de pedidos y el precio total. Comprueba si el
     * producto ya está en el pedido y, en tal caso, suma la cantidad.
     *
     * @param producto El producto a añadir.
     * @param cantidad La cantidad del producto a añadir.
     */
    private void anyadirProducto(Producto producto, int cantidad) {
        pedidoActual.addLineaPedido(producto, cantidad);
        tablaPedidos.refresh();
        precioTotal.setText("Total: " + pedidoActual.getTotal() + " €");
    }

    /**
     * Muestra un mensaje de error en una ventana emergente.
     * Utilizar la clase de utilidades Errores para obtener el mensaje de error.
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

    /**
     * Genera una gráfica con los productos del pedido actual.
     *
     */
    @FXML
    private void onClickGrafica() {
        Stage stage = new Stage();

        PieChart pieChart = new PieChart();
        List<PieChart.Data> data = new ArrayList<>();

        // Calcular el total de productos del pedido para mostrar el porcentaje
        int totalProductos = lineasPedido.stream().mapToInt(LineaPedido::getCantidad).sum();

        // Añadir los productos del pedido a la gráfica
        lineasPedido.forEach(lp -> data.add(new PieChart.Data(lp.getProducto().getNombre() + " - " + Math.round(lp.getCantidad() * 10000.0 / totalProductos) / 100.0 + "%", lp.getCantidad())));

        // Añadir los datos a la gráfica y ajustar las propiedades
        pieChart.setData(FXCollections.observableArrayList(data));
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
        pieChart.setClockwise(true);
        pieChart.setStartAngle(90);

        // Mostrar la gráfica en una ventana emergente
        stage.setScene(new Scene(pieChart, 600, 550));
        stage.setTitle("Gráfica de productos del pedido");
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/logo.png"))));
        stage.show();
    }

    /**
     * Muestra la factura del pedido en una ventana emergente.
     *
     * @param pedido El pedido del que se va a mostrar la factura.
     */
    private void mostrarFactura(Pedido pedido) {
        try {
            // Crear la factura y renderizarla a una imagen
            CrearFactura factura = new CrearFactura(pedido);
            File pdfFile = factura.crearFactura();
            Image pdfImage = PdfToImage.renderPDFToImage(pdfFile, 0);

            // Crear un ImageView con la imagen de la factura
            ImageView pdfImageView = new ImageView(pdfImage);
            pdfImageView.setFitHeight(600);
            pdfImageView.setPreserveRatio(true);
            pdfImageView.setPickOnBounds(true);

            // Mostrar la imagen de la factura en una ventana emergente alineada al centro
            Stage stage = new Stage();
            VBox vBox = new VBox(pdfImageView);
            vBox.setAlignment(Pos.CENTER);
            stage.setScene(new Scene(vBox, 600, 600));
            stage.setTitle("Previsualización de la factura");
            stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/logo.png"))));
            stage.setResizable(false);
            stage.show();
        } catch (FileNotFoundException e) {
            mostrarError("Error al crear la factura.");
        } catch (IOException e) {
            mostrarError("Error al renderizar la factura.");
        }
    }
}
