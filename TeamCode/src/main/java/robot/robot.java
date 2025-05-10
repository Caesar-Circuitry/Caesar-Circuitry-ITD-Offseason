package robot;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.seattlesolvers.solverslib.command.Robot;
import com.seattlesolvers.solverslib.command.RunCommand;
import com.seattlesolvers.solverslib.command.button.Trigger;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;

import robot.subsystems.robotHardware;

public class robot extends Robot {
    private HardwareMap hardwareMap;
    private robotHardware hardware;
    private GamepadEx driver, operator;


    //GamePad1 Triggers
    private Trigger startTrigger; //switches drive centric
    private Trigger optionTrigger; //switches heading lock
    //TODO Consider choosing diffirent bindings due to same finger driving as pressing dpad
    private Trigger DpadRightTrigger; //switches locked heading to 0 degrees
    private Trigger DpadUpTrigger; //switches locked heading to 90 degrees
    private Trigger DpadLeftTrigger; //switches locked heading to 180 degrees
    private Trigger DpadDownTrigger; //switches locked heading to 270 degrees

    private Trigger psButton; //resets follower heading to 0

    // the constructor with a specified opmode type
    /**
    * @param hardwareMap the hardware map of the robot
     *        inits as auto
    */
    public robot(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
            initAuto();
    }
    public robot(HardwareMap hardwareMap, Gamepad gamepad1, Gamepad gamepad2) {
        this.hardwareMap = hardwareMap;
        initTele(gamepad1,gamepad2);
    }

    /*
     * Initialize teleop or autonomous, depending on which is used
     */
    public void initTele(Gamepad gamepad1, Gamepad gamepad2) {
        // initialize teleop-specific scheduler
        hardware = new robotHardware(hardwareMap);
        this.driver = new GamepadEx(gamepad1);
        this.operator = new GamepadEx(gamepad2);
        schedule(
                new RunCommand(hardware::read),
                new RunCommand(hardware::loop),
                new RunCommand(hardware::write)
        );
    }

    public void initAuto() {
        // initialize auto-specific scheduler
        hardware = new robotHardware(hardwareMap);
    }

    private void driverTriggers(){
        startTrigger = new Trigger(() -> driver.getButton(GamepadKeys.Button.START));
        optionTrigger = new Trigger(() -> driver.getButton(GamepadKeys.Button.OPTIONS));
        DpadRightTrigger = new Trigger(() -> driver.getButton(GamepadKeys.Button.DPAD_RIGHT));
        DpadUpTrigger = new Trigger(() -> driver.getButton(GamepadKeys.Button.DPAD_UP));
        DpadLeftTrigger = new Trigger(() -> driver.getButton(GamepadKeys.Button.DPAD_LEFT));
        DpadDownTrigger = new Trigger(() -> driver.getButton(GamepadKeys.Button.DPAD_DOWN));
        psButton = new Trigger(() -> driver.getButton(GamepadKeys.Button.PS));
    }
    private void driverTriggerCommands(){
        startTrigger.whenActive(hardware.getDrive()::switchCentric);
        optionTrigger.whenActive(hardware.getDrive()::switchLock);
        DpadRightTrigger.whenActive(hardware.getDrive()::LockedHeading0);
        DpadUpTrigger.whenActive(hardware.getDrive()::LockedHeading90);
        DpadLeftTrigger.whenActive(hardware.getDrive()::LockedHeading180);
        DpadDownTrigger.whenActive(hardware.getDrive()::LockedHeading270);
        psButton.whenActive(hardware.getDrive()::resetHeading);
    }
}
