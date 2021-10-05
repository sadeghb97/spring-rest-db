package ir.sbpro.springdb.modules.games;

import ir.sbpro.springdb.modules._interfaces.EntityUtils;
import ir.sbpro.springdb.modules._interfaces.ModuleService;
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
public class GameService extends ModuleService<GameModel> {
    StudioRepository studioRepository;

    @Autowired
    public GameService(GameRepository gameRepository, StudioRepository studioRepository) {
        super(gameRepository);
        this.studioRepository = studioRepository;
    }

    public ResponseEntity<Object> upGameCover(Long gamePk, MultipartFile file){
        Optional<GameModel> gameOptional = repository.findById(gamePk);
        if(gameOptional.isEmpty()){
            return EntityUtils.entityNotFoundResponse("game");
        }

        try {
            GameModel game = gameOptional.get();
            String coverFileName = EntityUtils.saveCover(file, game.getCover());

            game.setCover(coverFileName);
            return new ResponseEntity<Object>(repository.save(game), HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something goes wrong!");
        }
    }

    public ResponseEntity<Object> patchGame(Long gamePk, Map<String, Object> gameMap){
        Optional<GameModel> gameOptional = repository.findById(gamePk);
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

        return new ResponseEntity<Object>(repository.save(gameModel), HttpStatus.ACCEPTED);
    }
}
