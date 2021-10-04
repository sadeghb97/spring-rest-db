package ir.sbpro.springdb.modules.games;

import ir.sbpro.springdb.modules.EntityUtils;
import ir.sbpro.springdb.modules.studios.StudioModel;
import ir.sbpro.springdb.modules.studios.StudioRepository;
import ir.sbpro.springdb.responses.ErrorsResponseMap;
import ir.sbpro.springdb.utils.PrimitiveWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class GameService {
    StudioRepository studioRepository;
    GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository, StudioRepository studioRepository) {
        this.gameRepository = gameRepository;
        this.studioRepository = studioRepository;
    }

    public List<GameModel> getAllGames(){
        return gameRepository.findAll();
    }

    public Optional<GameModel> getGame(Long gamePk){
        return gameRepository.findById(gamePk);
    }

    public ResponseEntity<Object> registerGame(GameModel game){
        return registerGame(game, null);
    }

    public ResponseEntity<Object> registerGame(GameModel game, MultipartFile file){
        return registerGame(game, file, false);
    }

    public ResponseEntity<Object> registerGame(GameModel game, MultipartFile file, boolean duplicateAllowed){
        EntityUtils<GameModel, GameRepository> entityUtils =
                new EntityUtils(gameRepository, game, "game");

        return entityUtils.patchEntity(file, duplicateAllowed);
    }

    public ResponseEntity<Object> upGameCover(Long gamePk, MultipartFile file){
        Optional<GameModel> gameOptional = gameRepository.findById(gamePk);
        if(gameOptional.isEmpty()){
            return EntityUtils.entityNotFoundResponse("game");
        }

        try {
            GameModel game = gameOptional.get();
            String coverFileName = EntityUtils.saveFile(
                    "img/covers/", file, ".jpg", game.getCover());

            game.setCover(coverFileName);
            return new ResponseEntity<Object>(gameRepository.save(game), HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something goes wrong!");
        }
    }

    public ResponseEntity<Object> patchGame(Long gamePk, Map<String, Object> gameMap){
        Optional<GameModel> gameOptional = gameRepository.findById(gamePk);
        if(gameOptional.isEmpty()){
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "game not found!");
            return EntityUtils.entityNotFoundResponse("game");
        }

        GameModel gameModel = gameOptional.get();
        Stream<String> gameMapKeysStream = gameMap.keySet().stream();

        final PrimitiveWrapper<Boolean> studioNotFound = new PrimitiveWrapper<>(false);
        gameMapKeysStream.forEach((key) -> {
            if(key.equals("name")) gameModel.setName((String) gameMap.get(key));
            else if(key.equals("year")) gameModel.setYear((Integer) gameMap.get(key));
            else if(key.equals("studio")){
                Long studioPk = ((Number) gameMap.get(key)).longValue();
                Optional<StudioModel> studioOptional = studioRepository.findById(studioPk);

                if(studioOptional.isEmpty()){
                    studioNotFound.setValue(true);
                    return;
                }

                gameModel.setStudio(studioOptional.get());
            }
        });

        if(studioNotFound.getValue()){
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "studio not found!");
            return new ErrorsResponseMap("studio", "studio not found!").getEntityResponse();
        }

        return new ResponseEntity<Object>(gameRepository.save(gameModel), HttpStatus.ACCEPTED);
    }
}
