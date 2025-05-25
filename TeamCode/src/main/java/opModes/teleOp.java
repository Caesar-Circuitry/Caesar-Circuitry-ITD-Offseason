package opModes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.seattlesolvers.solverslib.command.CommandOpMode;

import robot.robot;

@TeleOp
public class teleOp extends CommandOpMode {
  private robot robot;

  @Override
  public void initialize() {
    robot = new robot(hardwareMap, gamepad1, gamepad2);
  }
}
