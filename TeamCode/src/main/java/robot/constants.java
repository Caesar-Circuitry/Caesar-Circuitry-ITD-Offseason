package robot;

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
    public static final String servoV4barName = "v4bar";
    public static final String servoClawRotateName = "clawRotate";
    public static final String servoClawName = "claw";
    /*amount before the change is written*/
    public static final double motorCachingFactor = 0.01;
    public static final double servoCachingFactor = 0.01;
    /*slide PID coefficients*/
    public static final double kP = 0.1;
    public static final double kI = 0;
    public static final double kD = 0.001;
    public static final double kF = 0.0;
    /*vertical slides target positions*/
    public static final double slideTransfer = 0.0;
    public static final double slideHighBasket = 0.0;
    public static final double slideHighChamber = 0.0;
    public static final double slideL2Ascent = 0.0; // move up to hang
    public static final double slideL2Hang = 0.0; // move slide down to lift robot up;
    /*v4bar servo target positions*/
    public static final double v4barTransfer = 0.0;
    public static final double v4barHighBasket = 0.0;
    public static final double v4barHighChamber = 0.0;
    public static final double wallPickup = 0.0;
    /*clawRotate servo target positions*/
    public static final double clawRotateTransfer = 0.0;
    public static final double clawRotateHighBasket = 0.0;
    public static final double clawRotateHighChamber = 0.0;
    public static final double clawRotateWallPickup = 0.0;
    /*claw servo target positions*/
    public static final double clawOpen = 0.0;
    public static final double clawClose = 0.0;
  }
}
