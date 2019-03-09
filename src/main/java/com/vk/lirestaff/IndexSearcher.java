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

    private String imgPath;
    private BufferedImage img = null;
    private boolean passed = false;
    private ArrayList<String> idList = new ArrayList<>();

    public IndexSearcher(String imgPath) throws IOException {
        this.imgPath = imgPath;

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
            System.out.println("        No image given as first argument. nameThread:"+Thread.currentThread().getName());
            System.out.println("        Run \"Searcher <query image>\" to search for <query image>.nameThread:"+Thread.currentThread().getName());
//            System.exit(1);
        }else{
            System.out.println("        img is good! nameThread: "+ Thread.currentThread().getName());
        }

        IndexReader ir = DirectoryReader.open(FSDirectory.open(Paths.get(Constants.indexPath)));
        ImageSearcher searcher = new GenericFastImageSearcher(30, CEDD.class);
        // ImageSearcher searcher = new GenericFastImageSearcher(30, AutoColorCorrelogram.class); // for another image descriptor ...

        /*
            If you used DocValues while Indexing, use the following searcher:
         */
        // ImageSearcher searcher = new GenericDocValuesImageSearcher(30, CEDD.class, ir);

        // searching with a image file ...
        ImageSearchHits hits = searcher.search(img, ir);
        // searching with a Lucene document instance ...
        // ImageSearchHits hits = searcher.search(ir.document(0), ir);


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
