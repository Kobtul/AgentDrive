package cz.agents.highway.vanet;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Created by ondra on 13.8.14.
 *
 * Object to save features of object
 */
public class Status {

    private int ID;
    private Point3f position;
    private Vector3f velocity;

    private String message;

    public Status(String message){
        this.message = message;
    }

    public Status(){

    }

    @Override
    public String toString() {
        String statesToPrint = "";
        statesToPrint += "\nobject ID " + getID();
        statesToPrint += "\nobject position " + getPosition();
        statesToPrint += "\nobject velocity " + getVelocity();
        statesToPrint += "\nobject message " + getMessage();
        return statesToPrint;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Point3f getPosition() {
        return position;
    }

    public void setPosition(Point3f position) {
        this.position = position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }
}
