package cz.agents.highway.agent;

import cz.agents.highway.storage.RoadObject;
import cz.agents.highway.storage.plan.Action;
import cz.agents.highway.storage.plan.WPAction;
import cz.agents.highway.vanet.Signals;
import cz.agents.highway.vanet.Status;
import cz.agents.highway.vanet.Vanet;
import cz.agents.highway.vanet.VanetTest;

import javax.vecmath.Point3f;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ondra on 25.8.14.
 */
public class V2VAgent extends RouteAgent {

    private VanetTest vanetTest;

    private Vanet vanet;

    public V2VAgent(int id, Vanet vanet) {
        super(id);
        this.vanetTest = new VanetTest(navigator, vanet);
        this.vanet = vanet;
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



        actions = navigator.getNextActions(me);
        Status myStatus = me.getStatus();
        myStatus.setActions(navigator.getNextActionsWithReset(me));
        Signals signals = vanetTest.testAll(myStatus, vanet.getConnectedStates(id));
        System.out.println("Ondra test: testing signals from Vanet");
        if(signals.isCollisionWay()) System.out.println("PROBLEM : car "+id+" is in collision way");
        return actions;
    }

    public LinkedList<Action> vanetCalculation(LinkedList<Action> actions) {
        RoadObject me = sensor.senseCurrentState();
        Status myStatus = me.getStatus();

        actions.clear();
        actions.add(new WPAction(sensor.getId(), me.getUpdateTime(),
                new Point3f(myStatus.getPosition().x, myStatus.getPosition().y, 0), 0));
        return actions;
    }

}



