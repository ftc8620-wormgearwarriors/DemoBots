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
import com.qualcomm.robotcore.hardware.TouchSensor;

public class RRIronManHardwareMap
{
    /* Public Sensors */
    public WGWIMU2018 imu;
    public BNO055IMU wgwIMU2018        = null;
    public DigitalChannel liftSensor   = null;
    public DigitalChannel topSensor    = null;

    public ModernRoboticsI2cRangeSensor leftRangeSensor = null;
    public ModernRoboticsI2cRangeSensor rightRangeSensor = null;

    public double testVar = 0;


    /* Public Motors */
    public DcMotor  frontLeftDrive         = null;
    public DcMotor  frontRightDrive        = null;
    public DcMotor  backLeftDrive          = null;
    public DcMotor  backRightDrive         = null;
    public DcMotor  liftMotor              = null;
   // public DcMotor  collectionMotor  = null;
    public DcMotor  extensionMotor         = null;
    public DcMotor  bigBoiMotor1  = null;
    public DcMotor  bigBoiMotor2 = null;

    /* Public Servos*/
    public Servo  markerServo = null;
    //public Servo  dropServo   = null;
    public Servo  craterServo     = null;
    public Servo  wristServo      = null;
    public Servo  collectionServo = null;
    public Servo  brakeServo      = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public RRIronManHardwareMap(){

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
        liftMotor         = hwMap.get(DcMotor.class, "liftMotor");
        //collectionMotor = hwMap.get(DcMotor.class, "collectionMotor");
        extensionMotor    = hwMap.get(DcMotor.class, "extensionMotor");
        bigBoiMotor1  = hwMap.get(DcMotor.class, "bigBoiMotor1");
        bigBoiMotor2  = hwMap.get(DcMotor.class, "bigBoiMotor2");

        markerServo      = hwMap.get(Servo.class, "markerServo");
        //dropServo   = hwMap.get(Servo.class, "dropServo");
        craterServo      = hwMap.get(Servo.class, "craterServo");
        wristServo       = hwMap.get(Servo.class, "wristServo");
        collectionServo  = hwMap.get(Servo.class, "collectionServo");
        brakeServo       = hwMap.get(Servo.class, "brakeServo");

        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD); // Set to FORWARD if using AndyMark motors
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);// Set to REVERSE if using AndyMark motors
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD); //  Set to FORWARD if using AndyMark motors
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);//  Set to FORWARD if using AndyMark motors
        liftMotor.setDirection(DcMotor.Direction.REVERSE );//  Set to FORWARD if using AndyMark motors
       // collectionMotor.setDirection(DcMotor.Direction.FORWARD);//  Set to FORWARD if using AndyMark motors
        extensionMotor.setDirection(DcMotor.Direction.FORWARD);//  Set to FORWARD if using AndyMark motors
        bigBoiMotor1.setDirection(DcMotor.Direction.FORWARD);//  Set to FORWARD if using AndyMark motors
        bigBoiMotor2.setDirection(DcMotor.Direction.FORWARD);//  Set to FORWARD if using AndyMark motors

        frontLeftDrive.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        liftMotor.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        //collectionMotor.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        extensionMotor.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        bigBoiMotor1.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        bigBoiMotor2.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to zero power
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        liftMotor.setPower(0);
        //collectionMotor.setPower(0);
        extensionMotor.setPower(0);
        bigBoiMotor1.setPower(0);
        bigBoiMotor2.setPower(0);


        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //collectionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        extensionMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        bigBoiMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bigBoiMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        liftSensor = hwMap.get(DigitalChannel.class, "liftSensor");
        liftSensor.setMode(DigitalChannel.Mode.INPUT);

        topSensor = hwMap.get(DigitalChannel.class, "topSensor");
        topSensor.setMode(DigitalChannel.Mode.INPUT);

        rightRangeSensor = hwMap.get(ModernRoboticsI2cRangeSensor.class, "rightRangeSensor");
        leftRangeSensor = hwMap.get(ModernRoboticsI2cRangeSensor.class, "leftRangeSensor");
    }
 }

