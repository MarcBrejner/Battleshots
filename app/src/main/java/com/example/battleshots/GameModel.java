package com.example.battleshots;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private Point startPoint;
    final int gridSize = 8;
    private List<Player> players;
    boolean isHit=false;

    public boolean getIsHit() {
        return isHit;
    }

    public GameModel(String playerName) {
        if (players == null) {
            players = new ArrayList<>();
        }
        if (players.size() < 3) {
            players.add(new Player(playerName));
        }
    }

    public GameModel() {

    }

    public void addPlayerToGameModel(String playerName) {
        // Where we want to add the second player to the list.
        players.add(new Player(playerName));
    }

    public List<Player> getPlayers() {
        return players;
    }



    public void setStartPosition(Point point){
        startPoint = point;
    }

    public Point getStartPoint() {
        return startPoint;
    }


    public int convertPointToIndex(Point point) {
        return point.getX() * gridSize + point.getY();
    }

    public Point convertIndexToPoint(int index) {
        return new Point(index/gridSize,index%gridSize);
    }

    public void checkShot(Point shot, String playerName) {
        for (Player player : players) {
            if (player.getPlayerName().equals(playerName)) {
                Boolean shipGotHitted = false;
                for (Ship ship : player.getShips()) {
                    if (ship.getShip().contains(shot) && !shipGotHitted) {
                        for (Point shipPart : ship.getShip()) {
                            if (shot.equals(shipPart) && shipPart.getStatus() == Status.DEPLOYED) {
                                shipPart.setStatus(Status.HIT);
                                player.getGrid().get(player.getGrid()
                                        .indexOf(shipPart)).setStatus(Status.HIT);
                                shipGotHitted = true;
                                isHit = true;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                }
                if (!shipGotHitted) {
                    player.getGrid().get(player.getGrid().indexOf(shot))
                            .setStatus(Status.MISS);
                    isHit = false;
                }
            }
        }
    }
}
