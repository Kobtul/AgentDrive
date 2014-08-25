package cz.agents.highway.agent;

import cz.agents.highway.storage.RoadObject;
import cz.agents.highway.storage.plan.Action;
import cz.agents.highway.storage.plan.WPAction;
import cz.agents.highway.vanet.Status;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ondra on 25.8.14.
 */
public class V2VAgent extends RouteAgent {

    public Collection<Status> statesOfConnectedObjects;

    public V2VAgent(int id) {
        super(id);
    }

    @Override
    protected List<Action> agentReact() {
        LinkedList<Action> actions = new LinkedList<Action>();
        RoadObject me = sensor.senseCurrentState();

        // Simulator did not send update yet
        if (me == null) {
            actions.add(new WPAction(id, 0d, getInitialPosition(), 0));
            return actions;
        }

        Point2f position2D = new Point2f(me.getPosition().getX(), me.getPosition().getY());

        List<Point2f> wps = new LinkedList<Point2f>();
        Point2f waypoint = null;

        int wpCount = Math.max(3, (int) (me.getVelocity().length() * WP_COUNT_CONST));
        navigator.setCheckpoint();

        //try to advance navigator closer to the actual position
        int a = 10;
        while (a-- > 0 && navigator.getRoutePoint().distance(position2D) > WAYPOINT_DISTANCE / 2) {
            navigator.advanceInRoute();
        }
        if( navigator.getRoutePoint().distance(position2D) > WAYPOINT_DISTANCE / 2){
            navigator.resetToCheckpoint();
        }else {
            navigator.setCheckpoint();
        }
        waypoint = navigator.getRoutePoint();

        for (int i = 0; i < wpCount; i++) {
            // If the next waypoint is too close, go to the next in route
            while (waypoint.distance(navigator.getRoutePoint()) < WAYPOINT_DISTANCE){
                navigator.advanceInRoute();
            }
            waypoint = navigator.getRoutePoint();
            wps.add(waypoint);
            actions.add(new WPAction(sensor.getId(), me.getUpdateTime(),
                    new Point3f(waypoint.x, waypoint.y, me.getPosition().z), MAX_SPEED));
        }
        navigator.resetToCheckpoint();

        if(!statesOfConnectedObjects.isEmpty())vanetCalculation();

        return actions;
    }
    public Collection<Status> getStatesOfConnectedObjects() {
        return statesOfConnectedObjects;
    }

    public void vanetCalculation(){
        Status myStatus =  sensor.senseCurrentState().getStatus();
    }
}
