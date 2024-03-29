/*
 * This file is part of the LIRE project: http://www.semanticmetadata.net/lire
 * LIRE is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LIRE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LIRE; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * We kindly ask you to refer the any or one of the following publications in
 * any publication mentioning or employing Lire:
 *
 * Lux Mathias, Savvas A. Chatzichristofis. Lire: Lucene Image Retrieval –
 * An Extensible Java CBIR Library. In proceedings of the 16th ACM International
 * Conference on Multimedia, pp. 1085-1088, Vancouver, Canada, 2008
 * URL: http://doi.acm.org/10.1145/1459359.1459577
 *
 * Lux Mathias. Content Based Image Retrieval with LIRE. In proceedings of the
 * 19th ACM International Conference on Multimedia, pp. 735-738, Scottsdale,
 * Arizona, USA, 2011
 * URL: http://dl.acm.org/citation.cfm?id=2072432
 *
 * Mathias Lux, Oge Marques. Visual Information Retrieval using Java and LIRE
 * Morgan & Claypool, 2013
 * URL: http://www.morganclaypool.com/doi/abs/10.2200/S00468ED1V01Y201301ICR025
 *
 * Copyright statement:
 * --------------------
 * (c) 2002-2013 by Mathias Lux (mathias@juggle.at)
 *     http://www.semanticmetadata.net/lire, http://www.lire-project.net
 */

package net.semanticmetadata.lire.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Some little helper methods.<br>
 * This file is part of the Caliph and Emir project: http://www.SemanticMetadata.net
 * <br>Date: 02.02.2006
 * <br>Time: 23:33:36
 *
 * @author Mathias Lux, mathias@juggle.at
 */
public class ImageUtils {
    /**
     * Scales down an image into a box of maxSideLength x maxSideLength.
     *
     * @param image         the image to scale down. It remains untouched.
     * @param maxSideLength the maximum side length of the scaled down instance. Has to be > 0.
     * @return the scaled image, the
     */
    public static BufferedImage scaleImage(BufferedImage image, int maxSideLength) {
        assert (maxSideLength > 0);
        double originalWidth = image.getWidth();
        double originalHeight = image.getHeight();
        double scaleFactor = 0.0;
        if (originalWidth > originalHeight) {
            scaleFactor = ((double) maxSideLength / originalWidth);
        } else {
            scaleFactor = ((double) maxSideLength / originalHeight);
        }
        // create new image
        BufferedImage img = new BufferedImage((int) (originalWidth * scaleFactor), (int) (originalHeight * scaleFactor), BufferedImage.TYPE_INT_RGB);
        // fast scale (Java 1.4 & 1.5)
        Graphics g = img.getGraphics();
//        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, img.getWidth(), img.getHeight(), null);
        return img;
    }

    /**
     * Scale image to an arbitrary shape not retaining proportions and aspect ratio.
     *
     * @param image
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        assert (width > 0 && height > 0);
        // create smaller image
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // fast scale (Java 1.4 & 1.5)
        Graphics g = img.getGraphics();
        g.drawImage(image, 0, 0, img.getWidth(), img.getHeight(), null);
        return img;
    }

    public static BufferedImage cropImage(BufferedImage image, int fromX, int fromY, int width, int height) {
        assert (width > 0 && height > 0);
        // create smaller image
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // fast scale (Java 1.4 & 1.5)
        Graphics g = img.getGraphics();
        g.drawImage(image, fromX, fromY, img.getWidth(), img.getHeight(), null);
        return img;
    }

    /**
     * Converts an image to grey. Use instead of color conversion op, which yields strange results.
     *
     * @param image
     */
    public static BufferedImage convertImageToGrey(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        result.getGraphics().drawImage(image, 0, 0, null);
        return result;
    }

    /**
     * Inverts a grey scale image.
     *
     * @param image
     */
    public static void invertImage(BufferedImage image) {
        WritableRaster inRaster = image.getRaster();
        int[] p = new int[3];
        float v = 0;
        for (int x = 0; x < inRaster.getWidth(); x++) {
            for (int y = 0; y < inRaster.getHeight(); y++) {
                inRaster.getPixel(x, y, p);
                p[0] = 255 - p[0];
                inRaster.setPixel(x, y, p);
            }
        }
    }

    /**
     * Converts an image to a standard internal representation.
     * Taken from OpenIMAJ. Thanks to these guys!
     * http://sourceforge.net/p/openimaj
     *
     * @param bimg
     * @return
     */
    public static BufferedImage createWorkingCopy(BufferedImage bimg) {
        BufferedImage image;
        if (bimg.getType() == BufferedImage.TYPE_INT_RGB) {
            image = bimg;
        } else {
            image = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(bimg, null, 0, 0);
        }
        return image;
    }

    /**
     * Trims the white (and respective the black) border around an image.
     *
     * @param img
     * @return a new image, hopefully trimmed.
     */
    public static BufferedImage trimWhiteSpace(BufferedImage img) {
        // idea is to scan lines of an image starting from each side.
        // As soon as a scan line encounters non-white (or non-black) pixels we know there is actual image content.
        WritableRaster raster = img.getRaster();
        boolean hasWhite = true;
        int ymin = 0, ymax = raster.getHeight() - 1, xmin = 0, xmax = raster.getWidth() - 1;
        int[] pixels = new int[3 * raster.getWidth()];
        int thresholdWhite = 250;
        int thresholdBlack = 5;
        while (hasWhite) {
            raster.getPixels(0, ymin, raster.getWidth(), 1, pixels);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] < thresholdWhite && pixels[i] > thresholdBlack) hasWhite = false;
            }
            if (hasWhite) ymin++;
        }
        hasWhite = true;
        while (hasWhite && ymax > ymin) {
            raster.getPixels(0, ymax, raster.getWidth(), 1, pixels);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] < thresholdWhite && pixels[i] > thresholdBlack) hasWhite = false;
            }
            if (hasWhite) ymax--;
        }
        pixels = new int[3 * raster.getHeight()];
        hasWhite = true;
        while (hasWhite) {
            raster.getPixels(xmin, 0, 1, raster.getHeight(), pixels);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] < thresholdWhite && pixels[i] > thresholdBlack) hasWhite = false;
            }
            if (hasWhite) xmin++;
        }
        hasWhite = true;
        while (hasWhite && xmax > xmin) {
            raster.getPixels(xmax, 0, 1, raster.getHeight(), pixels);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] < thresholdWhite && pixels[i] > thresholdBlack) hasWhite = false;
            }
            if (hasWhite) xmax--;
        }
        BufferedImage result = new BufferedImage(xmax - xmin, ymax - ymin, BufferedImage.TYPE_INT_RGB);
        result.getGraphics().drawImage(img, 0, 0, result.getWidth(), result.getHeight(),
                xmin, ymin, xmax, ymax, null);
        return result;
    }

    /** creates a Gaussian kernel for ConvolveOp for blurring an image
     *
     * @param radius the radius, i.e. 5
     * @param sigma  sigma, i.e. 1.4f
     * @return
     */
    public static float[] makeGaussianKernel(int radius, float sigma) {
            float[] kernel = new float[radius * radius];
            float sum = 0;
            for (int y = 0; y < radius; y++) {
                for (int x = 0; x < radius; x++) {
                    int off = y * radius + x;
                    int xx = x - radius / 2;
                    int yy = y - radius / 2;
                    kernel[off] = (float) Math.pow(Math.E, -(xx * xx + yy * yy)
                            / (2 * (sigma * sigma)));
                    sum += kernel[off];
                }
            }
            for (int i = 0; i < kernel.length; i++)
                kernel[i] /= sum;
            return kernel;
    }

}
