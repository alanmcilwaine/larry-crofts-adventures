package nz.ac.wgtn.swen225.lc.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * store all the images
 */
enum Img{
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
       File imageFolder = new File("src/nz/ac/wgtn/swen225/lc/render/AllItemsImages");
       File[] imageFiles = imageFolder.listFiles();
        assert imageFiles != null;
        for(File file: imageFiles){
           try{
               imageToName.put(file.getName(), ImageIO.read(file));
           }
           catch (IOException e){
               throw new Error("Failed to load image: " + file.getName());
           }

       }
    }

    /**
     * get all the type Image through the name of the image
     */
    public Image getImgs(String name){
        return imageToName.getOrDefault(name, throwError(name));
    }

    /**
     * throw the error if does not find the image in map.
     */
    private Image throwError(String name) {
        throw new RuntimeException("Image not found: " + name);
    }
}


