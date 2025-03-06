package image.spatial.filtering.filters;

import image.spatial.filtering.util.Config;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class GaussianFilter extends ImageFilter {
    private int sigma;
    private double k;

    public GaussianFilter(BufferedImage img, int sigma, double k) {
        this.srcImage = img;
        this.initImageMatrix();
        this.k = k;
        this.sigma = sigma;
    }

    @Override
    public FilterOutput filterImage() {
        FilterOutput output = new FilterOutput();
        generateKernel();
        output.rgbOutput = preformConvolution(this.imageRGBMatrix, BufferedImage.TYPE_INT_RGB);
        output.grayscaleOutput = preformConvolution(this.imageGrayscaleMatrix, BufferedImage.TYPE_BYTE_GRAY);

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

        int filteredRed = (int) (rgbFilterTotal[0] / kernelTotal);
        int filteredGreen = (int) (rgbFilterTotal[1] / kernelTotal);
        int filteredBlue = (int) (rgbFilterTotal[2] / kernelTotal);

        return new Color(filteredRed, filteredGreen, filteredBlue).getRGB();
    }

    @Override
    protected void generateKernel() {
        int kernelCenter = this.kernelSize / 2;

        for (int s = 0; s < this.kernelSize; s++) {
            for (int t = 0; t < this.kernelSize; t++) {
                int kernelX = s - kernelCenter;
                int kernelY = t - kernelCenter;

                this.kernel[s][t] = Math.exp((double) (kernelX * kernelX + kernelY * kernelY) / (2 * (this.sigma * this.sigma)) * (-1) * k);
            }
        }

        if (Boolean.parseBoolean(Config.getProperty("showKernels"))) {
            System.out.println("Generated Gaussian Kernel:");
            for (double[] row : this.kernel) {
                System.out.println(Arrays.toString(row));
            }
        }
    }
}
