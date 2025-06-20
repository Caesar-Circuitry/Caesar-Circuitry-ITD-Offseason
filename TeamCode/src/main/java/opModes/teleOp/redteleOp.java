package opModes.teleOp;

import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.RunCommand;

import config.robot.constants;
import config.robot.robot;

@TeleOp(name = "teleOp", group = "teleOp")
public class redteleOp extends CommandOpMode {
  private robot robot;

  @Override
  public void initialize() {
    super.reset();
    waitForStart();
    robot = new robot(hardwareMap, gamepad1, gamepad2);
    robot.getHardware().getFollower().setStartingPose(new Pose(20, 26, Math.toRadians(0)));
    schedule(
        new RunCommand(this.robot::read),
        new RunCommand(this.robot::loop),
        new RunCommand(this.robot::write));
  }

  @Override
  public void run() {
    super.run();
    this.robot
        .getHardware()
        .getDrive()
        .drivePowers(
            robot
                .getHardware()
                .voltageCompensatePower(
                    -gamepad1.left_stick_y * .75 * constants.DriveConstants.driveMagnitude),
            robot
                .getHardware()
                .voltageCompensatePower(
                    -gamepad1.left_stick_x * .75 * constants.DriveConstants.driveMagnitude),
            robot.getHardware().voltageCompensatePower(-gamepad1.right_stick_x / 2));
    telemetry.addData("state", this.robot.getHardware().getDrive().getState());
    telemetry.addData("outtakePower", this.robot.getHardware().getOutput().getM_motor_power());
    telemetry.addData("outtakePower", this.robot.getHardware().getOutput().getM_currentPosition());
    telemetry.update();
  }
}
