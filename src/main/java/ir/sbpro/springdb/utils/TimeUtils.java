package ir.sbpro.springdb.utils;

public class TimeUtils {
    public static double getSepHours(long smallTime, long bigTime){
        long sep = bigTime - smallTime;
        return  ((double) sep) / 3600000;
    }
}
