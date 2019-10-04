import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Driver {
    private static final String COMMA = ",";
    private static final String HASH = "#";
    private static final String RESOURCES_PATH = "src/main/resources";

    public static void main(String args[]) {
        try (BufferedReader br = new BufferedReader(new FileReader(RESOURCES_PATH + "/input.txt"));
             FileWriter writer = new FileWriter(RESOURCES_PATH + "/output.txt")) {
            String url;
            while ((url = br.readLine()) != null) {
                Map<String, Integer> occurrences = getColorOccurrences(url);
                String result = topThreeColors(occurrences, url);

                // if at least 1 color column has a color, but other colors are null, the image had fewer than 3 colors.
                // if all colors are 'null', image was most likely not found.
                // alternative to writing data for image not found, could avoid writing the data
                // and have a separate 'exceptions.txt' file to figure out what happened and decide how to proceed.
                writer.append(result).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Map<String, Integer> getColorOccurrences(String url) {
        Map<String, Integer> occurrences = new HashMap<>();

        try (InputStream in = new URL(url).openStream()) {
            BufferedImage image = ImageIO.read(in);
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    Color color = new Color(image.getRGB(x, y));
                    StringBuilder hexFormat = new StringBuilder(Integer.toHexString(color.getRGB()).toUpperCase());

                    // remove alpha information
                    hexFormat.delete(0, 2);

                    // prepend missing 0's
                    while (hexFormat.length() < 6) {
                        hexFormat.insert(0, "0");
                    }

                    hexFormat.insert(0, HASH);

                    occurrences.compute(hexFormat.toString(), (k, v) -> v == null ? 1 : v + 1);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return occurrences;
    }

    private static String topThreeColors(Map<String, Integer> occurences, String url) {
        String firstHex = null;
        int firstOccur = 0;

        String secondHex = null;
        int secondOccur = 0;

        String thirdHex = null;
        int thirdOccur = 0;

        for (Map.Entry<String, Integer> e : occurences.entrySet()) {
            String hex = e.getKey();
            Integer o = e.getValue();
            if (o > firstOccur) {
                thirdHex = secondHex;
                thirdOccur = secondOccur;

                secondHex = firstHex;
                secondOccur = firstOccur;

                firstHex = hex;
                firstOccur = o;
            } else if (o > secondOccur) {
                thirdHex = secondHex;
                thirdOccur = secondOccur;

                secondHex = hex;
                secondOccur = o;
            } else if (o > thirdOccur) {
                thirdHex = hex;
                thirdOccur = o;
            }
        }

        StringBuilder result = new StringBuilder();
        result.append(url).append(COMMA).append(firstHex).append(COMMA).append(secondHex).append(COMMA).append(thirdHex);

        return result.toString();
    }
}
