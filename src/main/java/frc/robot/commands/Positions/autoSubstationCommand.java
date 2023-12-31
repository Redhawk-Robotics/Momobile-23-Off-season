// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Positions;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.extenderSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class autoSubstationCommand extends SequentialCommandGroup {
  /** Creates a new autoSubstationCommand. */
  public autoSubstationCommand(extenderSubsystem extender, ArmSubsystem arm, WristSubsystem wrist) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        new ParallelDeadlineGroup(
            new InstantCommand(() -> wrist.setPosition(5)),
            new InstantCommand(() -> extender.setPosition(0))),
        new WaitCommand(1),
        new InstantCommand(() -> arm.setPosition(44)),
        new ParallelCommandGroup(
            new InstantCommand(() -> extender.setPosition(0)),
            new InstantCommand(() -> wrist.setPosition(-30)),
            new InstantCommand(() -> arm.setPosition(44))));
  }
}
