package ru.kfu.search.postings;

import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.BytesRef;

import java.io.*;
import java.util.List;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class PostingsFile {

    private String postingsFileDir;
    private File dir;

    public PostingsFile(String path){
        postingsFileDir=path;
        dir = new File(postingsFileDir);
    }

    public File create(boolean isShot, String postingFileName) throws IOException, ParseException {
        dir.mkdirs();
        File postingFile = new File(postingsFileDir+File.separator+postingFileName);

        postingFile.createNewFile();

        BufferedWriter bw = new BufferedWriter(new FileWriter(postingFile));

        LuceneIndexManager lim = new LuceneIndexManager();
        lim.createIndex();
        Terms terms =lim.getTerms();
        TermsEnum iterator = terms.iterator(null);
        BytesRef byteRef = null;
        String term ="";
        String numbers;
        while((byteRef = iterator.next()) != null) {
            term = new String(byteRef.bytes, byteRef.offset, byteRef.length);

            if(isShot){
                numbers =  getShotString(lim.getDocIds(term));
            }else{
                numbers = getString(lim.getDocIds(term));
            }

            bw.write(String.format("%s=%s", term, numbers));

            bw.newLine();
        }

        bw.close();

        return postingFile;

    }

    public static String getString(List<Integer> numbers){
        StringBuffer sb = new StringBuffer();
        for(Integer num : numbers){
            sb.append(num+" ");
        }
        return sb.toString();
    }

    public static String getShotString(List<Integer> numbers){
        StringBuffer sb = new StringBuffer();

        //First number
        if(numbers.size()>0){
            sb.append(numbers.get(0)+" ");
        }

        for(int i=1; i<numbers.size(); i++){
            sb.append(numbers.get(i) - numbers.get(i-1)+" ");
        }
        return sb.toString();
    }

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }
}
