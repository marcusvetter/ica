package de.marcusvetter.ica.analyzer;

import java.awt.image.BufferedImage;

public class Analyzer {

    private boolean filterCondition(FilterType filterType, int filterValue, int pixel) {
        if (filterType == FilterType.EQUALS) {
            return pixel == filterValue;
        } else if (filterType == FilterType.GREATER_THAN) {
            return pixel > filterValue;
        } else {
            return pixel < filterValue;
        }
    }

    private boolean filterPixel(int pixel, Color color, FilterType filterType, int filterValue) {
        if (color == Color.RED) {
            int red = (pixel >> 16) & 0xff;
            return filterCondition(filterType, filterValue, red);
        } else if (color == Color.GREEN) {
            int green = (pixel >> 8) & 0xff;
            return filterCondition(filterType, filterValue, green);
        } else {
            int blue = (pixel) & 0xff;
            return filterCondition(filterType, filterValue, blue);
        }
    }

    public AnalyzerResult countPixel(BufferedImage image, Color color, FilterType filterType, int filterValue) {
        int w = image.getWidth();
        int h = image.getHeight();
        int countPixelBelowSpecified = 0;

        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int pixel = image.getRGB(col, row);
                if (filterPixel(pixel, color, filterType, filterValue)) {
                    countPixelBelowSpecified++;
                }
            }
        }

        return new AnalyzerResult(w * h, countPixelBelowSpecified);
    }

    public enum Color {
        RED("Red"), GREEN("Green"), BLUE("Blue");

        private String viewValue;

        Color(String viewValue) {
            this.viewValue = viewValue;
        }

        @Override
        public String toString() {
            return viewValue;
        }
    }

    public enum FilterType {
        EQUALS("Equals"), LESS_THAN("Less then"), GREATER_THAN("Greater than");

        private String viewValue;

        FilterType(String viewValue) {
            this.viewValue = viewValue;
        }

        @Override
        public String toString() {
            return viewValue;
        }
    }
}