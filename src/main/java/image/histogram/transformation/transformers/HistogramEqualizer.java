package image.histogram.transformation.transformers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class HistogramEqualizer implements HistogramTransformer {

    public static Image transformImage(BufferedImage inputImage) {
        int[] histogram = HistogramTransformer.getImageHistogram(inputImage);
        int[] newHistogram = getNewHistogram(histogram);
        return HistogramTransformer.applyNewHistogram(inputImage, newHistogram);
    }

    private static int[] getNewHistogram(int[] histogram) {
        int numPixels = Arrays.stream(histogram).sum();
        int histLength = histogram.length;
        double[] cdf = new double[histLength];
        int[] normCDF = new int[histLength];
        double minCDF = histogram[0];

        for (int i = 0; i < histLength; i++) {
            if (i == 0) {
                cdf[i] = minCDF;
            } else {
                cdf[i] = cdf[i - 1] + histogram[i];
            }

            normCDF[i] = Math.toIntExact(Math.round(((cdf[i] - minCDF) / (numPixels - minCDF) * 255)));
        }

        return normCDF;
    }

    public static Image[] transformImages(BufferedImage[] inputImages) {
        Image[] output = new Image[inputImages.length];
        for (int i = 0; i < inputImages.length; i++)
            output[i] = transformImage(inputImages[i]);
        return output;
    }

    public static List<Image> transformImages(List<BufferedImage> inputImages) {
        return inputImages.stream().map(HistogramEqualizer::transformImage).toList();
    }
}
