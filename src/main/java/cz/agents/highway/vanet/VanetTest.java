package cz.agents.highway.vanet;

import cz.agents.highway.agent.RouteNavigator;
import cz.agents.highway.storage.plan.Action;
import cz.agents.highway.storage.plan.WPAction;
import cz.agents.highway.util.Utils;

import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import java.util.*;

/**
 * Created by ondra on 26.8.14.
 */
public class VanetTest {

    private final double ACCELERATION_BREAKS = 6; // m/s2
    private  final double SAFE_DISTANCE = 3;
    private final double TIME_PREDICT = 7;
    private  final double SAFE_SIDE_DISTANCE = 3;
    private final double delTime = 0.1;

    private Status myStatus;
    private Collection<Status> states;
    private RouteNavigator navigator;
    private Vanet vanet;

    public boolean notSafeForwardDistance;

    public VanetTest(RouteNavigator navigator, Vanet vanet){
        this.navigator = navigator;
        this.vanet = vanet;
    }

    public LinkedList<Action> vanetAdvise(LinkedList<Action> actions){
        setStates(vanet.getConnectedStates(myStatus.getID()));
        return actions;
    }

    public void setStates(Collection<Status> states) {
        this.states = states;
    }



    public Signals testAll(Status status, Collection<Status> states) {
        Signals signals = new Signals();
        setStates(vanet.getConnectedStates(status.getID()));
        signals.setCollisionWay(collision(status, states));
        return signals;
    }

    private boolean collision(Status status, Collection<Status> states){
        System.out.println("VANET: Testing collisions");
        LinkedList<Point3d> myPosAfterDelTime = countTrace(status);
        System.out.println(""+myPosAfterDelTime);
        for(Status other : states){
            System.out.println("VANET: testing");
            LinkedList<Point3d> otherPosAfterDelTime = countTrace(other);
            for(int i = 0; i < myPosAfterDelTime.size() && i < otherPosAfterDelTime.size(); i++){

//                System.out.println(""+myPosAfterDelTime.get(i).distance(otherPosAfterDelTime.get(i)));
                if(myPosAfterDelTime.get(i).distance(otherPosAfterDelTime.get(i)) < SAFE_DISTANCE){
                    System.out.println("VANET: Collision detect");
                    return true;
                }
            }
        }
        return false;
    }

    private LinkedList<Point3d> countTrace(Status status){
        LinkedList<Point3d> myPosAfterDelTime = new LinkedList<Point3d>();
        myPosAfterDelTime.add(new Point3d(status.getPosition().x, status.getPosition().y, 0));
        if (status.getActions() == null){
            return myPosAfterDelTime;
        }
        ListIterator<Action> actions = status.getActions().listIterator();
        Point3f actPos = status.getPosition();
        double actSpeed = status.getSpeed();
        WPAction next = (WPAction) actions.next();
        double length = actPos.distance(next.getPosition());
        double checkPointTime = (length / actSpeed);
        double time = (length / actSpeed);
        double xDiv = (next.getPosition().x - actPos.x) / time;
        double yDiv = (next.getPosition().y - actPos.y) / time;
        for(double i = delTime; i < TIME_PREDICT && actions.hasNext(); i += delTime){
            if(i > checkPointTime){
                double newStarTime = i - checkPointTime;
                double restTime = delTime - newStarTime;
                double newX = myPosAfterDelTime.getLast().x+ xDiv * restTime;
                double newY = myPosAfterDelTime.getLast().y + yDiv * restTime;

                actPos = next.getPosition();
                actSpeed = next.getSpeed();
                next = (WPAction) actions.next();
                length = actPos.distance(next.getPosition());
                time = (length / actSpeed);
                xDiv = (next.getPosition().x - actPos.x) / time;
                yDiv = (next.getPosition().y - actPos.y) / time;

                newX += xDiv * newStarTime;
                newY += yDiv * newStarTime;
                myPosAfterDelTime.add(new Point3d(newX, newY, 0));

                checkPointTime += time;
            }else{
                double newX = myPosAfterDelTime.getLast().x+ xDiv * delTime;
                double newY = myPosAfterDelTime.getLast().y + yDiv * delTime;
                myPosAfterDelTime.add(new Point3d(newX, newY, 0));
            }

        }

        return myPosAfterDelTime;
    }

//    private LinkedList<Point3d> countTrace(Status status){
//        LinkedList<Point3d> myPosAfterDelTime = new LinkedList<Point3d>();
//        myPosAfterDelTime.add(new Point3d(status.getPosition().x, status.getPosition().y, 0));
//        if (status.getActions() != null){
//            Point3f actPos = status.getPosition();
//            double actSpeed = status.getSpeed();
//            double startTime = 0;
//            double totalLength = 0;
//            for(Action wp : status.getActions()){
//                WPAction next = (WPAction) wp;
//
//                double length = actPos.distance(next.getPosition());
//                if(totalLength < COUTING_RANGE){
//                    double time = (length / actSpeed);
//                    double changeDistance = actSpeed * delTime;
//                    double xDiv = (next.getPosition().x - actPos.x) / time;
//                    double yDiv = (next.getPosition().y - actPos.y) / time;
//                    if(startTime != 0) myPosAfterDelTime.add(new Point3d(actPos.x + xDiv * startTime, actPos.y + yDiv * startTime, 0));
//                    double actDist = (startTime * actSpeed) + changeDistance;
//                    for(; actDist < length; actDist += changeDistance){
//                        double newX = myPosAfterDelTime.getLast().x+ xDiv * delTime;
//                        double newY = myPosAfterDelTime.getLast().y + yDiv * delTime;
//                        myPosAfterDelTime.add(new Point3d(newX, newY, 0));
//                    }
//                    actDist -= changeDistance;
//                    startTime = delTime -(length - actDist)/actSpeed;
//                    totalLength += length;
//                }
//                actPos = next.getPosition();
//                actSpeed = next.getSpeed();
//
//            }
//        }
//
//        return myPosAfterDelTime;
//    }

//    private boolean collision2(Status status){
//        LinkedList<Point3d> myPosAfterDelTime = new LinkedList<Point3d>();
//        Point3f actPos = status.getPosition();
//        double actSpeed = status.getSpeed();
//        double startTime = 0;
//        double totalLenght = 0;
//        for(Action wp : status.getActions()){
//            WPAction next = (WPAction) wp;
//            double lenght = actPos.distance(next.getPosition());
//            if(totalLenght < SAFE_DISTANCE){
//                double numOfSteps = (lenght / status.getSpeed()) / delTime;
//                double xDiv = (next.getPosition().x - actPos.x) / numOfSteps;
//                double yDiv = (next.getPosition().y - actPos.y) / numOfSteps;
//                for(int i = 0; i < numOfSteps; i++){
//                    myPosAfterDelTime.add(new Point3d(actPos.x + xDiv, actPos.y + yDiv, 0));
//                }
//                startTime = delTime - (lenght / status.getSpeed() - (int)(numOfSteps)*delTime);
//                totalLenght += lenght;
//            }
//
//        }
//        for(Status other : states){
//
//        }
//        return false;
//    }
}
