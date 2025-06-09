package config.robot.subsystems;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.PIDFController;
import com.seattlesolvers.solverslib.geometry.Vector2d;

public class drive extends WSubsystem {
  public enum driveState {
    ROBOT_CENTRIC_UNLOCKED,
    ROBOT_CENTRIC_LOCKED,
    FIELD_CENTRIC_UNLOCKED,
    FIELD_CENTRIC_LOCKED,
  }

  private driveState currentDriveState = driveState.ROBOT_CENTRIC_UNLOCKED;
  private final robotHardware hardware;
  private final Follower follower;
  private double lockedHeading = 0;
  private double currentHeading = 0;
  private Vector2d driveVector = new Vector2d(0, 0);
  private double driverotation = 0;
  private boolean headingLock = false;
  private boolean fieldCentric = false;
  private double headingCorrection = 0;
  private double targetHeading = Math.toRadians(180);
  private PIDFController headingPIDController =
      new PIDFController(FollowerConstants.headingPIDFCoefficients);

  public drive(robotHardware hardware) {
    this.hardware = hardware;
    follower = this.hardware.getFollower();
  }

  @Override
  public void read() {
    this.currentHeading = follower.getTotalHeading();
  }

  @Override
  public void loop() {
    follower.update();
  }

  @Override
  public void write() {
    setTeleOpMovement(driveVector, driverotation);
  }

  /**
   * @param heading in degrees
   */
  public void setLockedHeading(double heading) {
    targetHeading = AngleUnit.normalizeRadians(Math.toRadians(heading));
  }

  public void setCurrentDriveState(driveState driveState) {
    this.currentDriveState = driveState;
  }

  public void drivePowers(double x, double y, double rotation) {
    Vector2d movementVector = new Vector2d(x, y);
    switch (currentDriveState) {
      case ROBOT_CENTRIC_UNLOCKED:
        headingLock = false;
        fieldCentric = false;
        driveVector = movementVector;
        driverotation = rotation;
        break;
      case ROBOT_CENTRIC_LOCKED:
        headingLock = true;
        fieldCentric = false;
        driveVector = movementVector;
        driverotation = headingLogic();
        break;
      case FIELD_CENTRIC_UNLOCKED:
        movementVector = movementVector.rotateBy(Math.toDegrees(-follower.getTotalHeading()));
        headingLock = false;
        fieldCentric = true;
        driveVector = movementVector;
        driverotation = rotation;
        break;
      case FIELD_CENTRIC_LOCKED:
        movementVector = movementVector.rotateBy(Math.toDegrees(-follower.getTotalHeading()));
        headingLock = true;
        fieldCentric = true;
        driveVector = movementVector;
        driverotation = headingLogic();
        break;
    }
  }

  private void setTeleOpMovement(Vector2d drivePose, double rotation) {
    follower.setTeleOpMovementVectors(drivePose.getX(), drivePose.getY(), rotation);
  }

  public void switchCentric() {
    switch (currentDriveState) {
      case ROBOT_CENTRIC_UNLOCKED:
        currentDriveState = driveState.FIELD_CENTRIC_UNLOCKED;
        break;
      case ROBOT_CENTRIC_LOCKED:
        currentDriveState = driveState.FIELD_CENTRIC_LOCKED;
        break;
      case FIELD_CENTRIC_UNLOCKED:
        currentDriveState = driveState.ROBOT_CENTRIC_UNLOCKED;
        break;
      case FIELD_CENTRIC_LOCKED:
        currentDriveState = driveState.ROBOT_CENTRIC_LOCKED;
        break;
    }
  }

  public void switchLock() {
    switch (currentDriveState) {
      case ROBOT_CENTRIC_UNLOCKED:
        currentDriveState = driveState.ROBOT_CENTRIC_LOCKED;
        break;
      case ROBOT_CENTRIC_LOCKED:
        currentDriveState = driveState.ROBOT_CENTRIC_UNLOCKED;
        break;
      case FIELD_CENTRIC_UNLOCKED:
        currentDriveState = driveState.FIELD_CENTRIC_LOCKED;
        break;
      case FIELD_CENTRIC_LOCKED:
        currentDriveState = driveState.FIELD_CENTRIC_UNLOCKED;
        break;
    }
  }

  public void LockedHeading0() {
    this.lockedHeading = 0;
  }

  public void LockedHeading90() {
    this.lockedHeading = 90;
  }

  public void LockedHeading180() {
    this.lockedHeading = 180;
  }

  public void LockedHeading270() {
    this.lockedHeading = 270;
  }

  public void resetHeading() {
    follower.setPose(new Pose(follower.getPose().getX(), follower.getPose().getY(), 0));
  }

  private double headingLogic() {
    if (headingLock) {

      double headingError = targetHeading - currentHeading;

      if (Math.abs(headingError) < Math.toRadians(2)) {
        headingCorrection = 0;
      } else {
        headingPIDController.updateError(headingError);
        headingCorrection = headingPIDController.runPIDF();
      }
    }
    return headingCorrection;
  }

  public driveState getState() {
    return currentDriveState;
  }
}
