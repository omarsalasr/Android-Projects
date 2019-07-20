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





//Reference from - Written By Nikodem Bartnik - nikodembartnik.pl
//Author - Harsh Devprakash Gupta
//Description: Code for the Base which includes the stepper motor and the ultrasonic sensor. When the tank is empty it 
//             arrives near the base and the distance is calculated by the ultrasonic sensor attached to pin 6 and 7. 
//             When the distance in inches is less than 2 inches and in cm less than 8 cm then the stepper motor starts and 
//             dispenses salt which is added to the tank.
// Libraries for stepper motor and ultrasonic sensor
#define STEPPER_PIN_1 9
#define STEPPER_PIN_2 10
#define STEPPER_PIN_3 11
#define STEPPER_PIN_4 12
const int pingPin = 7; // Trigger Pin of Ultrasonic Sensor
const int echoPin = 6; // Echo Pin of Ultrasonic Sensor

int step_number = 0;
void setup() {
pinMode(STEPPER_PIN_1, OUTPUT);
pinMode(STEPPER_PIN_2, OUTPUT);
pinMode(STEPPER_PIN_3, OUTPUT);
pinMode(STEPPER_PIN_4, OUTPUT);
Serial.begin(9600); // Starting Serial Terminal

}

void loop() {
   long duration, inches, cm;
   pinMode(pingPin, OUTPUT);
   digitalWrite(pingPin, LOW);
   delayMicroseconds(2);
   digitalWrite(pingPin, HIGH);
   delayMicroseconds(10);
   digitalWrite(pingPin, LOW);
   pinMode(echoPin, INPUT);
   duration = pulseIn(echoPin, HIGH);
   inches = microsecondsToInches(duration);
   cm = microsecondsToCentimeters(duration);
   Serial.print(inches);
   Serial.print("in, ");
   Serial.print(cm);
   Serial.print("cm");
   Serial.println();
   if ((inches <= 1 && cm < 5) || (inches = 918 && cm >=2342)){
    OneStep(false);
   delay(2);
   }
   
  

}
long microsecondsToInches(long microseconds) {
   return microseconds / 74 / 2;
}

long microsecondsToCentimeters(long microseconds) {
   return microseconds / 29 / 2;
}


void OneStep(bool dir){
    if(dir){
switch(step_number){
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
  }else{
    switch(step_number){
  case 0:
  digitalWrite(STEPPER_PIN_1, LOW);
  digitalWrite(STEPPER_PIN_2, LOW);
  digitalWrite(STEPPER_PIN_3, LOW);
  digitalWrite(STEPPER_PIN_4, HIGH);
  break;
  case 1:
  digitalWrite(STEPPER_PIN_1, LOW);
  digitalWrite(STEPPER_PIN_2, LOW);
  digitalWrite(STEPPER_PIN_3, HIGH);
  digitalWrite(STEPPER_PIN_4, LOW);
  break;
  case 2:
  digitalWrite(STEPPER_PIN_1, LOW);
  digitalWrite(STEPPER_PIN_2, HIGH);
  digitalWrite(STEPPER_PIN_3, LOW);
  digitalWrite(STEPPER_PIN_4, LOW);
  break;
  case 3:
  digitalWrite(STEPPER_PIN_1, HIGH);
  digitalWrite(STEPPER_PIN_2, LOW);
  digitalWrite(STEPPER_PIN_3, LOW);
  digitalWrite(STEPPER_PIN_4, LOW);
 
  
} 
  }
step_number++;
  if(step_number > 3){
    step_number = 0;
  }
}
