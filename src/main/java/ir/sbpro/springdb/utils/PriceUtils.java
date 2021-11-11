package ir.sbpro.springdb.utils;

public class PriceUtils {
    public static final int dollarVal = 28000;
    public static final int poundVal = 40000;

    public static double getPriceValue(String basePrice){
        basePrice = basePrice.trim();
        if(basePrice.toLowerCase().equals("free")) return 0;
        try {
            return Double.parseDouble(basePrice.substring(1));
        }
        catch (Exception ex){
            return -1;
        }
    }

    public static double dollarToToman(double dollarPrice){
        return dollarPrice * dollarVal;
    }

    public static double poundToToman(double poundPrice){
        return poundPrice * poundVal;
    }
}
