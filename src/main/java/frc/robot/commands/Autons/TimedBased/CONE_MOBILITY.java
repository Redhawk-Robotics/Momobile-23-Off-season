// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Autons.TimedBased;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.Positions.groundCubeCommand;
import frc.robot.commands.Swerve.DriveForward;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClawSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.WristSubsystem;
import frc.robot.subsystems.extenderSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CONE_MOBILITY extends SequentialCommandGroup {
  /** Creates a new CONE_MOBILITY. */

  public CONE_MOBILITY(SwerveSubsystem SwerveDrive, extenderSubsystem extender, ArmSubsystem arm, WristSubsystem wrist,
      ClawSubsystem claw) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        new DriveForward(SwerveDrive, .5, -25, 1),
        new WaitCommand(1),
        new ParallelCommandGroup(
            new DriveForward(SwerveDrive, 4, 15, 7),
            new groundCubeCommand(extender, arm, wrist, claw))

    );
  }
}
