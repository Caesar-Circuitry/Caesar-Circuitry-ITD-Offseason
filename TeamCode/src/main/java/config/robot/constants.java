package config.robot;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class constants {

  public static class robotConstants {
    public static final double nominalVoltage = 12.0;
  }

  public static class DriveConstants {
    public static final String leftFrontMotorName = "FLM";
    public static final String leftRearMotorName = "BLM";
    public static final String rightFrontMotorName = "FRM";
    public static final String rightRearMotorName = "BRM";

    public static final DcMotorSimple.Direction leftFrontMotorDirection =
        DcMotorSimple.Direction.REVERSE;
    public static final DcMotorSimple.Direction leftRearMotorDirection =
        DcMotorSimple.Direction.REVERSE;
    public static final DcMotorSimple.Direction rightFrontMotorDirection =
        DcMotorSimple.Direction.FORWARD;
    public static final DcMotorSimple.Direction rightRearMotorDirection =
        DcMotorSimple.Direction.FORWARD;
  }

  public static class outputConstants {
    /*names*/
    public static final String motorName = "output";
    public static final String servoV4barName = "outtakeRotate1";
    public static final String servoClawRotateName = "outtakeClawRotate";
    public static final String servoClawName = "outtakeClaw";
    /*amount before the change is written*/
    public static final double motorCachingFactor = 0.01;
    public static final double servoCachingFactor = 0.01;
    /*slide PID coefficients*/
    public static final double kP = 0.04;
    public static final double kI = 0;
    public static final double kD = 0;
    public static final double kF = 3;
    public static final double ticksPerInch = 154.75;
    public static final double minError = 0.2 * ticksPerInch;
    /*vertical slides target positions*/
    public static final double slideTransfer = 0.0;
    public static final double slideWall = 0.0;
    public static final double slideHighBasket = 29;
    public static final double slideHighChamber = 12;
    public static final double slideL2Ascent = 0.0; // move up to hang
    public static final double slideL2Hang = 0.0; // move slide down to lift robot up;
    /*v4bar servo target positions*/
    public static final double v4barTransfer = 1;
    public static final double v4barHighBasket = 0.0;
    public static final double v4barHighChamber = .5;
    public static final double wallPickup = .1;
    /*clawRotate servo target positions*/
    public static final double clawRotateTransfer = 0.93;
    public static final double clawRotateHighBasket = 0.0;
    public static final double clawRotateHighChamber = 0.15;
    public static final double clawRotateWallPickup = 0.5;
    /*claw servo target positions*/
    public static final double clawOpen = .4;
    public static final double clawClose = 0.16;
  }

  public static class intakeConstants {
    /*names*/
    public static final String motorName = "intake";
    public static final String servoClawPivotName = "intakeClawPivot";
    public static final String servoClawRotateName = "intakeClawRotate";
    public static final String servoClawName = "intakeClaw";
    public static final String servoIntakeRotateName = "intakeRotate";

    /*amount before the change is written*/
    public static final double motorCachingFactor = 0.01;
    public static final double servoCachingFactor = 0.00;
    /*slide PID coefficients*/
    public static final double kP = 0.1;
    public static final double kI = 0;
    public static final double kD = 0;
    public static final double kF = 0.05;
    public static final double ticksPerInch = 8.33;
    public static final double minError = 0.2 * ticksPerInch;
    public static final double springOffset = 8 * ticksPerInch;
    /*vertical slides target positions*/
    public static final double slideTransfer = 0.0;
    public static final double slideGroundPickup = 0.0;
    public static final double slideSub = 10.0 * ticksPerInch;
    public static final double slideZero = 0.0;
    /*Rotate servo target positions*/
    public static final double RotateTransfer = 0.42;
    public static final double RotateSub = 0.0;
    public static final double RotateHp = .42;
    public static final double RotateHover = .05;
    /*clawRotate servo target positions*/
    public static final double clawRotateTransfer = 0.70;
    public static final double clawRotateSub = 0.0;
    public static final double clawRotateHP = 0;
    public static final double clawRotateHover = 0.0;
    /*claw Pivot servo target positions*/
    public static final double ClawPivotRight = .08;
    public static final double ClawPivotMiddle = .62;
    public static final double ClawPivotLeft = 1; // dont use this position, it is not fully left

    /*claw servo target positions*/
    public static final double clawOpen = .64;
    public static final double clawClose = .37;

    public static final int transferServoDelay = 0; // milliseconds
    public static final int subServoDelay = 0; // milliseconds
    public static final int getSubServoDelay2 = 500; // milliseconds
    public static final int groundPickupServoDelay = 1000; // milliseconds
    public static final int humanPlayerServoDelay = 1000; // milliseconds
    public static final int hoverServoDelay = 0; // milliseconds
  }
}
