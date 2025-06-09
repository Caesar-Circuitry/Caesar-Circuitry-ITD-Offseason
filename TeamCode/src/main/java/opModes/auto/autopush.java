package opModes.auto;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import config.paths.autoPath5Push;
import config.pedroPathing.constants.FConstants;
import config.pedroPathing.constants.LConstants;

/** Autonomous OpMode implementation */
@Autonomous
public class autopush extends CommandOpMode {
  // private robot robot;
  private Follower follower;

  @Override
  public void initialize() {
    super.reset();
    // Initialize the robot for autonomous mode
    // robot = new robot(hardwareMap);

    // Get the follower from the robot hardware
    // robotHardware hardware = robot.getHardware();
    follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
    follower.setStartingPose(autoPath5Push.startPose);

    // Create the autonomous path

    // Create a sequential command group for the autonomous routine
    SequentialCommandGroup autonomousRoutine =
        new SequentialCommandGroup(
            new FollowPathCommand(follower, autoPath5Push.specimen0(), true),
            new WaitCommand(500),
            new FollowPathCommand(follower, autoPath5Push.Sample1(), true),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.Sample2(), true),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.Sample3(), true),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.placeSpecimen1(), true),
            new WaitCommand(500),
            new FollowPathCommand(follower, autoPath5Push.grabSpecimen2(), true),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.placeSpecimen2(), true),
            new WaitCommand(500),
            new FollowPathCommand(follower, autoPath5Push.grabSpecimen3(), true),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.placeSpecimen3(), true),
            new WaitCommand(500),
            new FollowPathCommand(follower, autoPath5Push.grabSpecimen4(), true),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.placeSpecimen4(), true),
            new WaitCommand(500));

    // Schedule commands through the robot instance
    schedule(new RunCommand(follower::update), autonomousRoutine);
  }

  @Override
  public void run() {
    // The scheduled commands will automatically run through the robot instance
    super.run();
  }
}
