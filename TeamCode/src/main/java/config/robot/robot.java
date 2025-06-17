package config.robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.InstantCommand;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.command.ScheduleCommand;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.command.button.Button;
import com.seattlesolvers.solverslib.command.button.GamepadButton;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.TriggerReader;

import config.robot.subsystems.robotHardware;

public class robot extends Robot {
  private HardwareMap hardwareMap;
  private robotHardware hardware;
  private GamepadEx driver, operator;
  private boolean isTeleOp = true;

  // GamePad1 Triggers
  Button leftStickButton; // toggle claw
  Button rightStickButton;
  Button share; // field centric toggle
  Button PsButton;
  Button rightBumper;
  Button leftBumper;
  TriggerReader rightTrigger; // sub mode the extend
  TriggerReader leftTrigger;
  Trigger Hover;
  Trigger sub;
  Trigger Transfer;
  Trigger Right;
  Trigger notRight;
  Trigger Left;
  Trigger OutClawClosed;
  Trigger autoDrive;

  // GamePad2 Triggers
  Button wallGrab;
  Button placeSpec;

  // the constructor with a specified opmode type
  /**
   * @param hardwareMap the hardware map of the robot inits as auto
   */
  public robot(HardwareMap hardwareMap) {
    this.hardwareMap = hardwareMap;
    initAuto();
  }

  public robot(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
    this.hardwareMap = hardwareMap;
    initTele(gamepad1, gamepad2);
  }

  /*
   * Initialize teleop or autonomous, depending on which is used
   */
  public void initTele(Gamepad gamepad1, Gamepad gamepad2) {
    // initialize teleop-specific scheduler
    hardware = new robotHardware(hardwareMap);
    this.driver = new GamepadEx(gamepad1);
    this.operator = new GamepadEx(gamepad2);
    hardware.getFollower().startTeleopDrive();
    isTeleOp = true;
    driverTriggers();
    driverTriggerCommands();
  }

  public void initAuto() {
    // initialize auto-specific scheduler
    constants.intakeConstants.slideSub = 0;
    hardware = new robotHardware(hardwareMap);
    isTeleOp = false;
  }

  private void driverTriggers() {
    leftStickButton = new GamepadButton(driver, GamepadKeys.Button.LEFT_STICK_BUTTON);
    rightStickButton = new GamepadButton(driver, GamepadKeys.Button.RIGHT_STICK_BUTTON);
    share = new GamepadButton(driver, GamepadKeys.Button.SHARE);
    PsButton = new GamepadButton(driver, GamepadKeys.Button.PS);
    rightTrigger = new TriggerReader(driver, GamepadKeys.Trigger.RIGHT_TRIGGER);
    leftTrigger = new TriggerReader(driver, GamepadKeys.Trigger.LEFT_TRIGGER);
    rightBumper = new GamepadButton(driver, GamepadKeys.Button.RIGHT_BUMPER);
    leftBumper = new GamepadButton(driver, GamepadKeys.Button.LEFT_BUMPER);
    Hover = new Trigger(() -> rightTrigger.isDown()).and(new Trigger(() -> !leftTrigger.isDown()));
    sub = new Trigger(() -> leftTrigger.isDown()).and(new Trigger(() -> rightTrigger.isDown()));
    Transfer =
        new Trigger(() -> !leftTrigger.isDown()).and(new Trigger(() -> !rightTrigger.isDown()));
    Right = new Trigger(() -> rightTrigger.isDown());
    notRight = new Trigger(() -> !rightTrigger.isDown());
    Left = new Trigger(() -> leftTrigger.isDown());
    OutClawClosed = new Trigger(() -> hardware.getOutput().isClawClosed());
    autoDrive = new Trigger(() -> operator.getButton(GamepadKeys.Button.CROSS));

    // Operator Controls
    wallGrab = new GamepadButton(operator, GamepadKeys.Button.RIGHT_BUMPER);
    placeSpec = new GamepadButton(operator, GamepadKeys.Button.LEFT_BUMPER);
  }

  private void driverTriggerCommands() {
    share.whenPressed(new InstantCommand(hardware.getDrive()::switchCentric));
    rightStickButton.whenPressed(new InstantCommand(hardware.getDrive()::switchLock));
    PsButton.whenPressed(new InstantCommand(hardware.getDrive()::resetHeading));
    Hover.whenActive(new InstantCommand(hardware.getIntake()::HoverPickup));
    sub.whenActive(new InstantCommand(hardware.getIntake()::Submersible));
    Transfer.whenActive(new InstantCommand(hardware.getIntake()::HumanPlayer));
    rightBumper.and(Right).whenActive(hardware.getIntake()::ClawPivotRight);
    leftBumper.and(Right).whenActive(hardware.getIntake()::ClawPivotMiddle);
    Left.and(OutClawClosed).whenActive(hardware.getOutput()::TargetHighChamber);
    Left.whenInactive(hardware.getOutput()::TargetTransfer);
    leftBumper.and(notRight).whenActive(hardware.getOutput()::TargetWall);
    autoDrive.whenActive(hardware.getDrive()::autoDrive);
    autoDrive.whenInactive(hardware.getDrive()::TeleOp);
    placeSpec.whenPressed(
        new ScheduleCommand(
            new SequentialCommandGroup(
                new InstantCommand(hardware.getOutput()::ScoreSpec),
                new WaitCommand(100),
                new InstantCommand(hardware.getOutput()::clawOpen),
                new WaitCommand(100),
                new InstantCommand(hardware.getOutput()::TargetTransfer))));
    wallGrab.whenPressed(new InstantCommand(hardware.getOutput()::switchClaw));
    // rightBumper.and(notRight).whenActive(hardware.getOutput()::clawClose);
  }

  public void read() {
    if (isTeleOp) {
      rightTrigger.readValue();
      leftTrigger.readValue();
      driver.readButtons();
    }
    hardware.read();
  }

  public void loop() {
    hardware.loop();
  }

  public void write() {
    hardware.write();
  }

  /**
   * Get the robot hardware subsystem
   *
   * @return The robotHardware instance
   */
  public robotHardware getHardware() {
    return hardware;
  }
}
