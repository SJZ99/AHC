/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.chassis;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.motor.MotorFactory;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants.*;


/**
 * Must be sure these objects will be instantiated only once
 */
public class DrivetrainBase extends SubsystemBase {
  protected static TalonFX leftMas  = new TalonFX(Motor.leftMaster);
  protected static TalonFX leftFol  = new TalonFX(Motor.leftFollewer);
  protected static TalonFX rightMas = new TalonFX(Motor.rightMaster);
  protected static TalonFX rightFol = new TalonFX(Motor.rightFollower);
  protected static AHRS ahrs = new AHRS(SPI.Port.kMXP);
  private   static boolean isFirst = true; 
  private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 40, 50, 1);

  /**
   * Creates a new DrivetrainBase.
   */
  public DrivetrainBase() {
    if(isFirst){
      firstConfig();
      isFirst = false;
    }
  }

  public void firstConfig(){
    MotorFactory.setFollower(leftMas, leftFol);
    MotorFactory.setInvert(leftMas, Motor.isLeftMotorInvert);
    MotorFactory.setPosion(leftMas, 0, 0, 10);
    MotorFactory.setSensor(leftMas, FeedbackDevice.IntegratedSensor);
    MotorFactory.setSensorPhase(leftMas, Motor.isLeftPhaseInvert);
    MotorFactory.configLikePrevious(leftFol, Motor.isLeftPhaseInvert, Motor.isLeftMotorInvert);
    leftMas.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);

    MotorFactory.setFollower(rightMas, rightFol);
    rightMas.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
    MotorFactory.configLikePrevious(rightMas, Motor.isRightPhaseInvert, Motor.isRightMotorInvert);
    MotorFactory.configLikePrevious(rightFol, Motor.isRightPhaseInvert, Motor.isRightMotorInvert);
    // MotorFactory.voltageCompSaturation(rightMas, 11);
    // MotorFactory.voltageCompSaturation(leftMas,  11);

    MotorFactory.configPF(leftMas,  0, 0, 0);
    MotorFactory.configPF(rightMas, 0, 0, 0);
    leftFol.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 255);
    rightFol.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 255);
    rightMas.setNeutralMode(NeutralMode.Coast);
    leftMas.setNeutralMode(NeutralMode.Coast);
    // rightMas.setNeutralMode(NeutralMode.Brake);
    // leftMas.setNeutralMode(NeutralMode.Brake);
    
    ahrs.reset();
  }

  public void resetSensor(){
    MotorFactory.setPosion(leftMas, 0, 0, 0);
    MotorFactory.setPosion(rightMas, 0, 0, 0);
    MotorFactory.setPosion(leftFol, 0, 0, 0);
    MotorFactory.setPosion(rightFol, 0, 0, 0);
    ahrs.reset();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("left", leftMas.getSelectedSensorVelocity());
    SmartDashboard.putNumber("right", rightMas.getSelectedSensorVelocity());
    // This method will be called once per scheduler run
  }
}
