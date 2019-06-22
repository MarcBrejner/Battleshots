package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private int gridSize = 8, shipAmount;
    private List<Point> grid;
    private List<Ship> ships;
    public boolean hasShip = false;

    public Player(String name) {
        this.playerName = name;
        grid = new ArrayList<>();
        ships = new ArrayList<>();
        makeGrid();
    }

    public void addShip(Point point, int length, Direction direction, GameModel gameModel){
        List<Point> shipList = new ArrayList<>();
        for (Ship ship: ships) {
            for (Point shipPart: ship.getShip()) {
                shipList.add(shipPart);
            }
        }
        if (direction == Direction.DOWN && gridSize <= (length-1)+point.getY() ||
                direction == Direction.UP && 0 > point.getY()-(length-1) ||
                direction == Direction.LEFT && gridSize <= (length-1)+point.getX() ||
                direction == Direction.RIGHT && 0 > point.getX()-(length-1) ||
                shipList.contains(point)) {
                hasShip = true;
        } else {
            Ship ship = new Ship(point, length, direction, "Ship_" + ++shipAmount);
            ships.add(ship);
            for(Point shipPart: ship.getShip()) {
                grid.get(gameModel.convertPointToIndex(shipPart)).setStatus(Status.DEPLOYED);
            }
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
