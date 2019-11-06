package fi.laituri.game;

import java.io.Serializable;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("GameState")
@Data
public class GameState implements Serializable {

    private boolean gameOver = false;
    private boolean playerWins = false;

    private String boardState;

    private String id;
    private String playerName;

    private String playerToken;
    private String computerToken;
}
