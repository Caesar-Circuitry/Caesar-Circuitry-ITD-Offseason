package config.robot.Commands.outputCommands;

import com.seattlesolvers.solverslib.command.CommandBase;

import config.robot.subsystems.output;

public class outputWallPickup extends CommandBase {
  private final output m_output;

  public outputWallPickup(output t_Output) {
    m_output = t_Output;
    addRequirements(this.m_output);
  }

  @Override
  public void initialize() {
    m_output.TargetWall();
  }
}
