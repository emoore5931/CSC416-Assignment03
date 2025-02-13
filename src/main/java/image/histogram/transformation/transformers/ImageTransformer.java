package image.histogram.transformation.transformers;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ImageTransformer {
    static Image transformImage(BufferedImage inputImage) {
        return null;
    }

    static Image[] transformImages(BufferedImage[] inputImages) {
        return null;
    }
}
