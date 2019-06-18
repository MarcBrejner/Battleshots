package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private List<Ship> ships = new ArrayList<Ship>();
    private List<Point> grid;
    private Point startPoint;
    private int gridSize;
    private int shipAmount = 0;


    public GameModel(int gridSize) {
        grid = new ArrayList<Point>();
        makeGrid(gridSize);
    }

    public void makeGrid(int size) {
        gridSize = size;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                grid.add(new Point(i, j, Status.VACANT));
            }
        }
    }

    public void setStartPoisiton(Point point){
        startPoint = point;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void addShip(Point point, int length, Direction direction) throws ShipException{
        if (direction == Direction.DOWN && gridSize <= (length-1)+point.getY() ||
                direction == Direction.UP && gridSize > (length-1)-point.getY() ||
                direction == Direction.LEFT && gridSize <= (length-1)+point.getX() ||
                direction == Direction.RIGHT && gridSize > (length-1)-point.getX()) {
            throw new ShipException("Ship out of boundaries");
            // TODO: Needs a toast message with the string "Ship out of boundaries"
        }
        else {
            Ship ship = new Ship(grid, point, length, direction, "Ship_" + ++shipAmount);
            ships.add(ship);
            for(Point shipPart: ship.getShip()) {
                grid.get(convertPointToIndex(shipPart)).setStatus(Status.OCCUPIED);
            }
        }
    }
    public int convertPointToIndex(Point point) {
        return point.getX() * gridSize + point.getY();
    }

    public int getShipAmount() {
        return ships.size();
    }

    public List<Point> getGrid() {
        return grid;
    }

}
