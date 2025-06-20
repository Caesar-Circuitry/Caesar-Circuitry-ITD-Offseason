package config.paths;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

public class autoPath5Push {
  // start pose
  // start poses
  public static final Pose startPose = new Pose(9.000, 65.00, Math.toRadians(180));
  private static final Pose specimen0Pose = new Pose(37.000, 67.00, Math.toRadians(180));

  // sample poses
  private static final Pose Sample1Pose = new Pose(16, 21, Math.toRadians(0));
  private static final Pose Sample2Pose = new Pose(20, 12, Math.toRadians(0));
  private static final Pose ToWall1 = new Pose(13.5, 12, Math.toRadians(0));
  private static final Pose ToWall2 = new Pose(13.5, 34, Math.toRadians(0));

  // grab specimen poses
  private static final Pose grabSpecimen1Pose = new Pose(14.000, 13.000, Math.toRadians(0));
  private static final Pose grabSpecimen2Pose = new Pose(20, 34.000, Math.toRadians(0));
  private static final Pose grabSpecimen3Pose = new Pose(20, 34.000, Math.toRadians(0));

  // place specimen poses
  private static final Pose placeSpecimen1Pose = new Pose(37.000, 66, Math.toRadians(180));
  private static final Pose placeSpecimen2Pose = new Pose(37.000, 67, Math.toRadians(180));
  private static final Pose placeSpecimen3Pose = new Pose(37.000, 68, Math.toRadians(180));

  private static final Pose parkPose = new Pose(20, 26, Math.toRadians(0));

  public static PathChain specimen0() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(startPose), new Point(specimen0Pose)))
        .setConstantHeadingInterpolation(startPose.getHeading())
        .build();
  }

  public static PathChain Sample1() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(specimen0Pose),
                new Point(14.5, 14.5, Point.CARTESIAN),
                new Point(73, 59.25, Point.CARTESIAN),
                new Point(89.5, 17, Point.CARTESIAN),
                new Point(Sample1Pose)))
        .setConstantHeadingInterpolation(Math.toRadians(0))
        .build();
  }

  public static PathChain Sample2() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(Sample1Pose),
                new Point(48, 34, Point.CARTESIAN),
                new Point(107, 13.5, Point.CARTESIAN),
                new Point(Sample2Pose)))
        .setConstantHeadingInterpolation((Math.toRadians(0)))
        .build();
  }

  public static PathChain GrabOffWall1() {
    return new PathBuilder()
        .addPath(new BezierCurve(new Point(Sample2Pose), new Point(ToWall1)))
        .setConstantHeadingInterpolation(0)
        .build();
  }

  public static PathChain GrabOffWall2() {
    return new PathBuilder()
        .addPath(new BezierCurve(new Point(grabSpecimen2Pose), new Point(ToWall2)))
        .setConstantHeadingInterpolation(0)
        .build();
  }

  public static PathChain placeSpecimen1() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(ToWall1),
                new Point(34.000, 12.750, Point.CARTESIAN),
                new Point(29.000, 37.500, Point.CARTESIAN),
                new Point(20.000, 61.000, Point.CARTESIAN),
                new Point(placeSpecimen1Pose)))
        .setLinearHeadingInterpolation(Math.toRadians(0), placeSpecimen1Pose.getHeading())
        .build();
  }

  public static PathChain grabSpecimen2() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(placeSpecimen1Pose), new Point(grabSpecimen2Pose)))
        .setLinearHeadingInterpolation(placeSpecimen1Pose.getHeading(), ToWall2.getHeading())
        .build();
  }

  public static PathChain placeSpecimen2() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(ToWall2),
                new Point(17, 55, Point.CARTESIAN),
                new Point(placeSpecimen2Pose)))
        .setLinearHeadingInterpolation(ToWall2.getHeading(), placeSpecimen2Pose.getHeading())
        .build();
  }

  public static PathChain grabSpecimen3() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(placeSpecimen2Pose), new Point(grabSpecimen3Pose)))
        .setLinearHeadingInterpolation(
            placeSpecimen2Pose.getHeading(), grabSpecimen3Pose.getHeading())
        .build();
  }

  public static PathChain placeSpecimen3() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(grabSpecimen3Pose),
                new Point(17, 55, Point.CARTESIAN),
                new Point(placeSpecimen3Pose)))
        .setLinearHeadingInterpolation(
            grabSpecimen3Pose.getHeading(), placeSpecimen3Pose.getHeading())
        .build();
  }

  public static PathChain park() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(placeSpecimen3Pose), new Point(parkPose)))
        .setLinearHeadingInterpolation(placeSpecimen3Pose.getHeading(), parkPose.getHeading())
        .build();
  }
}
