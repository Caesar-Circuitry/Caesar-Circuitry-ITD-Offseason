package config.pedroPathing.constants;

import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Localizers;

import config.robot.constants;

public class FConstants {
  static {
    FollowerConstants.localizers = Localizers.PINPOINT;

    FollowerConstants.leftFrontMotorName = constants.DriveConstants.leftFrontMotorName;
    FollowerConstants.leftRearMotorName = constants.DriveConstants.leftRearMotorName;
    FollowerConstants.rightFrontMotorName = constants.DriveConstants.rightFrontMotorName;
    FollowerConstants.rightRearMotorName = constants.DriveConstants.rightRearMotorName;

    FollowerConstants.leftFrontMotorDirection = constants.DriveConstants.leftFrontMotorDirection;
    FollowerConstants.leftRearMotorDirection = constants.DriveConstants.leftRearMotorDirection;
    FollowerConstants.rightFrontMotorDirection = constants.DriveConstants.rightFrontMotorDirection;
    ;
    FollowerConstants.rightRearMotorDirection = constants.DriveConstants.rightRearMotorDirection;

    FollowerConstants.mass = 13;

    FollowerConstants.xMovement = 69.49399274350246;
    FollowerConstants.yMovement = 48.23506582769686;

    FollowerConstants.forwardZeroPowerAcceleration = -43.93083856199569;
    FollowerConstants.lateralZeroPowerAcceleration = -73.04883058653962;

    FollowerConstants.translationalPIDFCoefficients.setCoefficients(.72, 0, .04576271186, 0);
    FollowerConstants.useSecondaryTranslationalPID = true;
    FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(
        0.72, 0, 0.03180288462, 0); // Not being used, @see useSecondaryTranslationalPID

    FollowerConstants.headingPIDFCoefficients.setCoefficients(5.2, 0, .12, 0);
    FollowerConstants.useSecondaryHeadingPID = true;
    FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(
        4.8, 0, 0.2724458204, 0); // Not being used, @see useSecondaryHeadingPID

    FollowerConstants.drivePIDFCoefficients.setCoefficients(0.02, 0, 0, 0.6, 0.04);
    FollowerConstants.useSecondaryDrivePID = true;
    FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(
        0.01, 0, 0, 0.6, 0); // Not being used, @see useSecondaryDrivePID

    FollowerConstants.zeroPowerAccelerationMultiplier = 4;
    FollowerConstants.centripetalScaling = 0.0005;

    FollowerConstants.pathEndTimeoutConstraint = 50;
    FollowerConstants.pathEndTValueConstraint = 0.995;
    FollowerConstants.pathEndVelocityConstraint = 0.1;
    FollowerConstants.pathEndTranslationalConstraint = 0.1;
    FollowerConstants.pathEndHeadingConstraint = 0.007;
    FollowerConstants.useVoltageCompensationInAuto = true;
    FollowerConstants.useVoltageCompensationInTeleOp = true;
    FollowerConstants.nominalVoltage = 12.5;
  }
}
