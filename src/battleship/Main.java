package battleship;

public class Main {

    public static void main(String[] args) {
        // field size
        final int ROWS = 10;
        final int COLUMNS = 10;

        // -- Initialization Start --
        // initialize array of Ships for Player One
        Ship[] shipsPlayerOne = new Ship[ShipType.values().length];

        // initialize array of Ships for Player One
        Ship[] shipsPlayerTwo = new Ship[ShipType.values().length];

        for (ShipType shipType : ShipType.values()) {
            shipsPlayerOne[shipType.ordinal()] = new Ship(shipType);
        }

        for (ShipType shipType : ShipType.values()) {
            shipsPlayerTwo[shipType.ordinal()] = new Ship(shipType);
        }

        // initialize Field for Player One
        Field fieldPlayerOne = new Field(ROWS, COLUMNS, Player.PLAYER_ONE);

        System.out.format("%s, place your ships on the game field%n%n", fieldPlayerOne.getPlayerName());

        // print grid
        fieldPlayerOne.printGrid(true, fieldPlayerOne);

        // for each ship, get coordinates
        fieldPlayerOne.askForShipCoordinates(shipsPlayerOne);
        //fieldPlayerOne.printGrid(false, fieldPlayerOne);

        System.out.println();
        Field.promptEnterKey("Press Enter and pass the move to another player");

        // initialize Field for Player One
        Field fieldPlayerTwo = new Field(ROWS, COLUMNS, Player.PLAYER_TWO);

        System.out.format("%s, place your ships on the game field%n%n", fieldPlayerTwo.getPlayerName());

        // print grid
        fieldPlayerTwo.printGrid(true, fieldPlayerTwo);
        System.out.println();

        // for each ship, get coordinates
        fieldPlayerTwo.askForShipCoordinates(shipsPlayerTwo);
        //fieldPlayerTwo.printGrid(false, fieldPlayerTwo);
        System.out.println();

        Field currentPlayer = fieldPlayerOne;
        Field alternatePlayer = fieldPlayerTwo;
        Ship[] otherPlayerShips = shipsPlayerTwo;
        // -- Initialization End --

        // loop until the game is over
        boolean endOfGame = false;
        do {
            Field.promptEnterKey("Press Enter and pass the move to another player");
            alternatePlayer.printGrid(true, alternatePlayer);
            System.out.println("---------------------");
            currentPlayer.printGrid(false, currentPlayer);

            System.out.println();
            System.out.format("%s, it's your turn:%n%n", currentPlayer.getPlayerName());

            endOfGame = currentPlayer.takeAShot(otherPlayerShips, alternatePlayer);
            if (endOfGame) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                break;
            }

            if (currentPlayer == fieldPlayerOne) {
                currentPlayer = fieldPlayerTwo;
                otherPlayerShips = shipsPlayerOne;
                alternatePlayer = fieldPlayerOne;
            } else {
                currentPlayer = fieldPlayerOne;
                otherPlayerShips = shipsPlayerTwo;
                alternatePlayer = fieldPlayerTwo;
            }
        } while (!endOfGame);
    }
}
