package com.orange.book.bookContent.util;



import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数字转换为汉语中人民币的大写<br>
 *
 * @author hongten
 * @contact hongtenzone@foxmail.com
 * @create 2013-08-13
 */
public class NumberToCN {
    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = { "零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖" };
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONETRAY_UNIT = { "分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟" };
    /**
     * 特殊字符：整
     */
    private static final String CN_FULL = "整";
    /**
     * 特殊字符：负
     */
    private static final String CN_NEGATIVE = "负";
    /**
     * 金额的精度，默认值为2
     */
    private static final int MONEY_PRECISION = 2;
    /**
     * 特殊字符：零元整
     */
    private static final String CN_ZEOR_FULL = "零元" + CN_FULL;

    /**
     * 中文數字转阿拉伯数组【十万九千零六十  --> 109060】

     * @param chineseNumber
     * @return
     */
public static String zn2num(String chineseNumber){
    char[] cnArr = new char[]{'零','一','二','三','四','五','六','七','八','九'};
    char[] chArr = new char[]{'十','百','千','万',};
    String[] chArrNum = new String[]{"0","00","000","0000"};
    char[] intNum = new char[]{'0','1','2','3','4','5','6','7','8','9'};
    String res="";
    for(int i = 0; i < chineseNumber.length(); i++){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(String.valueOf(chineseNumber.toCharArray()[i]));
        if(isNum.matches() ){
            continue;
        }
        for (int j = 0; j < intNum.length; j++) {//
            if(cnArr[j] == chineseNumber.toCharArray()[i]){
                res+=intNum[j];
            }
        }

    }
    for (int j = 0; j < chArr.length; j++) {//
        if(chineseNumber.endsWith(String.valueOf(chArr[j]))){
            res+=chArrNum[j];
        }
    }
    if(chineseNumber.startsWith("十")){
    	res=String.valueOf(Integer.parseInt(res)+10);
    }
    if ("".equals(res)) {
        return chineseNumber;
    }
    return res;
}

    public static int chineseNumber2Int(String chineseNumber){
        int result = 0;
        int temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'零','一','二','三','四','五','六','七','八','九'};
        char[] chArr = new char[]{'十','百','千','万','亿'};
        char[] intNum = new char[]{'0','1','2','3','4','5','6','7','8','9'};
        String res="";
        for(int i = 0; i < chineseNumber.length(); i++){
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(String.valueOf(chineseNumber.toCharArray()[i]));
            if(isNum.matches() ){
                continue;
            }
            for (int j = 0; j < intNum.length; j++) {//
                if(cnArr[j] == chineseNumber.toCharArray()[i]){
                    res+=intNum[j];
                }
            }

        }


        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            Pattern pattern = Pattern.compile("[0-9]*");
            Matcher isNum = pattern.matcher(String.valueOf(chineseNumber.toCharArray()[i]));
            if(isNum.matches() ){
                continue;
            }

            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if(0 != count){//添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if(b){//单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        if (result == 0) {
            return Integer.parseInt(chineseNumber);
        }
        return result;
    }

    /**
     * 把输入的金额转换为汉语中人民币的大写
     *
     * @param numberOfMoney
     *            输入的金额
     * @return 对应的汉语大写
     */
    public static String number2CNMontrayUnit(BigDecimal numberOfMoney) {
        StringBuffer sb = new StringBuffer();
        // -1, 0, or 1 as the value of this BigDecimal is negative, zero, or
        // positive.
        int signum = numberOfMoney.signum();
        // 零元整的情况
        if (signum == 0) {
            return CN_ZEOR_FULL;
        }
        //这里会进行金额的四舍五入
        long number = numberOfMoney.movePointRight(MONEY_PRECISION)
                .setScale(0, 4).abs().longValue();
        // 得到小数点后两位值
        long scale = number % 100;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;
        // 判断最后两位数，一共有四中情况：00 = 0, 01 = 1, 10, 11
        if (!(scale > 0)) {
            numIndex = 2;
            number = number / 100;
            getZero = true;
        }
        if ((scale > 0) && (!(scale % 10 > 0))) {
            numIndex = 1;
            number = number / 10;
            getZero = true;
        }
        int zeroSize = 0;
        while (true) {
            if (number <= 0) {
                break;
            }
            // 每次获取到最后一个数
            numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                ++zeroSize;
                if (!(getZero)) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0) {
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                    }
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000 > 0)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }
            // 让number每次都去掉最后一个数
            number = number / 10;
            ++numIndex;
        }
        // 如果signum == -1，则说明输入的数字为负数，就在最前面追加特殊字符：负
        if (signum == -1) {
            sb.insert(0, CN_NEGATIVE);
        }
        // 输入的数字小数点后两位为"00"的情况，则要在最后追加特殊字符：整
        if (!(scale > 0)) {
            sb.append(CN_FULL);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
    	String titleVal ="第十章";
    	String num="";
    	if (titleVal.startsWith("第")) {
			if (titleVal.contains("章")) {
				num = String.valueOf(NumberToCN.zn2num(titleVal.substring(titleVal.indexOf("第") + 1, titleVal.indexOf("章"))));
			} else {
				num = String.valueOf(NumberToCN.zn2num(titleVal.substring(titleVal.indexOf("第") + 1, titleVal.indexOf(" "))));
			}
    	}
        System.out.println("开始："+num);

    }
}