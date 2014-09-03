package cz.agents.highway.vanet;

import cz.agents.alite.common.event.Event;
import cz.agents.alite.environment.eventbased.EventBasedStorage;
import cz.agents.alite.simulation.SimulationEventType;
import cz.agents.highway.agent.Agent;
import cz.agents.highway.environment.HighwayEnvironment;
import cz.agents.highway.storage.HighwayEventType;
import cz.agents.highway.storage.RadarData;
import cz.agents.highway.storage.RoadObject;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by ondra on 13.8.14.
 *
 * Main class of vanet system which connect objects by settled rules.
 */
public class Vanet  extends EventBasedStorage {
    final double MAX_CONNECTION_DIST = 30;
    private final Logger logger = Logger.getLogger(Vanet.class);

    private HighwayEnvironment environment;

    private int lastUpdate = 0;
    private Map<Integer, Agent> agents;
    private final Map<Integer, RoadObject> includedObjects = new LinkedHashMap<Integer, RoadObject>();
    private final Map<Integer, ArrayList<RoadObject>> connectedObjects = new LinkedHashMap<Integer, ArrayList<RoadObject>>();
    private final Map<Integer, Status> states = new HashMap<Integer, Status>();
    private  LinkedHashMap<Integer, Collection<Status>> distribStates = new LinkedHashMap<Integer, Collection<Status>>();

    public Vanet(HighwayEnvironment environment,Map<Integer, Agent> agents){
        super(environment);
        environment.getEventProcessor().addEventHandler(this);
        this.environment = environment;
        this.agents = agents;
    }

    @Override
    public void handleEvent(Event event) {

        if (event.isType(SimulationEventType.SIMULATION_STARTED)) {
            logger.info("Vanet: handled simulation START");
        }else if(event.isType(HighwayEventType.RADAR_DATA)){
            logger.info("Vanet: handled: RADAR_DATA");
            RadarData radar_data = (RadarData) event.getContent();
            updateObjects(radar_data);
        }

    }

    public void updateObject(RoadObject object) {
        int carId = object.getId();
        includedObjects.put(carId, object);
        connectedObjects.put(carId, new ArrayList<RoadObject>());
        Status st = object.getStatus();
        st.setActions(agents.get(carId).getNavigator().getNextActionsWithReset(object));
        states.put(carId, st);

    }
    public void removeObject(RoadObject object){
        includedObjects.remove(object);
    }


    public void updateObjects(RadarData objects) {
        for (RoadObject object : objects.getCars()) {
            updateObject(object);
        }
        logger.info("Vanet updated vehicles: received " + objects);

        for (RoadObject object : includedObjects.values()) {
            for (RoadObject roadObject : includedObjects.values()){
                if (object != roadObject){
                    if(inConnectionDistance(object, roadObject)){
                        if(!connectedObjects.get(object.getId()).contains(roadObject)){
                            connectedObjects.get(object.getId()).add(roadObject);
                        }
                    }else{
                        connectedObjects.get(object.getId()).remove(roadObject);
                    }
                }
            }
        }
        distribStates = distributeStates();
    }

    public LinkedHashMap<Integer, Collection<Status>> distributeStates(){
        LinkedHashMap<Integer, Collection<Status>> distStates = new LinkedHashMap<Integer, Collection<Status>>();
        for (Integer objectID : connectedObjects.keySet()){
            Collection<Status> statesToSend = new LinkedList<Status>();
            for (RoadObject object : connectedObjects.get(objectID)){
                statesToSend.add(object.getStatus());
            }
//            includedObjects.get(objectID).setReceivedStates(statesToSend);
            distStates.put(objectID, statesToSend);
        }
        return distStates;
    }

    private boolean inConnectionDistance(Integer idObj1, Integer idObj2){
        RoadObject obj1 = includedObjects.get(idObj1);
        RoadObject obj2 = includedObjects.get(idObj2);
        double distance = obj1.getPosition().distance(obj2.getPosition());
        return distance < MAX_CONNECTION_DIST;
    }

    private boolean inConnectionDistance(RoadObject obj1, RoadObject obj2){
        double distance = obj1.getPosition().distance(obj2.getPosition());
        return distance < MAX_CONNECTION_DIST;
    }


    public Map<Integer,RoadObject> getIncludedObjects() {
        return includedObjects;
    }

    public Map<Integer, ArrayList<RoadObject>> getConnectedObjects() {
        return connectedObjects;
    }

    public Collection<Status> getConnectedStates(int id){
        return distribStates.get(id);
    }

}
