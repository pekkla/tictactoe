import fi.laituri.game.GameEngine;
import fi.laituri.game.GameState;
import fi.laituri.game.ObjectSerializer;
import org.junit.Test;

public class GameEngineTest {
    @Test
    public void printBoard() {
        GameEngine ge = new GameEngine();
        ge.setGridSize(9);
        ge.setGridSize(9);
        GameState gs = new GameState();
        gs.setBoardState(ObjectSerializer.serialize(
            new String[ge.getGridSize()][ge.getGridSize()]));
        //Just to check board printed correctly
        System.out.println(ge.printBoard(gs));
    }

    //TODO - Test different game states with different boards
}
