package org.example;

//package CSC4101Assignment2

/*
    @author: Nick Gendron
    @LSUID: 898333829
 */

public class Convertor {

    /* Returns the binary representation of an integer */
    public static StringBuilder convertToBinary(int exponent){
        StringBuilder returnString = new StringBuilder();
        while(true){
            int binDigit = exponent % 2;
            if(binDigit < 0){ binDigit *= -1;}
            returnString.append(binDigit);
            exponent /= 2;
            if(exponent == 0){
                break;
            }
        }
        returnString = returnString.reverse();
        return returnString;
    }

    public static void IEEE754(double inputNum){

        /* Find sign bit */
        int sign = 0;
        if(inputNum < 0) {sign = 1; inputNum *= -1;}

        /* Get whole and fractional portions */
        int wholePart = (int) inputNum;
        double fractionalPart = inputNum - wholePart;

        /* Case for 0.0 or -0.0 */
        if (wholePart == 0 && fractionalPart == 0.0) {
            StringBuilder zeroString = new StringBuilder();
            for(int i = 0; i < 52; i ++){
                zeroString.append(0);
            }
            if(String.valueOf(inputNum).charAt(0) == '-'){
                sign = 1;
            }
            System.out.println(sign + " " + zeroString.substring(0,8) + " " + zeroString.substring(0,23));
            System.out.println(sign + " " + zeroString.substring(0,11) + " " + zeroString.substring(0,52));
            return;
        }

        /* Binary string for whole number portion */
        StringBuilder wholePartString = convertToBinary(wholePart);

        /* Convert fractional portion to binary */
        StringBuilder fractionalString = new StringBuilder();

        /* If wholePart > 1, we need to add that to the fractional string */
        if(wholePart >= 1) {
            wholePartString.deleteCharAt(0);
             fractionalString.insert(0, wholePartString);
        }

        /* Convert fractional part to binary, taking extra bits bc we can */
        for(int i = 0; i < 200; i++){
            fractionalPart *= 2;
            if(fractionalPart >= 1){
                /* As we ignore values on LHS when dividing on next iteration */
                fractionalPart -= 1;
                fractionalString.append(1);
            }
            else{
                fractionalString.append(0);
            }
        }

        int exponent32 = 0;
        int exponent64 = 0;

        if(wholePart == 0) {
            char currentChar = '0';
            int index = 0;

            /* Finding exponent bias */
            while(currentChar != '1'){
                currentChar = fractionalString.charAt(index);
                index += 1;
            }

            /* Dropping leading 0's and left most bit found in loop */
            fractionalString.delete(0, index);

            /* Calculate exponent value */
            exponent32 = 127 - index;
            exponent64 = 1023 - index;
        }

        else {
            /* Find decimal exponent value for each precision */
            exponent32 = wholePartString.length() + 127;
            exponent64 = wholePartString.length() + 1023;
        }

        /* Convert exponent decimal to binary */
        StringBuilder exponentBuilder32 = convertToBinary(exponent32);
        StringBuilder exponentBuilder64 = convertToBinary(exponent64);

        /* add leading zero's if needed */
        if(exponentBuilder32.length() < 8){
            int numToAdd = 8 - exponentBuilder32.length();
            for(int i = 0; i < numToAdd; i++){
                exponentBuilder32.insert(0, 0);
            }
        }

        /* add leading zero's if needed */
        if(exponentBuilder64.length() < 11){
            int numToAdd = 11 - exponentBuilder64.length();
            for(int i = 0; i < numToAdd; i++){
                exponentBuilder64.insert(0, 0);
            }
        }

        /* create each mantissa string */
        String mantissa32 = fractionalString.substring(0, 23);
        String mantissa64 = fractionalString.substring(0, 52);

        /* Print results */
        System.out.println(sign + " " + exponentBuilder32 + " " + mantissa32);
        System.out.println(sign + " " + exponentBuilder64 + " " + mantissa64);
    }

    public static void main(String[] args) {

//        double test = 0.02;
        double test = 0.00000000000000001;
        IEEE754(test);

    }
}
