package opModes.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.solversHardware.SolversMotor;

@TeleOp
@Config
public class intakeTuning extends LinearOpMode {
  public static double LKP = 0, LKD = 0, kF = 0.05;
  public static double targetPosition = 0;
  public static double TicksPerInch = 8.33;
  public static boolean disableIntake = false;
  public double actualPosition = 0;
  public static double minError = 0.2; // inches
  private PIDFController largeIntakePIDFController;
  private SolversMotor intakeMotor;
  private MultipleTelemetry multipleTelemetry;
  private double motorPower = 0;

  @Override
  public void runOpMode() throws InterruptedException {
    largeIntakePIDFController = new PIDFController(LKP, 0, LKD, 0);
    intakeMotor = new SolversMotor(hardwareMap.get(DcMotorEx.class, "intake"), 0);
    intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    multipleTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    waitForStart();
    while (opModeIsActive()) {
      // TODO: consider error being in encoder ticks instead of inches and converting target from
      // inches to ticks
      actualPosition = intakeMotor.getPosition();
      largeIntakePIDFController.setPIDF(LKP, 0, LKD, 0);
      if (!disableIntake) {
        double KFResponse = (kF * -((targetPosition - 8) - (actualPosition - 8) / TicksPerInch));
        if (actualPosition / TicksPerInch < 8) {
          KFResponse = 0;
        }
        motorPower =
            largeIntakePIDFController.calculate(actualPosition, targetPosition * TicksPerInch)
                + KFResponse;
        if (targetPosition - (actualPosition / TicksPerInch) <= minError
            && targetPosition - (actualPosition / TicksPerInch) >= -minError) {
          motorPower = 0 + KFResponse;
        }
      }
      if (Math.abs(motorPower) >= 1) {
        motorPower = Math.signum(motorPower) * 1;
      }
      intakeMotor.setPower(motorPower);
      multipleTelemetry.addData("actualPosition", actualPosition / TicksPerInch);
      multipleTelemetry.addData("targetPosition", targetPosition);
      multipleTelemetry.addData("motorPower", motorPower);
      multipleTelemetry.addData("error", targetPosition - (actualPosition / TicksPerInch));
      multipleTelemetry.update();
    }
  }
}
