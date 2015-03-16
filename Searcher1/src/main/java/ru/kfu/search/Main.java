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

    public static  void main(String args[]) throws IOException, ParseException {
        if(args.length<1){
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
        File postingFile = pf.create();

        //Algorithm
        BytePostingsFile bpf = new BytePostingsFile(dir+File.separator+"bytePostingFile");
        File bytePostingFile = bpf.create(postingFile);

        //Create file with decoded data
        bpf.getNumbersIntoFile(bytePostingFile, dir+File.separator+"decodedData");

        long pFileSizeInBytes = postingFile.length();
        long pFileSizeInMB = (pFileSizeInBytes / 1024) ;

        long bFileSizeInBytes = bytePostingFile.length();
        long bFileSizeInMB = (bFileSizeInBytes / 1024) ;

        System.out.println(String.format("Size of posting file[%s] is %s KB",postingFile.getAbsolutePath(), pFileSizeInMB));
        System.out.println(String.format("Size of posting file[%s] after decoding is %s KB",bytePostingFile.getAbsolutePath(), bFileSizeInMB));

    }

}
