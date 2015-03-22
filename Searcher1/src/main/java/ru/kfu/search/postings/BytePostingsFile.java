package ru.kfu.search.postings;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.log4j.Logger;
import ru.kfu.search.VariableByteCoder;
import ru.kfu.search.util.NumberConverter;

import java.io.*;
import java.util.List;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class BytePostingsFile {

    private static Logger LOG = Logger.getLogger(BytePostingsFile.class);

    private String filename;

    public BytePostingsFile(String fileName) {
        this.filename= fileName;
    }


    public File create(File postingFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(postingFile));

        NumberConverter converter = new NumberConverter();

        File resultByteFile = new File(filename);
        BufferedWriter bw = new BufferedWriter(new FileWriter(resultByteFile));

        String str;
        while ((str=br.readLine())!=null) {
            String key = str.substring(0, str.indexOf("="));
            String numbersStr = str.substring(str.indexOf("=") + 1);

            List<Integer> numbers = converter.convertStringToInt(numbersStr);

            byte[] bytes = VariableByteCoder.vbEncode(numbers);

            LOG.debug("Original numbers:" + numbers);

            String encoded = new String(Base64.encode(bytes));

            LOG.debug("Encoded bytes as string: "+encoded);

            bw.write(key + "=" + encoded);
            bw.newLine();
        }
        bw.close();
        return resultByteFile;
    }

    public File getNumbersIntoFile(File bytePostingFile, String filePath) throws IOException {
        File file = new File(filePath);

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));

        BufferedReader br = new BufferedReader(new FileReader(bytePostingFile));

        String str;
        while((str=br.readLine())!=null){
            String numbers = str.substring(str.indexOf("=")+1);
            List<Integer> nums = getNumbers(numbers);
            bw.write(str.substring(0, str.indexOf("="))+"="+PostingsFile.getString(nums));
            bw.newLine();
        }
        bw.close();
        return file;
    }

    public List<Integer> getNumbers(String decoded) throws UnsupportedEncodingException {
        byte[] encoded = Base64.decode(decoded);
        List<Integer> numbers = VariableByteCoder.vbDecode(encoded);
        LOG.debug("Decoded numbers: " + numbers);
        return numbers;
    }

}
