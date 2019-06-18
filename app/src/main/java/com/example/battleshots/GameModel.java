package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private List<Ship> ships = new ArrayList<Ship>();
    private List<Point> grid;
    private int gridSize;
    private int shipAmount = 0;


    public GameModel() {

    }

    public void makeGrid(int size) {
        gridSize = size;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                grid.add(new Point(i, j, Status.VACANT));
            }
        }
    }

    public void addShip(Point point, int length, Direction direction) throws ShipOutOfBoundariesException{
        if (direction == Direction.DOWN && gridSize <= length+point.getY() ||
                direction == Direction.UP && gridSize > length-point.getY() ||
                direction == Direction.LEFT && gridSize <= length+point.getX() ||
                direction == Direction.RIGHT && gridSize > length-point.getX()) {
            throw new ShipOutOfBoundariesException("Ship out of boundaries");
            // Needs a toast message with the string "Ship out of boundaries"
        }
        else {
            Ship ship = new Ship(grid, point, length, direction, "Ship_" + ++shipAmount);
            ships.add(ship);

        }
    }

    public int getShipAmount() {
        return ships.size();
    }



}
