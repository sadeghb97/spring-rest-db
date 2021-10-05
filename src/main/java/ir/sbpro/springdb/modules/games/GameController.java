package ir.sbpro.springdb.modules.games;

import ir.sbpro.springdb.configs.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constants.API_PREFIX + "games/")
public class GameController {
    GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Object> registerGame(@RequestBody GameModel game){
        return gameService.registerRecord(game);
    }

    @PatchMapping(value = "upcover/{game_pk}/")
    public ResponseEntity<Object> uploadGameCover(@PathVariable("game_pk") Long gamePk,
                                                  @RequestParam("file") MultipartFile file){
        return gameService.upGameCover(gamePk, file);
    }

    @GetMapping(value = {"", "/"})
    public List<GameModel> getAllGames(){
        return gameService.getAllRecords();
    }

    @PatchMapping(value = "/{game_pk}/")
    public ResponseEntity<Object> patchGame(@PathVariable("game_pk") Long gamePk,
                                            @RequestBody Map<String, Object> game){
        return gameService.patchGame(gamePk, game);
    }
}
