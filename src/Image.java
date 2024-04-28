import java.awt.Color;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.*;
import java.util.Arrays;
import java.util.Random;

public class Image {

    private static final Random rand = new Random();
    private final Pixel[][] pixelMatrix;
    private final int charLimit;
    int xLength;
    int yLength;

    //initializing an image in the form of a 2d array of RGB Pixels
    public Image(File inputFile) throws IOException {
        BufferedImage rawImage = ImageIO.read(inputFile);
        xLength = rawImage.getWidth();
        yLength = rawImage.getHeight();
        charLimit = xLength * yLength;
        pixelMatrix = new Pixel[xLength][yLength];
        Color c;
        for (int x=0; x < xLength; x++) {
            for (int y=0; y < yLength; y++) {
                c = new Color(rawImage.getRGB(x, y));
                pixelMatrix[x][y] = new Pixel(c.getRed(), c.getGreen(), c.getBlue());
            }
        }
    }

    //getters
    public int getCharLimit() {return charLimit;}
    public int[] getRGB(int x, int y) {
        return pixelMatrix[x][y].getRGB();
    }
    public int getIntRGB(int x, int y) {
        return pixelMatrix[x][y].getInt();
    }

    public int[][] getRandomPixels(int numCords) {
        int[][] output = new int[numCords][2];
        int x, y;
        for (int i=0; i < numCords; i++) {
            do {
                x = rand.nextInt(0, xLength); //xLength
                y = rand.nextInt(0, yLength); //yLength
            } while (cordsInArray(new int[]{x,y}, output));
            output[i][0] = x;
            output[i][1] = y;
        }
        Arrays.sort(output, (cord1, cord2) -> {
            if (cord1[0] == cord2[0]) {
                return Integer.compare(cord1[1], cord2[1]);
            } else {
                return Integer.compare(cord1[0], cord2[0]);
            }
        });
        return output;
    }
    public static boolean cordsInArray(int[] inputCords, int[][] array) {
        boolean cordsFound = false;
        for (int[] cords : array) {
            if (cords[0] == inputCords[0] && cords[1] == inputCords[1]) {
                cordsFound = true;
                break;
            }
        }
        return cordsFound;
    }

    //Pixel used in image array, holds RGB values
    public static class Pixel {
        private final int red;
        private final int green;
        private final int blue;

        public Pixel(int r, int g, int b) {
            this.red = r;
            this.green = g;
            this.blue = b;
        }

        //turn RGB values into a bufferedImage compatible int value
        public int getInt() {
            return 65536*this.red + 256*this.green + this.blue;
        }

        public int[] getRGB() {
            return new int[]{this.red, this.green, this.blue};
        }
    }
}

