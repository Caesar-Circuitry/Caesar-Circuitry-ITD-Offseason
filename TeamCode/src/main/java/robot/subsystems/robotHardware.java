package robot.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.HardwareMap;

import robot.constants;

public class robotHardware extends WSubsystem {
  private Follower m_follower;
  private output m_output;
  private drive m_drive;
  private HardwareMap hardwareMap;

  public robotHardware(HardwareMap hardwareMap) {
    this.hardwareMap = hardwareMap;
    m_follower = new Follower(this.hardwareMap, Constants.fConstants, Constants.lConstants);
    m_drive = new drive(this);
    m_output = new output(this);
  }

  private double voltage;

  @Override
  public void read() {
    voltage = m_follower.getVoltage();
  }

  @Override
  public void loop() {}

  @Override
  public void write() {}

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
}
