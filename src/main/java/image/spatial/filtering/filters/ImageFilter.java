package image.spatial.filtering.filters;

import image.spatial.filtering.util.Config;

import java.awt.image.BufferedImage;
import java.util.Objects;

public abstract class ImageFilter {
    protected BufferedImage srcImage;
    protected int kernelSize = Integer.parseInt(Config.getProperty("defaultKernelSize"));
    public double[][] kernel = new double[kernelSize][kernelSize];

    protected int[][] imageRGBMatrix;
    protected int[][] imageGrayscaleMatrix;

    public FilterOutput filterImage() {throw new UnsupportedOperationException("Method not yet implemented for the chosen filter: " + this.getClass().getName());}

    public void resetKernel(Integer kernelSize) {
        this.kernel = new double[Objects.nonNull(kernelSize) ? kernelSize : this.kernelSize][Objects.nonNull(kernelSize) ? kernelSize : this.kernelSize];
    }

    protected void initImageMatrix() {
        int height = srcImage.getHeight();
        int width = srcImage.getWidth();
        imageRGBMatrix = new int[height][width];
        imageGrayscaleMatrix = new int[height][width];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixelRGB = srcImage.getRGB(x, y);
                imageRGBMatrix[x][y] = pixelRGB;
                imageGrayscaleMatrix[x][y] = rgbToGrayscale(pixelRGB);
            }
        }
    }

    private static int rgbToGrayscale(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        int grayscale = (int)(0.299 * r + 0.587 * g + 0.114 * b);

        return (grayscale << 16) | (grayscale << 8) | grayscale;
    }

    protected BufferedImage preformConvolution(int[][] inputMatrix, int imageType) {
        int height = inputMatrix.length;
        int width = inputMatrix[0].length;
        BufferedImage output = new BufferedImage(width, height, imageType);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                output.setRGB(x, y, filterPixel(inputMatrix, x, y));
            }
        }

        return output;
    }

    protected int filterPixel(int[][] inputMatrix, int x, int y) {throw new UnsupportedOperationException("Method not yet implemented for the chosen filter: " + this.getClass().getName());}

    protected void generateKernel() {throw new UnsupportedOperationException("Method not yet implemented for the chosen filter: " + this.getClass().getName());}

    protected boolean coordinatesInMatrix(int[][] matrix, int x, int y) {
        return x >= 0 && x < matrix.length && y < matrix.length && y >= 0;
    }

    public double getKernelTotal() {
        double total = 0.0;
        for (int x = 0; x < this.kernelSize; x++) {
            for (int y = 0; y < this.kernelSize; y++) {
                total += this.kernel[x][y];
            }
        }
        return total;
    }
}
