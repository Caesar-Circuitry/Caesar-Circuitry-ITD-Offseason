package robot.Commands.outputCommands;

import com.seattlesolvers.solverslib.command.CommandBase;

import robot.subsystems.output;
import robot.subsystems.robotHardware;

public class outputClawOpen extends CommandBase {
    private final output m_output;

    public outputClawOpen(output mOutput) {
        m_output = mOutput;
        addRequirements(this.m_output);
    }
    @Override
    public void initialize() {
        m_output.clawOpen();
    }
}
