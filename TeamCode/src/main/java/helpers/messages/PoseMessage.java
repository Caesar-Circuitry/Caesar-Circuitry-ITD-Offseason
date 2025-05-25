package helpers.messages;

import com.pedropathing.localization.Pose;

public class PoseMessage {
    public long timestamp;
    public double x;
    public double y;
    public double heading;

    public PoseMessage(Pose pose) {
        this.timestamp = timestamp;
        this.x = pose.getX();
        this.y = pose.getY();
        this.heading = pose.getHeading();
    }
}
