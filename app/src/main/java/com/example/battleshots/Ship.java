package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private Direction direction;
    private Point point;
    private int length;
    private String shipName;
    private List<Point> ship;

    public Ship(Point point, int length, Direction direction, String name) {
        shipName = name;
        this.point = point;
        ship = new ArrayList<Point>(length);
        this.length = length;
        this.direction = direction;
        createShip();
    }

    public void createShip() {
        point.setStatus(Status.DEPLOYED);
        if (length == 1) {
           ship.add(point);
        } else if (direction == Direction.LEFT) {
            ship.add(point);
            for(int i = 1; i < length; i++) {
                ship.add(new Point(point.getX()+i, point.getY(), Status.DEPLOYED));
            }
        } else if (direction == Direction.RIGHT){
            ship.add(point);
            for(int i = 1; i < length; i++) {
                ship.add(new Point(point.getX()-i, point.getY(), Status.DEPLOYED));
            }
        } else if (direction == Direction.UP) {
            ship.add(point);
            for(int i = 1; i < length; i++) {
                ship.add(new Point(point.getX(), point.getY()-i, Status.DEPLOYED));
            }
        } else if (direction == Direction.DOWN) {
            ship.add(point);
            for(int i = 1; i < length; i++) {
                ship.add(new Point(point.getX(), point.getY()+i, Status.DEPLOYED));
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
}

