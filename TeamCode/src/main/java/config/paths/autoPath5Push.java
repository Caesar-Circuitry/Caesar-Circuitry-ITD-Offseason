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
  private static final Pose specimen0Pose = new Pose(38.000, 67.00, Math.toRadians(180));

  // sample poses
  private static final Pose Sample1Pose = new Pose(12.243, 21.187, Math.toRadians(0));
  private static final Pose Sample2Pose =
      new Pose(12.243122009569378, 12.22966507177033, Math.toRadians(0));
  private static final Pose Sample3Pose =
      new Pose(11.898624401913876, 8.095693779904312, Math.toRadians(0));

  // grab specimen poses
  private static final Pose grabSpecimen1Pose = new Pose(14.000, 13.000, Math.toRadians(0));
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

  public static PathChain Sample1() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(specimen0Pose),
                new Point(13.449, 32.727, Point.CARTESIAN),
                new Point(103.535, 18.603, Point.CARTESIAN),
                new Point(45.487, 22.565, Point.CARTESIAN),
                new Point(Sample1Pose)))
        .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(0))
        .build();
  }

  public static PathChain Sample2() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(Sample1Pose),
                new Point(72.186, 39.100, Point.CARTESIAN),
                new Point(91.305, 6.201, Point.CARTESIAN),
                new Point(Sample2Pose)))
        .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
        .build();
  }

  public static PathChain Sample3() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(Sample2Pose),
                new Point(110.08044258373205, 8.612440191387565, Point.CARTESIAN),
                new Point(91.305, 6.201, Point.CARTESIAN),
                new Point(Sample3Pose)))
        .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(0))
        .build();
  }

  public static PathChain placeSpecimen1() {
    return new PathBuilder()
        .addPath(
            new BezierCurve(
                new Point(Sample3Pose),
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
