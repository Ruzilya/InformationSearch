package ru.kfu.search.util;

import org.apache.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class NumberConverter {

    private static Logger LOG = Logger.getLogger(NumberConverter.class);

    public List<Integer> convertStringToInt(String numbersStr){
        List<Integer> numbers = new LinkedList<>();
        String[] numbersArr = numbersStr.trim().split(" ");
        for(String num : numbersArr){
            Integer number = new Integer(0);
            try{
                number = Integer.valueOf(num);
            }catch(NumberFormatException e){

            }
            numbers.add(number);
        }
        return numbers;
    }
}
