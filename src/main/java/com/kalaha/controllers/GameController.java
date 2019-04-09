package com.kalaha.controllers;

import com.kalaha.models.Game;
import com.kalaha.repositories.GameRepository;
import com.kalaha.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class GameController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GameService gameService;

    private final GameRepository gameRepository;

    @Autowired
    public GameController(GameService gameService, GameRepository gameRepository) {
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    /**
     * method to get a game
     * @param gameId - game id
     * @return - game
     */
    @Cacheable(value = "games", key = "#gameId")
    @RequestMapping(value = "/{gameId}", method = RequestMethod.GET)
    public Game getGame(@PathVariable String gameId) {
        logger.info("Getting game with ID {}.", gameId);
        Game game = gameRepository.findOne(Long.valueOf(gameId));
        logger.info("found game {}", game);
        return game;
    }

    /**
     * method to start game
     * @return - created response
     */
    @PostMapping("/start")
    public ResponseEntity<Game> createNewGame() {
        logger.info("Creating a new game current games {}.", gameRepository.count());
        Game newGame = new Game();
        gameRepository.save(newGame);
        return ResponseEntity.created(URI.create("http://localhost:9080/" + newGame.getId())).build();
    }

    /**
     *
     * @param gameId - game id
     * @param index - index to move from
     * @return - game
     */
     @CachePut(value = "games", key = "#gameId")
     @PutMapping(value = "/{gameId}/{index}")
    public Game makeAMove(@PathVariable String gameId, @PathVariable String index) {
        Game game =  gameRepository.findOne(Long.valueOf(gameId));
        if (game != null && gameService.isValidMove(game, gameId, index)){
            logger.info("Making a move for the player {} from index {}.", index.charAt(0), index);
            gameService.makeAmove(game, gameId,index);
            if (gameService.isGameOver(game)){
                gameRepository.save(game);
                return game;
            }
            Game res = gameRepository.findOne(Long.valueOf(gameId));
            return res;
        }else {
            return null;
        }
    }

    /**
     * delete a game from cache
     * @param gameId - to delete from redis
     */
    @CacheEvict(value = "games", allEntries = true)
    @DeleteMapping("/{gameId}")
    public void deleteGameById(@PathVariable Long gameId) {
        logger.info("deleting game with id {}", gameId);
        gameRepository.delete(gameId);
    }
}
