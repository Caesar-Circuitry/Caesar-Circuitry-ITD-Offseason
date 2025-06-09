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

@Config
@TeleOp
public class outtakeTuning extends LinearOpMode {
  public static double kP = 0.04, kD = 0, kF = 3;
  public static double targetPosition = 0;
  public static double TicksPerInch = 154.75;
  public static boolean disableOuttake = false;
  public double actualPosition = 0;
  private PIDFController outtakePIDFController;
  private SolversMotor outtakeMotor;
  private MultipleTelemetry multipleTelemetry;
  private double motorPower = 0;

  @Override
  public void runOpMode() throws InterruptedException {
    outtakePIDFController = new PIDFController(kP, 0, kD, kF);
    outtakeMotor = new SolversMotor(hardwareMap.get(DcMotorEx.class, "output"), 0.01);
    outtakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    outtakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    outtakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    multipleTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    waitForStart();
    while (opModeIsActive()) {
      // TODO: consider error being in encoder ticks instead of inches and converting target from
      // inches to ticks
      actualPosition = outtakeMotor.getPosition() / TicksPerInch;
      outtakePIDFController.setPIDF(kP, 0, kD, kF);
      if (!disableOuttake) {
        motorPower = outtakePIDFController.calculate(actualPosition, targetPosition);
        outtakeMotor.setPower(motorPower);
      }
      multipleTelemetry.addData("actualPosition", actualPosition);
      multipleTelemetry.addData("targetPosition", targetPosition);
      multipleTelemetry.addData("motorPower", motorPower);
      multipleTelemetry.addData("error", targetPosition - actualPosition);
      multipleTelemetry.update();
    }
  }
}
