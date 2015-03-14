package ru.kfu.search.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by RuzilyaS on 14-Mar-15.
 */
public class NumberConverter {

    public List<Integer> convertStringToInt(String numbersStr){
        List<Integer> numbers = new LinkedList<>();
        String[] numbersArr = numbersStr.trim().split(" ");
        for(String num : numbersArr){
            Integer number = Integer.valueOf(num);
            numbers.add(number);
        }
        return numbers;
    }
}
