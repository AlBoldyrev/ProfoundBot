package com.vk.lirestaff;

import net.semanticmetadata.lire.builders.GlobalDocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.utils.FileUtils;
import net.semanticmetadata.lire.utils.LuceneUtils;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Indexer {

    private String imgFolderPath;


    private Logger log = LoggerFactory.getLogger(Indexer.class);

    public Indexer(String imgFolderPath,String pathForIndex) {

        this.imgFolderPath = imgFolderPath;
        boolean passed = false;
        ArrayList<String> images = null;
        GlobalDocumentBuilder globalDocumentBuilder = null;
        IndexWriter iw = null;
        if (imgFolderPath.length() > 0) {
            File f = new File(imgFolderPath);
            if (f.exists() && f.isDirectory()) {
                passed = true;
            }
        }

        if (!passed) {
            log.error("Before this log was system.exit(1). Some terrible mistake!");
            /*System.exit(1);*/
        }
        try {
            images = FileUtils.getAllImages(new File(imgFolderPath), true);
            globalDocumentBuilder = new GlobalDocumentBuilder(CEDD.class);
            iw = LuceneUtils.createIndexWriter(pathForIndex, true, LuceneUtils.AnalyzerType.WhitespaceAnalyzer);
        } catch (IOException ioe) {
            log.error("Can not operate with file system at the local computer" + ioe.getStackTrace());
        }
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
        try {
            LuceneUtils.closeWriter(iw);
        } catch (IOException ioe) {
            log.error("Can not close writer whatever it means!!!!" + ioe.getStackTrace());
        }
    }



}