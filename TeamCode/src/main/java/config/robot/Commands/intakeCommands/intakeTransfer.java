package config.robot.Commands.intakeCommands;

import com.seattlesolvers.solverslib.command.CommandBase;

import config.robot.subsystems.intake;

public class intakeTransfer extends CommandBase {
  private final intake m_intake;

  public intakeTransfer(intake t_intake) {
    m_intake = t_intake;
    addRequirements(this.m_intake);
  }

  @Override
  public void initialize() {
    m_intake.Transfer();
  }
}
