package config.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.seattlesolvers.solverslib.solversHardware.SolversMotor;
import com.seattlesolvers.solverslib.solversHardware.SolversServo;

import config.helpers.control.springPIDF;
import config.robot.constants;

public class intake extends WSubsystem {
  public enum intakeState {
    TRANSFER,
    Submersible,
    HumanPlayer,
    HoverPickup,
  }

  private ElapsedTime m_timer;
  private intakeState targetState = intakeState.TRANSFER;

  private final robotHardware hardware;
  private final SolversMotor m_motor;
  private final springPIDF m_pidfController;

  private final SolversServo m_servo_Rotate;
  private final SolversServo m_servo_clawRotate;
  private final SolversServo m_servo_claw;
  private final SolversServo m_servo_ClawPivot;

  private double m_motor_targetPosition = constants.intakeConstants.slideTransfer;
  private double m_servo_Rotate_targetPosition = constants.intakeConstants.RotateTransfer;
  private double m_servo_clawRotate_targetPosition = constants.intakeConstants.clawRotateTransfer;
  private double m_servo_claw_targetPosition = constants.intakeConstants.clawOpen;
  private double m_servo_ClawPivot_targetPosition = constants.intakeConstants.ClawPivotMiddle;

  private double m_currentPosition = 0.0;
  private double power;

  public intake(robotHardware Hardware) {
    this.hardware = Hardware;
    this.m_motor =
        new SolversMotor(
            hardware.getHardwareMap().get(DcMotor.class, constants.intakeConstants.motorName),
            constants.intakeConstants.motorCachingFactor);
    this.m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    this.m_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    this.m_servo_Rotate =
        new SolversServo(
            hardware
                .getHardwareMap()
                .get(Servo.class, constants.intakeConstants.servoIntakeRotateName),
            constants.intakeConstants.servoCachingFactor);
    this.m_servo_clawRotate =
        new SolversServo(
            hardware
                .getHardwareMap()
                .get(Servo.class, constants.intakeConstants.servoClawRotateName),
            constants.intakeConstants.servoCachingFactor);
    this.m_servo_claw =
        new SolversServo(
            hardware.getHardwareMap().get(Servo.class, constants.intakeConstants.servoClawName),
            constants.intakeConstants.servoCachingFactor);
    this.m_servo_ClawPivot =
        new SolversServo(
            hardware
                .getHardwareMap()
                .get(Servo.class, constants.intakeConstants.servoClawPivotName),
            constants.intakeConstants.servoCachingFactor);
    this.m_pidfController =
        new springPIDF(
            constants.intakeConstants.kP,
            constants.intakeConstants.kI,
            constants.intakeConstants.kD,
            constants.intakeConstants.kF,
            constants.intakeConstants.springOffset,
            constants.intakeConstants.minError);
    m_timer = new ElapsedTime();
    m_timer.reset();
  }

  @Override
  public void read() {
    this.m_currentPosition = m_motor.getPosition();
  }

  @Override
  public void loop() {
    switchCase();
    power = m_pidfController.caculate(m_currentPosition, m_motor_targetPosition);
  }

  @Override
  public void write() {
    this.m_motor.setPower(this.power);
    this.m_servo_Rotate.setPosition(this.m_servo_Rotate_targetPosition);
    this.m_servo_clawRotate.setPosition(this.m_servo_clawRotate_targetPosition);
    this.m_servo_claw.setPosition(this.m_servo_claw_targetPosition);
    this.m_servo_ClawPivot.setPosition(this.m_servo_ClawPivot_targetPosition);
  }

  private void setM_motor_targetPosition(double t_targetPosition) {
    this.m_motor_targetPosition = t_targetPosition;
  }

  private void setIntakeState(intakeState t_targetState) {
    this.targetState = t_targetState;
    m_timer.reset();
  }

  private void switchCase() {
    switch (targetState) {
      case TRANSFER:
        m_servo_ClawPivot_targetPosition = constants.intakeConstants.ClawPivotMiddle;
        m_servo_Rotate_targetPosition = constants.intakeConstants.RotateTransfer;
        m_servo_clawRotate_targetPosition = constants.intakeConstants.clawRotateTransfer;
        if (m_timer.milliseconds() >= constants.intakeConstants.transferServoDelay) {
          m_timer.reset();
          setM_motor_targetPosition(constants.intakeConstants.slideTransfer);
        }
        break;
      case HoverPickup:
        m_servo_claw_targetPosition = constants.intakeConstants.clawOpen;
        m_servo_Rotate_targetPosition = constants.intakeConstants.RotateHover;
        m_servo_clawRotate_targetPosition = constants.intakeConstants.clawRotateHover;
        if (m_timer.milliseconds() >= constants.intakeConstants.hoverServoDelay) {
          m_timer.reset();
          setM_motor_targetPosition(constants.intakeConstants.slideSub);
        }
        break;
      case HumanPlayer:
        m_servo_ClawPivot_targetPosition = constants.intakeConstants.ClawPivotMiddle;
        m_servo_Rotate_targetPosition = constants.intakeConstants.RotateHp;
        m_servo_clawRotate_targetPosition = constants.intakeConstants.clawRotateHP;
        if (m_timer.milliseconds() >= constants.intakeConstants.hoverServoDelay) {
          m_timer.reset();
          setM_motor_targetPosition(constants.intakeConstants.slideTransfer);
        }
        break;
      case Submersible:
        m_servo_Rotate_targetPosition = constants.intakeConstants.RotateSub;
        m_servo_clawRotate_targetPosition = constants.intakeConstants.clawRotateSub;
        if (m_timer.milliseconds() >= constants.intakeConstants.subServoDelay) {
          m_timer.reset();
          m_servo_claw_targetPosition = constants.intakeConstants.clawClose;
          if (m_timer.milliseconds() >= constants.intakeConstants.getSubServoDelay2) {
            targetState = intakeState.HoverPickup;
          }
        }
        break;
    }
  }

  public void clawClose() {
    m_servo_claw_targetPosition = constants.intakeConstants.clawClose;
  }

  public void clawOpen() {
    m_servo_claw_targetPosition = constants.intakeConstants.clawOpen;
  }

  public void ClawPivotRight() {
    m_servo_ClawPivot_targetPosition = constants.intakeConstants.ClawPivotRight;
  }

  public void ClawPivotMiddle() {
    m_servo_ClawPivot_targetPosition = constants.intakeConstants.ClawPivotMiddle;
  }

  public boolean isClawOpen() {
    if (m_servo_claw_targetPosition == constants.intakeConstants.clawOpen) {
      return true;
    } else {
      return false;
    }
  }

  public void Transfer() {
    setIntakeState(intakeState.TRANSFER);
  }

  public void HoverPickup() {
    setIntakeState(intakeState.HoverPickup);
  }

  public void HumanPlayer() {
    setIntakeState(intakeState.HumanPlayer);
  }

  public void Submersible() {
    setIntakeState(intakeState.Submersible);
  }
}
