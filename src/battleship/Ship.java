package battleship;

import java.util.Arrays;

public class Ship {
    //private int sunkCount = 0;
    private final ShipType shipType;
    private String name;
    private int size;
    private boolean isSunk; // determine if the ship is sunk
    private boolean isSet; // determine if the ship is all set with coordinates and status
    private final int[] coordinatesArray;
    private int[][] fullCoordinates;
    private final String[] shipStatus;
    private int hitCount = 0;

    public int getHitCount() {
        return hitCount;
    }

    public boolean hit() {
        this.hitCount++;
        // if the hits are equals to ship size
        if (getHitCount() == getSize()) {
            //this.sunkCount++;
            return true;
        }
        return false;
    }

    public Ship(ShipType shipType) {
        this.shipType = shipType;
        setName(shipType.getName());
        setSize(shipType.getSize());
        this.coordinatesArray = new int[4]; // it will store X1,Y1 X2, Y2
        this.shipStatus = new String[shipType.getSize()]; // allocate space for shipStatus
        this.isSunk = false;
        this.isSet = false;
    }

    public int[] getCoordinatesArray() {
        return coordinatesArray;
    }

    public int[][] getFullCoordinates() {
        return fullCoordinates;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    private void setSize(int size) {
        this.size = size;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public void saveCoordinates(int[] coordinates) {
        System.arraycopy(coordinates, 0, coordinatesArray, 0, coordinates.length);

        int iterator = 0;
        // determine if ship is horizontal or vertical placed
        if (coordinates[1] == coordinates[3]) { // ship is horizontal
            fullCoordinates = new int[coordinates[2] - coordinates[0] + 1][2];
            iterator = coordinates[0];
            for (int[] row : fullCoordinates) {
                row[0] = iterator++;
                row[1] = coordinates[1];
            }
        } else { // ship is vertical
            fullCoordinates = new int[coordinates[3] - coordinates[1] + 1][2];
            iterator = coordinates[1];
            for (int[] row : fullCoordinates) {
                row[0] = coordinates[0];
                row[1] = iterator++;
            }
        }

        Arrays.fill(shipStatus, "O");
    }

    @Override
    public String toString() {
        return getName() + "," + getSize() + ", is sunk: " + isSunk() + ", is ready: " + isReady();
    }

    public boolean isReady() {
        return isSet;
    }

    public void setReady(boolean isReady) {
        this.isSet = isReady;
    }
}