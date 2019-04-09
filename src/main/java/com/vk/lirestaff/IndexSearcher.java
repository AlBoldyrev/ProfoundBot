package com.vk.lirestaff;

import com.vk.constants.Constants;
import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class IndexSearcher {

    private ArrayList<String> idList = new ArrayList<>();

    public IndexSearcher(String imgPath, String reIndex) throws IOException {

        boolean passed = false;
        BufferedImage img = null;
        if (imgPath.length() > 0) {
            File f = new File(imgPath);
            if (f.exists()) {
                try {
                    img = ImageIO.read(f);
                    passed = true;
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        if (!passed) {
            System.out.println("        No image given as first argument. nameThread:" + Thread.currentThread().getName());
            System.out.println("        Run \"Searcher <query image>\" to search for <query image>.nameThread:" + Thread.currentThread().getName());
        }

        IndexReader ir = DirectoryReader.open(FSDirectory.open(Paths.get(reIndex)));
        ImageSearcher searcher = new GenericFastImageSearcher(30, CEDD.class);
        ImageSearchHits hits = searcher.search(img, ir);
        TreeMap<Double,Integer> map = new TreeMap<>();

        for (int i = 0; i < hits.length(); i++) {
            map.put(hits.score(i), hits.documentID(i));
        }
        for (Map.Entry m:map.entrySet()){
            String fileName = ir.document((int)m.getValue()).getValues(DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
            System.out.println(m.getKey() + ": \t" + fileName);
            File f = new File(fileName);
            idList.add(f.getName());
        }
    }
    public ArrayList<String> getIdList() {
        return idList;
    }
}
