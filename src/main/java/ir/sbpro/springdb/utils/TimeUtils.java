package ir.sbpro.springdb.utils;

public class TimeUtils {
    public static double getSepHours(long smallTime, long bigTime){
        long sep = bigTime - smallTime;
        return  ((double) sep) / 3600000;
    }

    public static String getDuration(long duration, boolean shortMode){
        final long secDur = 1000;
        final long minDur = secDur * 60;
        final long hourDur = minDur * 60;
        final long dayDur = hourDur * 24;
        final long monthDur = dayDur * 30;
        final long yearDur = monthDur * 12;

        long year = duration / yearDur;
        long remYear = duration % yearDur;
        long month = remYear / monthDur;
        long remMonth = remYear % monthDur;
        long day = remMonth / dayDur;
        long remDay = remMonth % dayDur;

        long hour = remDay / hourDur;
        long remHour = remDay % hourDur;
        long min = remHour / minDur;
        long remMin = remHour % minDur;
        long sec = remMin / secDur;

        StringBuilder sb = new StringBuilder();

        if(year > 0){
            sb.append(year);
            sb.append(" Year");
            if(year > 1) sb.append("s");
            if(shortMode) return sb.toString();

            if(month > 0){
                sb.append(" and ");
                sb.append(month);
                sb.append(" Month");
                if(month > 1) sb.append("s");
            }
        }
        else if(month > 0){
            sb.append(month);
            sb.append(" Month");
            if(month > 1) sb.append("s");
            if(shortMode) return sb.toString();

            if(day > 0){
                sb.append(" and ");
                sb.append(day);
                sb.append(" Day");
                if(day > 1) sb.append("s");
            }
        }
        else if(day > 0){
            sb.append(day);
            sb.append(" Day");
            if(day > 1) sb.append("s");
            if(shortMode) return sb.toString();

            if(hour > 0){
                sb.append(" and ");
                sb.append(hour);
                sb.append(" Hour");
                if(hour > 1) sb.append("s");
            }
        }
        else if(hour > 0){
            sb.append(hour);
            sb.append(" Hour");
            if(hour > 1) sb.append("s");
            if(shortMode) return sb.toString();

            if(min > 0){
                sb.append(" and ");
                sb.append(min);
                sb.append(" Min");
                if(min > 1) sb.append("s");
            }
        }
        else if(min > 0){
            sb.append(min);
            sb.append(" Min");
            if(min > 1) sb.append("s");
            if(shortMode) return sb.toString();

            if(sec > 0){
                sb.append(" and ");
                sb.append(sec);
                sb.append(" Sec");
                if(sec > 1) sb.append("s");
            }
        }
        else if(sec > 0){
            sb.append(min);
            sb.append(" Sec");
            if(sec > 1) sb.append("s");
        }

        return sb.toString();
    }
}
