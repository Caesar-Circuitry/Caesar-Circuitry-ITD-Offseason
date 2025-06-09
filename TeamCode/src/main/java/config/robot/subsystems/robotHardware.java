package config.robot.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.HardwareMap;

import config.pedroPathing.constants.FConstants;
import config.pedroPathing.constants.LConstants;
import config.robot.constants;

public class robotHardware extends WSubsystem {
  private Follower m_follower;
  private output m_output;
  private intake m_intake;
  private drive m_drive;
  private HardwareMap hardwareMap;

  public robotHardware(HardwareMap hardwareMap) {
    this.hardwareMap = hardwareMap;
    Constants.setConstants(FConstants.class, LConstants.class);
    m_follower = new Follower(this.hardwareMap, Constants.fConstants, Constants.lConstants);
    m_drive = new drive(this);
    m_intake = new intake(this);
    m_output = new output(this);
  }

  private double voltage;

  @Override
  public void read() {
    m_output.read();
    m_intake.read();
    m_drive.read();
    voltage = m_follower.getVoltage();
  }

  @Override
  public void loop() {
    m_output.loop();
    m_intake.loop();
    m_drive.loop();
  }

  @Override
  public void write() {
    m_output.write();
    m_intake.write();
    m_drive.write();
  }

  public double voltageCompensatePower(double power) {
    return power * (constants.robotConstants.nominalVoltage / voltage);
  }

  public HardwareMap getHardwareMap() {
    return this.hardwareMap;
  }

  public Follower getFollower() {
    return m_follower;
  }

  public drive getDrive() {
    return m_drive;
  }

  public intake getIntake() {
    return m_intake;
  }

  public output getOutput() {
    return m_output;
  }
}
