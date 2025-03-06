# CSC 416 Assignment 03 #

## Environment ##
Language: Java 16+

Compiler: Maven


## Notes ##
- Tiff images not supported in markup so links to images are given instead.
- Parts of the program can be configured in [config.properties](config.properties)

## Gaussian Filter ##
Main logic for the Gaussian filtering process is located in the package
image.spatial.filtering.filters.GaussianFilter


Input image: [lenna-noise](images/lenna-noise.tif)

Console input: 

![lena!](images/report/gaussian_input.png)

Output image: [lenna-noise_gaussian](images/lenna-noise_gaussian.tiff)

## Laplacian Filter ##
Main logic for the Laplacian sharpening process is located in the package
image.spatial.filtering.filters.LaplacianFilter


Input image: [lenna-noise](images/lenna-noise.tif)

Console input:

![lena!](images/report/laplacian_input.png)

Output image: [lenna-noise_sharpen](images/lenna-noise_sharpen.tiff)

## Median Filter ##
Main logic for the Median filtering process is located in the package
image.spatial.filtering.filters.MedianFilter


Input image: [lenna-noise](images/lenna-noise.tif)

Console input:

![lena!](images/report/median_input.png)

Output image: [lenna-noise_median](images/lenna-noise_median.tiff)