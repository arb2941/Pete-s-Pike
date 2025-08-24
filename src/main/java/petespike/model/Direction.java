package petespike.model;

public enum Direction {
    UP("UP"),
    DOWN("DOWN"),
    LEFT("LEFT"),
    RIGHT("RIGHT"); 
    
    private final String name;

    /**
     * Self constructor for assigning String names
     * @param name for enum value
     */
    private Direction (String name){
        this.name = name;
    }

    /**
     * Gets the name for the enum value
     * @return name of enum value
     */
    public String getName() {
        return name;
    }
}
