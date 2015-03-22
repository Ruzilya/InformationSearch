package ru.kfu.search;

import org.apache.lucene.queryParser.ParseException;
import ru.kfu.search.postings.BytePostingsFile;
import ru.kfu.search.postings.LuceneIndexManager;
import ru.kfu.search.postings.PostingsFile;

import java.io.*;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class Main {

    public static void main(String args[]) throws IOException, ParseException {

        //Get test collection dir
        if (args.length < 1) {
            System.out.println("You should write path to directory with files to index. Try again.");
            System.exit(0);
        }

        String dir = "data";
        String filesToIndexDir = args[0];
        String indexDirectory = dir + File.separator + "indexes";

        LuceneIndexManager.setDocsDirPath(filesToIndexDir);
        LuceneIndexManager.setIndexDirPath(indexDirectory);

        //Create posting file
        PostingsFile pf = new PostingsFile(dir);
        File postingFile = pf.create(false, "postingFile");

        File shotPostingFile = pf.create(true, "postingFileShot");

        //Algorithm
        BytePostingsFile bpf = new BytePostingsFile(dir + File.separator + "bytePostingFile");
        File bytePostingFile = bpf.create(postingFile);

        //Shot
        BytePostingsFile bpfShot = new BytePostingsFile(dir + File.separator + "bytePostingFileShot");
        File bytePostingFileShot = bpfShot.create(shotPostingFile);

        //Create file with decoded data
        bpf.getNumbersIntoFile(bytePostingFile, dir + File.separator + "decodedData");

        //Shot
        bpfShot.getNumbersIntoFile(bytePostingFileShot, dir + File.separator + "decodedDataShot");

        System.out.println("All files in " + pf.getDir().getAbsolutePath());

    }

}
