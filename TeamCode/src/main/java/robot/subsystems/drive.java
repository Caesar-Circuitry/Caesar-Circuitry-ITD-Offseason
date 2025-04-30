package robot.subsystems;

import com.pedropathing.follower.Follower;
import com.seattlesolvers.solverslib.controller.PIDFController;

public class drive extends WSubsystem {
  private final robotHardware hardware;
  private final Follower follower;
  private final PIDFController headinglock;
  private double lockedHeading = 0;
  private boolean isHeadingLocked = false;
  private double currentHeading = 0;

  public drive(robotHardware hardware) {
    this.hardware = hardware;
    follower = this.hardware.getFollower();
    this.headinglock = new PIDFController(0, 0, 0, 0); // use values from FConstants
  }

  @Override
  public void read() {
    if (isHeadingLocked) {
      this.currentHeading = follower.getTotalHeading();
    }
  }

  @Override
  public void loop() {
    follower.update();
  }

  @Override
  public void write() {}

  private void enableHeadingLock() {
    setLockedHeading(follower.getTotalHeading());
    headinglock.setSetPoint(this.lockedHeading);
  }

  private void setLockedHeading(double heading) {
    this.lockedHeading = wrapAngle(heading);
  }

  private void drivePowers(double x, double y, double rotation) {
    if (isHeadingLocked) {
      follower.setTeleOpMovementVectors(x, y, headinglock.calculate(this.currentHeading));
    }
    follower.setTeleOpMovementVectors(x, y, rotation);
  }

  private double wrapAngle(double angle) {
    while (angle > 180) {
      angle -= 360;
    }
    while (angle < -180) {
      angle += 360;
    }
    return angle;
  }
}
