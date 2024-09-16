import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum anotherImg {
    INSTANCE;

    List<String> allImages;
    Map<String, Image> imageToName = new HashMap<>();

    anotherImg(){
        allImages = List.of("chip", "Treasure", "keyRED", "keyBLUE", "freeTile", "wallTile", "exit", "lockedDoor");
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


}
