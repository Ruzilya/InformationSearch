package ru.kfu.search.postings;

import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class PostingsFile {

    private String postingsFilePath;
    private String indexDirPath;

    public PostingsFile(String path, String indexDirPath){
        postingsFilePath=path;
        this.indexDirPath = indexDirPath;
    }

    public File create() throws IOException, ParseException {
        File file = new File(postingsFilePath);
        LuceneIndexManager lim = new LuceneIndexManager();
        lim.createIndex();
        Terms terms =lim.getTerms();
        TermsEnum iterator = terms.iterator(null);
        BytesRef byteRef = null;
        while((byteRef = iterator.next()) != null) {
            String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
            System.out.println(term);
            System.out.println(lim.getDocIds(term));
            System.out.println();
        }

        return file;
    }

    public static  void main(String args[]) throws IOException, ParseException {
        PostingsFile pf = new PostingsFile("data\\postingFile", "data\\indexDirectory");
        pf.create();
    }
}
