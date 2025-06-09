package config.pedroPathing.constants;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import com.pedropathing.localization.*;
import com.pedropathing.localization.constants.*;

public class LConstants {
  static {
    PinpointConstants.forwardY = -5.25;
    PinpointConstants.strafeX = -1.6;
    PinpointConstants.distanceUnit = DistanceUnit.INCH;
    PinpointConstants.hardwareMapName = "pinpoint";
    PinpointConstants.useYawScalar = false;
    PinpointConstants.yawScalar = 1.0;
    PinpointConstants.useCustomEncoderResolution = false;
    PinpointConstants.encoderResolution =
        GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD;
    PinpointConstants.customEncoderResolution = 13.26291192;
    PinpointConstants.forwardEncoderDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
    PinpointConstants.strafeEncoderDirection = GoBildaPinpointDriver.EncoderDirection.REVERSED;
  }
}
