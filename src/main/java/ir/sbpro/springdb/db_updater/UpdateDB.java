package ir.sbpro.springdb.db_updater;

public class UpdateDB {
    public static void update() {
        DBInitializer dbInitializer = new DBInitializer();
        dbInitializer.fetchRepository();
        dbInitializer.loadPlatinumGames();
        dbInitializer.syncDB();
    }
}
