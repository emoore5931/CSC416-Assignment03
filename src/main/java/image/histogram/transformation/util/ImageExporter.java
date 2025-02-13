package image.histogram.transformation.util;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageExporter {

    public static void exportImage(Image img, String fileName) throws IOException {
        String imageOutputType = Config.getProperty("imageOutputType");
        ImageIO.write(
                (BufferedImage) img,
                imageOutputType.toUpperCase(),
                new File(Config.getProperty("outputPath") + fileName + "." + imageOutputType)
        );
    }
}
