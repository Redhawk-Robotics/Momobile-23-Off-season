// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Autons.TimedBased;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Positions.stowAway;
import frc.robot.commands.Swerve.DriveForward;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.extenderSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MIDSCORE extends SequentialCommandGroup {
  /** Creates a new MIDSCORE. */
  public MIDSCORE(SwerveSubsystem SwerveDrive, extenderSubsystem extender, ArmSubsystem arm, WristSubsystem wrist,
      ClawSubsystem claw) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        // new ClawCMD(claw, true),
        // CLOSE
        new InstantCommand(() -> claw.closeClaw()),
        // SAFETY UP
        new ParallelCommandGroup(
            new InstantCommand(() -> extender.setPosition(0)),
            new InstantCommand(() -> wrist.setPosition(5))),
        // MOVE ARM
        new InstantCommand(() -> arm.setPosition(43)),
        // MOVE WRIST
        new ParallelCommandGroup(
            new InstantCommand(() -> wrist.setPosition(-28)),
            new InstantCommand(() -> extender.setPosition(0)),
            new InstantCommand(() -> arm.setPosition(43))),

        new WaitCommand(1),
        // OPEN CLAW
        new ParallelCommandGroup(
            new InstantCommand(() -> claw.openClaw()),
            new InstantCommand(() -> arm.setPosition(43))),

        new WaitCommand(1),
        // SAFETY
        new InstantCommand(() -> wrist.setPosition(5)),
        // WE DIP
        // new DriveForward(SwerveDrive, .5, -1, .5),
        new stowAway(extender, arm, wrist));
  }
}
