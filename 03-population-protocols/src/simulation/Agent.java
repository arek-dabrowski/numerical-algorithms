package simulation;

public class Agent {
    State state;

    public enum State {
        YES, NO, UNDEFINED
    }
    public enum Change{
        INC_YES, INC_NO, INC_UNDEFINED, INC_NOTHING
    }

    public Agent(State state){
        this.state = state;
    }

    public Change interaction(Agent agent) {
        if(this.state == State.YES){
            if(agent.state == State.NO){
                this.state = State.UNDEFINED;
                agent.state = State.UNDEFINED;
                return Change.INC_UNDEFINED;
            }
            else if(agent.state == State.UNDEFINED){
                agent.state = State.YES;
                return Change.INC_YES;
            }
        }
        else if(this.state == State.NO){
            if(agent.state == State.YES){
                this.state = State.UNDEFINED;
                agent.state = State.UNDEFINED;
                return Change.INC_UNDEFINED;
            }
            else if(agent.state == State.UNDEFINED){
                agent.state = State.NO;
                return Change.INC_NO;
            }
        }
        else if(this.state == State.UNDEFINED){
            if(agent.state == State.YES){
                this.state = State.YES;
                return Change.INC_YES;
            }
            else if(agent.state == State.NO){
                this.state = State.NO;
                return Change.INC_NO;
            }
        }
        return Change.INC_NOTHING;
    }
}
