package com.example.battleshots;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Player {
    private String playerName;
    private int gridSize = 8;
    public List<Point> grid;
    public HashSet<Integer> shipList;
    public List<Ship> ships;

    public Player(String name) {
        this.playerName = name;
        grid = new ArrayList<>();
        ships = new ArrayList<>();
        shipList = new HashSet<>();
        makeGrid();
    }

    public void addShip(Point point, int length, Direction direction, GameModel gameModel){
        if (direction == Direction.DOWN && 0 > point.getY() + (length - 1) ||
                direction == Direction.UP && gridSize <= (length - 1) - point.getY() ||
                direction == Direction.LEFT && gridSize <= (length - 1) + point.getX() ||
                direction == Direction.RIGHT && 0 > point.getX() - (length - 1)) {

        } else if (!hasShipInside(point, direction, length, gameModel)) {
            Ship ship = new Ship(point, length, direction, "Ship_" + length);
            ships.add(ship);
            for (Point shipPart : ship.getShip()) {
                shipList.add(gameModel.convertPointToIndex(shipPart));
                grid.get(gameModel.convertPointToIndex(shipPart)).setStatus(Status.DEPLOYED);
            }
        }
    }


    public boolean hasShipInside(Point point, Direction direction, int length, GameModel gameModel) {
        for (int i = 0; i < length; i++) {
            if (direction == Direction.LEFT && shipList.contains(gameModel.convertPointToIndex(new Point(point.getX() + i, point.getY())))) {
                return true;
            } else if (direction == Direction.RIGHT && shipList.contains(gameModel.convertPointToIndex(new Point(point.getX() - i, point.getY())))) {
                return true;
            } else if (direction == Direction.UP && shipList.contains(gameModel.convertPointToIndex(new Point(point.getX(), point.getY() - i)))) {
                return true;
            } else if (direction == Direction.DOWN && shipList.contains(gameModel.convertPointToIndex(new Point(point.getX(), point.getY() + i)))) {
                return true;
            }
        }
        return false;
    }

    public void makeGrid() {
        for(int i = 0; i < gridSize; i++) {
            for(int j = 0; j < gridSize; j++) {
                grid.add(new Point(i, j, Status.VACANT));
            }
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Point> getGrid() {
        return grid;
    }

    public List<Ship> getShips() {
        return ships;
    }
}
