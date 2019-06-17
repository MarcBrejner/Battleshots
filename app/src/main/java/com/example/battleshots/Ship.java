package com.example.battleshots;

import java.util.ArrayList;

public class Ship {

    private Direction direction;
    private Point point;
    private int length;
    private String shipName;
    private ArrayList<Point> ship;

    public Ship(Point point, int length, Direction direction, String name) {
        shipName = name;
        this.point = point;
        ship = new ArrayList<Point>(length);
        this.length = length;
        this.direction = direction;
        createShip();
    }

    public void createShip() {
        if (direction == Direction.LEFT) {
            for(int i = 0; i < length; i++) {
                ship.add(new Point(point.getX()+i, point.getY()));
            }
        } else if (direction == Direction.RIGHT){
            for(int i = 0; i < length; i++) {
                ship.add(new Point(point.getX()-i, point.getY()));
            }
        } else if (direction == Direction.UP) {
            for(int i = 0; i < length; i++) {
                ship.add(new Point(point.getX(), point.getY()-i));
            }
        } else if (direction == Direction.DOWN) {
            for(int i = 0; i < length; i++) {
                ship.add(new Point(point.getX(), point.getY()+i));
            }
        }
    }

    public int getShipSize() {
        return ship.size();
    }

    public ArrayList<Point> getShip() {
        return ship;
    }

    public String getShipName() {
        return shipName;
    }

    public boolean isDestroyed() {
        int amountOfShipPartsHitted = 0;
        for (Point shipPart : ship) {
            if (shipPart.getHitted()) {
                amountOfShipPartsHitted++;
            }
        }
        return amountOfShipPartsHitted == length;
    }

    public void checkShot(Point shot) {
        for (Point shipPart : ship) {
            if (shipPart.equals(shot)) {
                shipPart.setHitted(true);
            }
        }
    }
}
