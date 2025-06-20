package opModes.auto;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import config.paths.autoPath5Push;
import config.robot.robot;
import config.robot.subsystems.robotHardware;

/** Autonomous OpMode implementation */
@Autonomous
public class autopush extends CommandOpMode {
  private robot robot;
  private Follower follower;

  @Override
  public void initialize() {
    super.reset();
    // Initialize the robot for autonomous mode
    robot = new robot(hardwareMap);

    // Get the follower from the robot hardware
    robotHardware hardware = robot.getHardware();
    follower = hardware.getFollower();
    follower.setStartingPose(autoPath5Push.startPose);

    // Create the autonomous path

    // Create a sequential command group for the autonomous routine
    SequentialCommandGroup autonomousRoutine =
        new SequentialCommandGroup(
            new InstantCommand(hardware.getOutput()::clawClose),
            new InstantCommand(hardware.getOutput()::TargetHighChamber),
            new WaitCommand(50),
            new FollowPathCommand(follower, autoPath5Push.specimen0(), true),
            new WaitCommand(50),
            new InstantCommand(hardware.getOutput()::ScoreSpec),
            new WaitCommand(200),
            new InstantCommand(hardware.getOutput()::clawOpen),
            new WaitCommand(100),
            new InstantCommand(hardware.getOutput()::TargetTransfer),
            new FollowPathCommand(follower, autoPath5Push.Sample1(), true),
            new FollowPathCommand(follower, autoPath5Push.Sample2(), true),

            // grab/ score 2nd spec
            new WaitCommand(250),
            new InstantCommand(hardware.getOutput()::TargetWall),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.GrabOffWall1(), true),
            new WaitCommand(250),
            new InstantCommand(hardware.getOutput()::clawClose),
            new WaitCommand(100),
            new InstantCommand(hardware.getOutput()::TargetHighChamber),
            new FollowPathCommand(follower, autoPath5Push.placeSpecimen1(), true),
            new WaitCommand(50),
            new InstantCommand(hardware.getOutput()::ScoreSpec),
            new WaitCommand(200),
            new InstantCommand(hardware.getOutput()::clawOpen),
            new WaitCommand(100),

            // grab/ score 3rd spec
            new FollowPathCommand(follower, autoPath5Push.grabSpecimen2(), true),
            new WaitCommand(250),
            new InstantCommand(hardware.getOutput()::TargetWall),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.GrabOffWall2(), true),
            new WaitCommand(250),
            new InstantCommand(hardware.getOutput()::clawClose),
            new WaitCommand(100),
            new InstantCommand(hardware.getOutput()::TargetHighChamber),
            new FollowPathCommand(follower, autoPath5Push.placeSpecimen2(), true),
            new WaitCommand(50),
            new InstantCommand(hardware.getOutput()::ScoreSpec),
            new WaitCommand(200),
            new InstantCommand(hardware.getOutput()::clawOpen),
            new WaitCommand(100),

            // grab/ score 4th spec
            new FollowPathCommand(follower, autoPath5Push.grabSpecimen3(), true),
            new WaitCommand(250),
            new InstantCommand(hardware.getOutput()::TargetWall),
            new WaitCommand(1000),
            new FollowPathCommand(follower, autoPath5Push.GrabOffWall2(), true),
            new WaitCommand(250),
            new InstantCommand(hardware.getOutput()::clawClose),
            new WaitCommand(100),
            new InstantCommand(hardware.getOutput()::TargetHighChamber),
            new FollowPathCommand(follower, autoPath5Push.placeSpecimen3(), true),
            new WaitCommand(50),
            new InstantCommand(hardware.getOutput()::ScoreSpec),
            new WaitCommand(200),
            new InstantCommand(hardware.getOutput()::clawOpen),
            new WaitCommand(100),

            // park
            new InstantCommand(hardware.getOutput()::TargetTransfer),
            new FollowPathCommand(follower, autoPath5Push.park(), true));

    // Schedule commands through the robot instance
    schedule(
        new RunCommand(this.robot::read),
        new RunCommand(this.robot::loop),
        new RunCommand(this.robot::write),
        autonomousRoutine);
  }

  @Override
  public void run() {
    // The scheduled commands will automatically run through the robot instance
    super.run();
  }

  @Override
  public void end() {
    robot.getHardware().getDrive().setTeleStartPose(follower.getPose());
  }
}
