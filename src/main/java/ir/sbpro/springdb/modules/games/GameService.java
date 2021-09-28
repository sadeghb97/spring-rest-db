package ir.sbpro.springdb.modules.games;

import ir.sbpro.springdb.modules.studios.StudioModel;
import ir.sbpro.springdb.modules.studios.StudioRepository;
import ir.sbpro.springdb.responses.ErrorsResponseMap;
import ir.sbpro.springdb.utils.PrimitiveWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public GameModel registerGame(GameModel game){
        return gameRepository.save(game);
    }

    public ResponseEntity<Object> patchGame(Long gamePk, Map<String, Object> gameMap){
        Optional<GameModel> gameOptional = gameRepository.findById(gamePk);
        if(gameOptional.isEmpty()){
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "game not found!");
            return new ErrorsResponseMap("pk", "game not found!").getEntityResponse();
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
