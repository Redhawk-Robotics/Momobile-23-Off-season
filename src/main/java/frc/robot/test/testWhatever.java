// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.test;

import java.util.Set;
import java.util.function.DoubleSupplier;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Ports;
import frc.robot.constants.Setting;

@Deprecated
public class testWhatever extends SubsystemBase {
  /** Creates a new testWhatever. */
  private final CANSparkMax leftClaw, rightClaw;

  private final CANSparkMax wrist;
  private final RelativeEncoder wristEncoder;

  private final CANSparkMax extender;
  private final RelativeEncoder extenderEncoder;

  private final DoubleSolenoid clawOpen;

  private double stop = 0;

  private double extenderSpeed = .5;
  private double extenderSpeedReverse = -.5;

  private double wristSpeed = 0.3;
  private double wristSpeedReverse = -0.2;

  public testWhatever() {

    // ------------------------------------- EXTENDER

    extender = new CANSparkMax(Ports.Extender.extender, MotorType.kBrushless); // 11
    extenderEncoder = extender.getEncoder();
    extender.restoreFactoryDefaults();

    extender.setIdleMode(IdleMode.kBrake);
    extender.setInverted(false);

    // ------------------------------------- CLAW

    leftClaw = new CANSparkMax(Ports.Claw.leftClaw, MotorType.kBrushless); // 12
    leftClaw.setInverted(true);
    // leftClaw.setIdleMode(IdleMode.kCoast);

    // RIGHT CLAW
    rightClaw = new CANSparkMax(Ports.Claw.rightClaw, MotorType.kBrushless); // 13
    rightClaw.setInverted(true);
    // rightClaw.setIdleMode(IdleMode.kCoast);

    // ------------------------------------- WRIST

    wrist = new CANSparkMax(Ports.Wrist.wrist, MotorType.kBrushless); // 14
    wristEncoder = wrist.getEncoder();
    wrist.restoreFactoryDefaults();
    wrist.setIdleMode(IdleMode.kBrake);
    wrist.setInverted(false);

    // -------------------------------------

    clawOpen = new DoubleSolenoid(PneumaticsModuleType.REVPH, Setting.clawPneumatic.clawForwardChan,
        Setting.clawPneumatic.clawReverseChan);

    // ----------------------------------------------------------------------------------
    // -------------------------------------

    /****************/
    /*** EXTENDER ***/
    /***************/

    extender.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 0);
    extender.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    extender.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, -270);
    extender.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);

    /*************/
    /*** WRIST ***/
    /*************/

    wrist.setSoftLimit(CANSparkMax.SoftLimitDirection.kForward, 5);
    wrist.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    wrist.setSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, -30);
    wrist.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);

    // -------------------------------------
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    // SmartDashboard.putNumber("testMotor Encoder Value",
    // getEncoderMeters(EncoderValue));

    // SmartDashboard.putNumber("TEST arm pivot right", armEncoderRight.getPosition());
    SmartDashboard.putNumber("TEST arm extender", extenderEncoder.getPosition());
    SmartDashboard.putNumber("TEST wrist pivot", wristEncoder.getPosition());
  }

  // -------------------------------------

  /*************/
  /*** CLAW ***/
  /*************/

  public void coneIntake() {
    clawOpen.set(Value.kForward);
    if(rightClaw.getOutputCurrent() < 5 && leftClaw.getOutputCurrent() < 5){
    rightClaw.set(.4);  
    leftClaw.set(-.4);
    }else{
      rightClaw.set(0);  
      leftClaw.set(0);
    }
  }

  public void outTake() {
    clawOpen.set(Value.kReverse);
    leftClaw.set(.25);
    rightClaw.set(-.25);
  }

  public void cubeIntake() {
    clawOpen.set(Value.kReverse);
    if(rightClaw.getOutputCurrent() < 5 && leftClaw.getOutputCurrent() < 5){
      rightClaw.set(.5);  
      leftClaw.set(-.5);
      }else{
        rightClaw.set(0);  
        leftClaw.set(0);
      }
  }

  public void stopClaw() {
    // clawOpen.set(Value.kOff);
    leftClaw.set(0);
    rightClaw.set(0);
  }

  // -------------------------------------

  /*************/
  /*** WRIST ***/
  /*************/
  public void wristController(double speed){
    wrist.set(speed);
  }

  public void upGoWrist() {
    wrist.set(wristSpeed);
  }

  public void stopWrist() {
    wrist.set(stop);
  }

  public void downGoWrist() {
    wrist.set(wristSpeedReverse);
  }

  // -------------------------------------

  /*************/
  /*** EXTENDER ***/
  /*************/
  public void extenderController(double speed){
    extender.set(speed);
  }

  public void upExtender() {
    extender.set(extenderSpeed);
  }

  public void stopExtender() {
    extender.set(stop);
  }

  public void downExtender() {
    extender.set(extenderSpeedReverse);
  }

  /***************/
  /*** COMMANDS ***/
  /***************/

  public Command rasieShoulderCMD() {
    return new Command() {

      @Override
      public Set<Subsystem> getRequirements() {
        // TODO Auto-generated method stub
        return null;

      };

      @Override
      public void execute() {
        //upGoArm();
        // armEncoderLeft

      }
    };
  }
}
