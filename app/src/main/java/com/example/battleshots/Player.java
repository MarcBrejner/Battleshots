package com.example.battleshots;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerName;
    private int gridSize = 8;
    public List<Point> grid, shipList;
    private List<Ship> ships;
    public boolean hasShip = false, noShipsLeft = false;

    public Player(String name) {
        this.playerName = name;
        grid = new ArrayList<>();
        ships = new ArrayList<>();
        shipList = new ArrayList<>();
        makeGrid();
    }

    public void addShip(Point point, int length, Direction direction, GameModel gameModel){
        for(Point shipPoint : shipList) {
            Log.d("shipLits:", shipPoint.toString());
            Log.d("Ship Size: ",""+ ships.size());
        }
        // hasShip = hasShipInside(point, direction, length);
        if (!hasShip) {
            if (direction == Direction.DOWN && 0 > point.getY() - (length - 1) ||
                    direction == Direction.UP && gridSize <= (length - 1) + point.getY() ||
                    direction == Direction.LEFT && gridSize <= (length - 1) + point.getX() ||
                    direction == Direction.RIGHT && 0 > point.getX() - (length - 1)) {
                hasShip = true;
            } else if (ships.size() >= 4) {
                noShipsLeft = true;
            } else {
                Ship ship = new Ship(point, length, direction, "Ship_" + length);
                ships.add(ship);
                for (Point shipPart : ship.getShip()) {
                    shipList.add(shipPart);
                    grid.get(gameModel.convertPointToIndex(shipPart)).setStatus(Status.DEPLOYED);
                }
            }
        }
    }


    public boolean hasShipInside(Point point, Direction direction, int length) {


        for (int i = 0; i < length; i++) {
            if (direction == Direction.LEFT && shipList.contains(new Point(point.getX() + i, point.getY()))) {
                return true;
            } else if (direction == Direction.RIGHT && shipList.contains(new Point(point.getX() - i, point.getY()))) {
                return true;
            } else if (direction == Direction.UP && shipList.contains(new Point(point.getX(), point.getY() - i))) {
                return true;
            } else if (direction == Direction.DOWN && shipList.contains(new Point(point.getX(), point.getY() + i))) {
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
