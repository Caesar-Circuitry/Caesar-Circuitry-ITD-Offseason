package robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.solversHardware.SolversMotor;

import robot.constants;

public class output extends WSubsystem {

  robotHardware hardware;

  private SolversMotor m_motor;
  private PIDFController m_pidfController;
  private double m_targetPosition = 0.0;
  private double m_currentPosition = 0.0;
  private double power;

  public output(robotHardware Hardware) {
    this.hardware = Hardware;
    this.m_motor =
        new SolversMotor(
            hardware.getHardwareMap().get(DcMotor.class, constants.outputConstants.motorName),
            constants.outputConstants.cachingFactor);
    this.m_pidfController =
        new PIDFController(
            constants.outputConstants.kP,
            constants.outputConstants.kI,
            constants.outputConstants.kD,
            constants.outputConstants.kF);
  }

  @Override
  public void read() {
    this.m_currentPosition = m_motor.getPosition();
  }

  @Override
  public void loop() {
    m_pidfController.setSetPoint(m_targetPosition);
    power = hardware.voltageCompensatePower(m_pidfController.calculate(m_currentPosition));
  }

  @Override
  public void write() {
    m_motor.setPower(this.power);
  }
}
