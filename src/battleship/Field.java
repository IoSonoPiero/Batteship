package battleship;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class Field {
    private final int rows;
    private final int columns;
    private final String[][] fieldContent;
    final String fog = "~";
    boolean gameEnd = false;
    String playerName;
    int sunkShips = 0;

    public String getPlayerName() {
        return playerName;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void cleanField() {
        // Fill with fog
        for (String[] row : fieldContent)
            Arrays.fill(row, fog);
    }

    public Field(int rows, int columns, Player playerName) {
        this.rows = rows;
        this.columns = columns;
        this.playerName = playerName.getName();
        fieldContent = new String[rows][columns];

        cleanField();
    }

    public void printGrid(boolean fogOfWar, Field aField) {
        char alphabet = 'A';

        // print the grid
        // print header
        StringBuilder rowLabel = new StringBuilder();
        rowLabel.append("  ");
        for (int i = 0; i < aField.getColumns(); i++) {
            rowLabel.append(i + 1);
            if (i + 1 != aField.getColumns()) {
                rowLabel.append(" ");
            }
        }

        System.out.println(rowLabel);
        StringBuilder row = new StringBuilder();

        // print rows
        for (int i = 0; i < getRows(); i++) {
            row.append(alphabet);
            row.append(" ");
            for (int j = 0; j < aField.getColumns(); j++) {
                if (fogOfWar && (aField.fieldContent[i][j].equals("X") || aField.fieldContent[i][j].equals("M"))) {
                    row.append(aField.fieldContent[i][j]);
                } else if (!fogOfWar) {
                    row.append(aField.fieldContent[i][j]);
                } else {
                    row.append(fog);
                }

                if (j + 1 != getColumns()) {
                    row.append(" ");
                }
            }
            row.append("\n");
            alphabet++;
        }
        System.out.print(row);
    }

    public static void promptEnterKey(String message) {
        System.out.println(message);
        System.out.println("...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void askForShipCoordinates(Ship[] ships) {
        int[] coordinates;
        Scanner scanner = new Scanner(System.in);

        //System.out.format("%s, place your ships on the game field%n%n", getPlayerName());

        for (Ship theShip : ships) {

            System.out.format("Enter the coordinates of the %s (%d cells):%n%n", theShip.getName(), theShip.getSize());

            do {
                String[] stringCoordinates = scanner.nextLine().toUpperCase(Locale.ROOT).trim().split("\\s");
                coordinates = parseCoordinates(stringCoordinates);

            } while (!checkCoordinates(coordinates, theShip, ships));

            saveCoordinates(coordinates, theShip);
            System.out.println();
            this.printGrid(false, this);
        }
    }

    private boolean proximityCheck(int[] coordinates, Ship theShip, Ship[] ships) {
        // proximity check
        // iterate the ships
        for (Ship aShip : ships) {
            // if a ship has been set, check for proximity
            if (aShip.isReady() && !(aShip.getName().equals(theShip.getName()))) {
                int x1 = coordinates[0];
                int y1 = coordinates[1];
                int x2 = coordinates[2];
                int y2 = coordinates[3];
                int[] existingCoordinates = aShip.getCoordinatesArray();
                int k1 = existingCoordinates[0];
                int r1 = existingCoordinates[1];
                int k2 = existingCoordinates[2];
                int r2 = existingCoordinates[3];

                for (int i = x1 - 1; i <= x2 + 1; i++) {
                    for (int j = y1 - 1; j <= y2 + 1; j++) {
                        if ((i == k1 && j == r1) || (i == k2 && j == r2)) {
                            System.out.format("%nError! You placed it too close to another one. Try again:%n%n");
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean checkCoordinates(int[] coordinates, Ship theShip, Ship[] ships) {
        int x1 = coordinates[0];
        int y1 = coordinates[1];
        int x2 = coordinates[2];
        int y2 = coordinates[3];
        int shipLength = 0;

        // check if coordinates are ok

        if (x1 < 0 || x1 >= this.columns || y1 < 0 || y1 >= this.rows || x2 < 0 || x2 >= this.columns || y2 < 0 || y2 >= this.rows) {
            System.out.format("%nError! Wrong coordinates! Try again:%n%n");
            return false;
        }

        // check if the ship is in wrong location
        if ((x1 != x2) && (y1 != y2)) { // the ship location is wrong!
            System.out.format("%nError! Wrong ship location! Try again:%n%n");
            return false;
        }
        // check length of ship
        if (x1 == x2) {
            shipLength = y2 - y1 + 1;
        }

        if (y1 == y2) {
            shipLength = x2 - x1 + 1;
        }

        if (theShip.getSize() != shipLength) {
            System.out.format("%nError! Wrong length of the %s! Try again:%n%n", theShip.getName());
            return false;
        }

        // last control, proximity check
        return proximityCheck(coordinates, theShip, ships);
    }

    private void saveCoordinates(int[] coordinates, Ship theShip) {

        int x1 = coordinates[0];
        int y1 = coordinates[1];
        int x2 = coordinates[2];
        int y2 = coordinates[3];

        theShip.saveCoordinates(coordinates);
        theShip.setReady(true);

        // determine if ship is horizontally placed or vertically placed
        if (y1 == y2) { // horizontal
            for (int i = x1; i <= x2; i++) {
                fieldContent[y1][i] = "O";
            }
        }
        if (x1 == x2) { // vertical
            for (int i = y1; i <= y2; i++) {
                fieldContent[i][x1] = "O";
            }
        }
    }

    private int[] parseCoordinates(String[] stringCoordinates) {
        // get X1
        char coordinate = stringCoordinates[0].charAt(0);
        int y1 = (coordinate - 'A');

        // get Y1
        int x1 = Integer.parseInt(stringCoordinates[0].substring(1, stringCoordinates[0].length()));
        x1--;

        // get X2
        coordinate = stringCoordinates[1].charAt(0);
        int y2 = (coordinate - 'A');

        // get Y2
        int x2 = Integer.parseInt(stringCoordinates[1].substring(1, stringCoordinates[1].length()));
        x2--;

        // reorder x1 and x2
        if (x1 == x2) { // same column
            if (y1 > y2) {
                int tmp = y2;
                y2 = y1;
                y1 = tmp;
            }
        }
        if (y1 == y2) { // same row
            if (x1 > x2) {
                int tmp = x2;
                x2 = x1;
                x1 = tmp;
            }
        }

        return new int[]{x1, y1, x2, y2};
    }

    public boolean takeAShot(Ship[] otherPlayerShips, Field player) {

        Scanner scanner = new Scanner(System.in);

        int x1;
        int y1;
        boolean isHit = false;
        boolean isSunk = false;

        do {
            String stringCoordinates = scanner.nextLine().toUpperCase(Locale.ROOT).trim();

            // get X1
            char coordinate = stringCoordinates.charAt(0);
            y1 = (coordinate - 'A');

            // get Y1
            x1 = Integer.parseInt(stringCoordinates.substring(1, stringCoordinates.length()));
            x1--;

            if (x1 < 0 || x1 >= this.columns || y1 < 0 || y1 >= this.rows) {
                System.out.format("%nError! You entered the wrong coordinates! Try again:%n%n");
                continue;
            }
            break;
        } while (true);

        for (Ship aShip : otherPlayerShips) {

            int[][] coordinates = aShip.getFullCoordinates();

            for (int[] coordinate : coordinates) {
                if (x1 == coordinate[0] && y1 == coordinate[1]) {
                    isSunk = aShip.hit();
                    if (isSunk) {
                        sunkShips++;
                        // if all the ships are sunk
                        if (sunkShips == otherPlayerShips.length) {
                            gameEnd = true;
                        }
                    }

                    isHit = true;
                    break;
                }
            }
            if (isHit) break;
        }
        String message;

        if (isSunk) {
            //fieldContent[y1][x1] = "X";
            player.fieldContent[y1][x1] = "X";
            message = "You sank a ship!%n";
        } else if (isHit) {
            player.fieldContent[y1][x1] = "X";
            //fieldContent[y1][x1] = "X";
            message = "You hit a ship!%n";
        } else {
            player.fieldContent[y1][x1] = "M";
            //fieldContent[y1][x1] = "M";
            message = "You missed!%n";
        }
        System.out.println();

        if (!gameEnd) {
            System.out.format(message);
        }
        return gameEnd;
    }
}
