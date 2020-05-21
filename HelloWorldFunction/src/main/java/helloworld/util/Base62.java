package helloworld.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Base62 <-> 10 进制 转换器
 *
 * @author bing
 */
public class Base62 {
    public static final String BASE_62_CHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 62进制字符串转为数字
     * 二进制转十进制的算法，从右到左用二进制的每个数去乘以2的相应次方，次方要从0开始。
     * 62进制转10进制也类似，从右往左每个数*62的N次方，N从0开始。
     *
     * @param num 10进制数字
     * @return 62进制字符串
     */
    public static String base62Encode(long num) {
        StringBuilder sb = new StringBuilder();
        int remainder = 0;
        int scale = 62;
        while (num > scale - 1) {
            remainder = Long.valueOf(num % scale).intValue();
            sb.append(BASE_62_CHAR.charAt(remainder));
            num = num / scale;
        }
        sb.append(BASE_62_CHAR.charAt(Long.valueOf(num).intValue()));
        String value = sb.reverse().toString();
        return StringUtils.leftPad(value, 6, '0');
    }

    /**
     * 62进制字符串转为数字
     * 十进制转二进制的算法么，除二取余，然后倒序排列，高位补零。转62进制也类似，不断除以62取余数，然后倒序。
     *
     * @param str 编码后的62进制字符串
     * @return 解码后的 10 进制字符串
     */
    public static long base62Decode(String str) {
        int scale = 62;
        str = str.replace("^0*", "");
        long num = 0;
        int index = 0;
        for (int i = 0; i < str.length(); i++) {
            index = BASE_62_CHAR.indexOf(str.charAt(i));
            num += (long) (index * (Math.pow(scale, str.length() - i - 1)));
        }
        return num;
    }
}
