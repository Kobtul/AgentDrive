package cz.agents.highway.vanet;

/**
 * Created by ondra on 27.8.14.
 */
public class Signals {

    private boolean dangerousTurnRight;
    private boolean dangerousTurnLeft;
    private boolean collisionWay;

    public boolean isCollisionWay() {
        return collisionWay;
    }

    public void setCollisionWay(boolean collisionWay) {
        this.collisionWay = collisionWay;
    }

    public boolean isDangerousTurnLeft() {
        return dangerousTurnLeft;
    }

    public void setDangerousTurnLeft(boolean dangerousTurnLeft) {
        this.dangerousTurnLeft = dangerousTurnLeft;
    }

    public boolean isDangerousTurnRight() {
        return dangerousTurnRight;
    }

    public void setDangerousTurnRight(boolean dangerousTurnRight) {
        this.dangerousTurnRight = dangerousTurnRight;
    }

}
