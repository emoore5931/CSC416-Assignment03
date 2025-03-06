package image.spatial.filtering.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MedianFilter extends ImageFilter {

    public MedianFilter(BufferedImage img, int kernelSize) {
        this.srcImage = img;
        this.initImageMatrix();
        this.kernelSize = kernelSize;
        this.resetKernel(this.kernelSize);
    }

    @Override
    public FilterOutput filterImage() {
        FilterOutput output = new FilterOutput();
        output.rgbOutput = preformConvolution(this.imageRGBMatrix, BufferedImage.TYPE_INT_RGB);
        output.grayscaleOutput = preformConvolution(this.imageGrayscaleMatrix, BufferedImage.TYPE_BYTE_GRAY);

        return output;
    }

    @Override
    protected int filterPixel(int[][] inputMatrix, int x, int y) {
        Map<String, List<Integer>> rgbNeighborhoodVals = Map.of("r", new ArrayList<>(), "g", new ArrayList<>(), "b", new ArrayList<>());
        int kernelCenter = this.kernelSize / 2;
        for (int s = -kernelCenter + x; s < x + kernelCenter; s++) {
            for (int t = -kernelCenter + y; t < y + kernelCenter; t++) {
                if (coordinatesInMatrix(inputMatrix, s, t)) {
                    Color pixel = new Color(inputMatrix[s][t]);
                    rgbNeighborhoodVals.get("r").add(pixel.getRed());
                    rgbNeighborhoodVals.get("g").add(pixel.getBlue());
                    rgbNeighborhoodVals.get("b").add(pixel.getGreen());
                }
            }
        }

        int medR = getMedianVal(rgbNeighborhoodVals.get("r"));
        int medG = getMedianVal(rgbNeighborhoodVals.get("g"));
        int medB = getMedianVal(rgbNeighborhoodVals.get("b"));

        return new Color(medR, medG, medB).getRGB();
    }

    private int getMedianVal(List<Integer> integers) {
        List<Integer> copy = new ArrayList<>(List.copyOf(integers));
        Collections.sort(copy);

        int listSize = copy.size();
        if (listSize == 0) return 0;

        if (listSize % 2 != 0) {
            return copy.get(listSize / 2);
        } else {
            return (copy.get(listSize / 2) + copy.get(listSize / 2 - 1)) / 2;
        }
    }
}
