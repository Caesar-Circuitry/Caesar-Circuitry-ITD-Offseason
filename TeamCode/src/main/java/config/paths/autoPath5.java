package config.paths;

import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

public class autoPath5 {
  // start poses
  public static final Pose startPose = new Pose(9.000, 65.00, Math.toRadians(180));
  private static final Pose specimen0Pose = new Pose(38.000, 67.00, Math.toRadians(180));

  // grab sample poses
  private static final Pose grabSample1Pose = new Pose(33.000, 36.000, Math.toRadians(320));
  private static final Pose grabSample2Pose = new Pose(33.000, 23.500, Math.toRadians(320));
  private static final Pose grabSample3Pose = new Pose(33.000, 14.000, Math.toRadians(320));

  // hp sample poses
  private static final Pose hpSample1Pose = new Pose(33.000, 35.500, Math.toRadians(220));
  private static final Pose hpSample2Pose = new Pose(33.000, 23.000, Math.toRadians(220));
  private static final Pose hpSample3Pose = new Pose(10.000, 20.000, Math.toRadians(90));

  // grab specimen poses
  private static final Pose grabSpecimen1Pose = new Pose(11.000, 13.000, Math.toRadians(90));
  private static final Pose grabSpecimen2Pose = new Pose(14.000, 37.000, Math.toRadians(0));
  private static final Pose grabSpecimen3Pose = new Pose(14.000, 37.000, Math.toRadians(0));
  private static final Pose grabSpecimen4Pose = new Pose(14.000, 37.000, Math.toRadians(0));

  // place specimen poses
  private static final Pose placeSpecimen1Pose = new Pose(36.000, 64.000, Math.toRadians(180));
  private static final Pose placeSpecimen2Pose = new Pose(36.000, 65.000, Math.toRadians(180));
  private static final Pose placeSpecimen3Pose = new Pose(36.000, 66.000, Math.toRadians(180));
  private static final Pose placeSpecimen4Pose = new Pose(36.000, 66.000, Math.toRadians(180));

  public static PathChain specimen0() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(startPose), new Point(specimen0Pose)))
        .setLinearHeadingInterpolation(startPose.getHeading(), specimen0Pose.getHeading())
        .build();
  }

  public static PathChain grabSample1() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(specimen0Pose),
                new Point(33.000, 51.750, Point.CARTESIAN),
                new Point(grabSample1Pose)))
        .setLinearHeadingInterpolation(specimen0Pose.getHeading(), grabSample1Pose.getHeading())
        .setPathEndTValueConstraint(.9)
        .build();
  }

  public static PathChain hpSample1() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(grabSample1Pose), new Point(hpSample1Pose)))
        .setLinearHeadingInterpolation(grabSample1Pose.getHeading(), hpSample1Pose.getHeading())
        .setPathEndTValueConstraint(.9)
        .build();
  }

  public static PathChain grabSample2() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(hpSample1Pose), new Point(grabSample2Pose)))
        .setLinearHeadingInterpolation(hpSample1Pose.getHeading(), grabSample2Pose.getHeading())
        .setPathEndTValueConstraint(.9)
        .build();
  }

  public static PathChain hpSample2() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(grabSample2Pose), new Point(hpSample2Pose)))
        .setLinearHeadingInterpolation(grabSample2Pose.getHeading(), hpSample2Pose.getHeading())
        .setPathEndTValueConstraint(.9)
        .build();
  }

  public static PathChain grabSample3() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(hpSample2Pose), new Point(grabSample3Pose)))
        .setLinearHeadingInterpolation(hpSample2Pose.getHeading(), grabSample3Pose.getHeading())
        .setPathEndTValueConstraint(.9)
        .build();
  }

  public static PathChain hpSample3() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(grabSample3Pose), new Point(hpSample3Pose)))
        .setLinearHeadingInterpolation(grabSample3Pose.getHeading(), hpSample3Pose.getHeading())
        .setPathEndTValueConstraint(.9)
        .build();
  }

  public static PathChain grabSpecimen1() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(hpSample3Pose), new Point(grabSpecimen1Pose)))
        .setLinearHeadingInterpolation(hpSample3Pose.getHeading(), grabSpecimen1Pose.getHeading())
        .build();
  }

  public static PathChain placeSpecimen1() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(grabSpecimen1Pose),
                new Point(34.000, 12.750, Point.CARTESIAN),
                new Point(29.000, 37.500, Point.CARTESIAN),
                new Point(20.000, 61.000, Point.CARTESIAN),
                new Point(placeSpecimen1Pose)))
        .setLinearHeadingInterpolation(
            grabSpecimen1Pose.getHeading(), placeSpecimen1Pose.getHeading())
        .build();
  }

  public static PathChain grabSpecimen2() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(placeSpecimen1Pose), new Point(grabSpecimen2Pose)))
        .setLinearHeadingInterpolation(
            placeSpecimen1Pose.getHeading(), grabSpecimen2Pose.getHeading())
        .build();
  }

  public static PathChain placeSpecimen2() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(grabSpecimen2Pose), new Point(placeSpecimen2Pose)))
        .setLinearHeadingInterpolation(
            grabSpecimen2Pose.getHeading(), placeSpecimen2Pose.getHeading())
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
        .addPath(new BezierLine(new Point(grabSpecimen3Pose), new Point(placeSpecimen3Pose)))
        .setLinearHeadingInterpolation(
            grabSpecimen3Pose.getHeading(), placeSpecimen3Pose.getHeading())
        .build();
  }

  public static PathChain grabSpecimen4() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(placeSpecimen3Pose), new Point(grabSpecimen4Pose)))
        .setLinearHeadingInterpolation(
            placeSpecimen3Pose.getHeading(), grabSpecimen4Pose.getHeading())
        .build();
  }

  public static PathChain placeSpecimen4() {
    return new PathBuilder()
        .addPath(new BezierLine(new Point(grabSpecimen4Pose), new Point(placeSpecimen4Pose)))
        .setLinearHeadingInterpolation(
            grabSpecimen4Pose.getHeading(), placeSpecimen4Pose.getHeading())
        .build();
  }
}
