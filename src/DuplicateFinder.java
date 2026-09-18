import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateFinder {
    public static void main(String[] args) {

        File selectedFolder = new File("C:\\Users\\jvzqz\\OneDrive\\Pictures\\finder_example");

        //GET ALL FILES FROM THE SELECTED FOLDER
        File[] fileList = selectedFolder.listFiles();

        //STORES EACH IMAGE FILE AND ITS GENERATED FINGERPRINT
        Map<File, String> fingerprints = new HashMap<>();

        //LOOPS THROUGH EVERY FILE IN THE FOLDER
        for (File file : fileList) {
            String fileName = file.getName().toLowerCase();
                // CHECKS THE FILE IS A SUPPORTED IMAGE TYPE
            if (
                    (
                            fileName.endsWith(".jpg") ||
                                    fileName.endsWith(".jpeg") ||
                                    fileName.endsWith(".png")
                    ) &&
                            file.isFile()
            ) {
                try {
                    // READS IMAGE
                    BufferedImage image = ImageIO.read(file);

                    // CREATES 8X8 EMPTY IMAGE
                    BufferedImage resizedImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);

                    //ALLOWS IMAGE TO BE CREATED IN RESIZEDIMAGE // RESIZES THE IMAGE INTO THE 8X8 IMAGE
                    Graphics2D graphics = resizedImage.createGraphics();
                    graphics.drawImage(image, 0, 0, 8, 8, null);
                    graphics.dispose();

                    //STORES THE BRIGHTNESS OF EACH OF THE 64 PIXELS
                    int[] brightnessValues = new int[64];
                    int total = 0;

                    // LOOPS THROUGH EVERY PIXEL IN THE 8X8 IMAGE
                    for (int y = 0; y < resizedImage.getHeight(); y++) {
                        for (int x = 0; x < resizedImage.getWidth(); x++) {

                            //CONVERTS X/Y POSITION INTO AN ARRAY INDEX
                            int index = (y * 8) + x;

                            // GETS RGB VALUES AND CALCULATES PIXEL BRIGHTNESS
                            int pixel = resizedImage.getRGB(x, y);
                            int red = (pixel >> 16) & 0xFF;
                            int green = (pixel >> 8) & 0xFF;
                            int blue = (pixel) & 0xFF;
                            int brightness = (red + green + blue) / 3;

                            brightnessValues[index] = brightness;

                        }
                    }

                    // ADDS ALL PIXEL BRIGHTNESS VALUES TOGETHER
                    for (int brightness : brightnessValues) {
                        total += brightness;
                    }

                    // CALCULATES THE IMAGES VALUES TOGETHER
                    int averageBrightness = total / 64;

                    String fingerPrint = "";

                    //CREATES A FINGERPRINT OF THE IMAGE BASED ON AVERAGE BRIGHTNESS
                    for (int brightness : brightnessValues) {
                        if (brightness >= averageBrightness) {
                            fingerPrint += "1";
                        } else {
                            fingerPrint += "0";
                        }
                    }

                    // STORES: IMAGE FILE -> FINGERPRINT
                    fingerprints.put(file, fingerPrint);

                } catch (IOException e) {
                    System.out.println("Could not read: " + file.getName());

                }
            }
        }
        // PRINTS HOW MANY IMAGE FINGERPRINTS WERE CREATED
        System.out.println(fingerprints.size());

        //LOOPS THROUGH THE ENTRIES - KEYS/VALUES USING MAP <<EXPERIMENT>>
//        for(Map.Entry<File, String> outerEntry : fingerprints.entrySet()){
//           for(Map.Entry<File, String> innerEntry: fingerprints.entrySet()){
//               System.out.println(
//                       outerEntry.getKey().getName() + " vs " + innerEntry.getKey().getName()
//               );
//           }
//       }

        //LOOPS THROUGH THE ENTRIES USING ARRAYS - KEYS/VALUES
        List<Map.Entry<File, String>> entries = new ArrayList<>(fingerprints.entrySet());
        for(int i = 0; i < entries.size(); i++){
            for(int j = i + 1; j < entries.size(); j++){
                String fingerPrint1 = entries.get(i).getValue();
                String fingerPrint2 = entries.get(j).getValue();

                int differences = 0;
                // LOOPS THROUGH THE CHAR IN FINGERPRINT
                for(int k = 0; k < fingerPrint1.length(); k++ ){
                    if( fingerPrint1.charAt(k) != fingerPrint2.charAt(k) ){
                        differences++;
                    }
                }
                if (differences == 0){
                    System.out.println(entries.get(i).getKey().getName() + " == " + entries.get(j).getKey().getName());
                }

//                System.out.println(entries.get(i).getKey().getName() + " vs " + entries.get(j).getKey().getName() + " = "+ differences );


            }

        }





     // / / / / / / / / / / / / / / END / / / / / / / / / / / / / / / / / / / / / / / / / / / /
    }
}
