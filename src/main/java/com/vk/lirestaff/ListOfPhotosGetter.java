package com.vk.lirestaff;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@Component
public class ListOfPhotosGetter {

    public ArrayList<String> listOfIdFromSearch(String URL, int userId, String userPhotoFolderPath, String reIndex) throws IOException {

        IndexSearcher indexSearcher = null;
        File userFile;
        BufferedImage img;
        String fileName;

        File folder = new File(userPhotoFolderPath + userId);
        if (!folder.exists()) {
            boolean isFolderCreated = folder.mkdir();
        }

        try(InputStream in = new URL(URL).openStream()){
            try {
                Files.copy(in, Paths.get(userPhotoFolderPath + userId + "\\" + userId + "&1.png"));
            } catch (FileAlreadyExistsException faee) {
            }
        }

        File[] files = new File(userPhotoFolderPath + userId).listFiles();

        for (File file : files) {
            if (file.isFile()) {
                boolean isFileDeleted = file.delete();
            }
            fileName = userPhotoFolderPath + userId + "\\" + userId + "&1.png";
            img = ImageIO.read(new URL(URL));
            userFile = new File(fileName);

            if (!userFile.exists()) {
                boolean isFileCreated = userFile.createNewFile();
            }

            if (img != null) {
                ImageIO.write(img, "png", userFile);
            } else {
                return new ArrayList<>();
            }

            if (userFile.exists()) {
                indexSearcher = new IndexSearcher(fileName, reIndex);
            } else {
                return new ArrayList<>();
            }
        }
        if (null != indexSearcher) {
            System.out.println("IndexSearcher worked well :) ");
            return new ArrayList<>(indexSearcher.getIdList());
        } else {
            System.out.println("IndexSearcher is null :( ");
            return new ArrayList<>();
        }
    }
}
