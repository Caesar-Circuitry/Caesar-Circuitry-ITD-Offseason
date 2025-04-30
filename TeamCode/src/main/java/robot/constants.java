package robot;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class constants {

  public static class robotConstants {
    public static final double nominalVoltage = 12.0;
  }

  public static class DriveConstants {
    public static final String leftFrontMotorName = "leftFront";
    public static final String leftRearMotorName = "leftRear";
    public static final String rightFrontMotorName = "rightFront";
    public static final String rightRearMotorName = "rightRear";

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
    public static final String motorName = "output";
    public static final double cachingFactor = 0.01;
    public static final double kP = 0.1;
    public static final double kI = 0;
    public static final double kD = 0.001;
    public static final double kF = 0.0;
  }
}
