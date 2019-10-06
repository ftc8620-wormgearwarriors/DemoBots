// Team 8620 IMU Functions (TeleOP), Created By Reece Watson on 12/28/16

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

//@Autonomous(name="MyIMU", group="InheritedClasses")
//@Disabled
public class MyIMU2016 {

    private BNO055IMU myIMU2016;
    private double offset = 0;

    public MyIMU2016(BNO055IMU imu) {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.useExternalCrystal = true;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        parameters.pitchMode = BNO055IMU.PitchMode.WINDOWS;
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        imu.initialize(parameters);

        myIMU2016 = imu;
    }

    public double getHeading() {
        Quaternion quatAngles = myIMU2016.getQuaternionOrientation();

        double w = quatAngles.w;
        double x = quatAngles.x;
        double y = quatAngles.y;
        double z = quatAngles.z;

        double yaw = (Math.atan2(1 - 2 * (y * y + z * z), 2 * (w * z + x * y)) * 180 / Math.PI) - 90;

        if (yaw < 0) {
            yaw = yaw + 360;
        }

        return yaw - offset;
    }

    public double resetHeading() {
        offset = getHeading() + offset;

        return offset;
    }
}



