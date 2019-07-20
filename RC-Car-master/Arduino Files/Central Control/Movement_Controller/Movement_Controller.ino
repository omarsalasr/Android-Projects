/*
 * Team Number: 37
 * Salt Dispensing RC Car
 * Team Members:
 * Omar Salas-Rodriguez- osalas3 osalas3@uic.edu
 * Harsh Gupta - hgupta20 hgupta20@uic.edu 
 * Brian Goldenberg - bgolde2 bgolde2@uic.edu
 * 
 * Abstract: 
 * Using a mobile application we will be designing a car that will 
 * maneuver through ice and dispense salt on the ground creating a 
 * safer walking environment. With three arduinos, a motor controller 
 * for the dual motors, chassis with custom 3D printed parts, wheels, 
 * chain, stepper motor and bluetooth module will be will dispense salt 
 * from the back of the tank. When in need of salt, the tank will pull up 
 * under a nearby base to request more salt to be dispensed(only when the car is in the proper position). 
 * All controlled from the mobile application that we will also design and implement. 

 */

/*
 *  Author: Omar Salas-Rodriguez
 *          CS 362 Spring 2019
 *          
 *  Description: Arduino INO file for all tank functionality.
 *               There are several components connected to this arduino.
 *               
 *               
 *               Stepper Motor and ULN2003 Stepper Motor Driver Board:
 *               
 *                  The driver board connects to pins 9, 10, 11, 13, 5V, and ground.
 *                  It's used to control the revolutions and steps for the 
 *                  stepper motor. The stepper motor is held in place my a small custom
 *                  3D printed holder and attached to a rotating 3D printed part to
 *                  drop the salt from the container to the back of the tank.
 *                  Pressing the drop salt button in the companion app will enable a
 *                  continuous dropping of salt until a refill is requested or the
 *                  button in the companion app is pressed again.
 * 
 *               Dual 260V Motors and L293D Motor Controller:
 *               
 *                  The motor controller connects to pins 3, 4, 5, 6, 7, 8, 5V, and ground.
 *                  It's used to control the speed of 2 motors individually. The dual motors
 *                  are what makes the tank move forward, backwards, turn left, and turn right.
 *               
 *               RF 433MHz Transmitter:
 *               
 *                  The RF 433Mhz transmitter connects to pins 12, 5V, and ground.
 *                  It's used to communicate with one of the arduinos that control the seperate
 *                  base containing all the salt. It's only purposes is to send a stream of bytes
 *                  that the other arduino will decode in order to confirm and drop salt onto the tank.
 *               
 *               HC-06 Bluetooth Module:
 *               
 *                  The Bluetooth module connects to pins 0 (RX), 5V, and ground.
 *                  It's used to communicate with the companion app through bluetooth by receiving
 *                  a stream of bytes that will be decoded here. 
 *                  There are several messages possible:
 *                    - "FORWARD" : Move the tank forward
 *                    - "REVERSE" : Move the tank in reverse
 *                    - "LEFT" : Turn the tank to the left in place
 *                    - "RIGHT" : Turn the tank to the right in place
 *                    - "STOP" : Stop the tank from moving
 *                    - "DROP" : Start turning the stepper motor so salt is dropped onto the ground
 *                    - "SPEED" : Adjust the speed of the tank accordingly
 *                    
 *  References: https://randomnerdtutorials.com/rf-433mhz-transmitter-receiver-module-with-arduino/
 *              Elegoo Super Starter Kit for UNO pdf
 *  
 *  
 */


// Constants for the motor controller pins
#define E1 6
#define DIRA1 7
#define DIRA2 8

#define DIRB2 4
#define DIRB1 3
#define E2 5

// Constants for the stepper motor
#define STEPPER_PIN_1 9
#define STEPPER_PIN_2 10
#define STEPPER_PIN_3 11
#define STEPPER_PIN_4 13

char command; // Used to retrieve byte stream from companion app
int tank_speed;
String str; // Final message from companion app

bool drop = false; // Continuous dropping of salt boolean
int stepNum = 0;

void setup() {
  // Initialize pins and values
  pinMode(E1, OUTPUT);
  pinMode(DIRA1, OUTPUT);
  pinMode(DIRA2, OUTPUT);
  pinMode(DIRB2, OUTPUT);
  pinMode(DIRB1, OUTPUT);
  pinMode(E2, OUTPUT);
  pinMode(STEPPER_PIN_1, OUTPUT);
  pinMode(STEPPER_PIN_2, OUTPUT);
  pinMode(STEPPER_PIN_3, OUTPUT);
  pinMode(STEPPER_PIN_4, OUTPUT);
  
  Serial.begin(9600);
  tank_speed = 180;
 
}

void goForwards(){ // Move the tank forward
  analogWrite(E1,tank_speed); 
  digitalWrite(DIRA1,LOW); 
  digitalWrite(DIRA2,HIGH);
  
  analogWrite(E2,tank_speed); 
  digitalWrite(DIRB1,LOW); 
  digitalWrite(DIRB2,HIGH); 
}
void goBackwards(){ // Move the tank backwards
  analogWrite(E1,tank_speed); 
  digitalWrite(DIRA1,HIGH); 
  digitalWrite(DIRA2,LOW);
  
  analogWrite(E2,tank_speed); 
  digitalWrite(DIRB1,HIGH); 
  digitalWrite(DIRB2,LOW); 
}
void goRight(){ // Turn the tank to the right
  analogWrite(E1,tank_speed); 
  digitalWrite(DIRA1,HIGH); 
  digitalWrite(DIRA2,LOW);
  
  analogWrite(E2,tank_speed); 
  digitalWrite(DIRB1,LOW); 
  digitalWrite(DIRB2,HIGH); 
}
void goLeft(){ // Turn the tank to the left
  analogWrite(E1,tank_speed); 
  digitalWrite(DIRA1,LOW); 
  digitalWrite(DIRA2,HIGH);
  
  analogWrite(E2,tank_speed); 
  digitalWrite(DIRB1,HIGH); 
  digitalWrite(DIRB2,LOW); 
}
void stopTank(){ // Stop the tank from moving
  digitalWrite(DIRA1,LOW); 
  digitalWrite(DIRA2,LOW);
  digitalWrite(DIRB1,LOW); 
  digitalWrite(DIRB2,LOW); 
}

void dropSalt(){ // Drop the salt to the ground
  if(drop) // Check if it's already dropping
    drop = false;
  else
    drop = true;
  
}

// Function to receive companion application byte stream and process it
void receiveMessage(){ 
  if (Serial.available() > 0) // Clear the message string
    str = "";
  while(Serial.available() > 0){ // Continue receiving bytes until done
    command = ((byte)Serial.read());
    if(command == ':')
      break;
    else
      str += command;
    delay(1);
  }
  Serial.println(str); // Debugging
  
  // Process the incoming message
  if(str == "FORWARD")
    goForwards();
  else if(str == "RIGHT")
    goRight();
  else if(str == "REVERSE")
    goBackwards();
  else if(str == "LEFT")
    goLeft();
  else if(str == "STOP")
    stopTank();
  else if(str == "DROP")
    dropSalt();
  else if(str.substring(0,5) == "SPEED")
    tank_speed = str.substring(5, str.length()).toInt(); // Update the tank speed
  
}

void motorStep(){
  switch(stepNum){ // Turn each piece of the stepper motor
    case 0:
      digitalWrite(STEPPER_PIN_1, HIGH);
      digitalWrite(STEPPER_PIN_2, LOW);
      digitalWrite(STEPPER_PIN_3, LOW);
      digitalWrite(STEPPER_PIN_4, LOW);
      break;
    case 1:
      digitalWrite(STEPPER_PIN_1, LOW);
      digitalWrite(STEPPER_PIN_2, HIGH);
      digitalWrite(STEPPER_PIN_3, LOW);
      digitalWrite(STEPPER_PIN_4, LOW);
      break;
    case 2:
      digitalWrite(STEPPER_PIN_1, LOW);
      digitalWrite(STEPPER_PIN_2, LOW);
      digitalWrite(STEPPER_PIN_3, HIGH);
      digitalWrite(STEPPER_PIN_4, LOW);
      break;
    case 3:
      digitalWrite(STEPPER_PIN_1, LOW);
      digitalWrite(STEPPER_PIN_2, LOW);
      digitalWrite(STEPPER_PIN_3, LOW);
      digitalWrite(STEPPER_PIN_4, HIGH);
      break;
  }
  // Make stepper motor keep on turning
  stepNum++;
  if(stepNum > 3)
    stepNum = 0;
}

void loop() {
  receiveMessage();

  if(drop)
    motorStep(); // Start stepper in reverse motion
  delay(2);
}
