package ir.sbpro.springdb.utils;

public class PriceUtils {
    public static final double dollarVal = 28;
    public static final double poundVal = 40;

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
