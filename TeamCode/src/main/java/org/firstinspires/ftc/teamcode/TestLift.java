/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import junit.framework.Test;


/**
 * this is our op mode
 */

@TeleOp(name="Basic: Linear OpMode", group="Linear Opmode")
//@Disabled
public class TestLift extends LinearOpMode
{

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode()
    {
        TestHardware robot = new TestHardware();
        robot.init(hardwareMap);
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.lift.setTargetPosition(0);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {

            // Setup a variable for each drive wheel to save power level for telemetry
            double leftPower;
            double rightPower;


            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            //we have NOT tested this yet
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;


            if(gamepad1.a)
            {
                robot.liftPosition = TestHardware.ONE_INCH_POSITION;
            }
            if(gamepad1.x)
            {
                robot.liftPosition = TestHardware.SEVEN_INCH_POSITION;
            }
            if(gamepad1.y)
            {
                robot.liftPosition = TestHardware.THIRTEEN_INCH_POSITION;
            }
            if(gamepad1.b)
            {
                robot.liftPosition = TestHardware.NINETEEN_INCH_POSITION;
            }
            if(gamepad1.left_bumper)
            {
                robot.rightClaw.setPosition(TestHardware.RIGHT_CLAW_CLOSE);
                robot.leftClaw.setPosition(TestHardware.LEFT_CLAW_CLOSE);
            }
            if(gamepad1.right_bumper)
            {
                robot.rightClaw.setPosition(TestHardware.RIGHT_CLAW_OPEN);
                robot.leftClaw.setPosition(TestHardware.LEFT_CLAW_OPEN);
            }
            if(gamepad1.right_stick_button)
            {
                robot.rightClaw.setPosition(TestHardware.RIGHT_CLAW_PUSH);
                robot.leftClaw.setPosition(TestHardware.LEFT_CLAW_PUSH);
            }


            // Send calculated power to wheels
            robot.leftDrive.setPower(leftPower);
            robot.rightDrive.setPower(rightPower);
            robot.lift.setTargetPosition(robot.liftPosition);
            if(robot.liftPosition >= TestHardware.MIN_POSITION && robot.liftPosition <= TestHardware.MAX_POSITION)
            {
                robot.lift.setPower(robot.LIFT_POWER);
            }
            else
            {
                robot.lift.setPower(0);
            }

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Lift position:",robot.lift.getCurrentPosition());
            telemetry.addData("Left claw position:", robot.leftClaw.getPosition());
            telemetry.addData("Right claw position:", robot.rightClaw.getPosition());
            telemetry.update();
        }
    }
}
