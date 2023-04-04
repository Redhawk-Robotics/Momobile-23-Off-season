// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Arm.ARMSTAY;
import frc.robot.commands.Arm.ArmManual;
import frc.robot.commands.Arm.ArmSetPoint;
import frc.robot.commands.Arm.StoweAway;
import frc.robot.commands.Arm.Substation;
import frc.robot.commands.Autons.AutoBase;
import frc.robot.commands.Autons.DoNothingAuton;
import frc.robot.commands.Claw.ClawCMD;
import frc.robot.commands.Swerve.Drive;
import frc.robot.commands.Swerve.DriveForward;
import frc.robot.commands.Wrist.WristSetPoint;
import frc.robot.commands.extender.ExtenderManual;
import frc.robot.commands.extender.ExtenderSetPoint;
import frc.robot.constants.Ports;
import frc.robot.constants.Setting;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.extenderSubsystem;
import frc.robot.subsystems.modules.CompressorModule;
import frc.robot.subsystems.modules.LEDColor;
import frc.robot.subsystems.modules.LEDController;
import frc.robot.subsystems.modules.PDH;
import frc.robot.subsystems.modules.SparkMaxModules;
import frc.robot.test.armTest;
import frc.robot.test.clawTest;
import frc.robot.test.extenderTest;
import frc.robot.test.testWhatever;
import frc.robot.test.wristTest;

import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.auto.SwerveAutoBuilder;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import static frc.robot.constants.Setting.SoftLimits;

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

        // private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

        /* Subsystems */
        private final SwerveSubsystem SwerveDrive = new SwerveSubsystem();
        // private final PDH powerDistributionHub = new PDH();
        // private final SparkMaxModules sparkMaxModules = new SparkMaxModules();
        // private final testWhatever testers = new testWhatever();

        // private final armTest arm = new armTest();
        // private final clawTest claw = new clawTest();
        // private final wristTest wrist = new wristTest();
        // private final extenderTest extender = new extenderTest();
        // private final WristSubsystem wrist = new WristSubsystem();

        private final LEDController LEDS = new LEDController();

        // private final ArmSubsystem armSubsystem = new ArmSubsystem();
        // private final ClawSubsystem clawSubsystem = new ClawSubsystem();
        // private final extenderSubsystem extenderSubsystem = new extenderSubsystem();
        // private final WristSubsystem wristSubsystem = new WristSubsystem();

        private final WristSubsystem wristSubsystem = new WristSubsystem();
        private final ArmSubsystem arm = new ArmSubsystem();
        private final extenderSubsystem extender = new extenderSubsystem();

        private final CompressorModule compressor = CompressorModule.getCompressorModule(); // this needs to be put into
                                                                                            // a
                                                                                            // PneumaticsSubsystem
        private final ClawSubsystem claw = new ClawSubsystem();

        // private PneumaticHub compressor = new PneumaticHub(1);

        /* Commands */

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

                /*************/
                /*** ARM ***/
                /*************/

                // up, down
                // armSubsystem.setDefaultCommand(
                // new ArmManual(
                // armSubsystem,
                // () -> opperator_A.getAsBoolean(),
                // () -> opperator_Y.getAsBoolean()));
                // ------------------------------------- armTest
                // arm.setDefaultCommand(
                // new ArmManual(
                // arm,
                // () -> OPERATOR.getRawAxis(leftYAxis2)));

                // // extederTest//
                // extender.setDefaultCommand(
                // new ExtenderManual(
                // extender,
                // () -> OPERATOR.getRawAxis(rightYAxis2)));

                // -------------------------------------

                /*************/
                /*** CLAW ***/
                /*************/

                // METHODS: coneIntake, cubeIntake, leftOutTake, rightOutTake
                // clawSubsystem.setDefaultCommand(
                // new ClawManual(
                // clawSubsystem,
                // () -> opperator_X.getAsBoolean(),
                // () -> opperator_B.getAsBoolean(),
                // () -> opperator_leftBumper.getAsBoolean(),
                // () -> opperator_RightBumper.getAsBoolean()));

                // -------------------------------------

                /*************/
                /*** EXTENDER ***/
                /*************/

                // METHODS: extend, retract
                // extenderSubsystem.setDefaultCommand(
                // new ExtenderManual(
                // extenderSubsystem,
                // () -> opperator_startButton.getAsBoolean(),
                // () -> opperator_BackButton.getAsBoolean()));

                // -------------------------------------

                /*************/
                /*** WRIST ***/
                /*************/

                // METHODS: up, down
                // wristSubsystem.setDefaultCommand(
                // new WristManual(
                // wristSubsystem,
                // () -> driver_X.getAsBoolean(),
                // () -> driver_B.getAsBoolean()));

                // -------------------------------------

                // Configure the trigger bindings, defaults, Autons
                configureDefaultCommands();
                configureButtonBindings();
                configureAutons();
        }

        /**
         * Use this method to define your trigger->command mappings. Triggers can be
         * created via the
         * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with
         * an arbitrary
         * predicate, or via the named factories in {@link
         * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for
         * {@link
         * CommandXboxController
         * Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
         * PS4} controllers or
         * {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
         * joysticks}.
         */

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
                // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
                // new Trigger(m_exampleSubsystem::exampleCondition)
                // .onTrue(new ExampleCommand(m_exampleSubsystem));
                // Schedule `exampleMethodCommand` when the Xbox controller's B button is
                // pressed,
                // cancelling on release.

                // FIXME
                // driver_B.onTrue(new InstantCommand(()-> wristSubsystem.setPosition(-28)));
                // driver_Y.onTrue(new InstantCommand(()-> wristSubsystem.setPosition(-14)));
                // driver_A_zeroGyro.onTrue(new InstantCommand(()->
                // wristSubsystem.setPosition(15
                // )));

                opperator_B.onTrue(stoweAway());
                opperator_Y.onTrue(substationCommand());
                opperator_X.onTrue(groundCommand());
                // driver_B.onTrue(new InstantCommand(() -> wristSubsystem.setPosition(6)));
                // driver_Y.onTrue(new InstantCommand(()-> extender.setPosition( 270
                // )));
                // driver_A_zeroGyro.onTrue(new InstantCommand(()-> arm.setPosition(44
                // )));

                driver_A_zeroGyro.onTrue(new InstantCommand(() -> SwerveDrive.zeroGyro()));//
                // A value for the Xbox Controller

                // ------------------------------------- ARM
                // opperator_Y.whileTrue(new InstantCommand(() -> testers.upGoArm()));
                // opperator_A.whileTrue(new InstantCommand(() -> testers.downGoArm()));

                // opperator_Y.whileFalse(new InstantCommand(() -> testers.stopArm()));
                // opperator_A.whileFalse(new InstantCommand(() -> testers.stopArm()));

                // driver_Y.onTrue(new SequentialCommandGroup(
                // new ParallelCommandGroup(
                // new WristSetPoint(wrist, 0),
                // new ExtenderSetPoint(extender, 0))
                // ));

                // opperator_leftBumper.onTrue(new ArmSetPoint(arm, 44));

                // opperator_leftBumper.whileFalse(new InstantCommand(() -> arm.stopArm()));

                // // opperator_A.onTrue(new StoweAway(arm, extender, wrist));

                // // opperator_A.whileTrue(new StoweAway(arm, extender, wrist));

                // // opperator_Y.onTrue(new Substation(arm, extender, wrist));

                // // test later
                // // opperator_A.onTrue(new ParallelCommandGroup(
                // // new WristSetPoint(wrist, 0),
                // // new ExtenderSetPoint(extender, 0)));

                // // Stoeaway
                opperator_leftBumper.whileTrue(stoweAway());

                // driver_TopRightRearButton.onTrue(stoweAway());

                // // FIX WRIST
                // // arm High
                opperator_X.onTrue(new SequentialCommandGroup(
                                // PROTECTION
                                new ParallelCommandGroup(
                                                new InstantCommand(() -> wristSubsystem.setPosition(0)),
                                                new InstantCommand(() -> wristSubsystem.setPosition(0)),
                                                new InstantCommand(() -> arm.setPosition(44))),
                                new ParallelCommandGroup(
                                                new InstantCommand(() -> arm.setPosition(44)),
                                                new InstantCommand(() -> wristSubsystem.setPosition(0))),
                                new InstantCommand(() -> extender.setPosition(50))));

                // // arm mid
                opperator_B.whileTrue(new SequentialCommandGroup(
                                // //PROTECTION
                                new ParallelCommandGroup(
                                                new InstantCommand(() -> wristSubsystem.setPosition(0)),
                                                new InstantCommand(() -> extender.setPosition(0))),

                                new InstantCommand(() -> arm.setPosition(35)),
                                new ParallelCommandGroup(
                                                new InstantCommand(() -> wristSubsystem.setPosition(28)),
                                                new InstantCommand(() -> extender.setPosition(0)),
                                                new InstantCommand(() -> arm.setPosition(35)))));

                // // substation
                // opperator_Y.whileTrue(substationCommand());
                // opperator_Y.whileFalse(new InstantCommand(() -> claw.stopClaw()));
                // // opperator_Y.whileTrue(new SequentialCommandGroup(
                // // new ArmSetPoint(arm, 44),
                // // new ParallelCommandGroup(
                // // new ExtenderSetPoint(extender, 20),
                // // new WristSetPoint(wrist, -35),
                // // new ArmSetPoint(arm, 44))));
                opperator_Y.whileTrue(substationCommand());

                // // FLOOR
                opperator_A.whileTrue(groundCommand());
                opperator_A.whileFalse(new InstantCommand(() -> claw.stopClaw()));

                // opperator_A.whileFalse(new InstantCommand(() -> claw.stopClaw()));
                // // driver_BottomRightRearButton.whileTrue(new SequentialCommandGroup(
                // // new ExtenderSetPoint(extender, 0),
                // // new ArmSetPoint(arm, 22),
                // // new ParallelCommandGroup(
                // // new ExtenderSetPoint(extender, 0),
                // // new WristSetPoint(wrist, -24)),
                // // new InstantCommand(() -> claw.coneIntake())
                // // ));

                // // opperator_A.whileFalse(new InstantCommand(() -> arm.stopArm()));
                // // opperator_A.whileFalse(new InstanCommand(() -> extender.stopExtender()));
                // // opperator_A.whileFalse(new InstantCommand(() -> wrist.stopWrist()));

                // // opperator_leftBumper.onTrue(new InstantCommand(() -> claw.outTake()));
                // // opperator_RightBumper.onTrue(new InstantCommand(() -> claw.coneIntake()));

                // // ------------------------------------- CLAW
                opperator_TopLeftRearButton.onTrue(new InstantCommand(() -> claw.coneIntake()));
                opperator_BottomLeftRearButton.onTrue(new InstantCommand(() -> claw.outTake()));

                opperator_TopLeftRearButton.onFalse(new InstantCommand(() -> claw.stopClaw()));
                opperator_BottomLeftRearButton.onFalse(new InstantCommand(() -> claw.stopClaw()));

                // opperator_LeftStick.onTrue(new InstantCommand(() -> claw.cubeIntake()));

                // opperator_BottomLeftRearButton.whileFalse(new InstantCommand(() ->
                // claw.stopClaw()));
                // opperator_TopLeftRearButton.whileFalse(new InstantCommand(() ->
                // claw.stopClaw()));

                // // opperator_leftBumper.onTrue(new InstantCommand(() -> stoweAway()));
                // opperator_RightBumper.onTrue(new InstantCommand(() -> stoweAway()));

                // // ------------------------------------- WRIST
                opperator_TopRightRearButton.onTrue(new InstantCommand(() -> wristSubsystem.upGoWrist()));
                opperator_TopRightRearButton.onFalse(new InstantCommand(() -> wristSubsystem.stopWrist()));

                opperator_BottomRightRearButton.onTrue(new InstantCommand(() -> wristSubsystem.downGoWrist()));
                opperator_BottomRightRearButton.onFalse(new InstantCommand(() -> wristSubsystem.stopWrist()));

                // // driver_X.onTrue(new InstantCommand(() -> testers.upGoWrist()));
                // // driver_X.onFalse(new InstantCommand(() -> testers.stopWrist()));

                // // driver_B.onTrue(new InstantCommand(() -> testers.downGoWrist()));
                // // driver_B.onFalse(new InstantCommand(() -> testers.stopWrist()));

                // // ------------------------------------- EXTENDER

                // // opperator_BackButton.onTrue(new InstantCommand(() ->
                // testers.upExtender()));
                // // opperator_BackButton.onFalse(new InstantCommand(() ->
                // // testers.stopExtender()));

                // // opperator_startButton.onTrue(new InstantCommand(() ->
                // // testers.downExtender()));
                // // opperator_startButton.onFalse(new InstantCommand(() ->
                // // testers.stopExtender()));

        }

        // /**************/
        // /*** AUTONS ***/
        // /**************/
        public void configureAutons() {
                SmartDashboard.putData("Autonomous: ", Autons);

                Autons.setDefaultOption("Do Nothing", new DoNothingAuton());
        }

        // // Autons.addOption("SPPLI2 Simple Auton", SwerveDrive.followTraj(
        // // PathPlanner.loadPath(
        // // "New New Path",
        // // new PathConstraints(
        // // 5,
        // // 5)),
        // // true));

        // /* Velocity is negative when we are facing grid because the velocity (x-axis)
        // * is field-centric meaning when looking down, left is negative and right is
        // postive.
        // */

        // Autons.addOption("CONE, MOBILTY", new SequentialCommandGroup(
        // new DriveForward(SwerveDrive, .5, -25, 1),
        // new WaitCommand(1),
        // new DriveForward(SwerveDrive, 4, 15, 7)));

        // Autons.addOption("CONE, ENGAGE", new SequentialCommandGroup(
        // new DriveForward(SwerveDrive, .5, -25, 1),
        // new WaitCommand(1),
        // new DriveForward(SwerveDrive, 0, 35, 2), // tilt charge pad
        // new DriveForward(SwerveDrive, 0, 20, 3), // continue past charge pad
        // new WaitCommand(1),
        // new DriveForward(SwerveDrive, 0, -30, 2.6), // reverse back onto the pad
        // new DriveForward(SwerveDrive, 0, 0, 2),
        // new WristSetPoint(wrist, 0))
        // );

        // // HAS NO REVERSE TO HIT THE GRID
        // Autons.addOption("JUST CHARGE STATION", new SequentialCommandGroup(
        // new DriveForward(SwerveDrive, 0, 35, 2), // tilt charge pad
        // new DriveForward(SwerveDrive, 0, 20, 3), // continue past charge pad
        // new WaitCommand(1),
        // new DriveForward(SwerveDrive, 0, -30, 3) // reverse back onto the pad
        // // new DriveForward(SwerveDrive, 0, -25, 2.8), // reverse back onto the pad
        // // new WaitCommand(1),
        // // new DriveForward(SwerveDrive, 0, -30, 1)
        // ));

        // Autons.addOption("[TESTING] CONE, MOB, PICKUP", new SequentialCommandGroup(
        // new DriveForward(SwerveDrive, .5, -30, 1),
        // new WaitCommand(1),
        // new ParallelDeadlineGroup(
        // new DriveForward(SwerveDrive, 4, 15, 7),
        // groundCommand()),
        // new InstantCommand(() -> claw.stopClaw()),
        // stoweAway()
        // ));

        // // Autons.addOption("armSetpoint", new SequentialCommandGroup(
        // // new ArmSetPoint(arm, 20)));

        // Autons.addOption("[TESTING] MID AUTO", new SequentialCommandGroup(
        // // new ClawCMD(claw, true),
        // //CLOSE
        // new InstantCommand(() -> claw.closeClaw()),
        // // SAFETY UP
        // new ParallelCommandGroup(
        // new ExtenderSetPoint(extender, 0),
        // new WristSetPoint(wrist, 5)),
        // // MOVE ARM
        // new ArmSetPoint(arm, 43),
        // //MOVE WRIST
        // new ParallelCommandGroup(
        // new WristSetPoint(wrist, -28),
        // new ExtenderSetPoint(extender, 0),
        // new ArmSetPoint(arm, 43)),
        // new WaitCommand(.3),
        // // OPEN CLAW
        // new ParallelCommandGroup(
        // new InstantCommand(() -> claw.openClaw()),
        // new ArmSetPoint(arm, 43)),
        // new WaitCommand(.3),
        // // SAFETY
        // new WristSetPoint(wrist, 0),
        // // WE DIP
        // stoweAway()
        // ));

        // // Autons.addOption("[TESTING] MID, ENGAGE", new SequentialCommandGroup(
        // // // DRIVE BACKWARDS
        // // // new DriveForward(SwerveDrive, .5, -25, 1),
        // // // new WaitCommand(1),
        // // // START PLACING CONE
        // // // GO UP
        // // new ClawCMD(claw, true),
        // // new ArmSetPoint(arm, 43),
        // // new ParallelCommandGroup(
        // // new WristSetPoint(wrist, -30),
        // // new ExtenderSetPoint(extender, 0),
        // // new ArmSetPoint(arm, 43)),
        // // // REALSE CONE
        // // new ParallelCommandGroup(new ClawCMD(claw, false), new ArmSetPoint(arm,
        // 43)),
        // // new WaitCommand(1),
        // // // STOWE AWAY
        // // new ParallelCommandGroup(new WristSetPoint(wrist, 0),
        // // new ExtenderSetPoint(extender, 0)),
        // // new ArmSetPoint(arm, 0),
        // // new DriveForward(SwerveDrive, 0, 35, 2), // tilt charge pad
        // // new DriveForward(SwerveDrive, 0, 20, 3), // continue past charge pad
        // // new WaitCommand(1),
        // // new DriveForward(SwerveDrive, 0, 30, 2.6)));

        // // Autons.addOption("GO ON CHARGE PAD", new SequentialCommandGroup(
        // // new DriveForward(SwerveDrive, .5, -30, 1),
        // // new WaitCommand(1),
        // // new DriveForward(SwerveDrive, 4, 20, 4)));
        // // Autons.addOption("mobility", new mobilitytest());
        // // Autons.addOption("AutoBalance", new TestPathPlannerAuton());

        // }

        // /**
        // * Use this to pass the autonomous command to the main {@link Robot} class.
        // *
        // * @return the command to run in autonomous
        // */
        public Command getAutonomousCommand() {
                // An example command will be run in autonomous
                return Autons.getSelected();
        }

        public Command substationCommand() {
                return new SequentialCommandGroup(
                                new InstantCommand(() -> extender.setPosition(0)),
                                new InstantCommand(() -> arm.setPosition(44)),
                                new InstantCommand(() -> wristSubsystem.setPosition(-5)),
                                new WaitCommand(1),
                                new ParallelCommandGroup(
                                                new InstantCommand(() -> extender.setPosition(0)),
                                                new InstantCommand(() -> wristSubsystem.setPosition(-32)),
                                                new InstantCommand(() -> arm.setPosition(44))));
        }

        public Command stoweAway() {
                return new SequentialCommandGroup(
                                new ParallelCommandGroup(
                                                new InstantCommand(() -> extender.setPosition(0)),
                                                new InstantCommand(() -> wristSubsystem.setPosition(5))),
                                new ParallelCommandGroup(
                                                new InstantCommand(() -> arm.setPosition(0)),
                                                new InstantCommand(() -> wristSubsystem.setPosition(5)),
                                                new InstantCommand(() -> extender.setPosition(0))));
        }

        public Command groundCommand() {
                return new SequentialCommandGroup(
                                // new ParallelCommandGroup(
                                // new InstantCommand(() -> extender.setPosition(0)),
                                // new InstantCommand(() -> wristSubsystem.setPosition(5))),
                                // new WaitCommand(2),
                                new InstantCommand(() -> arm.setPosition(13)),
                                new InstantCommand(() -> claw.coneIntake()),
                                new ParallelCommandGroup(
                                                new InstantCommand(() -> extender.setPosition(0)),
                                                new InstantCommand(() -> wristSubsystem.setPosition(-25))));
        }
}

// public Command autoMid() {
// return null;
// }
// }
