package robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.solversHardware.SolversMotor;
import com.seattlesolvers.solverslib.solversHardware.SolversServo;

import robot.constants;

public class output extends WSubsystem {

  private final robotHardware hardware;

  private final SolversMotor m_motor;
  private final PIDFController m_pidfController;

  private final SolversServo m_servo_v4bar;
  private final SolversServo m_servo_clawRotate;
  private final SolversServo m_servo_claw;

  private double m_motor_targetPosition = 0.0;
  private double m_servo_v4bar_targetPosition = 0.0;
  private double m_servo_clawRotate_targetPosition = 0.0;
  private double m_servo_claw_targetPosition = 0.0;

  private double m_currentPosition = 0.0;
  private double power;

  public output(robotHardware Hardware) {
    this.hardware = Hardware;
    this.m_motor =
        new SolversMotor(
            hardware.getHardwareMap().get(DcMotor.class, constants.outputConstants.motorName),
            constants.outputConstants.motorCachingFactor);
    this.m_servo_v4bar =
        new SolversServo(
            hardware.getHardwareMap().get(Servo.class, constants.outputConstants.servoV4barName),
            constants.outputConstants.servoCachingFactor);
    this.m_servo_clawRotate =
        new SolversServo(
            hardware
                .getHardwareMap()
                .get(Servo.class, constants.outputConstants.servoClawRotateName),
            constants.outputConstants.servoCachingFactor);
    this.m_servo_claw =
        new SolversServo(
            hardware.getHardwareMap().get(Servo.class, constants.outputConstants.servoClawName),
            constants.outputConstants.servoCachingFactor);
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
    m_pidfController.setSetPoint(m_motor_targetPosition);
    power = hardware.voltageCompensatePower(m_pidfController.calculate(m_currentPosition));
  }

  @Override
  public void write() {
    this.m_motor.setPower(this.power);
    this.m_servo_v4bar.setPosition(this.m_servo_v4bar_targetPosition);
    this.m_servo_clawRotate.setPosition(this.m_servo_clawRotate_targetPosition);
    this.m_servo_claw.setPosition(this.m_servo_claw_targetPosition);
  }

  private void setM_motor_targetPosition(double targetPosition) {
    this.m_motor_targetPosition = targetPosition;
  }

  public void slidesTransfer() {
    setM_motor_targetPosition(constants.outputConstants.slideTransfer);
  }

  public void slidesHighBasket() {
    setM_motor_targetPosition(constants.outputConstants.slideHighBasket);
  }

  public void slidesHighChamber() {
    setM_motor_targetPosition(constants.outputConstants.slideHighChamber);
  }

  public void slidesL2Ascent() {
    setM_motor_targetPosition(constants.outputConstants.slideL2Ascent);
  }

  public void slidesL2Hang() {
    setM_motor_targetPosition(constants.outputConstants.slideL2Hang);
  }

  public void v4BarTransfer() {
    this.m_servo_v4bar_targetPosition = constants.outputConstants.v4barTransfer;
  }

  public void v4BarHighBasket() {
    this.m_servo_v4bar_targetPosition = constants.outputConstants.v4barHighBasket;
  }

  public void v4BarHighChamber() {
    this.m_servo_v4bar_targetPosition = constants.outputConstants.v4barHighChamber;
  }

  public void v4BarWallPickup() {
    this.m_servo_v4bar_targetPosition = constants.outputConstants.wallPickup;
  }

  public void clawRotateTransfer() {
    this.m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateTransfer;
  }

  public void clawRotateHighBasket() {
    this.m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateHighBasket;
  }

  public void clawRotateHighChamber() {
    this.m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateHighChamber;
  }

  public void clawRotateWallPickup() {
    this.m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateWallPickup;
  }

  public void clawOpen() {
    this.m_servo_claw_targetPosition = constants.outputConstants.clawOpen;
  }

  public void clawClose() {
    this.m_servo_claw_targetPosition = constants.outputConstants.clawClose;
  }
}
