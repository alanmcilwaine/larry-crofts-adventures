package nz.ac.wgtn.swen225.lc.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * a singleton used to store and manage all the images used in the game.
 * It loads images from a folder, resizes them, and stores them in a map for easy access.
 */
public enum Img{
    /**
     * one instance store all the images
     */
    INSTANCE;

    // stores image names as keys and corresponding Image objects as values.
    final Map<String, Image> imageToName = new HashMap<>();

    /**
     * initializes the Img enum and loads all the images when the instance is created.
     */
    Img(){
        loadImage();
    }

    /**
     * Loads all images from the "AllItemsImages" folder, resizes them to the required size,
     * and stores them in a map with their filenames as keys.
     *
     * @throws Error if an image fails to load.
     */
    public void loadImage() {
        int size = ImageImplement.IMAGE_SIZE;
        File imageFolder = new File("AllItemsImages");
        File[] imageFiles = imageFolder.listFiles();
        assert imageFiles != null;
        // load each image, resize each, put to the map with the image name as the key
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

    /**
     * Resizes the provided image to the suitable width and height.
     *
     * @param originalImage the original image to be resized.
     * @param width the desired width of the resized image.
     * @param height the desired height of the resized image.
     * @return the resized Image object.
     */
    public static Image resizeImage(Image originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    /**
     * Retrieves an image from the map by its filename.
     *
     * @param name the name of the image file to retrieve.
     * @return the corresponding Image object, or null if not found.
     */
    public Image getImgs(String name){
        return imageToName.get(name);
    }
}


