package config.robot.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.controller.PIDFController;
import com.seattlesolvers.solverslib.solversHardware.SolversServo;
import com.seattlesolvers.solverslib.util.MathUtils;

import config.robot.constants;

public class output extends WSubsystem {
  public enum outputState {
    TRANSFER,
    HIGH_CHAMBER,
    PLACE_SPEC,
    WALL_PICKUP,
    L2_ASCENT,
    L2_HANG,
  }

  private enum clawState {
    OPEN,
    CLOSE
  }

  public clawState ClawState = clawState.OPEN;

  private outputState targetState = outputState.TRANSFER;
  private final robotHardware hardware;

  private final DcMotorEx m_motor;
  private final PIDFController m_pidfController;

  private final SolversServo m_servo_v4bar;
  private final SolversServo m_servo_clawRotate;
  private final SolversServo m_servo_claw;

  private double m_motor_targetPosition = constants.outputConstants.slideTransfer;
  private double m_servo_v4bar_targetPosition = constants.outputConstants.v4barTransfer;
  private double m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateTransfer;
  private double m_servo_claw_targetPosition = constants.outputConstants.clawOpen;

  private double m_currentPosition = 0.0;
  private double power;

  public output(robotHardware Hardware) {
    this.hardware = Hardware;
    this.m_motor =
        Hardware.getHardwareMap().get(DcMotorEx.class, constants.outputConstants.motorName);
    this.m_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    this.m_motor.setDirection(DcMotorSimple.Direction.REVERSE);
    this.m_motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    this.m_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
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
    this.m_currentPosition = m_motor.getCurrentPosition();
  }

  @Override
  public void loop() {
    m_pidfController.setSetPoint(m_motor_targetPosition);
    if ((Math.abs(m_pidfController.getPositionError()) >= constants.outputConstants.minError)) {
      power = MathUtils.clamp(m_pidfController.calculate(this.m_currentPosition), -1, 1);
    } else {
      power = 0;
    }
    switchState();
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

  private void setTargetState(outputState targetState) {
    this.targetState = targetState;
  }

  public void TargetTransfer() {
    setTargetState(outputState.TRANSFER);
  }

  public void TargetHighChamber() {
    setTargetState(outputState.HIGH_CHAMBER);
  }

  public void TargetWall() {
    setTargetState(outputState.WALL_PICKUP);
  }

  public void ScoreSpec() {
    setTargetState(outputState.PLACE_SPEC);
  }

  private void switchState() {
    switch (targetState) {
      case TRANSFER:
        setM_motor_targetPosition(constants.outputConstants.slideTransfer);
        this.m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateTransfer;
        this.m_servo_v4bar_targetPosition = constants.outputConstants.v4barTransfer;
        break;
      case HIGH_CHAMBER:
        setM_motor_targetPosition(constants.outputConstants.slideHighChamber);
        this.m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateHighChamber;
        this.m_servo_v4bar_targetPosition = constants.outputConstants.v4barHighChamber;
        break;
      case WALL_PICKUP:
        setM_motor_targetPosition(constants.outputConstants.slideTransfer);
        this.m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateWallPickup;
        this.m_servo_v4bar_targetPosition = constants.outputConstants.wallPickup;
        break;
      case PLACE_SPEC:
        this.m_servo_clawRotate_targetPosition = constants.outputConstants.clawRotateHighChamber;
        this.m_servo_v4bar_targetPosition = constants.outputConstants.v4barPlaceSpec;
        setM_motor_targetPosition(constants.outputConstants.slideHighChamberClip);
    }
  }

  public void clawOpen() {
    ClawState = clawState.OPEN;
    this.m_servo_claw_targetPosition = constants.outputConstants.clawOpen;
  }

  public void clawClose() {
    ClawState = clawState.CLOSE;
    this.m_servo_claw_targetPosition = constants.outputConstants.clawClose;
  }

  public void switchClaw() {
    switch (ClawState) {
      case OPEN:
        clawClose();
        break;
      case CLOSE:
        clawOpen();
        break;
    }
  }

  public boolean isClawClosed() {
    if (this.m_servo_claw_targetPosition == constants.outputConstants.clawClose) {
      return true;
    } else {
      return false;
    }
  }

  public double getM_motor_power() {
    return this.power;
  }

  public double getM_currentPosition() {
    return this.m_currentPosition;
  }
}
