// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
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
  private final SwerveSubsystem m_swerveSubsystem = new SwerveSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController = new CommandXboxController(
      OperatorConstants.kDriverControllerPort);

  private boolean m_isFieldRelative = true;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    m_swerveSubsystem.setDefaultCommand(
        new RunCommand(
            () -> m_swerveSubsystem.drive(
                -m_driverController.getLeftY() * Constants.DriveConstants.MAX_SPEED_METERS_PER_SECOND,
                -m_driverController.getLeftX() * Constants.DriveConstants.MAX_SPEED_METERS_PER_SECOND,
                -m_driverController.getRightX() * Constants.DriveConstants.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND,
                m_isFieldRelative),
            m_swerveSubsystem));

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
  private void configureBindings() {
    m_driverController.y().onTrue(Commands.runOnce(m_swerveSubsystem::zeroHeading, m_swerveSubsystem));

    m_driverController.x().onTrue(Commands.runOnce(() -> m_isFieldRelative = !m_isFieldRelative));

    m_driverController.b().whileTrue(Commands.run(m_swerveSubsystem::setXPattern, m_swerveSubsystem));

    m_driverController.start()
        .onTrue(Commands.runOnce(() -> m_swerveSubsystem.resetOdometry(new Pose2d()), m_swerveSubsystem));

    m_driverController.rightBumper().whileTrue(
        Commands.run(
            () -> m_swerveSubsystem.drive(
                -m_driverController.getLeftY() * Constants.DriveConstants.MAX_SPEED_METERS_PER_SECOND * 0.5,
                -m_driverController.getLeftX() * Constants.DriveConstants.MAX_SPEED_METERS_PER_SECOND * 0.5,
                -m_driverController.getRightX() * Constants.DriveConstants.MAX_ANGULAR_SPEED_RADIANS_PER_SECOND * 0.5,
                m_isFieldRelative),
            m_swerveSubsystem));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    // return Autos.exampleAuto(m_exampleSubsystem);
    return null;
  }

  public CommandXboxController getDriverController() {
    return m_driverController;
  }

  public SwerveSubsystem getSwerveDriveSubsystem() {
    return m_swerveSubsystem;
  }
}
