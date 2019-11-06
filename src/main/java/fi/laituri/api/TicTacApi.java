package fi.laituri.api;

import fi.laituri.dto.Cell;
import fi.laituri.dto.ErrorResponse;
import fi.laituri.dto.UserOptions;
import fi.laituri.game.GameEngine;
import fi.laituri.game.GameState;
import fi.laituri.game.ObjectSerializer;
import fi.laituri.repo.GameRepo;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@Slf4j
public class TicTacApi {

    @Autowired
    private GameRepo repo;

    @Autowired
    private GameEngine engine;

    @PostMapping(value = "/start", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public String startGame(@RequestBody final UserOptions userOptions){
        final GameState gameState = new GameState();

        if (!(userOptions.getCharacter().equals("X") ||
            userOptions.getCharacter().equals("O"))){
            throw new RuntimeException("Invalid character. Allowed are X and O.");
        }

        gameState.setId(UUID.randomUUID().toString());
        gameState.setPlayerToken(userOptions.getCharacter());
        if (userOptions.getCharacter().equals("X")) {
            gameState.setComputerToken("O");
        } else {
            gameState.setComputerToken("X");
        }
        gameState.setPlayerName(userOptions.getName());
        gameState.setBoardState(ObjectSerializer.serialize(
            new String[engine.getGridSize()][engine.getGridSize()]));
        log.info("Game started");
        repo.save(gameState);
        return gameState.getId();
    }

    @GetMapping(value = "/game/{id}", produces = {"text/plain"})
    @ResponseStatus(HttpStatus.OK)
    public String gameStatus(@PathVariable final String id){
        final GameState gs = repo.findById(id).get();

        String board = engine.printBoard(gs);

        String winMessage = null;
        if (gs.isGameOver()) {
            if (gs.isPlayerWins()) {
               winMessage = "Player " + gs.getPlayerName() + " wins!";
            } else {
                winMessage = "Computer wins. Better luck next time!";
            }
        }
        if (winMessage != null) {
            return winMessage + "\n\n" + board;
        }
        return board;
    }

    @PostMapping(value = "/game/{id}/move", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public void move(@RequestBody final Cell cell, @PathVariable final String id){
        final GameState gs = repo.findById(id).get();
        log.info("Player " + gs.getPlayerName() + " is making a move");

        if (gs.isGameOver()) {
            throw new RuntimeException("Game " + id + " is already over. Winner was "
                                           + engine.getWinner(gs));
        }

        engine.playerMoves(cell, gs); //Might throw exception if item already placed

        if (engine.playerWins(gs)) {
            log.info("Player won");
            gs.setPlayerWins(true);
            gs.setGameOver(true);
            repo.save(gs);
            return;
        }

        log.info("Computer moves");
        engine.computerMoves(gs);
        if (engine.computerWins(gs)) {
            gs.setPlayerWins(false);
            gs.setGameOver(true);
            log.info("Computer won");
        }

        repo.save(gs);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse exception(final Exception e) {
        log.error("There were errors", e);
        return new ErrorResponse(e.getMessage());
    }
}
