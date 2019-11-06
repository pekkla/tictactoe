package fi.laituri;

import fi.laituri.dto.Cell;
import fi.laituri.game.GameEngine;
import fi.laituri.game.GameState;
import java.util.Scanner;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class Standalone {

    public static void main(String[] args) {

        final GameState state = new GameState();
        state.setId(UUID.randomUUID().toString());

        final Scanner scanner = new Scanner(System.in);

        System.out.println("Choose your token (X / O)");
        String token = scanner.next();

        if (token.equals("X")) {
            state.setPlayerToken("X");
            state.setComputerToken("O");
        } else {
            state.setPlayerToken("O");
            state.setComputerToken("X");
        }

        final GameEngine engine = new GameEngine();

        while (true) {

            System.out.println("Make your move:");
            String response = scanner.next();
            String[] move = StringUtils.split(response, ",");

            engine.playerMoves(new Cell(move[0], move[1]), state);
            if (engine.playerWins(state)) {
                System.out.println("You won!");
                System.out.println(engine.printBoard(state));
                return;
            }
            engine.computerMoves(state);
            if (engine.computerWins(state)) {
                System.out.println("I won! Better luck next time.");
                System.out.println(engine.printBoard(state));
                return;
            }

            System.out.println(engine.printBoard(state));
        }
    }
}
