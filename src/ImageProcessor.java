import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ImageProcessor {
    private Image inImage = null;
    private Image keyImage = null;
    private String rawText = null;
    private File outputFile = null;
    private static final char[] charLibrary = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890 \n!@#$%^&*()-=_+[]{}:;,./<>?`~|\\'\"".toCharArray();

    public void setInputFile(File input) throws IOException {
        this.inImage = new Image(input);
    }

    public void setKeyImage(File input) throws IOException {
        this.keyImage = new Image(input);
    }
    
    public void setInputText(String input) throws SizeException {
        this.rawText = input;
    }
    
    public void setOutputFile(File outputFile, String outputFileName, String type) {
        if (!outputFileName.contains(type)) {
            outputFileName += type;
        }
        String outputFolderPath = outputFile.getAbsolutePath();
        this.outputFile = new File(outputFolderPath+"\\"+outputFileName);
    }

    public void encode() throws IOException, SizeException, CharacterException {
        if (rawText.length() > inImage.getCharLimit()) {
            throw new SizeException("");
        }
        int[][] selectedPixels = inImage.getRandomPixels(this.rawText.length());
        int width = this.inImage.xLength;
        int height = this.inImage.yLength;
        BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                outImage.setRGB(x,y ,this.inImage.getIntRGB(x,y));
            }
        }
        int x, y;
        int[] RGB;
        for (int i=0; i<this.rawText.length(); i++) {
            x = selectedPixels[i][0];
            y = selectedPixels[i][1];
            RGB = this.inImage.getRGB(x, y);
            outImage.setRGB(x, y, charToRGB(this.rawText.charAt(i), RGB));

        }
        ImageIO.write(outImage, "png", this.outputFile);
    }

    public void decode() throws IOException {
        int[] RGB, compRGB, diffRGB = new int[3];
        int totalDiff;
        StringBuilder output = new StringBuilder();
        for (int x=0; x<this.inImage.xLength; x++) {
            for (int y=0; y<this.inImage.yLength; y++) {
                RGB = this.inImage.getRGB(x, y);
                compRGB = this.keyImage.getRGB(x, y);
                totalDiff = 0;
                for (int i=0; i<3; i++) {
                    diffRGB[i] = diff(RGB[i], compRGB[i]);
                    totalDiff += diffRGB[i];
                }
                if (totalDiff != 0) {
                    try {
                        output.append(RGBtoChar(diffRGB));
                    } catch (Exception e) {
                        System.out.print(Arrays.toString(diffRGB));
                    }
                }
            }
        }
        FileOutputStream outFile = new FileOutputStream(outputFile.getAbsoluteFile());
        PrintWriter fileWriter = new PrintWriter(outFile);
        fileWriter.println(output);
        fileWriter.close();
        outFile.close();
    }

    private static String RGBtoChar(int[] diffRGB) {
        int charIndex = (diffRGB[0]*10) + (diffRGB[1]*5) + diffRGB[2];
        if (charIndex == 0) {
            return "";
        }
        charIndex--;
        return Character.toString(charLibrary[charIndex]);
    }

    private static int charToRGB(char input, int[] RGB) throws CharacterException {
        int index=-1;
        for (int i=0; i < charLibrary.length; i++) {
            if (input == charLibrary[i]) {
                index = i;
            }
        }
        if (index == -1) {
            throw new CharacterException(input);
        }
        index++;
        int remainder, red, green, blue;
        if (index >= 10) {
            remainder = index % 10;
            red = (index - remainder) / 10;
            index = remainder;
        } else {
            red = 0;
        }
        if (index >= 5) {
            remainder = index % 5;
            green = (index - remainder) / 5;
            index = remainder;
        } else {
            green = 0;
        }
        blue = index;
        int[] offsets = new int[]{red, green, blue};
        for (int i=0; i<3; i++) {
            RGB[i] += offsets[i];
            if (RGB[i] > 255) {RGB[i] -= 2*offsets[i];}
        }
        return 65536*RGB[0] + 256*RGB[1] + RGB[2];
    }

    private static int diff(int int1, int int2) {
        int output = int1 - int2;
        if (output < 0) {
            output *= -1;
        }
        return output;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public static class SizeException extends Exception {
        public SizeException(String message) {
            super(message);
        }
    }
    
    public static class CharacterException extends Exception {
        private char input;
        public CharacterException(char input) {
            super("");
            this.input = input;
        }

        @Override
        public String toString() {
            return "Character: \"" + input + "\" not in character library";
        }
    }
}
