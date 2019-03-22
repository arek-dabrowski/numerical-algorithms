package simulation;

import types.MyDouble;

public class MonteCarlo {

    public MyDouble[] test(int numberOfSimulations, int numberOfAgents, int matrixSize){
        int agentNo = -1, agentYes=0;
        MyDouble[] results = new MyDouble[matrixSize];

        for (int j = 0; j < matrixSize; j++) {
            if((agentNo+agentYes)!=numberOfAgents)
                agentNo++;
            else{
                agentYes++;
                agentNo = 0;
            }
            double result = getProbabilityOfYes(numberOfSimulations, numberOfAgents, agentYes, agentNo);
            results[j] = new MyDouble(result);
            //System.out.println("yes: " + agentYes + " no: " + agentNo + " result: " + result);
        }
        return results;
    }

    public double getProbabilityOfYes(int numberOfSimulations, int numberOfAgents,int agentYes, int agentNo){
            int positiveResult = 0;
            for (int i = 0; i < numberOfSimulations; i++) {
                Simulation s = new Simulation(numberOfAgents, agentYes, agentNo);
                Agent.State result = s.doSimulation();
                if(result == Agent.State.YES){
                    positiveResult++;
                }
            }

        return (double)positiveResult/(double)numberOfSimulations;
    }
}
