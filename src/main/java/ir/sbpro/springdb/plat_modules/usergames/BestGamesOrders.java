package ir.sbpro.springdb.plat_modules.usergames;

import java.util.ArrayList;
import java.util.List;

public class BestGamesOrders {
    private List<UserGame> rankedGames;
    private List<UserGame> unrankedGames;

    BestGamesOrders(){
        rankedGames = new ArrayList<>();
        unrankedGames = new ArrayList<>();
    }

    public List<UserGame> getRankedGames() {
        return rankedGames;
    }

    public void setRankedGames(ArrayList<UserGame> rankedGames) {
        this.rankedGames = rankedGames;
    }

    public List<UserGame> getUnrankedGames() {
        return unrankedGames;
    }

    public void setUnrankedGames(ArrayList<UserGame> unrankedGames) {
        this.unrankedGames = unrankedGames;
    }

    public void load(ArrayList<UserGame> games){
        rankedGames = new ArrayList<>();
        unrankedGames = new ArrayList<>();

        for (UserGame g: games) {
            if(g.getRank() > 0) rankedGames.add(g);
            else unrankedGames.add(g);
        }
    }

    public ArrayList<UserGame> mix(){
        ArrayList<UserGame> games = new ArrayList<>();
        for(int i=0; rankedGames.size()>i; i++){
            UserGame rg = rankedGames.get(i);
            rg.setRank(i + 1);
            games.add(rg);
        }

        for(int i=0; unrankedGames.size()>i; i++){
            UserGame ug = unrankedGames.get(i);
            ug.setRank(0);
            games.add(ug);
        }

        return games;
    }
}
