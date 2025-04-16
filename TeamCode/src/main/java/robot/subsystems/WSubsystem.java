package robot.subsystems;

public abstract class WSubsystem {
    /**
     * put all code that reads hardware in this function
     */
    public abstract void read();
    /**
     * stores all non hardware accessing code in this function
     */
    public abstract void loop();
    /**
     * put all code that writes to hardware in this function
     */
    public abstract void write();

}
