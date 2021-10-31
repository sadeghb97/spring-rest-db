package ir.sbpro.springdb.utils;

public class PriceUtils {
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
}
