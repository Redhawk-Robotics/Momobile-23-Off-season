// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Autons.AutoBase;
import frc.robot.commands.Autons.DoNothingAuton;
import frc.robot.commands.Autons.TimedBased.CONE_MOBILITY;
import frc.robot.commands.Autons.TimedBased.JUST_CHARGE_PAD;
import frc.robot.commands.Autons.TimedBased.LOW_ENGAGE;
import frc.robot.commands.Autons.TimedBased.MIDSCORE;
import frc.robot.commands.Positions.CheckCompressor;
import frc.robot.commands.Positions.autoSubstationCommand;
import frc.robot.commands.Positions.groundCommand;
import frc.robot.commands.Positions.groundCubeCommand;
import frc.robot.commands.Positions.highAuto;
import frc.robot.commands.Positions.highCommand;
import frc.robot.commands.Positions.midCommand;
import frc.robot.commands.Positions.shootCube;
import frc.robot.commands.Positions.shootLow;
import frc.robot.commands.Positions.singleSubstation;
import frc.robot.commands.Positions.stoweAway;
import frc.robot.commands.Positions.substationCommand;
import frc.robot.commands.Swerve.Drive;
import frc.robot.commands.Swerve.DriveForward;
import frc.robot.commands.Swerve.DriveTurn;
import frc.robot.commands.extender.ExtenderManual;
import frc.robot.constants.Ports;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.extenderSubsystem;

import java.util.HashMap;
import java.util.List;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import com.pathplanner.lib.commands.PPSwerveControllerCommand;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...

  /* Subsystems */
  private final SwerveSubsystem SwerveDrive = new SwerveSubsystem();
  // private final LEDController LEDS = new LEDController();

  private final WristSubsystem wrist = new WristSubsystem();
  private final ArmSubsystem arm = new ArmSubsystem();
  private final extenderSubsystem extender = new extenderSubsystem();
  private final ClawSubsystem claw = new ClawSubsystem();

  private final Compressor compressor = new Compressor(PneumaticsModuleType.REVPH);
  private final AutoBase autoBase = new AutoBase(SwerveDrive);

  // Positions
  private final autoSubstationCommand autoSubstationCommand = new autoSubstationCommand(extender, arm, wrist);
  // private final groundCommand groundCommand = new groundCommand(extender, arm,
  // wrist, claw);
  private final highCommand highCommand = new highCommand(extender, arm, wrist);
  private final midCommand midCommand = new midCommand(extender, arm, wrist);
  private final shootLow shootLow = new shootLow(extender, arm, wrist, claw);
  private final singleSubstation singleSubstation = new singleSubstation(extender, arm, wrist, claw);
  private final stoweAway stoweAway = new stoweAway(extender, arm, wrist);
  private final substationCommand substationCommand = new substationCommand(extender, arm, wrist, claw);
  private final groundCubeCommand groundCubeCommand = new groundCubeCommand(extender, arm, wrist, claw);
  private final shootCube shootCube = new shootCube(extender, arm, wrist, claw);

  // Replace with CommandPS4Controller or CommandJoystick if needed

  /* Controllers */
  private final XboxController DRIVER = new XboxController(Ports.Gamepad.DRIVER);
  private final XboxController OPERATOR = new XboxController(Ports.Gamepad.OPERATOR);

  /* Drive Controls */
  private final int translationAxis = XboxController.Axis.kLeftY.value;
  private final int strafeAxis = XboxController.Axis.kLeftX.value;
  private final int rotationAxis = XboxController.Axis.kRightX.value;

  private final int rightYAxis1 = XboxController.Axis.kRightY.value;

  private final int leftTrigger1 = XboxController.Axis.kLeftTrigger.value;
  private final int rightTrigger1 = XboxController.Axis.kRightTrigger.value;

  /* Driver Buttons */
  private final Trigger driver_A_zeroGyro = new JoystickButton(DRIVER, XboxController.Button.kA.value);
  private final Trigger driver_X_robotCentric = new JoystickButton(DRIVER, XboxController.Button.kX.value);

  private final Trigger driver_slowSpeed_rightBumper = new JoystickButton(DRIVER,
      XboxController.Button.kRightBumper.value);

  // Additional buttons
  private final Trigger driver_leftBumper = new JoystickButton(DRIVER, XboxController.Button.kLeftBumper.value);

  private final Trigger driver_B = new JoystickButton(DRIVER, XboxController.Button.kB.value);

  private final Trigger driver_Y = new JoystickButton(DRIVER, XboxController.Button.kY.value);

  private final Trigger driver_BottomRightRearButton = new JoystickButton(DRIVER,
      XboxController.Button.kStart.value);
  private final Trigger driver_BottomLeftRearButton = new JoystickButton(DRIVER,
      XboxController.Button.kBack.value);

  private final Trigger driver_TopRightRearButton = new JoystickButton(DRIVER,
      XboxController.Button.kRightStick.value);

  private final Trigger driver_START = new JoystickButton(DRIVER, XboxController.Button.kStart.value);
  private final Trigger driver_BACK = new JoystickButton(DRIVER, XboxController.Button.kBack.value);

  // Controller two - Operator
  private final Trigger dpadUpButton = new Trigger(() -> OPERATOR.getPOV() == 0);
  private final Trigger dpadRightButton = new Trigger(() -> OPERATOR.getPOV() == 90);
  private final Trigger dpadDownButton = new Trigger(() -> OPERATOR.getPOV() == 180);
  private final Trigger dpadLeftButton = new Trigger(() -> OPERATOR.getPOV() == 270);

  private final int leftYAxis2 = XboxController.Axis.kLeftY.value;
  private final int leftXAxis2 = XboxController.Axis.kLeftX.value;

  private final int rightYAxis2 = XboxController.Axis.kRightY.value;
  private final int rightXAxis2 = XboxController.Axis.kRightX.value;

  private final int leftTrigger2 = XboxController.Axis.kLeftTrigger.value;
  private final int rightTrigger2 = XboxController.Axis.kRightTrigger.value;

  private final Trigger opperator_A = new JoystickButton(OPERATOR, XboxController.Button.kA.value);
  private final Trigger opperator_B = new JoystickButton(OPERATOR, XboxController.Button.kB.value);
  private final Trigger opperator_X = new JoystickButton(OPERATOR, XboxController.Button.kX.value);
  private final Trigger opperator_Y = new JoystickButton(OPERATOR, XboxController.Button.kY.value);

  private final Trigger opperator_RightBumper = new JoystickButton(OPERATOR,
      XboxController.Button.kRightBumper.value);
  private final Trigger opperator_leftBumper = new JoystickButton(OPERATOR,
      XboxController.Button.kLeftBumper.value);

  private final Trigger opperator_BottomRightRearButton = new JoystickButton(OPERATOR,
      XboxController.Button.kStart.value);
  private final Trigger opperator_BottomLeftRearButton = new JoystickButton(OPERATOR,
      XboxController.Button.kBack.value);

  private final Trigger opperator_TopLeftRearButton = new JoystickButton(OPERATOR,
      XboxController.Button.kLeftStick.value);
  private final Trigger opperator_TopRightRearButton = new JoystickButton(OPERATOR,
      XboxController.Button.kRightStick.value);

  // Create SmartDashboard chooser for autonomous routines
  private static SendableChooser<Command> Autons = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */

  public RobotContainer() {
    /*************/
    /*** DRIVE ***/
    /*************/

    SwerveDrive.setDefaultCommand(
        new Drive(
            SwerveDrive,
            () -> -DRIVER.getRawAxis(translationAxis),
            () -> -DRIVER.getRawAxis(strafeAxis),
            () -> -DRIVER.getRawAxis(rotationAxis),
            () -> driver_X_robotCentric.getAsBoolean(),
            () -> driver_slowSpeed_rightBumper.getAsBoolean()));

    // -------------------------------------

    // wristSubsystem.setDefaultCommand(
    // new WristManual(wristSubsystem,
    // () -> OPERATOR.getRawAxis(leftYAxis2) * .5));

    /*************/
    /*** EXTENDER ***/
    /*************/

    // METHODS: extend, retract
    // extender.setDefaultCommand(
    // new ExtenderManual(
    // extender,
    // () -> OPERATOR.getRawAxis(rightYAxis2)));

    // -------------------------------------

    // Configure the trigger bindings, defaults, Autons
    configureDefaultCommands();
    configureButtonBindings();
    configureAutons();
  }

  /****************/
  /*** DEFAULTS ***/
  /****************/

  private void configureDefaultCommands() {
    // Compressor code
    compressor.enableAnalog(100, 120);
    // LEDS.setColor(LEDColor.BEAT,0);

    // compressor.disableCompressor();

    // Camera Server
    // CameraServer.startAutomaticCapture();
  }

  /***************/
  /*** BUTTONS ***/
  /***************/

  private void configureButtonBindings() {
    // Driver
    driver_A_zeroGyro.onTrue(new InstantCommand(() -> SwerveDrive.zeroGyro()));//

    // Operator
    opperator_B.onTrue(midCommand);
    opperator_X.onTrue(highCommand);
    // opperator_X.onTrue(highCommand());//works

    dpadUpButton.onTrue(new groundCommand(extender, arm, wrist, claw));
    dpadUpButton.whileFalse(new InstantCommand(() -> claw.stopClaw()));// - good

    dpadLeftButton.onTrue(new singleSubstation(extender, arm, wrist, claw));
    dpadLeftButton.whileFalse(new InstantCommand(() -> claw.stopClaw()));// - good

    dpadDownButton.onTrue(groundCubeCommand);
    dpadDownButton.whileFalse(new InstantCommand(() -> claw.stopClaw()));// - good

    opperator_Y.onTrue(substationCommand); // - good
    opperator_Y.whileFalse(new InstantCommand(() -> claw.stopClaw()));// - good
    // opperator_Y.whileFalse(stoweAway());

    // opperator_A.whileTrue(stoweAway); // FIME IDK IF ITS GOOD
    opperator_A.onTrue(stoweAway); // - good
    opperator_A.whileFalse(new InstantCommand(() -> claw.stopClaw()));// - good

    // ------------------------------------- ARM
    opperator_leftBumper.whileTrue(shootLow);
    opperator_leftBumper.whileFalse(new InstantCommand(() -> claw.stopClaw()));

    opperator_RightBumper.whileTrue(shootCube);
    opperator_RightBumper.whileFalse(new InstantCommand(() -> claw.stopClaw()));

    // // ------------------------------------- CLAW
    opperator_TopLeftRearButton.onTrue(new InstantCommand(() -> claw.coneIntake()));
    opperator_BottomLeftRearButton.onTrue(new InstantCommand(() -> claw.outTake1()));

    opperator_TopLeftRearButton.onFalse(new InstantCommand(() -> claw.stopClaw()));
    opperator_BottomLeftRearButton.onFalse(new InstantCommand(() -> claw.stopClaw()));

    // // ------------------------------------- WRIST
    opperator_TopRightRearButton.onTrue(new InstantCommand(() -> wrist.upGoWrist()));
    opperator_TopRightRearButton.onFalse(new InstantCommand(() -> wrist.stopWrist()));

    opperator_BottomRightRearButton.onTrue(new InstantCommand(() -> wrist.downGoWrist()));
    opperator_BottomRightRearButton.onFalse(new InstantCommand(() -> wrist.stopWrist()));

    // // ------------------------------------- EXTENDER

  }

  // /**************/
  // /*** AUTONS ***/
  // /**************/
  public void configureAutons() {
    SmartDashboard.putData("Autonomous: ", Autons);

    Autons.setDefaultOption("Do Nothing", new DoNothingAuton());

    // /* Velocity is negative when we are facing grid because the velocity (x-axis)
    // * is field-centric meaning when looking down, left is negative and right is
    // postive.
    // */

    List<PathPlannerTrajectory> pathGroup = PathPlanner.loadPathGroup("High, Pickup",
        new PathConstraints(4, 3));

    // This is just an example event map. It would be better to have a constant,
    // global event map
    // in your code that will be used by all path following commands.
    HashMap<String, Command> eventMap = new HashMap<>();
    eventMap.put("Place High", new SequentialCommandGroup(
        new CheckCompressor(compressor, 10),
        new InstantCommand(() -> claw.coneIntake()),
        new WaitCommand(1),
        new highAuto(extender, arm, wrist),
        new InstantCommand(() -> claw.stopClaw()),
        new WaitCommand(1.25),
        new InstantCommand(() -> wrist.setPosition(-29)),
        new WaitCommand(.5),
        new InstantCommand(() -> claw.openClaw()),
        new stoweAway(extender, arm, wrist)));
    eventMap.put("Stowe1", new stoweAway(extender, arm, wrist));
    eventMap.put("Ground Start", new groundCommand(extender, arm, wrist, claw));

    SwerveAutoBuilder autoBuilder = autoBase.CustomSwerveAutoBuilder();
    Command fullAuto = autoBuilder.fullAuto(pathGroup);
    Autons.addOption("TEST PATH", fullAuto);
    /*
     * IMPORTANT!!!
     * IMPORTANT!!!
     * When ever calling a command preset (like the button ones), call the command
     * with new, example: new whateverCommand()...
     * Why?
     * The code break and gives startComp() error, SO MAKE SURE THAT THIS IS DONE
     */

    // rename to BLUE-LEFT
    Autons.addOption("[BLUE-LEFT] PLACE MID, DIP", new SequentialCommandGroup(
        new InstantCommand(() -> SwerveDrive.autoReverseGyro()),
        new WaitCommand(6),
        new InstantCommand(() -> claw.closeClaw()),
        new InstantCommand(() -> wrist.setPosition(0)),
        new WaitCommand(1),
        new stoweAway(extender, arm, wrist),
        new midCommand(extender, arm, wrist),
        new WaitCommand(2.5),
        new InstantCommand(() -> wrist.setPosition(-29)),
        new WaitCommand(2),
        new InstantCommand(() -> claw.openClaw()),
        new stoweAway(extender, arm, wrist),
        new DriveForward(SwerveDrive, 0, 2, 1) // Should be pos velo now because of
    // autoReverseGyro
    ));

    Autons.addOption("[BLUE-RIGHT] PLACE MID, DIP", new SequentialCommandGroup(
        new InstantCommand(() -> SwerveDrive.zeroGyro()),
        new WaitCommand(6),
        new InstantCommand(() -> claw.closeClaw()),
        new InstantCommand(() -> wrist.setPosition(0)),
        new WaitCommand(1),
        new midCommand(extender, arm, wrist),
        new WaitCommand(2.5),
        new InstantCommand(() -> wrist.setPosition(-29)),
        new WaitCommand(2),
        new InstantCommand(() -> claw.openClaw()),
        new stoweAway(extender, arm, wrist),
        new DriveForward(SwerveDrive, .5, -23, 4)));

    Autons.addOption("THROW, DIP", new SequentialCommandGroup(
        new InstantCommand(() -> claw.closeClaw()),
        new InstantCommand(() -> wrist.setPosition(1)),
        new WaitCommand(.5),
        new InstantCommand(
            () -> claw.outTake1()),
        new WaitCommand(.5),
        new InstantCommand(
            () -> claw.stopClaw()),
        new DriveForward(SwerveDrive, .5, -12, 5)));

    // POSTIVE VELO MEANS COUNTERCLOCKWISE TURN
    Autons.addOption("Test turn", new SequentialCommandGroup(
        // FINAL, NEVER CHANGE
        // FULL 180*
        new DriveTurn(SwerveDrive, 0, 35, 1.5)));

    Autons.addOption("MID, TURN, GRAB", new SequentialCommandGroup(
        new InstantCommand(() -> claw.closeClaw()),
        new InstantCommand(() -> wrist.setPosition(1)),
        new WaitCommand(.5),
        new MIDSCORE(SwerveDrive, extender, arm, wrist, claw),
        new DriveForward(SwerveDrive, .5, -5, 1),
        new DriveTurn(SwerveDrive, 0, 5, 2),
        new groundCommand(extender, arm, wrist, claw),
        new InstantCommand(() -> claw.coneIntake()),
        new DriveForward(SwerveDrive, 0, 5, 5),
        new InstantCommand(() -> claw.stopClaw())));

    Autons.addOption("single", new SequentialCommandGroup(
        new stoweAway(extender, arm, wrist),
        new singleSubstation(extender, arm, wrist, claw)));

    Autons.addOption("high", new SequentialCommandGroup(
        new WaitCommand(6),
        new InstantCommand(() -> claw.closeClaw()),
        new stoweAway(extender, arm, wrist),
        new highCommand(extender, arm, wrist),
        new InstantCommand(() -> wrist.setPosition(-29)),
        new WaitCommand(2),
        new InstantCommand(() -> claw.openClaw()),
        new DriveForward(SwerveDrive, 0, -3, 2),
        new stoweAway(extender, arm, wrist),
        new DriveTurn(SwerveDrive, 0, -35, 1.5)));

    Autons.addOption("[P] LOW CONE + MOBILITY", new CONE_MOBILITY(SwerveDrive));
    Autons.addOption("JUST_CHARGE_PAD", new JUST_CHARGE_PAD(SwerveDrive));
    Autons.addOption("LOW_ENGAGE",
        new LOW_ENGAGE(SwerveDrive, new groundCommand(extender, arm, wrist, claw)));

    Autons.addOption("[PP] UNTESTED pls change", new SequentialCommandGroup(
        new InstantCommand(() -> SwerveDrive.zeroGyro()),
        new CheckCompressor(compressor, 10),
        new InstantCommand(() -> claw.coneIntake()),
        new WaitCommand(1),
        new highAuto(extender, arm, wrist),
        new InstantCommand(() -> claw.stopClaw()),
        new WaitCommand(1.25),
        new InstantCommand(() -> wrist.setPosition(-29)),
        new WaitCommand(.5),
        new InstantCommand(() -> claw.openClaw()),
        new stoweAway(extender, arm, wrist),
        new DriveForward(SwerveDrive, 0, -15, 5),
        new DriveTurn(SwerveDrive, 0, 35, 1.5),
        new InstantCommand(() -> SwerveDrive.autoReverseGyro())).finallyDo((end) -> {
          SwerveDrive.autoReverseGyro();
        }));

    // Autons.addOption("Gyro", new SequentialCommandGroup(
    // new
    // ));
    // Autons.addOption("TEST GYRO RENEW", new AutoBalanceRenew(SwerveDrive));
  }

  // /**
  // * Use this to pass the autonomous command to the main {@link Robot} class.

  // *
  // * @return the command to run in autonomous
  // */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autons.getSelected();
  }
}
