package helpers.messages;

import com.pedropathing.localization.PoseUpdater;
import com.pedropathing.pathgen.Vector;

public final class DriveCommandMessage {
    public long timestamp;
    public double forwardVelocity;
    public double forwardAcceleration;
    public double lateralVelocity;
    public double lateralAcceleration;
    public double angularVelocity;
    public double angularAcceleration;

    public DriveCommandMessage(PoseUpdater poseUpdater) {
        this.timestamp = System.nanoTime();

        // Get linear velocities and accelerations
        Vector velocity = poseUpdater.getVelocity(); // x = forward, y = lateral
        Vector acceleration = poseUpdater.getAcceleration();

        this.forwardVelocity = velocity.getXComponent();
        this.lateralVelocity = velocity.getYComponent();
        this.forwardAcceleration = acceleration.getXComponent();
        this.lateralAcceleration = acceleration.getYComponent();

        // Angular velocity and acceleration (heading)
        this.angularVelocity = poseUpdater.getAngularVelocity();
        // PedroPathing does not have direct angular acceleration, so you may need to compute it:
        // Example: (currentAngularVelocity - previousAngularVelocity) / (dt)
        this.angularAcceleration = 0; // Placeholder: you could extend PoseUpdater to track this
    }
}