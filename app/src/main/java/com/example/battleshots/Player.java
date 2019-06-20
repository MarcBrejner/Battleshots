package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Point> grid;
    private List<Ship> ship;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Point> getGrid() {
        return grid;
    }

    public void setGrid(List<Point> grid) {
        this.grid = grid;
    }

    public List<Ship> getShip() {
        return ship;
    }

    public void setShip(List<Ship> ship) {
        this.ship = ship;
    }

    public Player(String name) {
        this.name = name;

    }
}
