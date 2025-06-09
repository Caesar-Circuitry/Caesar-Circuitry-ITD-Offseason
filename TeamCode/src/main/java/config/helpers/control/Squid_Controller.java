package config.helpers.control;

public class Squid_Controller {
  private double kSQ, kD;
  private double setPoint;
  private double measuredValue;

  private double errorVal_p;
  private double errorVal_v;

  private double prevErrorVal;

  private double errorTolerance_p = 0.05;
  private double errorTolerance_v = Double.POSITIVE_INFINITY;

  private double lastTimeStamp;
  private double period;

  /** The base constructor for the PIDF controller */
  public Squid_Controller(double kSQ, double kd) {
    this(kSQ, kd, 0, 0);
  }

  /**
   * This is the full constructor for the PIDF controller. Our PIDF controller includes a
   * feed-forward value which is useful for fighting friction and gravity. Our errorVal represents
   * the return of e(t) and prevErrorVal is the previous error.
   *
   * @param sp The setpoint of the pid control loop.
   * @param pv The measured value of he pid control loop. We want sp = pv, or to the degree such
   *     that sp - pv, or e(t) < tolerance.
   */
  public Squid_Controller(double kSQ, double kd, double sp, double pv) {
    this.kSQ = kSQ;
    kD = kd;

    setPoint = sp;
    measuredValue = pv;

    lastTimeStamp = 0;
    period = 0;

    errorVal_p = setPoint - measuredValue;
    reset();
  }

  public void reset() {
    prevErrorVal = 0;
    lastTimeStamp = 0;
  }

  /**
   * Sets the error which is considered tolerable for use with {@link #atSetPoint()}.
   *
   * @param positionTolerance Position error which is tolerable.
   */
  public void setTolerance(double positionTolerance) {
    setTolerance(positionTolerance, Double.POSITIVE_INFINITY);
  }

  /**
   * Sets the error which is considered tolerable for use with {@link #atSetPoint()}.
   *
   * @param positionTolerance Position error which is tolerable.
   * @param velocityTolerance Velocity error which is tolerable.
   */
  public void setTolerance(double positionTolerance, double velocityTolerance) {
    errorTolerance_p = positionTolerance;
    errorTolerance_v = velocityTolerance;
  }

  /**
   * Returns the current setpoint of the PIDFController.
   *
   * @return The current setpoint.
   */
  public double getSetPoint() {
    return setPoint;
  }

  /**
   * Sets the setpoint for the PIDFController
   *
   * @param sp The desired setpoint.
   */
  public void setSetPoint(double sp) {
    setPoint = sp;
    errorVal_p = setPoint - measuredValue;
    errorVal_v = (errorVal_p - prevErrorVal) / period;
  }

  /**
   * Returns true if the error is within the percentage of the total input range, determined by
   * {@link #setTolerance}.
   *
   * @return Whether the error is within the acceptable bounds.
   */
  public boolean atSetPoint() {
    return Math.abs(errorVal_p) < errorTolerance_p && Math.abs(errorVal_v) < errorTolerance_v;
  }

  /**
   * @return the PIDF coefficients
   */
  public double[] getCoefficients() {
    return new double[] {kSQ, kD};
  }

  /**
   * @return the positional error e(t)
   */
  public double getPositionError() {
    return errorVal_p;
  }

  /**
   * @return the tolerances of the controller
   */
  public double[] getTolerance() {
    return new double[] {errorTolerance_p, errorTolerance_v};
  }

  /**
   * @return the velocity error e'(t)
   */
  public double getVelocityError() {
    return errorVal_v;
  }

  /**
   * Calculates the next output of the PIDF controller.
   *
   * @return the next output using the current measured value via {@link #calculate(double)}.
   */
  public double calculate() {
    return calculate(measuredValue);
  }

  /**
   * Calculates the next output of the PIDF controller.
   *
   * @param pv The given measured value.
   * @param sp The given setpoint.
   * @return the next output using the given measurd value via {@link #calculate(double)}.
   */
  public double calculate(double pv, double sp) {
    // set the setpoint to the provided value
    setSetPoint(sp);
    return calculate(pv);
  }

  /**
   * Calculates the control value, u(t).
   *
   * @param pv The current measurement of the process variable.
   * @return the value produced by u(t).
   */
  public double calculate(double pv) {
    prevErrorVal = errorVal_p;

    double currentTimeStamp = (double) System.nanoTime() / 1E9;
    if (lastTimeStamp == 0) lastTimeStamp = currentTimeStamp;
    period = currentTimeStamp - lastTimeStamp;
    lastTimeStamp = currentTimeStamp;

    if (measuredValue == pv) {
      errorVal_p = setPoint - measuredValue;
    } else {
      errorVal_p = setPoint - pv;
      measuredValue = pv;
    }

    if (Math.abs(period) > 1E-6) {
      errorVal_v = (errorVal_p - prevErrorVal) / period;
    } else {
      errorVal_v = 0;
    }

    // returns u(t)
    return kSQ * Math.copySign(Math.sqrt(Math.abs(errorVal_p)), errorVal_p) + kD * errorVal_v;
  }

  public void setSQUID(double kSQ, double kd) {
    this.kSQ = kSQ;
    kD = kd;
  }

  public void setKSQ(double kSQ) {
    this.kSQ = kSQ;
  }

  public void setD(double kd) {
    kD = kd;
  }

  public double getKSQ() {
    return kSQ;
  }

  public double getD() {
    return kD;
  }

  public double getPeriod() {
    return period;
  }
}
