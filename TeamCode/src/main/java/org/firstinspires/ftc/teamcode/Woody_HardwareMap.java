/**
 * File:            RRIronManHardwareMap.java
 * Author:          WormGear Warriors, FTC 8620
 * Last Modified:   January 2019
 * Description: Defines all hardware components of the robot.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Woody_HardwareMap
{
    /* Public Sensors */
    public WGWIMU2018 imu;
    public BNO055IMU wgwIMU2018        = null;

    /* Public Motors */
    public DcMotor  frontLeftDrive         = null;
    public DcMotor  frontRightDrive        = null;
    public DcMotor  backLeftDrive          = null;
    public DcMotor  backRightDrive         = null;


    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public Woody_HardwareMap(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        wgwIMU2018 = hwMap.get(BNO055IMU.class, "imu");
        imu = new WGWIMU2018(wgwIMU2018);

        // Define and Initialize Motors
        frontLeftDrive    = hwMap.get(DcMotor.class, "frontLeftDrive");
        frontRightDrive   = hwMap.get(DcMotor.class, "frontRightDrive");
        backLeftDrive     = hwMap.get(DcMotor.class, "backLeftDrive");
        backRightDrive    = hwMap.get(DcMotor.class, "backRightDrive");

        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); // Set to FORWARD if using AndyMark motors
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);// Set to REVERSE if using AndyMark motors
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD); //  Set to FORWARD if using AndyMark motors
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);//  Set to FORWARD if using AndyMark motors

        frontLeftDrive.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to zero power
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);


        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
 }

