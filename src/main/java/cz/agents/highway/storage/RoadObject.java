package cz.agents.highway.storage;

import cz.agents.highway.vanet.CommunicationInterface;
import cz.agents.highway.vanet.Status;
import cz.agents.highway.vanet.VanetObject;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.Collection;

public class RoadObject extends VanetObject{

    private int id = -1;
    private double updateTime = -1;
    private Point3f position;
    private Vector3f velocity;
    private int lane = -1;

    public RoadObject(int id, double updateTime, int lane, Point3f position, Vector3f velocity) {
        this.id = id;
        this.updateTime = updateTime;
        this.lane = lane;
        this.position = position;
        this.velocity = velocity;

    }

    public RoadObject(int id) {
       this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getUpdateTime() {
        return updateTime;
    }

    public int getLane() {
        return lane;
    }

    public Point3f getPosition() {
        return position;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    @Override
    public String toString() {
        return "RoadObject [id = " + id + ", updateTime=" + updateTime + ", lane=" + lane
                + ", pos=" + position + ", v=" + velocity + "]";

    }

    private String printStates(){
        String statesToPrint = "";
        for (Status st : getStatesOfConnectedObjects()){
            statesToPrint += st;
            System.out.println(st);
        }
        return statesToPrint;
    }

    @Override
    public Status getStatus() {
       Status status = new Status();
        status.setID(getId());
        status.setPosition(getPosition());
        status.setVelocity(getVelocity());
        return status;
    }

    @Override
    public void useStates() {
        if(getId() == 6 && !getStatesOfConnectedObjects().isEmpty()){
            this.velocity = new Vector3f(0,0,0);
        }
    }

}
