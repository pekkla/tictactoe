package fi.laituri.game;

import fi.laituri.dto.Cell;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.springframework.util.StringUtils;

public class GameEngine {

    private int gridSize = 3; //Could also be user param or configured at app props
    private int required = 3; //Could also be user param or configured at app props

    private static final Map<Integer, String> lettersForNumbers = new HashMap<>();
    private static final Map<String, Integer> numbersForLetters = new HashMap<>();

    public GameEngine() {
        lettersForNumbers.put(0, "A");
        lettersForNumbers.put(1, "B");
        lettersForNumbers.put(2, "C");
        lettersForNumbers.put(3, "D");
        lettersForNumbers.put(4, "E");
        lettersForNumbers.put(5, "F");
        lettersForNumbers.put(6, "G");
        lettersForNumbers.put(7, "H");
        lettersForNumbers.put(8, "I");
        lettersForNumbers.put(9, "J");

        numbersForLetters.put("A", 0);
        numbersForLetters.put("B", 1);
        numbersForLetters.put("C", 2);
        numbersForLetters.put("D", 3);
        numbersForLetters.put("E", 4);
        numbersForLetters.put("F", 5);
        numbersForLetters.put("G", 6);
        numbersForLetters.put("H", 7);
        numbersForLetters.put("I", 8);
        numbersForLetters.put("J", 9);
    }

    public boolean playerWins(final GameState gameState) {
        return wins(gameState.getPlayerToken(), gameState);
    }

    public boolean computerWins(final GameState gameState) {
        return wins(gameState.getComputerToken(), gameState);
    }

    private boolean wins(String token, final GameState gameState) {
        String v = token;
        String x = v + v + v;

        if (checkRows(x, gameState)) {
            return true;
        }

        if (checkCols(x, gameState)) {
            return true;
        }

        return checkDiagonal(x, gameState);
    }

    public String getWinner(GameState gameState) {
        if (gameState.isPlayerWins()) {
            return gameState.getPlayerName();
        }
        return "computer";
    }

    private boolean checkRows(String x, GameState gameState) {
        for (int row = 0; row < gridSize; row++) {
            if (checkRow(row, x, gameState)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkCols(String x, GameState gameState) {
        for (int col = 0; col < gridSize; col++) {
            if (checkCol(col, x, gameState)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRow(int row, String x, GameState gameState) {
       return checkRowOrCol(true, row, x, gameState);
    }

    private boolean checkCol(int col, String x, GameState gameState) {
        return checkRowOrCol(false, col, x, gameState);
    }

    private boolean checkRowOrCol(boolean checkRow, int index, String x, final GameState gameState) {
        String[][] table = (String[][]) ObjectSerializer.deserialize(gameState.getBoardState());

        int start = 0;
        while (start <= gridSize - required) {
            String vector = "";
            //Build single vector
            for (int i = start; i < required + start; i++) {
                if (checkRow) {
                    vector = vector + table[index][i];
                } else {
                    vector = vector + table[i][index];
                }
            }
            if (eq(vector, x)) {
                return true;
            }
            start++;
        }
        return false;
    }

    private boolean checkDiagonal(String x, final GameState gameState) {
        //TODO
        return false;
    }

    private boolean eq(String s, String c) {
        if (s == null || c == null) {
            return false;
        }
        return s.equals(c);
    }

    public String printBoard(final GameState gameState) {

        String[][] table = (String[][]) ObjectSerializer.deserialize(gameState.getBoardState());

        String result = " ";

        for (int i = 0; i < gridSize; i++) {
            result = result + "|" + letterForNumber(i);
        }

        result = result + "|\n" +  "A";
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (col == 0) {
                    result = result + "|" + emptyOrVal(table[row][col]) + "|";
                } else {
                    result = result + emptyOrVal(table[row][col]) + "|";
                }
            }
            //Print row number
            if (row < gridSize - 1) {
                result = result + "\n" + letterForNumber(row + 1);
            } else {
                result = result + "\n";
            }
        }
        return result;
    }

    private String letterForNumber(Integer number) {
        return lettersForNumbers.get(number);
    }

    private Integer numberForLetter(String str) {
        return numbersForLetters.get(str);
    }

    private String emptyOrVal(final String val) {
        if (val == null) {
            return " ";
        }
        return val;
    }

    public void playerMoves(final Cell cell, final GameState gameState) {
        String[][] table = (String[][]) ObjectSerializer.deserialize(gameState.getBoardState());

        final String item = table[numberForLetter(cell.getRow())][numberForLetter(cell.getCol())];
        if (!StringUtils.isEmpty(item)) {
            throw new RuntimeException("Item already placed");
        }

        table[numberForLetter(cell.getRow())][numberForLetter(cell.getCol())] = gameState.getPlayerToken();

        final String newState = ObjectSerializer.serialize(table);
        gameState.setBoardState(newState);
    }

    public void computerMoves(final GameState gameState) {
        Random randomGenerator = new Random();

        String[][] table = (String[][]) ObjectSerializer.deserialize(gameState.getBoardState());

        while (true) {
            int row = randomGenerator.nextInt(gridSize);
            int col = randomGenerator.nextInt(gridSize);

            if (StringUtils.isEmpty(table[row][col])) {
                table[row][col] = gameState.getComputerToken();
                final String newState = ObjectSerializer.serialize(table);
                gameState.setBoardState(newState);
                return;
            }
        }
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public int getGridSize() {
        return gridSize;
    }
}