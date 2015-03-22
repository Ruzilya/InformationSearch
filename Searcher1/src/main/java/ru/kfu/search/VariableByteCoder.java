package ru.kfu.search;

import java.nio.ByteBuffer;
import java.util.*;

import static java.lang.Math.log;

/**
 * Created by RuzilyaS on 13-Mar-15.
 */
public class VariableByteCoder {

    public static byte[] vbEncodeNumber(int n) {
        if (n == 0) {
            return new byte[]{0};
        }
        int i = (int) (log(n) / log(128)) + 1;
        byte[] bytes = new byte[i];
        int j = i - 1;
        do {
            bytes[j--] = (byte) (n % 128);
            n /= 128;
        } while (j >= 0);
        bytes[i - 1] += 128;
        return bytes;
    }

    public static byte[] vbEncode(List<Integer> numbers) {
        ByteBuffer buf = ByteBuffer.allocate(numbers.size() * 4);
        for (Integer number : numbers) {
            buf.put(vbEncodeNumber(number));
        }
        buf.flip();
        byte[] bytes = new byte[buf.limit()];
        buf.get(bytes);
        return bytes;
    }

    public static List<Integer> vbDecode(byte[] byteStream) {
        List<Integer> numbers = new ArrayList<Integer>();
        int n = 0;
        for (byte b : byteStream) {
            if ((b & 0xff) < 128) {
                n = 128 * n + b & 0xff;
            } else {
                int num = (128 * n + (((b & 0xff) - 128)));
                numbers.add(num);
                n = 0;
            }
        }
        return numbers;
    }

}
