package de.marcusvetter.ica.analyzer;

public class AnalyzerResult {

    private int allPixels;
    private int filteredPixels;

    public AnalyzerResult(int allPixels, int filteredPixels) {
        this.allPixels = allPixels;
        this.filteredPixels = filteredPixels;
    }

    public int getAllPixels() {
        return allPixels;
    }

    public int getFilteredPixels() {
        return filteredPixels;
    }

    public double getFilteredPixelInPercent() {
        return (double) this.getFilteredPixels() / (double) this.getAllPixels() * 100.0;
    }
}
