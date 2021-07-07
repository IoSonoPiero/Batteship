package battleship;

public enum Player {
    PLAYER_ONE("Player 1"), PLAYER_TWO("Player 2");

    private String name;

    public String getName() {
        return name;
    }

    Player(String name) {
        this.name = name;
    }
}
