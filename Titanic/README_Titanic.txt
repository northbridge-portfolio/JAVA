Program: Titanic.java
@author NorthBridge
@version 0.9
Java SDK 1.8
Copyright 2017 - NorthBridge - All Rights Reserved


## OVERVIEW:
   This exercise examines the passenger manifest data for the 1918 Titanic 
   disaster, in order to predict who will survive based on information such 
   as age, gender, social status, cabin location, etc.


## USAGE:
   This class is the main driver for the program and handles the setup, 
   configuration and execution of the machine learning model, in this case, 
   an artificial neural network (ANN). This class hands off data parsing, 
   cleaning, and replacement to the class TitanicDataCleanser.java.

   The passenger manifest data is contained in three CSV files located in 
   /DATA_FILES. These contain the training and testing data linked by the 
   passenger ID.

   In order to use this program, run this file (Titanic.java), which will 
   output to the console.


## DEPENDENCIES:
   Dependencies include the Encog Machine Learning Framework from Heaton 
   Research, which provides various machine learning models.
