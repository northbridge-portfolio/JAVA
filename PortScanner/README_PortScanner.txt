Program: PortScanner.java
@author NorthBridge
@version 1.2
Java SDK 1.8
Copyright 2017 - NorthBridge - All Rights Reserved


## OVERVIEW:
   This Java class enumerates over both a machine IPv4 range and Port Range 
   (0-65535) looking for open ports. If a port is open, the machine IP and and 
   port number are logged to the PortScanner.reportList and printed at the end 
   of the scan.


## USAGE:
   To run this program from the command line use the following arguments:
   $>java PortScanner host:A.A.A.A port:XX
   where: A.A.A.A is the target machine's IPv4 address
          XX is the target machine's port number
        
   OR to specify a range including multiple machines and port numbers, use:

   $>java PortScanner host:A.A.A.A-B.B.B.B port:XX-YY
   where: A.A.A.A is the STARTING target machine's IPv4 address
          B.B.B.B is the ENDING target machine's IPv4 address
          XX is the target machine's STARTING port number
          YY is the target machine's ENDING port number

WARNING: FOR LEARNING PURPOSES ONLY. USE OF THIS PROGRAM ON UNAUTHORIZED 
COMPUTERS/MACHINES/EQUIPMENT NOT YOUR OWN MAY RESULT IN THE VIOLATION OF LAWS. 
PLEASE USE RESPONSIBLY OR NOT AT ALL.
