package opModes.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.solversHardware.SolversMotor;

import config.helpers.control.SquidL_Controller;

@TeleOp
@Config
public class intakeTuningSquid extends LinearOpMode {
  public static double kSQ = 0, kD = 0, kL = 0;
  public static double targetPosition = 0;
  public static double TicksPerInch = 5.2;
  public static boolean disableIntake = false;
  public static double minError = 0.2; // inches
  public double actualPosition = 0;
  private SquidL_Controller SQUIDController;
  private SolversMotor intakeMotor;
  private MultipleTelemetry multipleTelemetry;
  private double motorPower = 0;

  @Override
  public void runOpMode() throws InterruptedException {
    SQUIDController = new SquidL_Controller(kSQ, kD, kL);
    intakeMotor = new SolversMotor(hardwareMap.get(DcMotorEx.class, "intakeMotor"), 0);
    intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    multipleTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    SQUIDController.setTolerance(.2 * TicksPerInch);
    waitForStart();
    while (opModeIsActive()) {
      // TODO: consider error being in encoder ticks instead of inches and converting target from
      // inches to ticks
      actualPosition = intakeMotor.getPosition();
      SQUIDController.setSQUIDL(kSQ, kD, kL);
      if (!disableIntake) {
        motorPower = SQUIDController.calculate(actualPosition, targetPosition * TicksPerInch);
        if (targetPosition - (actualPosition / TicksPerInch) <= minError) {
          motorPower = 0;
        }
        intakeMotor.setPower(motorPower);
      }
      multipleTelemetry.addData("actualPosition", actualPosition / TicksPerInch);
      multipleTelemetry.addData("targetPosition", targetPosition);
      multipleTelemetry.addData("motorPower", motorPower);
      multipleTelemetry.addData("error", targetPosition - (actualPosition / TicksPerInch));
      multipleTelemetry.update();
    }
  }
}
