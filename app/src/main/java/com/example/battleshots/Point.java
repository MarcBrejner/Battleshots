package com.example.battleshots;

public class Point {
    private int x, y;
    private Status status;

    public Point(int x, int y, Status status) {
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object object) {
        Point other = (Point) object;
        if (this == object) {
            return true;
        }
        else if (this.getClass() != object.getClass()) {
            return false;
        }
        else if (this.x != other.x || this.y != other.y) {
            return false;
        }
        else {
            return true;
        }
    }

    public String toString() {
        return "x: " + x + " y: " + y;
    }
}
