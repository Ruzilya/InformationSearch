package ru.kfu.search;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by RuzilyaS on 13-Mar-15.
 */
public class App {

    public static void main( String[] args ) throws IOException {
        System.out.println( "№1:" );
        VariableByteCoder coder = new VariableByteCoder();
        List<Integer> numbers = new LinkedList<>();
        numbers.add(864);
        numbers.add(13);
        numbers.add(14);
        numbers.add(12);
        System.out.println("numbers before = "+numbers);
        byte[] bytes =  coder.encode(numbers);
        System.out.println("bytes size = "+bytes.length+", bytes"+bytes);
        List<Integer> decodedNumbers = coder.decode(bytes);
        System.out.println("numbers after = "+decodedNumbers);

        System.out.println( "№2:" );
        //write
        Properties properties = new Properties();
        properties.setProperty("first", bytes.toString());
        System.out.println("bytes to string = "+bytes.toString());

        File file = new File("src\\main\\resources\\test2.properties");
        FileOutputStream fileOut = new FileOutputStream(file);
        properties.store(fileOut, "Word positions file");
        fileOut.close();

        //read
        Properties prop = new Properties();
        String propFileName = "test2.properties";

        InputStream inputStream = (new App()).getInputStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
        String encodedString = prop.getProperty("first");
        List<Integer> decodedNumbers2 = coder.decode(encodedString.getBytes());
        System.out.println("numbers after = "+decodedNumbers2);

    }

    private InputStream getInputStream(String propFileName){
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        return inputStream;
    }
}
