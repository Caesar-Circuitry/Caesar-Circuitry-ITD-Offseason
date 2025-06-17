package config.robot.subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.hardware.motors.Motor;
import com.seattlesolvers.solverslib.util.MathUtils;

public class newDrive extends WSubsystem {
  private Motor FRM, BRM, FLM, BLM;
  private Follower follower;
  private double modifyBySine = Math.sin(Math.PI / 4);

  public newDrive(Follower follower, HardwareMap hardwareMap) {
    FRM = new Motor(hardwareMap, "FRM", Motor.GoBILDA.RPM_435);
    BRM = new Motor(hardwareMap, "BRM", Motor.GoBILDA.RPM_435);
    FLM = new Motor(hardwareMap, "FLM", Motor.GoBILDA.RPM_435);
    BLM = new Motor(hardwareMap, "BLM", Motor.GoBILDA.RPM_435);
    this.follower = follower;
  }

  @Override
  public void read() {
    // Implement reading logic here
  }

  @Override
  public void loop() {
    // Implement looping logic here
  }

  @Override
  public void write() {
    // Implement writing logic here
  }

  // code adapted from Team 13017
  public void ProMotorControl(double left_stick_y, double left_stick_x, double right_stick_x) {
    double powerLeftY = left_stick_y; // DRIVE : Backward -1 <---> 1 Forward
    double powerLeftX = -left_stick_x * -1; // STRAFE:     Left -1 <---> 1 Right
    double powerRightX = right_stick_x; // ROTATE:     Left -1 <---> 1 Right

    double r = Math.hypot(powerLeftX, powerLeftY);
    double robotAngle = Math.atan2(powerLeftY, powerLeftX) - Math.PI / 4;
    double leftX = powerRightX;
    final double v1 = r * Math.cos(robotAngle) / modifyBySine + leftX;
    final double v2 = r * Math.sin(robotAngle) / modifyBySine - leftX;
    final double v3 = r * Math.sin(robotAngle) / modifyBySine + leftX;
    final double v4 = r * Math.cos(robotAngle) / modifyBySine - leftX;

    FLM.set(clampMotorPower(v1));
    FRM.set(clampMotorPower(v2));
    BLM.set(clampMotorPower(v3));
    BRM.set(clampMotorPower(v4));
  }

  public void fieldCentric(double left_stick_y, double left_stick_x, double right_stick_x) {
    double y = left_stick_y; // DRIVE : Backward -1 <---> 1 Forward
    double x = left_stick_x; // STRAFE:     Left -1 <---> 1 Right
    double rx = right_stick_x; // ROTATE:     Left -1 <---> 1 Right

    double botHeading = follower.getTotalHeading();

    // Rotate the movement direction counter to the bot's rotation
    double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
    double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

    rotX = rotX * 1.1; // Counteract imperfect strafing

    // Denominator is the largest motor power (absolute value) or 1
    // This ensures all the powers maintain the same ratio,
    // but only if at least one is out of the range [-1, 1]
    double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
    double frontLeftPower = (rotY + rotX + rx) / denominator;
    double backLeftPower = (rotY - rotX - rx) / denominator;
    double frontRightPower = (rotY - rotX + rx) / denominator;
    double backRightPower = (rotY + rotX - rx) / denominator;

    PowerMotorControl(frontLeftPower, backLeftPower, frontRightPower, backRightPower);

    telemetry.addData("rotX", rotX);
    telemetry.addData("rotY", rotY);
    telemetry.addData("angle", botHeading);
  }

  private void PowerMotorControl(double FL, double BL, double FR, double BR) {
    final double v1 = FL / modifyBySine;
    final double v2 = BL / modifyBySine;
    final double v3 = FR / modifyBySine;
    final double v4 = BR / modifyBySine;

    FLM.set(clampMotorPower(v1));
    FRM.set(clampMotorPower(v2));
    BLM.set(clampMotorPower(v3));
    BRM.set(clampMotorPower(v4));
  }

  private double clampMotorPower(double power) {
    return MathUtils.clamp(power, -1.0, 1.0);
  }
}
