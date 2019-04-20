package com.vk.lirestaff;

import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Indexer {

    private String imgFolderPath;

    public Indexer(String imgFolderPath,String pathForIndex) throws IOException {

        this.imgFolderPath = imgFolderPath;
        boolean passed = false;

        if (imgFolderPath.length() > 0) {
            File f = new File(imgFolderPath);
            if (f.exists() && f.isDirectory()) {
                passed = true;
            }
        }

        if (!passed) {

            System.exit(1);
        }
        ArrayList<String> images = FileUtils.getAllImages(new File(imgFolderPath), true);
        GlobalDocumentBuilder globalDocumentBuilder = new GlobalDocumentBuilder(CEDD.class);
        IndexWriter iw = LuceneUtils.createIndexWriter(pathForIndex, true, LuceneUtils.AnalyzerType.WhitespaceAnalyzer);

        for (Iterator<String> it = images.iterator(); it.hasNext(); ) {
            String imageFilePath = it.next();
            try {
                BufferedImage img = ImageIO.read(new FileInputStream(imageFilePath));
                Document document = globalDocumentBuilder.createDocument(img, imageFilePath);
                iw.addDocument(document);
            } catch (Exception e) {
                System.err.println("Error reading image or indexing it.");
                e.printStackTrace();
            }
        }
        LuceneUtils.closeWriter(iw);
    }



}