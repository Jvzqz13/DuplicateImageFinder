import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DuplicateFinder {
    public static void main(String[]args){

        File selectedFolder = new File("C:\\Users\\jvzqz\\OneDrive\\Pictures\\finder_example");

        File[] fileList = selectedFolder.listFiles();

        for (File file : fileList){
            String fileName = file.getName().toLowerCase();
            if(
                (
                 fileName.endsWith(".jpg") ||
                 fileName.endsWith(".jpeg") ||
                 fileName.endsWith(".png")
                ) &&
                 file.isFile()
            )
            {
                try{
                    // READS IMAGE
                    BufferedImage image = ImageIO.read(file);

                    // CREATES 8X8 EMPTY IMAGE
                    BufferedImage resizedImage = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);

                    //ALLOWS IMAGE TO BE CREATED IN RESIZEDIMAGE
                    Graphics2D graphics = resizedImage.createGraphics();
                    graphics.drawImage(image, 0,0, 8, 8,  null);
                    graphics.dispose();

                    int[] brightnessValues = new int[64];
                    int total = 0;



                    for(int y = 0; y < resizedImage.getHeight(); y++){
                        for (int x = 0; x < resizedImage.getWidth(); x++){
                            int index = (y * 8) + x;

                            int pixel = resizedImage.getRGB(x,y);
                            int red = (pixel >> 16) & 0xFF;
                            int green = (pixel >> 8) & 0xFF;
                            int blue = (pixel) & 0xFF;
                            int brightness = (red + green + blue) / 3;

                            brightnessValues[index] = brightness;

                        }
                    }
                    for (int brightness : brightnessValues){
                        total+= brightness;
                    }

                    int averageBrightness = total/64;

                    String fingerPrint = "";

                    //CREATES A FINGERPRINT OF THE IMAGE
                    for(int brightness : brightnessValues){
                        if(brightness >= averageBrightness){
                            fingerPrint += "1";
                        } else {
                            fingerPrint += "0";
                        }
                    }
                    System.out.println(file.getName() + " " + fingerPrint);


                }catch (IOException e){
                    System.out.println("Could not read: " + file.getName());

                }

            }
        }


    }
}
