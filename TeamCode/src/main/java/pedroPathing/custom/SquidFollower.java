package pedroPathing.custom;

import java.util.ArrayList;
import java.util.List;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.follower.DriveVectorScaler;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.localization.Localizer;
import com.pedropathing.localization.Pose;
import com.pedropathing.localization.PoseUpdater;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Vector;
import com.pedropathing.util.Constants;
import com.pedropathing.util.DashboardPoseTracker;
import com.pedropathing.util.FilteredPIDFController;
import com.pedropathing.util.KalmanFilter;
import com.pedropathing.util.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is the Follower class. It handles the actual following of the paths and all the on-the-fly
 * calculations that are relevant for movement.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/4/2024
 */
@Config
public class SquidFollower extends Follower {
  private HardwareMap hardwareMap;

  private DcMotorEx leftFront;
  private DcMotorEx leftRear;
  private DcMotorEx rightFront;
  private DcMotorEx rightRear;
  private List<DcMotorEx> motors;

  private DriveVectorScaler driveVectorScaler;

  public PoseUpdater poseUpdater;
  private DashboardPoseTracker dashboardPoseTracker;

  private Pose closestPose;

  private Path currentPath;

  private PathChain currentPathChain;

  private int BEZIER_CURVE_SEARCH_LIMIT;
  private int AVERAGED_VELOCITY_SAMPLE_NUMBER;

  private int chainIndex;

  private long[] pathStartTimes;

  private boolean followingPathChain;
  private boolean holdingPosition;
  private boolean isBusy, isTurning;
  private boolean reachedParametricPathEnd;
  private boolean holdPositionAtEnd;
  private boolean teleopDrive;

  private double globalMaxPower = 1;
  private double previousSecondaryTranslationalIntegral;
  private double previousTranslationalIntegral;
  private double holdPointTranslationalScaling;
  private double holdPointHeadingScaling;
  public double driveError;
  public double headingError;

  private long reachedParametricPathEndTime;

  private double[] drivePowers;
  private double[] teleopDriveValues;

  private ArrayList<Vector> velocities = new ArrayList<>();
  private ArrayList<Vector> accelerations = new ArrayList<>();

  private double centripetalScaling;

  private PIDFController secondaryTranslationalPIDF;
  private PIDFController secondaryTranslationalIntegral;
  private PIDFController translationalPIDF;
  private PIDFController translationalIntegral;
  private PIDFController secondaryHeadingPIDF;
  private PIDFController headingPIDF;
  private FilteredPIDFController secondaryDrivePIDF;
  private FilteredPIDFController drivePIDF;

  private KalmanFilter driveKalmanFilter;
  private double[] driveErrors;
  private double rawDriveError;
  private double previousRawDriveError;
  private double turnHeadingErrorThreshold;

  public static boolean drawOnDashboard = true;
  public static boolean useTranslational = true;
  public static boolean useCentripetal = true;
  public static boolean useHeading = true;
  public static boolean useDrive = true;

  /*
   * Voltage Compensation
   * Credit to team 14343 Escape Velocity for the voltage code
   * Credit to team 23511 Seattle Solvers for implementing the voltage code into Follower.java
   */
  private boolean cached = false;

  private VoltageSensor voltageSensor;
  public double voltage = 0;
  private final ElapsedTime voltageTimer = new ElapsedTime();

  private boolean logDebug = true;

  private ElapsedTime zeroVelocityDetectedTimer;

  /**
   * This creates a new Follower given a HardwareMap.
   *
   * @param hardwareMap HardwareMap required
   */
  public SquidFollower(HardwareMap hardwareMap, Class<?> FConstants, Class<?> LConstants) {
    super(hardwareMap, FConstants, LConstants);
    this.hardwareMap = hardwareMap;
    setupConstants(FConstants, LConstants);
    initialize();
  }

  /**
   * This creates a new Follower given a HardwareMap and a localizer.
   *
   * @param hardwareMap HardwareMap required
   * @param localizer the localizer you wish to use
   */
  public SquidFollower(
      HardwareMap hardwareMap, Localizer localizer, Class<?> FConstants, Class<?> LConstants) {
    super(hardwareMap, localizer, FConstants, LConstants);
    this.hardwareMap = hardwareMap;
    setupConstants(FConstants, LConstants);
    initialize(localizer);
  }

  /**
   * Setup constants for the Follower.
   *
   * @param FConstants the constants for the Follower
   * @param LConstants the constants for the Localizer
   */
  @Override
  public void setupConstants(Class<?> FConstants, Class<?> LConstants) {
    Constants.setConstants(FConstants, LConstants);
    BEZIER_CURVE_SEARCH_LIMIT = FollowerConstants.BEZIER_CURVE_SEARCH_LIMIT;
    AVERAGED_VELOCITY_SAMPLE_NUMBER = FollowerConstants.AVERAGED_VELOCITY_SAMPLE_NUMBER;
    holdPointTranslationalScaling = FollowerConstants.holdPointTranslationalScaling;
    holdPointHeadingScaling = FollowerConstants.holdPointHeadingScaling;
    centripetalScaling = FollowerConstants.centripetalScaling;
    secondaryTranslationalPIDF =
        new SquidController(FollowerConstants.secondaryTranslationalPIDFCoefficients);
    secondaryTranslationalIntegral =
        new SquidController(FollowerConstants.secondaryTranslationalIntegral);
    translationalPIDF = new SquidController(FollowerConstants.translationalPIDFCoefficients);
    translationalIntegral = new SquidController(FollowerConstants.translationalIntegral);
    secondaryHeadingPIDF = new SquidController(FollowerConstants.secondaryHeadingPIDFCoefficients);
    headingPIDF = new SquidController(FollowerConstants.headingPIDFCoefficients);
    secondaryDrivePIDF =
        new FilteredSquidController(FollowerConstants.secondaryDrivePIDFCoefficients);
    drivePIDF = new FilteredSquidController(FollowerConstants.drivePIDFCoefficients);
    driveKalmanFilter = new KalmanFilter(FollowerConstants.driveKalmanFilterParameters);
    turnHeadingErrorThreshold = FollowerConstants.turnHeadingErrorThreshold;
  }
}
