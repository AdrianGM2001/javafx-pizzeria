package adrian.utilidades;

import adrian.modelo.Cliente;
import adrian.modelo.LineaPedido;
import adrian.modelo.Pedido;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;

public class CrearFactura {
    private final Pedido pedido;

    public CrearFactura(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * Crea un archivo PDF con la factura del pedido.
     * @throws FileNotFoundException si no se puede crear el archivo.
     */
    public File crearFactura() throws FileNotFoundException {
        // Crear el archivo PDF
        File pdfFile = new File("factura.pdf");
        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Datos del cliente
        String nif = pedido.getCliente().getNif();
        Cliente cliente = pedido.getCliente();

        // Añadir contenido al PDF
        document.add(new Paragraph("Factura")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("NIF Cliente: " + nif));
        document.add(new Paragraph("Nombre: " + cliente.getNombre()));
        document.add(new Paragraph("Apellidos: " + cliente.getApellidos()));
        document.add(new Paragraph("Fecha: " + LocalDate.now()));

        // Crear tabla con los campos de la factura
        float[] columnWidths = { 1, 1, 1, 1 };
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));
        table.addHeaderCell(new Cell().add(new
                Paragraph("Cantidad").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new
                Paragraph("Nombre del producto").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new
                Paragraph("Precio unitario").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));
        table.addHeaderCell(new Cell().add(new
                Paragraph("Precio total").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY)));

        // Añadir las líneas del pedido a la tabla
        for (LineaPedido linea : pedido.getLineasPedido()) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(linea.getCantidad()))));
            table.addCell(new Cell().add(new Paragraph(linea.getProducto().getNombre())));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f€", linea.getProducto().getPrecio()))));
            table.addCell(new Cell().add(new Paragraph(String.format("%.2f€", linea.getPrecio()))));
        }

        document.add(table); // Añadir la tabla al documento

        // Añadir el total del pedido
        document.add(new Paragraph("TOTAL: " + pedido.getTotal() + "€").setFontSize(12).setBold().setTextAlignment(TextAlignment.RIGHT));
        document.close();

        return pdfFile;
    }
}
