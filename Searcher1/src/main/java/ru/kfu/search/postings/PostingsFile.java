package ru.kfu.search.postings;

import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.BytesRef;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class PostingsFile {

    private String postingsFileDir;

    public PostingsFile(String path){
        postingsFileDir=path;
    }

    public File create() throws IOException, ParseException {
        File postingFile = new File(postingsFileDir+File.separator+"postingsFile.properties");
        postingFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(postingFile));

        LuceneIndexManager lim = new LuceneIndexManager();
        lim.createIndex();
        Terms terms =lim.getTerms();
        TermsEnum iterator = terms.iterator(null);
        BytesRef byteRef = null;
        String term ="";
        while((byteRef = iterator.next()) != null) {
            term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
            bw.write(String.format("%s=%s", term, getString(lim.getDocIds(term))));
            bw.newLine();
        }

        bw.close();

        return postingFile;
    }

    private String getString(List<Integer> numbers){
        StringBuffer sb = new StringBuffer();
        for(Integer num : numbers){
            sb.append(num+" ");
        }
        return sb.toString();
    }

    public Properties loadValues(String postingFilePath) throws IOException {
        Properties prop = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(postingFilePath);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("Property file '" + postingFilePath + "' not found in the classpath");
        }
        return prop;
    }

}
