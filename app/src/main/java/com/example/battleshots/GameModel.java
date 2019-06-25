package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    final int gridSize = 8;
    private List<Player> players;

    public GameModel(String playerName) {
        if (players == null) {
            players = new ArrayList<>();
        }
        if (players.size() < 3) {
            players.add(new Player(playerName));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int convertPointToIndex(Point point) {
        return point.getX() * gridSize + point.getY();
    }

    public Point convertIndexToPoint(int index) {
        return new Point(index%gridSize,index/gridSize);
    }
}
