package de.marcusvetter.ica.analyzer;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AnalyzerTest {

    private BufferedImage readFile(String fileName) throws IOException {
        return ImageIO.read(this.getClass().getResourceAsStream("/" + fileName));
    }

    @Test
    public void blackImageGreaterThan() throws IOException {
        BufferedImage image = this.readFile("4xG0.jpg");
        AnalyzerResult allMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.GREATER_THAN, -1);
        AnalyzerResult nonMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.GREATER_THAN, 0);

        assertEquals(allMatchesResult.getAllPixels(), 4);
        assertEquals(allMatchesResult.getFilteredPixels(), 4);

        assertEquals(nonMatchesResult.getAllPixels(), 4);
        assertEquals(nonMatchesResult.getFilteredPixels(), 0);
    }

    @Test
    public void blackImageLessThan() throws IOException {
        BufferedImage image = this.readFile("4xG0.jpg");
        AnalyzerResult allMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.LESS_THAN, 1);
        AnalyzerResult nonMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.LESS_THAN, 0);

        assertEquals(allMatchesResult.getAllPixels(), 4);
        assertEquals(allMatchesResult.getFilteredPixels(), 4);

        assertEquals(nonMatchesResult.getAllPixels(), 4);
        assertEquals(nonMatchesResult.getFilteredPixels(), 0);
    }

    @Test
    public void blackImageEquals() throws IOException {
        BufferedImage image = this.readFile("4xG0.jpg");
        AnalyzerResult allMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.EQUALS, 0);
        AnalyzerResult nonMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.EQUALS, 1);

        assertEquals(allMatchesResult.getAllPixels(), 4);
        assertEquals(allMatchesResult.getFilteredPixels(), 4);

        assertEquals(nonMatchesResult.getAllPixels(), 4);
        assertEquals(nonMatchesResult.getFilteredPixels(), 0);
    }

    @Test
    public void blackWhiteGreaterThan() throws IOException {
        BufferedImage image = this.readFile("2xG0-2xG255.jpg");
        AnalyzerResult allMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.GREATER_THAN, -1);
        AnalyzerResult whiteMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.GREATER_THAN, 254);
        AnalyzerResult nonMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.GREATER_THAN, 255);

        assertEquals(allMatchesResult.getAllPixels(), 4);
        assertEquals(allMatchesResult.getFilteredPixels(), 4);

        assertEquals(whiteMatchesResult.getAllPixels(), 4);
        assertEquals(whiteMatchesResult.getFilteredPixels(), 2);

        assertEquals(nonMatchesResult.getAllPixels(), 4);
        assertEquals(nonMatchesResult.getFilteredPixels(), 0);
    }

    @Test
    public void blackWhiteLessThan() throws IOException {
        BufferedImage image = this.readFile("2xG0-2xG255.jpg");
        AnalyzerResult allMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.LESS_THAN, 256);
        AnalyzerResult blackMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.LESS_THAN, 1);
        AnalyzerResult nonMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.LESS_THAN, 0);

        assertEquals(allMatchesResult.getAllPixels(), 4);
        assertEquals(allMatchesResult.getFilteredPixels(), 4);

        assertEquals(blackMatchesResult.getAllPixels(), 4);
        assertEquals(blackMatchesResult.getFilteredPixels(), 2);

        assertEquals(nonMatchesResult.getAllPixels(), 4);
        assertEquals(nonMatchesResult.getFilteredPixels(), 0);
    }

    @Test
    public void blackWhiteEquals() throws IOException {
        BufferedImage image = this.readFile("2xG0-2xG255.jpg");
        AnalyzerResult blackMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.EQUALS, 0);
        AnalyzerResult whiteMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.EQUALS, 255);
        AnalyzerResult nonMatchesResult = new Analyzer().countPixel(image, Analyzer.Color.GREEN, Analyzer.FilterType.EQUALS, 42);

        assertEquals(blackMatchesResult.getAllPixels(), 4);
        assertEquals(blackMatchesResult.getFilteredPixels(), 2);

        assertEquals(whiteMatchesResult.getAllPixels(), 4);
        assertEquals(whiteMatchesResult.getFilteredPixels(), 2);

        assertEquals(nonMatchesResult.getAllPixels(), 4);
        assertEquals(nonMatchesResult.getFilteredPixels(), 0);
    }

}
