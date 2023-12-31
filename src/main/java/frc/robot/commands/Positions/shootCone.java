// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Positions;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.extender.ResetExtender;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.extenderSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class shootCone extends SequentialCommandGroup {
  /** Creates a new shootLow. */
  public shootCone(extenderSubsystem extender, ArmSubsystem arm, WristSubsystem wristSubsystem, ClawSubsystem claw) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(

        new ParallelCommandGroup(
            new ResetExtender(extender, 0),
            new InstantCommand(() -> wristSubsystem.setPosition(5))),
        new ParallelCommandGroup(
            new InstantCommand(() -> arm.setPosition(0)),
            new InstantCommand(() -> wristSubsystem.setPosition(5)),
            new InstantCommand(() -> claw.outTakeCone())));
  }
}
