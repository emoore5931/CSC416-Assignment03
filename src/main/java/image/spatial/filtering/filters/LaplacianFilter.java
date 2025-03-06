package image.spatial.filtering.filters;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LaplacianFilter extends ImageFilter {
    private double weight;

    public LaplacianFilter(BufferedImage img, double[][] kernel, double weight) {
        this.srcImage = img;
        initImageMatrix();
        this.kernel = kernel;
        this.kernelSize = kernel.length;
        this.weight = weight;
    }

    @Override
    public FilterOutput filterImage() {
        FilterOutput output = new FilterOutput();
        BufferedImage blurredRgbImg = preformConvolution(this.imageRGBMatrix, BufferedImage.TYPE_INT_RGB);
        BufferedImage blurredGrayscaleImg = preformConvolution(this.imageGrayscaleMatrix, BufferedImage.TYPE_BYTE_GRAY);

        output.rgbOutput = sharpenImage(this.imageRGBMatrix, blurredRgbImg);
        output.grayscaleOutput = sharpenImage(this.imageGrayscaleMatrix, blurredGrayscaleImg);
        return output;
    }

    private BufferedImage sharpenImage(int[][] imageMatrix, BufferedImage blurredImg) {
        BufferedImage output = new BufferedImage(blurredImg.getWidth(), blurredImg.getHeight(), blurredImg.getType());
        for (int x = 0; x < imageMatrix.length; x++) {
            for (int y = 0; y < imageMatrix[0].length; y++) {
                Color imgPixel = new Color(imageMatrix[x][y]);
                Color blurredPixel = new Color(blurredImg.getRGB(x, y));

                //get mask vals
                int redMask = imgPixel.getRed() - blurredPixel.getRed();
                int greenMask = imgPixel.getGreen() - blurredPixel.getGreen();
                int blueMask = imgPixel.getGreen() - blurredPixel.getGreen();

                //get sharpened vals
                int sharpenedRed = clampRGB((int) (imgPixel.getRed() - this.weight * redMask));
                int sharpenedGreen = clampRGB((int) (imgPixel.getGreen() - this.weight * greenMask));
                int sharpenedBlue = clampRGB((int) (imgPixel.getBlue() - this.weight * blueMask));

                output.setRGB(x, y, new Color(sharpenedRed, sharpenedGreen, sharpenedBlue).getRGB());
            }
        }
        return output;
    }

    @Override
    protected int filterPixel(int[][] inputMatrix, int x, int y) {
        double[] rgbFilterTotal = new double[3];
        double kernelTotal = this.getKernelTotal();
        int kernelCenter = this.kernelSize / 2;
        for (int s = 0; s < this.kernelSize; s++) {
            for (int t = 0; t < this.kernelSize; t++) {
                int matrixX = (s - kernelCenter) + x;
                int matrixY = (t - kernelCenter) + y;

                if (coordinatesInMatrix(inputMatrix, matrixX, matrixY)) {
                    Color pixelColor = new Color(inputMatrix[matrixX][matrixY]);
                    rgbFilterTotal[0] += (this.kernel[s][t] * pixelColor.getRed());
                    rgbFilterTotal[1] += (this.kernel[s][t] * pixelColor.getGreen());
                    rgbFilterTotal[2] += (this.kernel[s][t] * pixelColor.getBlue());
                }
            }
        }

        int filteredRed = clampRGB((int) (rgbFilterTotal[0] / kernelTotal));
        int filteredGreen = clampRGB((int) (rgbFilterTotal[1] / kernelTotal));
        int filteredBlue = clampRGB((int) (rgbFilterTotal[2] / kernelTotal));

        return new Color(filteredRed, filteredGreen, filteredBlue).getRGB();
    }

    private static int clampRGB(int value) {
        return Math.max(0, Math.min(value, 255));
    }
}
