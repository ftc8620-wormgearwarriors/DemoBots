package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


@TeleOp(name="IronMan_Tele", group="TeleOp")
//@Disabled
public class RoverRuckus_IronMan_Tele extends OpMode{

    /* Declare OpMode members. */
    RRIronManHardwareMap robot       = new RRIronManHardwareMap(); // use the class created to define a Pushbot's hardware
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
        robot.brakeServo.setPosition(0.8);
        brakeTimer.reset();
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
    double dropServoPos  = 1.1;
    double wristServoPos = 0.4;
    @Override
    public void loop() {
        double markerServoPos = .72;
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

        robot.frontLeftDrive.setPower(frontLeft);
        robot.frontRightDrive.setPower(frontRight);
        robot.backLeftDrive.setPower(backLeft);
        robot.backRightDrive.setPower(backRight);



        if(gamepad1.y) {  // assigns y botton to imu resting
            robot.imu.resetHeading();
        }

        if (gamepad1.a) {
            robot.craterServo.setPosition(0.5);
        }
        else  if (gamepad1.b) {  // assigns b botton to setting craterServo position to 0.05
            robot.craterServo.setPosition(0.05);
        }

        if (gamepad1.dpad_left) {  // assigns dpad left to setting markerServo position to 0.28
            markerServoPos = 0.28;
        }
        else if (gamepad1.dpad_right)
            markerServoPos = 0.72;
        robot.markerServo.setPosition(markerServoPos);

//        telemetry.addData("Lift Switch ", "%b", robot.liftSensor.getState());
//        telemetry.update();
       if (gamepad2.dpad_down) {  // asigns dpad down to setting LiftMotor power to .9 forwards
           robot.liftMotor.setPower(0.9);
       }
       else if (gamepad2.dpad_up  && robot.liftSensor.getState( ) ==  true) {  // need robot. before the sensor name!
           robot.liftMotor.setPower(-0.9);  // // asigns dpad down to setting LiftMotor power to .9 backwards
       }
       else {
           robot.liftMotor.setPower(0);
       }

       if (gamepad2.right_trigger > .1) {
           robot.collectionServo.setPosition(0.9);  // set collection servo to game pad 2 left trigger
       }
       else if (gamepad2.left_trigger > .1) {
           robot.collectionServo.setPosition(0.1);  // sets collection servo to game pad 2 right trigger opposite way.
       }
       else {
           robot.collectionServo.setPosition(0.5);
       }

       robot.extensionMotor.setPower(gamepad2.left_stick_x);  // assigns left stick x tom moving the arm
       if (gamepad2.right_stick_y > -0.1 && gamepad2.right_stick_y < 0.1) {
           robot.brakeServo.setPosition(0.8);
           if (brakeTimer.seconds() < .5) {  // gives time to activate the brake
               robot.bigBoiMotor1.setPower(0.20);
               robot.bigBoiMotor2.setPower(0.20);
           } else {
               robot.bigBoiMotor1.setPower(0);
               robot.bigBoiMotor2.setPower(0);
           }

               } else {
                   robot.brakeServo.setPosition(0.4);
                   robot.bigBoiMotor1.setPower(gamepad2.right_stick_y);
                   robot.bigBoiMotor2.setPower(gamepad2.right_stick_y);
                   brakeTimer.reset();
                   }
        if (gamepad2.left_bumper) {  // asingns right bumper to moving the arm slowly forwards
            robot.brakeServo.setPosition(0.4);
            robot.bigBoiMotor1.setPower(0.25);
            robot.bigBoiMotor2.setPower(0.25);
        }
        else if (gamepad2.right_bumper) {  // asingns right bumper to moving the arm slowly backwards
            robot.brakeServo.setPosition(0.4);
            robot.bigBoiMotor1.setPower(-0.25);
            robot.bigBoiMotor2.setPower(-0.25);
        }
                   /* if (gamepad2.a) {
            dropServoPos = 0.5;
        }
        else if (gamepad2.b) {
            dropServoPos = 1.1;
        }
        robot.dropServo.setPosition(dropServoPos); */

               if (gamepad2.dpad_left) {  // assigns dpad left to moving wristServo 0.01 fowards
                   wristServoPos += 0.01;
               }
               if (gamepad2.dpad_right) {  // assigns dpad right to moving wristServo 0.01 backwards
                   wristServoPos -= 0.01;
               }
               if (wristServoPos > 0.5)
                   wristServoPos = 0.5;

               if (wristServoPos < .4)
                   wristServoPos = 0.4;

               robot.wristServo.setPosition(wristServoPos);


               telemetry.addData("Marker Servo Position", "%5.2f", markerServoPos);
               telemetry.addData("Drop Servo Position", "%5.2f", dropServoPos);
               telemetry.addData("Lift Motor Position", "%d", robot.liftMotor.getCurrentPosition());
               telemetry.addData("Collect Servo", "%5.2f", robot.collectionServo.getPosition());
               telemetry.update();


       /*
       double x = gamepad1.left_stick_x;
       double y = gamepad1.left_stick_y;
       double z = gamepad1.right_stick_x;
       double frontLeftPower = -x + y + z;
       double frontRightPower = x + y - z;
       double backLeftPower = x + y + z;
       double backRightPower = -x +y - z;

       double speedScalar = Math.max(Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower)),
               Math.max(Math.abs(backLeftPower), Math.abs(backRightPower)));

       if (speedScalar > 1) {
           frontLeftPower = frontLeftPower / speedScalar;
           frontRightPower = frontRightPower / speedScalar;
           backLeftPower = backLeftPower / speedScalar;
           backRightPower = backRightPower / speedScalar;
       }

       robot.frontLeftDrive.setPower(frontLeftPower);
       robot.frontRightDrive.setPower(frontRightPower);
       robot.backLeftDrive.setPower(backLeftPower);
       robot.backRightDrive.setPower(backRightPower);
*/
           }


           /*
            * Code to run ONCE after the driver hits STOP
            */
           @Override
           public void stop(){
           }}
