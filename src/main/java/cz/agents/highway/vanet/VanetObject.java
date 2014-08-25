package cz.agents.highway.vanet;

import com.google.common.collect.Lists;
import cz.agents.highway.storage.RoadObject;

import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ondra on 14.8.14.
 */
public class VanetObject implements CommunicationInterface {


    public Collection<Status> statesOfConnectedObjects;

    public VanetObject(){
        statesOfConnectedObjects = new ArrayList<Status>();
    }

    public Collection<Status> getStatesOfConnectedObjects() {
        return statesOfConnectedObjects;
    }

    @Override
    public Status getStatus() {
        return null;
    }

    @Override
    public void setReceivedStates(Collection<Status> states) {
        statesOfConnectedObjects = states;
    }

    @Override
    public void useStates() {

    }
}
