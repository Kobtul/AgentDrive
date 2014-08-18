package cz.agents.highway.vanet;

import cz.agents.highway.storage.RoadObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ondra on 13.8.14.
 *
 * Interface, which id necessary to implement on object object, which you want to add to
 * this vanet system. It has to be able to send its status and received states of objects,
 * which are connected together by network.
 */
public interface CommunicationInterface {

    ArrayList<RoadObject> connectedObjects = new ArrayList<RoadObject>();
    /*
    * Procedure to send its status to network
    * */
    public Status getStatus();
    /*
    * Procedure to save states of connected object
    * */
    public void setReceivedStates(Collection<Status> states);
}
