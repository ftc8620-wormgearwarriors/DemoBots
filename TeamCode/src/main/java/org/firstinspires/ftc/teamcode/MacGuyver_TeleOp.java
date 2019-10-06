package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="MacGuyver-TeleOp", group="TeleOp")
//@Disabled
public class MacGuyver_TeleOp extends LinearOpMode {

    DcMotor     leftDrive;
    DcMotor     rightDrive;

    //DcMotor     liftExtension;
    //DcMotor     liftWinch;

    @Override
    public void runOpMode() {

        // Set up hardware map for motors on robot
        leftDrive       = hardwareMap.dcMotor.get("leftDrive");
        rightDrive      = hardwareMap.dcMotor.get("rightDrive");

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // Wait until play button is pressed on driver station
        waitForStart();

        // Run indefinitely unless stop button is pressed on driver station
        while (opModeIsActive()) {

            // Enable tank drive style control
            leftDrive.setPower (gamepad1.left_stick_y);
            rightDrive.setPower (gamepad1.right_stick_y);

        }
    }
}
