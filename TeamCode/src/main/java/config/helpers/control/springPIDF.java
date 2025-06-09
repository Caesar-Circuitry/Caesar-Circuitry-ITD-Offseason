package config.helpers.control;

import com.seattlesolvers.solverslib.controller.PIDController;

public class springPIDF {
  private double kP, kI, kD, kF;
  private PIDController pidController;
  private double minError;
  private double springoffset;

  /**
   * @param kP Proportional gain
   * @param kI Integral gain
   * @param kD Derivative gain
   * @param kF spring constant (feedforward gain)
   * @param springoffset offset of the spring before it starts to apply force
   * @param minError minimum error to apply pid controller
   */
  public springPIDF(
      double kP, double kI, double kD, double kF, double springoffset, double minError) {
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kF = kF;
    this.pidController = new PIDController(kP, kI, kD);
    this.springoffset = springoffset;
    this.minError = minError;
  }

  public double caculate(double pv, double sp) {
    double motorOutput = pidController.calculate(pv, sp);
    double KFResponse = (kF * -((sp - springoffset) - (pv - springoffset)));
    motorOutput += KFResponse;
    if (sp - (pv) <= minError && sp - (pv) >= -minError) {
      motorOutput = KFResponse;
    }
    if (Math.abs(motorOutput) >= 1) {
      motorOutput = Math.signum(motorOutput) * 1;
    }
    return motorOutput;
  }

  public void setPIDF(double kP, double kI, double kD, double kF) {
    this.kP = kP;
    this.kI = kI;
    this.kD = kD;
    this.kF = kF;
    pidController.setPID(kP, kI, kD);
  }

  public double getKp() {
    return kP;
  }

  public double getKi() {
    return kI;
  }

  public double getKd() {
    return kD;
  }

  public double getKf() {
    return kF;
  }

  public double getMinError() {
    return minError;
  }

  public double getSpringOffset() {
    return springoffset;
  }

  public void setMinError(double minError) {
    this.minError = minError;
  }

  public void setSpringOffset(double springoffset) {
    this.springoffset = springoffset;
  }

  public void reset() {
    pidController.reset();
  }
}
