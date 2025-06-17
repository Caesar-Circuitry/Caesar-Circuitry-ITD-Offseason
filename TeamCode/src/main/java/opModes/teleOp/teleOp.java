package opModes.teleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.RunCommand;

import config.robot.robot;

@TeleOp
public class teleOp extends CommandOpMode {
  private robot robot;

  @Override
  public void initialize() {
    super.reset();
    waitForStart();
    robot = new robot(hardwareMap, gamepad1, gamepad2);
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
            -gamepad1.left_stick_y * .75,
            -gamepad1.left_stick_x * .75,
            -gamepad1.right_stick_x / 2);
    telemetry.addData("state", this.robot.getHardware().getDrive().getState());
    telemetry.addData("outtakePower", this.robot.getHardware().getOutput().getM_motor_power());
    telemetry.addData("outtakePower", this.robot.getHardware().getOutput().getM_currentPosition());
    telemetry.update();
  }
}
