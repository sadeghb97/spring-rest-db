package ir.sbpro.springdb.plat_modules.platgames;

public class PlatGamesFilter {
    public long userPk = 1;
    public boolean libIncluded = false;

    public PlatGamesFilter(){}

    PlatGamesFilter setUserPk(long userPk){
        this.userPk = userPk;
        return this;
    }

    PlatGamesFilter setLibIncluded(boolean libIncluded){
        this.libIncluded = libIncluded;
        return this;
    }
}
