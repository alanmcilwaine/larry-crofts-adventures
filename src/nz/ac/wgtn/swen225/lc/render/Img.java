import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * store all the images
 */
enum Img{
    CHAP("chap"),
    WALLTILE("wallTile"),
    FREETILE("freeTile"),
    KEY("key"),
    LOCKEDDOOR("lockedDoor"),
    INFOFIELD("infoField"),
    TREASURE("treasure"),
    EXITLOCK("exitLock"),
    EXIT("exit");

    private String imagePath;
    private Image image;
    private static List<Img> imgs = new ArrayList<>();
    Img(String imagePath){
        this.imagePath = imagePath;
        // construct the Image image in the constructor
        try{
            this.image = ImageIO.read(new File(imagePath));

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    static {
        // strore all the images in Img in list.
        Collections.addAll(imgs, Img.values());
    }
    public Image getImage(){return image;}
    public String getImageName(){return imagePath;}
    public static List<Img> getImgs(){return imgs;}
}


