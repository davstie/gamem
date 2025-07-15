package game.enums;

public enum SubStates {

    SpellSelection(false);

    private boolean state;


    SubStates(boolean state){
        this.state = state;
    }

    public void setState(boolean b){
        state = b;
    }

    public boolean getState(){
        return state;
    }
}
