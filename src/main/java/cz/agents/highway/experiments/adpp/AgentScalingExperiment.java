package cz.agents.highway.experiments.adpp;

import cz.agents.highway.agent.ADPPAgent;
import cz.agents.highway.agent.Agent;
import cz.agents.highway.experiments.Experiment;

import java.util.ArrayList;
import java.util.List;

/**
 * Experiment scaling the number of agents
 * Created by wmatex on 26.11.14.
 */
public class AgentScalingExperiment extends Experiment {
    /**
     * Run the experiment with the scaling quality
     *
     * @param quality Current value for the measured quality
     * @return
     */
    @Override
    public boolean run(double quality, boolean verbose) {
        // Quality is number of agents
        float[] speedArray = new float[speeds];
        for (int i = 1; i <= speeds; ++i) {
            speedArray[i-1] = i;
        }
        for (int i = 0; i < quality; ++i) {
            new ADPPAgent(i, speedArray, waitPenalty, movePenalty, heuristic, waitDuration, false, verbose, (i==0));
        }
        return false;
    }

    /**
     * Initialize the experiment
     */
    @Override
    protected void init() {

    }

}