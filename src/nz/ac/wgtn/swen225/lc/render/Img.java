import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * store all the images
 */
enum Img{
    INSTANCE;

    List<String> allImages;
    Map<String, Image> imageToName = new HashMap<>();

    Img(){
        allImages = List.of("chip", "Treasure", "keyRED", "keyBLUE", "freeTile", "wallTile", "exit", "lockedDoor");
        loadImage();
    }

    public void loadImage(){
        allImages.forEach(image -> {
            try {
                imageToName.put(image, ImageIO.read(new File(image)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Image getImgs(String name){
        return imageToName.getOrDefault(name, throwError(name));
    }

    private Image throwError(String imageName) {
        throw new RuntimeException("No such image");
    }
}


