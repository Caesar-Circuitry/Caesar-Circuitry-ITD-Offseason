package robot.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.CustomPIDFCoefficients;
import com.seattlesolvers.solverslib.geometry.Vector2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import pedroPathing.custom.SquidController;

public class drive extends WSubsystem {
  public enum driveState{
    ROBOT_CENTRIC_UNLOCKED,
    ROBOT_CENTRIC_LOCKED,
    FIELD_CENTRIC_UNLOCKED,
    FIELD_CENTRIC_LOCKED,
  }
  private driveState currentDriveState = driveState.ROBOT_CENTRIC_UNLOCKED;
  private final robotHardware hardware;
  private final Follower follower;
  private final SquidController headinglock;
  private double lockedHeading = 0;
  private double currentHeading = 0;
  private Vector2d driveVector = new Vector2d(0, 0);
  private double driverotation = 0;

  public drive(robotHardware hardware) {
    this.hardware = hardware;
    follower = this.hardware.getFollower();
    this.headinglock = new SquidController(new CustomPIDFCoefficients(0, 0, 0, 0)); // use values from FConstants
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
  setTeleOpMovement(driveVector,driverotation);
  }
  /**
   * @param heading in degrees
   */
  public void setLockedHeading(double heading) {
    this.lockedHeading = AngleUnit.normalizeRadians(Math.toRadians(heading));
    headinglock.setTargetPosition(lockedHeading);
  }

  public void setCurrentDriveState(driveState driveState){
    this.currentDriveState = driveState;
  }

  private void drivePowers(double x, double y, double rotation) {
    Vector2d movementVector = new Vector2d(x, y);
    double theta = rotation;
    switch (currentDriveState){
        case ROBOT_CENTRIC_UNLOCKED:
          this.driveVector = movementVector;
          this.driverotation = theta;
            break;
        case ROBOT_CENTRIC_LOCKED:
            headinglock.updatePosition(this.currentHeading);
            theta = headinglock.runPIDF();
            this.driveVector = movementVector;
            this.driverotation = theta;
            break;
        case FIELD_CENTRIC_UNLOCKED:
            movementVector.rotateBy(Math.toDegrees(currentHeading)-90);
            this.driveVector = movementVector;
            this.driverotation = theta;
        case FIELD_CENTRIC_LOCKED:
            headinglock.updatePosition(this.currentHeading);
            movementVector.rotateBy(Math.toDegrees(currentHeading)-90);
            theta = headinglock.runPIDF();
            this.driveVector = movementVector;
            this.driverotation = theta;
            break;
    }
  }
  private void setTeleOpMovement(Vector2d drivePose, double rotation) {
    follower.setTeleOpMovementVectors(drivePose.getX(),drivePose.getY(), rotation);
  }

  public void switchCentric(){
    switch (currentDriveState){
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
  public void switchLock(){
    switch (currentDriveState){
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
  public void LockedHeading0(){
    this.lockedHeading = 0;
  }
  public void LockedHeading90(){
    this.lockedHeading = 90;
  }
  public void LockedHeading180(){
    this.lockedHeading = 180;
  }
  public void LockedHeading270(){
    this.lockedHeading = 270;
  }
  public void resetHeading(){
    follower.setPose(new Pose(follower.getPose().getX(),follower.getPose().getY(),
            0));
  }

}
