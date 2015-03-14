package ru.kfu.search.postings;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import ru.kfu.search.VariableByteCoder;
import ru.kfu.search.util.NumberConverter;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class BytePostingsFile {

    public BytePostingsFile() {

    }

    public File create(Properties prop) throws IOException {
        Set<Object> keys = prop.keySet();
        NumberConverter converter = new NumberConverter();
        String filename = "data\\postingFile\\byteFile.txt";
        File resultByteFile = new File(filename);
        BufferedWriter bw = new BufferedWriter(new FileWriter(resultByteFile));
        for (Object k : keys) {
            String key = (String) k;
            String numbersStr = prop.getProperty(key);
            List<Integer> numbers = converter.convertStringToInt(numbersStr);
            byte[] bytes = VariableByteCoder.encode(numbers);

            System.out.println("original:" + numbers);
            String decoded = new String(Base64.encode(bytes));
            System.out.println("decoded:" + decoded);
            bw.write(key + "=" + decoded);
            bw.newLine();
        }
        bw.close();
        return resultByteFile;
    }

    public void getNumbers(File bytePostingFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(bytePostingFile));
        String str;
        List<Integer> numbers;
        while((str=br.readLine())!=null){
            String nums = str.substring(str.indexOf("=")+1);
            numbers = getNumbers(nums);
            System.out.println(numbers);
        }
    }

    public List<Integer> getNumbers(String decoded) throws UnsupportedEncodingException {
        byte[] encoded = Base64.decode(decoded);
        List<Integer> numbers = VariableByteCoder.decode(encoded);
        System.out.println("decryptedText = " + numbers);
        return numbers;
    }

}
