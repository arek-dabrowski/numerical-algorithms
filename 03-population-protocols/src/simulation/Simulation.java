package simulation;

import java.util.Random;

public class Simulation {
    private int numberOfYes;
    private int numberOfNo;
    private int numberOfUndefined;
    private int numberOfAgents;
    private int numberOfIterations = 0;
    private Random random;
    private Agent[] agents;

    public Simulation(int numberOfAgents, int numberOfYes, int numberOfNo){
        this.numberOfAgents = numberOfAgents;
        this.numberOfYes = numberOfYes;
        this.numberOfNo = numberOfNo;
        this.numberOfUndefined = numberOfAgents - numberOfYes - numberOfNo;
        this.random = new Random();
        createSimulation();
    }

    public void createSimulation(){
        agents = new Agent[numberOfAgents];
        for (int i = 0; i < numberOfYes; i++) {
            agents[i] = new Agent(Agent.State.YES);
        }
        for (int i = numberOfYes; i < numberOfYes + numberOfNo; i++) {
            agents[i] = new Agent(Agent.State.NO);
        }
        for (int i = numberOfYes + numberOfNo; i < numberOfAgents; i++) {
            agents[i] = new Agent(Agent.State.UNDEFINED);
        }
    }

    public Agent.State doSimulation(){
        while(numberOfYes != numberOfAgents && numberOfNo != numberOfAgents && numberOfUndefined != numberOfAgents){
            int firstIndex = random.nextInt(numberOfAgents);
            int secondIndex = random.nextInt(numberOfAgents);
            Agent.Change change = agents[firstIndex].interaction(agents[secondIndex]);
            switch(change){
                case INC_YES: numberOfYes++;
                    numberOfUndefined--;
                    break;
                case INC_NO: numberOfNo++;
                    numberOfUndefined--;
                    break;
                case INC_UNDEFINED: numberOfUndefined+=2; numberOfYes--; numberOfNo--;
                    break;
            }
            numberOfIterations++;
        }

        if(numberOfYes == numberOfAgents){
            return Agent.State.YES;
        }
        if(numberOfNo == numberOfAgents){
            return Agent.State.NO;
        }
        return Agent.State.UNDEFINED;
    }
}
