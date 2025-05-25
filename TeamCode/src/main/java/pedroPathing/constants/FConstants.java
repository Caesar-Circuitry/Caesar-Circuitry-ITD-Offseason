package pedroPathing.constants;

import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Localizers;

import robot.constants;

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

    FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.1, 0, 0.01, 0);
    FollowerConstants.useSecondaryTranslationalPID = false;
    FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(
        0.1, 0, 0.01, 0); // Not being used, @see useSecondaryTranslationalPID

    FollowerConstants.headingPIDFCoefficients.setCoefficients(2, 0, 0.1, 0);
    FollowerConstants.useSecondaryHeadingPID = false;
    FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(
        2, 0, 0.1, 0); // Not being used, @see useSecondaryHeadingPID

    FollowerConstants.drivePIDFCoefficients.setCoefficients(0.1, 0, 0, 0.6, 0);
    FollowerConstants.useSecondaryDrivePID = false;
    FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(
        0.1, 0, 0, 0.6, 0); // Not being used, @see useSecondaryDrivePID

    FollowerConstants.zeroPowerAccelerationMultiplier = 4;
    FollowerConstants.centripetalScaling = 0.0005;

    FollowerConstants.pathEndTimeoutConstraint = 500;
    FollowerConstants.pathEndTValueConstraint = 0.995;
    FollowerConstants.pathEndVelocityConstraint = 0.1;
    FollowerConstants.pathEndTranslationalConstraint = 0.1;
    FollowerConstants.pathEndHeadingConstraint = 0.007;
  }
}
