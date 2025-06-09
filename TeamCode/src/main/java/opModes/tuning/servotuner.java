package opModes.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class servotuner extends LinearOpMode {
  private enum Servos {
    ONE,
    TWO,
    THREE,
    FOUR
  }

  public static Servos numberOfServos = Servos.ONE;
  private Servos ServoSelected = Servos.ONE;
  private Servo servo1, servo2, servo3, servo4;
  public static String servoName1 = "";
  public static String servoName2 = "";
  public static String servoName3 = "";
  public static String servoName4 = "";
  public static double servoLeftPosition = 0, servoMiddlePosition = 0.5, servoRightPosition = 1;
  private double servo1LeftPosition = 0, servo1MiddlePosition = 0.5, servo1RightPosition = 1;
  private double servo2LeftPosition = 0, servo2MiddlePosition = 0.5, servo2RightPosition = 1;
  private double servo3LeftPosition = 0, servo3MiddlePosition = 0.5, servo3RightPosition = 1;
  private double servo4LeftPosition = 0, servo4MiddlePosition = 0.5, servo4RightPosition = 1;
  private MultipleTelemetry dashboardTelemetry;

  @Override
  public void runOpMode() throws InterruptedException {
    dashboardTelemetry =
        new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    switch (numberOfServos) {
      case ONE:
        servo1 = hardwareMap.get(Servo.class, servoName1);
        break;
      case TWO:
        servo1 = hardwareMap.get(Servo.class, servoName1);
        servo2 = hardwareMap.get(Servo.class, servoName2);
        break;
      case THREE:
        servo1 = hardwareMap.get(Servo.class, servoName1);
        servo2 = hardwareMap.get(Servo.class, servoName2);
        servo3 = hardwareMap.get(Servo.class, servoName3);
        break;
      case FOUR:
        servo1 = hardwareMap.get(Servo.class, servoName1);
        servo2 = hardwareMap.get(Servo.class, servoName2);
        servo3 = hardwareMap.get(Servo.class, servoName3);
        servo4 = hardwareMap.get(Servo.class, servoName4);
        break;
    }

    // Initialize with a rumble to indicate which servo is selected
    rumbleForServo(ServoSelected);

    waitForStart();
    while (opModeIsActive()) {

      switch (numberOfServos) {
        case ONE:
          if (gamepad1.circle) {
            servo1LeftPosition = servoLeftPosition;
            servo1.setPosition(servo1LeftPosition);
          } else if (gamepad1.cross) {
            servo1MiddlePosition = servoMiddlePosition;
            servo1.setPosition(servo1MiddlePosition);
          } else if (gamepad1.square) {
            servo1RightPosition = servoRightPosition;
            servo1.setPosition(servo1RightPosition);
          }
          dashboardTelemetry.addData("Servo 1 Position", servo1.getPosition());
          break;
        case TWO:
          switch (ServoSelected) {
            case ONE:
              if (gamepad1.circle) {
                servo1LeftPosition = servoLeftPosition;
                servo1.setPosition(servo1LeftPosition);
              } else if (gamepad1.cross) {
                servo1MiddlePosition = servoMiddlePosition;
                servo1.setPosition(servo1MiddlePosition);
              } else if (gamepad1.square) {
                servo1RightPosition = servoRightPosition;
                servo1.setPosition(servo1RightPosition);
              }
              if (gamepad1.right_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo2LeftPosition;
                servoMiddlePosition = servo2MiddlePosition;
                servoRightPosition = servo2RightPosition;
                ServoSelected = Servos.TWO;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 1 Position", servo1.getPosition());
              break;
            case TWO:
              if (gamepad1.circle) {
                servo2LeftPosition = servoLeftPosition;
                servo2.setPosition(servo2LeftPosition);
              } else if (gamepad1.cross) {
                servo2MiddlePosition = servoMiddlePosition;
                servo2.setPosition(servo2MiddlePosition);
              } else if (gamepad1.square) {
                servo2RightPosition = servoRightPosition;
                servo2.setPosition(servo2RightPosition);
              }
              if (gamepad1.left_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo1LeftPosition;
                servoMiddlePosition = servo1MiddlePosition;
                servoRightPosition = servo1RightPosition;
                ServoSelected = Servos.ONE;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 2 Position", servo2.getPosition());
              break;
          }
          break;
        case THREE:
          switch (ServoSelected) {
            case ONE:
              if (gamepad1.circle) {
                servo1LeftPosition = servoLeftPosition;
                servo1.setPosition(servo1LeftPosition);
              } else if (gamepad1.cross) {
                servo1MiddlePosition = servoMiddlePosition;
                servo1.setPosition(servo1MiddlePosition);
              } else if (gamepad1.square) {
                servo1RightPosition = servoRightPosition;
                servo1.setPosition(servo1RightPosition);
              }
              if (gamepad1.right_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo2LeftPosition;
                servoMiddlePosition = servo2MiddlePosition;
                servoRightPosition = servo2RightPosition;
                ServoSelected = Servos.TWO;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 1 Position", servo1.getPosition());
              break;
            case TWO:
              if (gamepad1.circle) {
                servo2LeftPosition = servoLeftPosition;
                servo2.setPosition(servo2LeftPosition);
              } else if (gamepad1.cross) {
                servo2MiddlePosition = servoMiddlePosition;
                servo2.setPosition(servo2MiddlePosition);
              } else if (gamepad1.square) {
                servo2RightPosition = servoRightPosition;
                servo2.setPosition(servo2RightPosition);
              }
              if (gamepad1.left_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo1LeftPosition;
                servoMiddlePosition = servo1MiddlePosition;
                servoRightPosition = servo1RightPosition;
                ServoSelected = Servos.ONE;
                rumbleForServo(ServoSelected);
              } else if (gamepad1.right_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo3LeftPosition;
                servoMiddlePosition = servo3MiddlePosition;
                servoRightPosition = servo3RightPosition;
                ServoSelected = Servos.THREE;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 2 Position", servo2.getPosition());
              break;
            case THREE:
              if (gamepad1.circle) {
                servo3LeftPosition = servoLeftPosition;
                servo3.setPosition(servo3LeftPosition);
              } else if (gamepad1.cross) {
                servo3MiddlePosition = servoMiddlePosition;
                servo3.setPosition(servo3MiddlePosition);
              } else if (gamepad1.square) {
                servo3RightPosition = servoRightPosition;
                servo3.setPosition(servo3RightPosition);
              }
              if (gamepad1.left_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo2LeftPosition;
                servoMiddlePosition = servo2MiddlePosition;
                servoRightPosition = servo2RightPosition;
                ServoSelected = Servos.TWO;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 3 Position", servo3.getPosition());
              break;
          }
          break;
        case FOUR:
          switch (ServoSelected) {
            case ONE:
              if (gamepad1.circle) {
                servo1LeftPosition = servoLeftPosition;
                servo1.setPosition(servo1LeftPosition);
              } else if (gamepad1.cross) {
                servo1MiddlePosition = servoMiddlePosition;
                servo1.setPosition(servo1MiddlePosition);
              } else if (gamepad1.square) {
                servo1RightPosition = servoRightPosition;
                servo1.setPosition(servo1RightPosition);
              }
              if (gamepad1.right_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo2LeftPosition;
                servoMiddlePosition = servo2MiddlePosition;
                servoRightPosition = servo2RightPosition;
                ServoSelected = Servos.TWO;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 1 Position", servo1.getPosition());
              break;
            case TWO:
              if (gamepad1.circle) {
                servo2LeftPosition = servoLeftPosition;
                servo2.setPosition(servo2LeftPosition);
              } else if (gamepad1.cross) {
                servo2MiddlePosition = servoMiddlePosition;
                servo2.setPosition(servo2MiddlePosition);
              } else if (gamepad1.square) {
                servo2RightPosition = servoRightPosition;
                servo2.setPosition(servo2RightPosition);
              }
              if (gamepad1.left_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo1LeftPosition;
                servoMiddlePosition = servo1MiddlePosition;
                servoRightPosition = servo1RightPosition;
                ServoSelected = Servos.ONE;
                rumbleForServo(ServoSelected);
              } else if (gamepad1.right_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo3LeftPosition;
                servoMiddlePosition = servo3MiddlePosition;
                servoRightPosition = servo3RightPosition;
                ServoSelected = Servos.THREE;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 2 Position", servo2.getPosition());
              break;
            case THREE:
              if (gamepad1.circle) {
                servo3LeftPosition = servoLeftPosition;
                servo3.setPosition(servo3LeftPosition);
              } else if (gamepad1.cross) {
                servo3MiddlePosition = servoMiddlePosition;
                servo3.setPosition(servo3MiddlePosition);
              } else if (gamepad1.square) {
                servo3RightPosition = servoRightPosition;
                servo3.setPosition(servo3RightPosition);
              }
              if (gamepad1.left_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo2LeftPosition;
                servoMiddlePosition = servo2MiddlePosition;
                servoRightPosition = servo2RightPosition;
                ServoSelected = Servos.TWO;
                rumbleForServo(ServoSelected);
              } else if (gamepad1.right_bumper && !gamepad1.isRumbling()) {
                servoLeftPosition = servo4LeftPosition;
                servoMiddlePosition = servo4MiddlePosition;
                servoRightPosition = servo4RightPosition;
                ServoSelected = Servos.FOUR;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 3 Position", servo3.getPosition());
              break;
            case FOUR:
              if (gamepad1.circle) {
                servo4LeftPosition = servoLeftPosition;
                servo4.setPosition(servo4LeftPosition);
              } else if (gamepad1.cross) {
                servo4MiddlePosition = servoMiddlePosition;
                servo4.setPosition(servo4MiddlePosition);
              } else if (gamepad1.square) {
                servo4RightPosition = servoRightPosition;
                servo4.setPosition(servo4RightPosition);
              }
              if (gamepad1.left_bumper) {
                servoLeftPosition = servo3LeftPosition;
                servoMiddlePosition = servo3MiddlePosition;
                servoRightPosition = servo3RightPosition;
                ServoSelected = Servos.THREE;
                rumbleForServo(ServoSelected);
              }
              dashboardTelemetry.addData("Servo 4 Position", servo4.getPosition());
              break;
          }
          break;
      }

      // Display current servo and position information
      dashboardTelemetry.addData("Current Servo", ServoSelected);
      dashboardTelemetry.addData("Left Position", servoLeftPosition);
      dashboardTelemetry.addData("Middle Position", servoMiddlePosition);
      dashboardTelemetry.addData("Right Position", servoRightPosition);
      dashboardTelemetry.addData("Controls", "X: middle, Circle: left, Square: Right");
      dashboardTelemetry.update();
    }
  }

  /**
   * Initiates rumble feedback based on which servo is selected Number of rumbles = servo number
   * (1-4)
   */
  private void rumbleForServo(Servos servo) {
    // Set up rumble sequence
    switch (servo) {
      case ONE:
        gamepad1.rumbleBlips(1);
        break;
      case TWO:
        gamepad1.rumbleBlips(2);
        break;
      case THREE:
        gamepad1.rumbleBlips(3);
        break;
      case FOUR:
        gamepad1.rumbleBlips(4);
        break;
    }
  }
}
