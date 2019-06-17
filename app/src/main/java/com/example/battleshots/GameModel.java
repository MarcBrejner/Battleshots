package com.example.battleshots;

import java.util.ArrayList;

public class GameModel {
    private ArrayList<Ship> ships = new ArrayList<Ship>();
    private int shipAmount = 0;


    public GameModel() {

    }

    public void addShip(Point point, int length, Direction direction) {
        Ship ship = new Ship(point, length, direction, "Ship_" + ++shipAmount);
        ships.add(ship);
    }

    public int getShipAmount() {
        return ships.size();
    }



}
