package ru.kfu.search;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.lucene.queryParser.ParseException;
import ru.kfu.search.postings.BytePostingsFile;
import ru.kfu.search.postings.PostingsFile;
import ru.kfu.search.util.NumberConverter;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class Main {

    public static  void main(String args[]) throws IOException, ParseException {
        //Create posting file
        PostingsFile pf = new PostingsFile("src\\main\\resources");
        File postingFile = pf.create();

        //Algorithm
        Properties prop = pf.loadValues(postingFile.getName());

        BytePostingsFile bpf = new BytePostingsFile();
        File bytePostingFile = bpf.create(prop);
        System.out.println(bytePostingFile);
        bpf.getNumbers(bytePostingFile);

//        NumberConverter converter = new NumberConverter();
//        Set<Object> keys = prop.keySet();
//        for(Object k:keys){
//            String key = (String)k;
//            String numbersStr = prop.getProperty(key);
//            List<Integer> numbers = converter.convertStringToInt(numbersStr);
//            byte[] bytes = VariableByteCoder.encode(numbers);
//
//            ////
//            String filename = "data\\postingFile\\byteFile.txt";
//            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));
//
//            System.out.println("original:" + numbers);
////            String decoded = new String(bytes, "ISO-8859-1");
////            System.out.println("decoded:" + decoded);
////            bw.write(key + "=" + decoded);
////            bw.newLine();
//
//            String stringToStore = new String(Base64.encode(bytes));
//            System.out.println("stringToStore:" + stringToStore);
//            byte[] restoredBytes = Base64.decode(stringToStore);
//            System.out.println("decoded:" + VariableByteCoder.decode(restoredBytes));


//            byte[] encoded = decoded.getBytes("ISO-8859-1");
//            System.out.println("encoded:" + java.util.Arrays.toString(encoded));
//
//            List<Integer> decryptedText = VariableByteCoder.decode(encoded);
//            System.out.println("decryptedText = "+decryptedText);
//            System.out.println();
//            BufferedOutputStream  bos = new BufferedOutputStream (new FileOutputStream(new File(filename)));
//            bos.write(("/n"+key+"=").getBytes());
//            bos.write(bytes);
            ////

//        }
    }

}
