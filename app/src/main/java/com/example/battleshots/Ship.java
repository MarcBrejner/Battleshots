package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class Ship {

    private Direction direction;
    private Point point;
    private int length;
    private String shipName;
    private List<Point> ship, grid;

    public Ship(List<Point> grid, Point point, int length, Direction direction, String name) {
        shipName = name;
        this.point = point;
        this.grid = grid;
        ship = new ArrayList<Point>(length);
        this.length = length;
        this.direction = direction;
        createShip();
    }

    public void createShip() {
        if (direction == Direction.LEFT) {
            for(int i = 0; i < length; i++) {
                Point newPoint = new Point(point.getX()+i, point.getY(), Status.OCCUPIED);
                grid.get(grid.indexOf(newPoint)).setStatus(Status.OCCUPIED);
                ship.add(newPoint);
            }
        } else if (direction == Direction.RIGHT){
            for(int i = 0; i < length; i++) {
                Point newPoint = new Point(point.getX()-i, point.getY(), Status.OCCUPIED);
                grid.get(grid.indexOf(newPoint)).setStatus(Status.OCCUPIED);
                ship.add(newPoint);
            }
        } else if (direction == Direction.UP) {
            for(int i = 0; i < length; i++) {
                Point newPoint = new Point(point.getX(), point.getY()-i, Status.OCCUPIED);
                grid.get(grid.indexOf(newPoint)).setStatus(Status.OCCUPIED);
                ship.add(newPoint);
            }
        } else if (direction == Direction.DOWN) {
            for(int i = 0; i < length; i++) {
                Point newPoint = new Point(point.getX(), point.getY()+i, Status.OCCUPIED);
                grid.get(grid.indexOf(newPoint)).setStatus(Status.OCCUPIED);
                ship.add(newPoint);
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
                if (shot.equals(shipPart) && shipPart.getStatus() == Status.OCCUPIED) {
                    shipPart.setStatus(Status.HIT);
                    grid.get(grid.indexOf(shipPart)).setStatus(Status.HIT);
                    } else {
                    grid.get(grid.indexOf(shot)).setStatus(Status.MISS);
                }
            }
        }
    }

