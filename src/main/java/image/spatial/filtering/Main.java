package image.spatial.filtering;

import image.spatial.filtering.util.ImageConversion;
import image.spatial.filtering.util.ImageExporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static String[] getInputImages() {
        ArrayList<String> inputImages = new ArrayList<>();
        boolean terminateInput = false;
        Scanner in = new Scanner(System.in);

        while (!terminateInput) {
            System.out.print("Please insert a file path (Press Enter to continue): ");
            String readInput = in.nextLine();
            if (readInput.isEmpty() || readInput.isBlank())
                if (!inputImages.isEmpty())
                    terminateInput = true;
                else
                    System.out.println("File path list cannot be empty, please insert at least one file path.");
            else
                inputImages.add(readInput);
        }
        return inputImages.toArray(new String[0]);
    }

    private static ImageConversion.ConversionOptions getImageConversionType() {
        Scanner in = new Scanner(System.in);
        ImageConversion.ConversionOptions[] supportedOptions = ImageConversion.ConversionOptions.values();

        System.out.println("Please select a supported image conversion method listed below:");
        for (int i = 1; i <= supportedOptions.length; i++) {
            System.out.println("\t" + i + ": " + supportedOptions[i - 1]);
        }
        int selection = in.nextInt() - 1;

        if (selection < 0 || selection >= supportedOptions.length) {
            System.out.println("Invalid Option\n");
            return getImageConversionType();
        }

        return supportedOptions[selection];
    }

    private static String getImageName(String fileName, ImageConversion.ConversionOptions imageConversionType) {
        String conversionProcedure = "_";

        switch (imageConversionType) {
            case GaussianFilter -> conversionProcedure += "gaussian";
            case MedianFilter -> conversionProcedure += "median";
            case LaplacianFilter -> conversionProcedure += "sharpen";
        }

        return fileName + conversionProcedure;
    }

    public static void main(String[] args) {
        String[] inputImagePaths = getInputImages();
        ImageConversion.ConversionOptions imageConversionType = getImageConversionType();
        ImageConversion conversion = new ImageConversion();

        conversion.convertImages(inputImagePaths, imageConversionType).forEach((img) -> {
            String fileName = inputImagePaths[0].split("[/.\\\\]")[inputImagePaths[0].split("[/.\\\\]").length - 2];
            String imgName = getImageName(fileName, imageConversionType);
            try {
                ImageExporter.exportImage(img, imgName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
