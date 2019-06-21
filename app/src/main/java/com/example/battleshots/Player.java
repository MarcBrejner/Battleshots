package com.example.battleshots;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private int gridSize;
    private int shipAmount = 4;
    private List<Point> grid;
    private List<Ship> ships;

    public Player(String name, int gridSize) {
        this.gridSize = gridSize;
        this.playerName = name;
        grid = new ArrayList<>();
        ships = new ArrayList<>();
        makeGrid();
    }

    public Ship addShip(Point point, int length, Direction direction, GameModel gameModel) throws ShipException{
        if (direction == Direction.DOWN && gridSize <= (length-1)+point.getY() ||
                direction == Direction.UP && 0 > point.getY()-(length-1) ||
                direction == Direction.LEFT && gridSize <= (length-1)+point.getX() ||
                direction == Direction.RIGHT && 0 > point.getX()-(length-1)) {
            throw new ShipException("Ship out of boundaries");
            // TODO: Needs a toast message with the string "Ship out of boundaries"
        }
        else {
            Ship ship = new Ship(point, length, direction, "Ship_" + ++shipAmount);
            ships.add(ship);
            for(Point shipPart: ship.getShip()) {
                grid.get(gameModel.convertPointToIndex(shipPart)).setStatus(Status.DEPLOYED);
            }
            return ship;
        }
    }

    public void makeGrid() {
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                grid.add(new Point(j, i, Status.VACANT));
            }
        }
    }

    public int getShipAmount() {
        return ships.size();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<Point> getGrid() {
        return grid;
    }

    public void setGrid(List<Point> grid) {
        this.grid = grid;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShip(List<Ship> ship) {
        this.ships = ship;
    }

}
