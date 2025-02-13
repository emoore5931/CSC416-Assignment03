package image.histogram.transformation.transformers;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public interface HistogramTransformer extends ImageTransformer {
    static int[] getImageHistogram(BufferedImage img) {
        int[] histogram = new int[256];
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        //traverse and count intensity
        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                int pixelRGB = img.getRGB(x, y);
                //convert to grayscale using luminosity
                int grayscaleIntensity = getGrayscaleIntensity(pixelRGB);

                histogram[grayscaleIntensity]++;
            }
        }

        return histogram;
    }

    static BufferedImage applyNewHistogram(BufferedImage inputImg, int[] newHistogram) {
        int imgWidth = inputImg.getWidth();
        int imgHeight = inputImg.getHeight();
        BufferedImage resultImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);

        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                int grayscaleIntensity = getGrayscaleIntensity(inputImg.getRGB(x, y));
                int newPixelIntensity = newHistogram[grayscaleIntensity];
                int newPixelVal = (newPixelIntensity << 16) | (newPixelIntensity << 8) | newPixelIntensity;
                resultImg.setRGB(x, y, newPixelVal);
            }
        }

        return  resultImg;
    }

    static int getGrayscaleIntensity(int pixelRGB) {
        int pixelRed = (pixelRGB >> 16) & 0xFF;
        int pixelGreen = (pixelRGB >> 8) & 0xFF;
        int pixelBlue = pixelRGB & 0xFF;

        return (int) ((0.3 * pixelRed) + (0.59 * pixelGreen) + (0.11 * pixelBlue));
    }
}
