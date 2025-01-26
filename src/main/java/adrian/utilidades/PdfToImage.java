package adrian.utilidades;

import javafx.scene.image.Image;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfToImage {
    /**
     * Renderiza un archivo PDF a una imagen.
     * @param pdfFile archivo PDF a renderizar.
     * @param pageIndex índice de la página a renderizar.
     * @return imagen de la página renderizada.
     * @throws IOException si ocurre un error al renderizar el PDF.
     */
    public static Image renderPDFToImage(File pdfFile, int pageIndex) throws IOException {
        PDDocument document = Loader.loadPDF(pdfFile);
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        // Renderiza la primera página a una imagen a 300 DPI
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300);
        document.close();

        // Convierte BufferedImage a Image de JavaFX
        File tempFile = File.createTempFile("pdf_preview", ".png");
        ImageIO.write(bufferedImage, "png", tempFile);
        return new Image(new FileInputStream(tempFile));
    }
}
