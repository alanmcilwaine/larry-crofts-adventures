package nz.ac.wgtn.swen225.lc.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * store all the images
 */
public enum Img{
    INSTANCE;

    // store all the image name with the type Image
    Map<String, Image> imageToName = new HashMap<>();

    Img(){
        loadImage();
    }

    /**
     * load all the images from a folder and save to the map.
     */
    public void loadImage() {
        int size = ImageImplement.IMAGE_SIZE;
       File imageFolder = new File("AllItemsImages");
       File[] imageFiles = imageFolder.listFiles();
        assert imageFiles != null;
        for(File file: imageFiles){
           try{
               Image originalImage = ImageIO.read(file);
               Image resizedImage = resizeImage(originalImage, size, size);
               imageToName.put(file.getName(), resizedImage);
           }
           catch (IOException e){
               throw new Error("Failed to load image: " + file.getName());
           }

       }
    }

    public Image resizeImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * get all the type Image through the name of the image
     */
    public Image getImgs(String name){
        return imageToName.get(name);
    }
}


