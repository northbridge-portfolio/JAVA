import java.io.IOException;
import java.net.*;
import java.util.*;


/**
 *  @author NorthBridge
 *  @version 1.2
 *  Java SDK 1.8
 *  Copyright 2017 - NorthBridge - All Rights Reserved
 *  
 *  This Java class enumerates over both a machine IPv4 range and Port Range (0-65535) looking for open ports.
 *  If a port is open, the machine IP and and port number are logged to the PortScanner.reportList and printed
 *  at the end of the scan.
 * 
 *  To run this program from the command line use the following arguments:
 *  $>java PortScanner host:A.A.A.A port:XX
 *  where: A.A.A.A is the target machine's IPv4 address
 *         XX is the target machine's port number
 *             
 *  OR to specify a range including multiple machines and port numbers, use:
 * 
 *  $>java PortScanner host:A.A.A.A-B.B.B.B port:XX-YY
 *  where: A.A.A.A is the STARTING target machine's IPv4 address
 *         B.B.B.B is the ENGING target machine's IPv4 address
 *         XX is the target machine's port number
 *         YY is the target machine's port number
 *         
 *  WARNING: FOR LEARNING PURPOSES ONLY. USE OF THIS PROGRAM ON COMPUTERS/MACHINES/EQUIPMENT NOT YOUR OWN MAY RESULT 
 *  		 IN THE VIOLATION OF LAWS. PLEASE USE RESPONSIBLY OR NOT AT ALL.
 * 
 */


/**
 * General Constructor
 */
class PortScanner
{
	private String startIP;
	private String finishIP;
	private int startPort;
	private int finishPort;
	private ArrayList<String[]> reportList = new ArrayList<String[]>();

	// General Constructor
	public PortScanner()
	{
		startIP = null;
		finishIP = null;
		startPort = 0;
		finishPort = 0;
		reportList = new ArrayList<String[]>();
	}
	
	
	/**
	 * Main method: see argument list in file header documentation or readme.txt
	 * 
	 * @param  args  program arguments including target ip/range and port number/range
	 * @return void  outputs to console
	 */
	public static void main(String[] args)
	{
		PortScanner scanner = new PortScanner();
		if(scanner.parseArgs(args))
		{
			System.out.println("ARGUMENT PARSING SUCCESSFUL...");
			System.out.println("REMOTE MACHINE START IP: " + scanner.startIP);
			System.out.println("REMOTE MACHINE FINISH IP: " + scanner.finishIP);
			System.out.println("REMOTE MACHINE START PORT: " + scanner.startPort);
			System.out.println("REMOTE MACHINE FINISH PORT: " + scanner.finishPort);
			try
			{
				scanner.startScan();
			}
			catch(UnknownHostException e)
			{
				System.out.println("An UnknownHostException has occurred.");
			}
			catch(IOException e)
			{
				System.out.println("An IOException has occurred.");
			}
		}
	}
	
	/**
	 * Runs a while loop enumerating over the specified machines and ports until the condition is reached.
	 * Relies on a hard break to exit loop.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException           indicates no port found.
	 * @param  none
	 * @return void                  outputs directly to the console.
	 */
	private void startScan() throws UnknownHostException, IOException
	{
		String currentIP = this.startIP;
		int currentPort = this.startPort;
		System.out.println("Commening Scan...");
		
		// Adjust finish IP Address and port number for use in While loop
		finishIP = getNextAddress(finishIP);
		finishPort = finishPort + 1;
		
		while((currentIP.compareTo(finishIP) != 0) && (currentIP != null))
		{
			while(currentPort < finishPort)
			{
				System.out.print("Scanning IP: " + currentIP + " on Port: " + currentPort + "...");
				InetSocketAddress currentMachine = new InetSocketAddress(InetAddress.getByName(currentIP), 
																		 currentPort);
				Socket sock = new Socket();
				try
				{
					sock.connect(currentMachine, 200);	
					System.out.print(" Port OPEN");
					String[] entry = {currentIP, Integer.toString(currentPort)};
					reportList.add(entry);
				}
				catch(UnknownHostException e)
				{
					System.out.println("An UnknownHostException has occurred.");
				}
				catch(IOException e)
				{
					System.out.print(" Port CLOSED");
				}
				finally
				{
					System.out.println("  Closing Socket.");
					sock.close();
					currentPort++;
				}
			}
			currentIP = getNextAddress(currentIP);
		}
		
		System.out.println("Scan Complete.");
		if(reportList.size() > 0)
		{
			System.out.println("Printing Report...");
			for(int i = 0; i < reportList.size(); i++)
				System.out.println("MACHINE: " + reportList.get(i)[0] + "  -  PORT " + reportList.get(0)[1] + " is OPEN.");
		}
		else
			System.out.println("No OPEN ports found on any machines.");
	}
	
	
	/**
	 * Takes in console arguments and ensures both that the IP is valid (range/value). Assigns these values directly
	 * to the class data members.
	 * 
	 * @param  args     from the command line arguments.
	 * @return boolean  boolean indicating if the parsing was successful or if there was an error. 
	 */
	private boolean parseArgs(String[] args)
	{
		startIP = null;
		finishIP = null;
		startPort = -1;
		finishPort = -1;
		
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].substring(0, 5).toLowerCase().compareTo("host:") == 0)
			{
				if(args[i].indexOf("-") == -1)
				{
					args[i] = args[i].substring(5, args[i].length());
					if(checkIPAddress(new String[]{args[i]}))
					{
						try
						{
							startIP = args[i];
							finishIP = args[i];
						}
						catch(Exception NumberFormatError)
						{
							System.out.println("Number Format Error: IP Address.");
						}
					}
					else
						System.out.println("ERROR: Invalid IP Address. Please try again.");
				}
				else if(args[i].indexOf("-") != -1)
				{
					args[i] = args[i].substring(5, args[i].length());
					String[] ipRange = args[i].split("-");
					if(ipRange.length == 2)
					{
						System.out.println(ipRange[0]);
						System.out.println(ipRange[1]);
						if((checkIPAddress(new String[]{ipRange[0]}))
							&& (checkIPAddress(new String[]{ipRange[1]})))
						{
							try
							{
								startIP = ipRange[0];
								finishIP = ipRange[1];
							}
							catch(Exception NumberFormatError)
							{
								System.out.println("Number Format Error: IP Address.");
							}
						}
						else
							System.out.println("ERROR: Invalid IP Address Format. Please try again.");
					}
					else
						System.out.println("ERROR: Invalid IP Address Range Format. Please try again.");
				}
				else
					System.out.println("Unknown Exception.");
			}
			else if(args[i].substring(0, 5).toLowerCase().compareTo("port:") == 0)
			{
				// Strip 'port:' from args string
				args[i] = args[i].substring(5, args[i].length());
				// Single port assignment
				if(args[i].indexOf("-") == -1)
				{
					int port_num = Integer.parseInt(args[i]);
					if((port_num >= 0) && (port_num <= 65535))
					{
						startPort = port_num;
						finishPort = port_num;
					}
					else
						System.out.println("Number Format Error: Port Range.");
				}
				// Multiple port assignment
				else if(args[i].indexOf("-") != -1)
				{
					String[] portArray = args[i].split("-");
					if(portArray.length == 2)
					{
						try
						{
							if(((Integer.parseInt(portArray[0]) >= 0) && (Integer.parseInt(portArray[0]) <= 65535)) &&
							  ((Integer.parseInt(portArray[1]) >= 0) && (Integer.parseInt(portArray[1]) <= 65535)))
							{
								startPort = Integer.parseInt(portArray[0]);
								finishPort = Integer.parseInt(portArray[1]);
							}
							else
								System.out.println("Number Format Error: Port Range.");
								
						}
						catch(Exception NumberFormatError)
						{
							System.out.println("Number Format Error: Port Range.");
						}
					}
					else
						System.out.println("Invalid port range format.");
				}
			}
		}
		
		if((startIP != null) && (finishIP != null) && (startPort != -1) && (finishPort != -1))
			return true;
		else
			return false;
	}
	
	
	/**
	 * Validates an IPv4 address for 4 octests and valid numerical ranges (0-255) for each octet.
	 * 
	 * @param  args_array  string representation of IPv4 address or multiple if a range is provided.
	 * @return boolean     boolean value indicating if the provided string is a valid IPv4 address.
	 */
	private boolean checkIPAddress(String[] args_array)
	{
		for(int i = 0; i < args_array.length; i++)
		{
			String[] ip_address = args_array[i].split("\\.");
			// Check potential ip address has 4 octets (=4 indexes)
			if(ip_address.length == 4)
			{
				for(int j = 0; j < ip_address.length; j++)
				{
					int ip_octet = Integer.parseInt(ip_address[j]);
					// Check octet range is between 0-255
					if((ip_octet >= 0) && (ip_octet <= 255))
					{
						return true;
					}
					else
					{
						System.out.println("ERROR: Invalid octet range. Must be between 0-255");
						return false;
					}
				}
			}
			else
			{
				System.out.println("ERROR: IPv4 requires 4 octects.");
				return false;
			}
		}
		return false;
	}

	
	/**
	 * This method returns the next numerically incremented IPv4 address in the range of (0.0.0.0-255.255.255.255.255)
	 * Example: 1.1.1.1 will return 1.1.1.2
	 *          192.168.0.2 will return 192.168.0.3
	 *          217.0.255.255 will return 217.1.0.0
	 * 
	 * @param  ipAddress  string representation of the address to get the next value for.
	 * @return String     string representation of the next address after the one provided in the arguments.
	 */
	private String getNextAddress(String ipAddress)
	{
		String[] ipArray = ipAddress.split("\\.");
		
		if(ipArray.length == 4)
		{
			for(int i = ipArray.length - 1; i >= 0; i--)
			{				
				try
				{
					int intOctet = Integer.parseInt(ipArray[i]);
					if((intOctet >= 0) && (intOctet < 255))
					{
						intOctet++;
						ipArray[i] = Integer.toString(intOctet);
						return String.join(".", ipArray);
					}
					else if(intOctet == 255)
					{
						if(i == 0)
							return null;
						else
							ipArray[i] = "0";
					}
					else
					{
						System.out.println("Above 255");
						return null;
					}
				}
				catch(Exception NumberFormatException)
				{
					System.out.println("Number Format Exception. There is an error in the IP address format.");
					return null;
				}
			}
		}
		else
			System.out.println("Invalid IP Address. Must contain 4 octets.");
		return null;
	}
}

/* Copyright 2017 - NorthBridge - All Rights Reserved */