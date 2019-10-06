package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="Woody_Tele", group="TeleOp")
//@Disabled
public class Rr_Woody_Tele extends OpMode{

    /* Declare OpMode members. */
    Woody_HardwareMap robot       = new Woody_HardwareMap(); // use the class created to define a Pushbot's hardware
    private ElapsedTime brakeTimer   = new ElapsedTime();          // timer to set delay on motors after brake engages.

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

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
        double x_axis = gamepad1.left_stick_x  * maxVel;
        double y_axis = -gamepad1.left_stick_y * maxVel;
        double x_prime;
        double y_prime;
        double theta =  Math.toRadians(-robot.imu.getHeading());
        double gyroHeading = robot.imu.getHeading();

        robot.frontLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //  Find robot's current axes in relation to original axes
        x_prime = x_axis * Math.cos(theta) + y_axis * Math.sin(theta);
        y_prime = -x_axis * Math.sin(theta) + y_axis * Math.cos(theta);

        telemetry.addData("Theta (in radians)", theta);
        telemetry.addData("x_axis", x_axis);
        telemetry.addData("y_axis", y_axis);
        telemetry.addData("x_prime", x_prime);
        telemetry.addData("y_prime", y_prime);
        telemetry.addData("Gyro Heading", gyroHeading);

        // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
        // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
        frontRight = y_prime - (gamepad1.right_stick_x /2 *maxVel) - x_prime;
        backRight  = y_prime - (gamepad1.right_stick_x /2 *maxVel) + x_prime;
        frontLeft  = y_prime + (gamepad1.right_stick_x /2 *maxVel) + x_prime;
        backLeft   = y_prime + (gamepad1.right_stick_x /2 *maxVel) - x_prime;

        if (gamepad1.left_trigger > .05)
            maxVel = 0.5;
        else if (gamepad1.right_trigger > .05)
            maxVel = 1.0;
        // Normalize the values so neither exceed +/- 1.0
              max = Math.max(Math.max(Math.abs(frontLeft), Math.abs(backLeft)), Math.max(Math.abs(frontRight), Math.abs(backRight)));
        if (max > 1)
        {
            frontLeft /= max;
            backLeft /= max;
            frontRight /= max;
            backRight /= max;
        }
// here is a comment
        robot.frontLeftDrive.setPower(frontLeft); //
        robot.frontRightDrive.setPower(frontRight);
        robot.backLeftDrive.setPower(backLeft);
        robot.backRightDrive.setPower(backRight);



        if(gamepad1.y) {  // assigns y botton to imu resting
            robot.imu.resetHeading();
        }
    }


           /*
            * Code to run ONCE after the driver hits STOP
            */
           @Override
           public void stop(){
           }}
