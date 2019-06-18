package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private Direction direction;
    private Point point;
    private int length, gridSize;
    private String shipName;
    private List<Point> ship, grid;

    public Ship(List<Point> grid, Point point, int length, Direction direction, String name) {
        shipName = name;
        this.point = point;
        this.grid = grid;
        ship = new ArrayList<Point>(length);
        this.length = length;
        this.gridSize = gridSize;
        this.direction = direction;
        createShip();
    }

    public void createShip() {
        if (direction == Direction.LEFT) {
            for(int i = 0; i < length; i++) {
                ship.add(new Point(point.getX()+i, point.getY(), Status.OCCUPIED));
            }
        } else if (direction == Direction.RIGHT){
            for(int i = 0; i < length; i++) {
                ship.add(new Point(point.getX()-i, point.getY(), Status.OCCUPIED));
            }
        } else if (direction == Direction.UP) {
            for(int i = 0; i < length; i++) {
                ship.add(new Point(point.getX(), point.getY()-i, Status.OCCUPIED));
            }
        } else if (direction == Direction.DOWN) {
            for(int i = 0; i < length; i++) {
                ship.add(new Point(point.getX(), point.getY()+i, Status.OCCUPIED));
            }
        }
    }

    public int getShipSize() {
        return ship.size();
    }

    public List<Point> getShip() {
        return ship;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String name) {
        shipName = name;
    }

    public boolean isDestroyed() {
        for (Point shipPart : ship) {
            if (shipPart.getStatus() != Status.HIT) {
                return false;
            }
        }
        return true;
    }

    public void checkShot(Point shot) {
        for (Point shipPart : ship) {
            for(Point point: grid) {
                if (shot.equals(shipPart) && shipPart.getStatus() != Status.HIT) {
                    shipPart.setStatus(Status.HIT);
                    if(point.equals(shipPart) && point.getStatus() != Status.HIT) {
                        point.setStatus(Status.HIT);
                    }
                }

            }
        }
    }
}
