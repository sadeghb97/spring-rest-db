package ir.sbpro.springdb;

import ir.sbpro.EPSingleton;
import ir.sbpro.NetworkConnection;

public class AppSingleton {
    private static AppSingleton _appSingletonInstance = null;
    public NetworkConnection networkConnection;
    public static String GAME_AVATARS_ALT_PATH = "/opt/lampp/htdocs/mypsn/images/game_avatars";

    private AppSingleton(){
        networkConnection = new NetworkConnection(true, "127.0.0.1", 2081);
    }

    public static AppSingleton getInstance(){
        if(_appSingletonInstance == null){
            _appSingletonInstance = new AppSingleton();
        }
        return _appSingletonInstance;
    }
}
