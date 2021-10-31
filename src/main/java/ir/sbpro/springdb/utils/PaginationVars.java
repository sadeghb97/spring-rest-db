package ir.sbpro.springdb.utils;

public class PaginationVars {
    public int firstItem;
    public int lastItem;

    public static PaginationVars getInstance(int current, int lastPage, int max){
        PaginationVars paginationVars = new PaginationVars();
        int half = max / 2;

        if(lastPage < max){
            paginationVars.firstItem = 0;
            paginationVars.lastItem = lastPage;
            return paginationVars;
        }

        int left = current;
        int right = lastPage - current;

        if(left >= half && right >= half){
            paginationVars.firstItem = current - half;
            paginationVars.lastItem = current + half;
            return paginationVars;
        }

        if(left >= half){
            int diff = half - right;

            paginationVars.firstItem = current - half - diff;
            paginationVars.lastItem = lastPage;
            return paginationVars;
        }

        int diff = half - left;
        paginationVars.firstItem = 0;
        paginationVars.lastItem = current + half + diff;
        return paginationVars;
    }
}
