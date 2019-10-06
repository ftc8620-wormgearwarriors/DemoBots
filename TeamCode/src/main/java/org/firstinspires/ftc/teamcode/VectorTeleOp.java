// Team 8620 TeleOp, Created By Reece Watson on 10/8/16

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.TouchSensor;


@TeleOp(name="vector TeleOp", group="TeleOp")

public class VectorTeleOp extends OpMode{

    ElapsedTime timer = new ElapsedTime();

    DcMotor backLeftMotor;
    DcMotor frontLeftMotor;
    DcMotor backRightMotor;
    DcMotor frontRightMotor;

    DcMotor Launcher;
    DcMotor Collector;

    DcMotor Lift;

    MyIMU2016 imu;
    TouchSensor limitSwitch;

    Servo ballRelease;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {

        BNO055IMU myIMU2016;

        myIMU2016 = hardwareMap.get(BNO055IMU.class, "imu");
        imu = new MyIMU2016(myIMU2016);

        backLeftMotor = hardwareMap.dcMotor.get("Back_Left_Drive");
        frontLeftMotor = hardwareMap.dcMotor.get("Front_Left_Drive");
        backRightMotor = hardwareMap.dcMotor.get("Back_Right_Drive");
        frontRightMotor = hardwareMap.dcMotor.get("Front_Right_Drive");
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);

        Collector = hardwareMap.dcMotor.get("CollectionMotor");
        Launcher = hardwareMap.dcMotor.get("LauncherMotor");
        Collector.setDirection(DcMotor.Direction.REVERSE);
        Launcher.setDirection(DcMotor.Direction.REVERSE);

        Lift = hardwareMap.dcMotor.get("Lift");

        ballRelease = hardwareMap.servo.get("ballRelease");

        limitSwitch = hardwareMap.touchSensor.get("limitSwitch");

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Running");
        updateTelemetry(telemetry);

        ballRelease.setPosition(0.0);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }


    private ShooterState shooterState = ShooterState.Idle;

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    double maxVel = 0.5;
    @Override
    public void loop() {
        double frontLeft;
        double backLeft;
        double frontRight;
        double backRight;
        double max;
        double x_axis =   gamepad1.left_stick_x/2 * maxVel;
        double y_axis =  -gamepad1.left_stick_y/2 * maxVel;
        double r_axis =  gamepad1.right_stick_x/2 * maxVel;            // rotatoin axis
        double x_prime;
        double y_prime;
        double theta =  Math.toRadians(-imu.getHeading());
        double gyroHeading = imu.getHeading();

       frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  Find robot's current axes in relation to original axes
        x_prime =  x_axis * Math.cos(theta) + y_axis * Math.sin(theta);
        y_prime = -x_axis * Math.sin(theta) + y_axis * Math.cos(theta);

        telemetry.addData("Theta (in radians)", theta);
        telemetry.addData("x_axis", x_axis);
        telemetry.addData("y _axis", y_axis);
        telemetry.addData("x_prime", x_prime);
        telemetry.addData("y_prime", y_prime);
        telemetry.addData("Gyro Heading", gyroHeading);

        // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
        // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
        frontRight  =   y_prime - r_axis - x_prime;
        backRight =     y_prime - r_axis + x_prime;
        frontLeft =     y_prime + r_axis + x_prime;
        backLeft =      y_prime + r_axis - x_prime;

        // Normalize the values so neither exceed +/- 1.0
        max = Math.max(Math.max(Math.abs(frontLeft), Math.abs(backLeft)), Math.max(Math.abs(frontRight), Math.abs(backRight)));
        if (max > 1.0)
        {
            frontLeft /= max;
            backLeft /= max;
            frontRight /= max;
            backRight /= max;
        }

        frontLeftMotor.setPower(frontLeft);
        backLeftMotor.setPower(backLeft);
        frontRightMotor.setPower(frontRight);
        backRightMotor.setPower(backRight);



        if(gamepad1.y) {
            imu.resetHeading();
        }

        if (gamepad1.left_trigger > .05)        // speed control for drive train
            maxVel = 0.5;
        else if (gamepad1.right_trigger > .05)
            maxVel = 1.0;

        if(gamepad2.dpad_up) {
           Lift.setPower(1);
        }
        else if(gamepad2.dpad_down) {
           Lift.setPower(-1);
        }
        else {
            Lift.setPower(0);
        }

        if(gamepad2.left_trigger > .02) {
            Collector.setPower(-1);
        }
        else if (gamepad2.left_bumper) {
            Collector.setPower(1);
        }
        else {
            Collector.setPower(0);
        }

        if(gamepad2.right_trigger < .1) {
            shooterState = ShooterState.Idle;
            if (gamepad2.a) {
                ballRelease.setPosition(.30);
            }
            else if (gamepad2.b) {
                ballRelease.setPosition(0.0);
            }

            if(gamepad2.right_bumper) {
                Launcher.setPower(1);
            }
            else {
                Launcher.setPower(0);
            }
        }
        switch(shooterState) {
            case Idle:
                if(gamepad2.right_trigger > .1) {
                    shooterState = ShooterState.StopArm;
                    Launcher.setPower(1);
                }

                else if(!gamepad2.right_bumper) {
                    Launcher.setPower(0);
                }
                break;

            case StopArm:
                if(limitSwitch.isPressed()) {
                    shooterState = ShooterState.OpenServo;
                    Launcher.setPower(0);
                }
                break;

            case OpenServo:
                shooterState = ShooterState.CloseServo;
                ballRelease.setPosition(.3);
                timer.reset();
                break;

            case CloseServo:
                if(timer.seconds() > .5) {
                    shooterState = ShooterState.LoadShooter;
                    ballRelease.setPosition(0.0);
                    timer.reset();
                }
                break;

            case LoadShooter:
                if(timer.seconds() > .5) {
                    shooterState = ShooterState.Shoot;
                    Launcher.setPower(1);
                }
                break;

            case Shoot:
                if(!limitSwitch.isPressed()) {
                    Launcher.setPower(0);
                    shooterState = ShooterState.Idle;
                }
                break;

            default:
                Launcher.setPower(0);
                shooterState =ShooterState.Idle;
                break;
        }

        telemetry.addData("ShooterState:", shooterState);
        telemetry.addData("Limit Switch = ", limitSwitch.isPressed());
        updateTelemetry(telemetry);

    }


    enum ShooterState {
        Idle, StopArm, OpenServo, CloseServo, LoadShooter, Shoot
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}



