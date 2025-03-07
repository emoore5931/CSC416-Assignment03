package image.spatial.filtering.util;

import image.spatial.filtering.filters.FilterOutput;
import image.spatial.filtering.filters.GaussianFilter;
import image.spatial.filtering.filters.LaplacianFilter;
import image.spatial.filtering.filters.MedianFilter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageConversion {
    protected enum ImageTypes {
        TIFF,
        RAW
    }

    public enum ConversionOptions {
        GaussianFilter,
        LaplacianFilter,
        MedianFilter
    }

    private ImageTypes outputType;
    private String outputPath;

    public ImageConversion() {
        String outputTypeConfigStr = Config.getProperty("imageOutputType");
        String outputPathConfigStr = Config.getProperty("outputPath");

        switch (outputTypeConfigStr.strip().toLowerCase()) {
            case "tiff":
                this.outputType = ImageTypes.TIFF;
                break;
            case "raw":
                this.outputType = ImageTypes.RAW;
                break;
            default:
                System.err.println("Unknown image output type applying default.");
                this.outputType = ImageTypes.TIFF;
        }

        File outDir = new File(outputPathConfigStr);
        if (outDir.exists() && outDir.isDirectory()) {
            this.outputPath = outputPathConfigStr;
        } else {
            System.err.println("Could not find output directory. Exiting...");
            System.exit(1);
        }
    }

    public List<Image> convertImages(String[] imgPaths, ConversionOptions convType) {
        ArrayList<BufferedImage> sourceImages = new ArrayList<>();
        for (String imgPath : imgPaths) {
            BufferedImage sourceImage = getImage(imgPath);
            if (sourceImage == null)
                return null;
            else
                sourceImages.add(sourceImage);
        }

        List<Image> filteredImages = new ArrayList<>();
        switch (convType) {
            case GaussianFilter -> sourceImages.forEach(img -> {
                int sigma = Integer.parseInt(Config.getProperty("sigma"));
                double K = Double.parseDouble(Config.getProperty("K"));
                FilterOutput result = new GaussianFilter(img, sigma, K).filterImage();
                filteredImages.add(result.grayscaleOutput);
            });
            case MedianFilter -> sourceImages.forEach(img -> {
                int kernelSize = Integer.parseInt(Config.getProperty("medianFilterKernelSize"));
                FilterOutput result = new MedianFilter(img, kernelSize).filterImage();
                filteredImages.add(result.grayscaleOutput);
            });
            case LaplacianFilter -> sourceImages.forEach(img -> {
                double weight = Double.parseDouble(Config.getProperty("laplacianWeight"));
                double[][] kernel = {
                        {0, 1, 0},
                        {1, -4, 1},
                        {0, 1, 0}
                };
                FilterOutput result = new LaplacianFilter(img, kernel, weight).filterImage();
                filteredImages.add(result.grayscaleOutput);
            });
        }
        return filteredImages;
    }

    private static BufferedImage getImage(String imgPath) {
        File imgFile = new File(imgPath);
        BufferedImage img;
        if (imgFile.exists()) {
            try {
                img = ImageIO.read(imgFile);
            } catch (IOException e) {
                System.err.println("Unable to read file as an Image: " + e.getMessage());
                return null;
            }
            return img;
        } else {
            System.err.println("File not found at: " + imgPath);
            return null;
        }
    }
}
