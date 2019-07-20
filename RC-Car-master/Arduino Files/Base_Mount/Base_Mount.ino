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

//Reference From - https://www.arduino.cc/en/Tutorial/HelloWorld
//Author - Brian Goldenberg
//Description: Code used to display base station through LCD display
//             Lights up multiple leds around display to emphasize 
//Libraties for Liquid crystal display



#include <LiquidCrystal.h>

// Setup code used for initliazing
  const int rs = 7, en = 6, d4 = 5, d5 = 4, d6 = 3, d7 = 2;
  LiquidCrystal lcd(rs, en, d4, d5, d6, d7);
    const int ledPin1 = 13;
    const int ledPin2 = 12;
  const int ledPin3 = 11;
  const int ledPin4 = 10;
  
  
void setup() {
  // initialize digital pins as outputs


  
  pinMode(LED_BUILTIN, OUTPUT);
  pinMode(ledPin1,OUTPUT);
  pinMode(ledPin2,OUTPUT);
  pinMode(ledPin3,OUTPUT);
  pinMode(ledPin4,OUTPUT);

 
 //Display to LCD
  lcd.begin(16, 2);
  lcd.print("BASE STATION!");
  delay(1000);
}

// the loop function runs over and over again forever
void loop() {
 //Loop function used for switching LEDS to on and oof 


 //Switch to on
  digitalWrite(ledPin1,HIGH);
  digitalWrite(ledPin2,HIGH);
  digitalWrite(ledPin3,HIGH);
  digitalWrite(ledPin4,HIGH);


  
  delay(1000);                       // wait for a second
      // turn the LED off by making the voltage LOW
   digitalWrite(ledPin1,LOW);
  digitalWrite(ledPin2,LOW);
  digitalWrite(ledPin3,LOW);
  digitalWrite(ledPin4,LOW);



  
  delay(1000);                       // wait for a second
}
