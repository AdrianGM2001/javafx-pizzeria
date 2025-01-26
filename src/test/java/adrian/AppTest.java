package adrian;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

class AppTest extends ApplicationTest {
    private TextField nif;
    private TextField nombre;
    private TextField apellidos;
    private Button anyadirCliente;
    private VBox clientesVBox;
    private HBox pedidosHBox;
    private ObservableList<Object> clientes;
    private ObservableList<Object> lineasPedido;

    @Override
    public void start(Stage stage) throws Exception {
        App app = new App();
        app.start(stage);
        nif = lookup("#nif").query();
        nombre = lookup("#nombre").query();
        apellidos = lookup("#apellidos").query();
        anyadirCliente = lookup("#botonAnyadir").query();
        clientesVBox = lookup("#clientesVBox").query();
        pedidosHBox = lookup("#pedidosHBox").query();
        clientes = lookup("#tablaClientes").queryTableView().getItems();
        lineasPedido = lookup("#tablaPedidos").queryTableView().getItems();
    }

    /**
     * Test para comprobar que se añade un cliente correctamente
     */
    @Test
    void testAgregarCliente() {
        FxRobot robot = new FxRobot();
        robot.clickOn(nif).write("12345678A");
        robot.clickOn(nombre).write("Juan");
        robot.clickOn(apellidos).write("Garcia Navarro");
        robot.clickOn(anyadirCliente);
        assert(clientes.size() == 1);
        assert(nif.getText().isEmpty());
        assert(nombre.getText().isEmpty());
        assert(apellidos.getText().isEmpty());
    }

    /**
     * Test para comprobar que se añaden varios clientes correctamente
     */
    @Test
    void testAgregarVariosClientes() {
        FxRobot robot = new FxRobot();
        robot.clickOn(nif).write("12345678A");
        robot.clickOn(nombre).write("Juan");
        robot.clickOn(apellidos).write("Garcia Navarro");
        robot.clickOn(anyadirCliente);
        robot.clickOn(nif).write("87654321B");
        robot.clickOn(nombre).write("Maria");
        robot.clickOn(apellidos).write("Perez Lopez");
        robot.clickOn(anyadirCliente);
        robot.clickOn(nif).write("12345728A");
        robot.clickOn(nombre).write("Pedro");
        robot.clickOn(apellidos).write("Diaz Martinez");
        robot.clickOn(anyadirCliente);
        assert(clientes.size() == 3);
        assert(nif.getText().isEmpty());
        assert(nombre.getText().isEmpty());
        assert(apellidos.getText().isEmpty());
    }

    /**
     * Test para comprobar que no se añade un cliente con un NIF repetido
     */
    @Test
    void testAgregarClienteRepetido() {
        FxRobot robot = new FxRobot();
        robot.clickOn(nif).write("12345678A");
        robot.clickOn(nombre).write("Juan");
        robot.clickOn(apellidos).write("Garcia Navarro");
        robot.clickOn(anyadirCliente);
        robot.clickOn(nif).write("12345678A");
        robot.clickOn(nombre).write("Juan");
        robot.clickOn(apellidos).write("Garcia Navarro");
        robot.clickOn(anyadirCliente);
        robot.type(javafx.scene.input.KeyCode.ENTER);
        assert(clientes.size() == 1);
    }

    /**
     * Test para comprobar que se modifica un cliente correctamente
     */
    @Test
    void testModificarCliente() {
        FxRobot robot = new FxRobot();
        robot.clickOn(nif).write("12345678A");
        robot.clickOn(nombre).write("Juan");
        robot.clickOn(apellidos).write("Garcia Navarro");
        robot.clickOn(anyadirCliente);
        robot.doubleClickOn("12345678A").write("87654321B");
        robot.type(javafx.scene.input.KeyCode.ENTER);
        assert(clientes.stream().filter(c -> c.toString().contains("87654321B")).count() == 1);
    }

    /**
     * Test para comprobar que se cambia de vista correctamente
     */
    @Test
    void cambiarVista() {
        FxRobot robot = new FxRobot();
        robot.clickOn("#pedidosBoton");
        assert(pedidosHBox.isVisible());
        assert(!clientesVBox.isVisible());
        robot.clickOn("#clientesBoton");
        assert(!pedidosHBox.isVisible());
        assert(clientesVBox.isVisible());
    }

    /**
     * Test para comprobar que se puede hacer un pedido correctamente
     */
    @Test
    void testHacerPedido() {
        Spinner<Integer> spinner0 = lookup("#spinner-0").query();
        Spinner<Integer> spinner1 = lookup("#spinner-5").query();

        FxRobot robot = new FxRobot();
        robot.clickOn(nif).write("12345678A");
        robot.clickOn(nombre).write("Juan");
        robot.clickOn(apellidos).write("Garcia Navarro");
        robot.clickOn(anyadirCliente);
        robot.clickOn("#pedidosBoton");
        robot.clickOn(spinner0.getChildrenUnmodifiable().get(1));
        robot.clickOn("#boton-0");
        assert(lineasPedido.size() == 1);
        robot.clickOn(spinner1.getChildrenUnmodifiable().get(1));
        robot.clickOn(spinner1.getChildrenUnmodifiable().get(1));
        robot.clickOn(spinner1.getChildrenUnmodifiable().get(1));
        robot.clickOn("#boton-5");
        assert(lineasPedido.size() == 2);
        robot.clickOn("#boton-7");
        assert(lineasPedido.size() == 3);
        robot.clickOn("#nifPedido").write("12345678A");
        robot.clickOn("#pagarBoton");

        assert(lineasPedido.isEmpty());
    }

    @Override
    public void stop() throws Exception {
        FxToolkit.hideStage();
    }
}