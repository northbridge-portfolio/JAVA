/**
 * 
 * @author NorthBridge
 * @version 1.2
 * Java SDK 1.8
 * Copyright 2017 - NorthBridge - All Rights Reserved
 *
 * This exercise examines the passenger manifest data for the 1918 Titanic disaster, in order to predict who will
 * survive based on information such as age, gender, social status, cabin location, etc. Please see the Titanic.java 
 * header for more general information.
 * 
 * This class handles data parsing, cleansing, replacement and preparation of the CSV data files contained in 
 * /DATA_FILES/. This class contains two methods, prepTrainingData() and prepTestingData(), which both return
 * triple nested arrays of type double, as required by the Encog Framework. 
 *
 */

import java.lang.*;
import java.io.*;
import java.util.*;

class TitanicDataCleanser
{
	
	/**
	 * This function prepares the training data. Also averages the passenger age and fare, separately based on gender.
	 * This exercise uses binary classifiers (one-hot encoding) for most inputs, using either a 0 or 1.
	 * 
	 * @param    none
	 * @return   nested array of type double, containing both the input and output (ideal) data.
	 *           [input/output][index][data set]
	 */		 
	public double[][][] prepTrainingData()
	{
		System.out.println(System.getProperty("user.dir"));
		HashMap<String, Integer> column_map = new HashMap<String, Integer>();
		column_map.put("survived", 1);
		column_map.put("class", 2);
		column_map.put("sex", 4);
		column_map.put("age", 5);
		column_map.put("siblings", 6);
		column_map.put("par_child", 7);
		column_map.put("fare", 9);
		column_map.put("embarked", 11);
		
		System.out.println("BEGIN PREPARATION OF TRAINING DATA...");
		try
		{
			String line;
			double age_max = 0;
			double fare_max = 0;
			ArrayList<Double> fare_denormalized = new ArrayList<Double>();
			ArrayList<Double> age_denormalized = new ArrayList<Double>();
			double age_sum_female = 0.0;
			double age_sum_male = 0.0;
			int age_count = 0;
			int female_count = 0;
			int male_count = 0;
			ArrayList<Double> answer_array = new ArrayList<Double>();
			ArrayList<ArrayList<Double>> training_array = new ArrayList<ArrayList<Double>>();
			
			BufferedReader br = new BufferedReader(new FileReader("C:\\LOCAL\\ECLIPSE PROJECTS\\Titanic\\DATA_FILES\\train.csv"));
			if(br.ready())
			{
				br.readLine();
				while((line = br.readLine()) != null)
				{		
					ArrayList<String> line_raw = new ArrayList<String>(Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")));
					ArrayList<Double> line_clean = new ArrayList<Double>();
						
					for(int i = 0; i < line_raw.size(); i++)
						System.out.print(line_raw.get(i)+" ");
					System.out.println();
					
					/* TRAINING: Passenger Class */
					if(line_raw.get(column_map.get("class")).compareTo("1") == 0)
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					
					if(line_raw.get(column_map.get("class")).compareTo("2") == 0)
					{
						line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);
					}
					
					if(line_raw.get(column_map.get("class")).compareTo("3") == 0)
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);
					}
					
					
					/* TRAINING: PASSENGER SEX */
					int[] sex_array = new int[1];
					if(line_raw.get(column_map.get("sex")).compareTo("male") == 0)
					{
						line_clean.add(1.0);
					}
					
					else if(line_raw.get(column_map.get("sex")).compareTo("female") == 0)
					{
						line_clean.add(0.0);
					}
					else
					{
						System.out.println("SEX ERROR");
						System.out.println(line_raw.get(column_map.get("sex")));
						return null;
					}
										
					
					/* TRAINING: Age (to be normalized before use) */
					if(line_raw.get(column_map.get("age")).compareTo("") != 0)
					{
						double current_age = Double.valueOf(line_raw.get(column_map.get("age")));
						if(line_raw.get(column_map.get("sex")).compareTo("male") == 0)
						{
							if(current_age > age_max)
								age_max = current_age;
							age_sum_male += current_age;
							male_count++;
						}
						else if(line_raw.get(column_map.get("sex")).compareTo("female") == 0)
						{
							if(current_age > age_max)
								age_max = current_age;
							age_sum_female += current_age;
							female_count++;
						}
						else
							System.out.println("AGE ERROR!!!");

						age_count++;
						age_denormalized.add(current_age);
					}
					else
					{
						age_denormalized.add(-1.0);
						System.out.println("MISSING AGE: "+line_raw.get(column_map.get("age")));
					}
					
					/* TRAINING: Siblings */
					if(line_raw.get(column_map.get("siblings")) == "0")
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("siblings")) == "1")
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("siblings")) == "2")
					{
						line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("siblings")) == "3")
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("siblings")) == "4")
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);
					}
					else
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);
					}
					
					
					/* TRAINING: Parents / Children - 0, 1, 2 or more */
					if(line_raw.get(column_map.get("par_child")) == "0")
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("par_child")) == "1")
					{
						line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("par_child")) == "2")
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);
					}
					else // 3 OR MORE
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);
					}
					
					
					/* TRAINING: Fare (to be normalized before use) */
					if(line_raw.get(column_map.get("fare")) != "")
					{
						/*double denormalized_value = Double.valueOf(line_raw.get(column_map.get("fare")));
						double normalized_value = denormalized_value / (1 + Math.abs(denormalized_value));
						line_clean.add(normalized_value);*/
						fare_denormalized.add(Double.valueOf(line_raw.get(column_map.get("fare"))));
						if(Double.valueOf(line_raw.get(column_map.get("fare"))) > fare_max)
							fare_max = Double.valueOf(line_raw.get(column_map.get("fare")));
					}
					else
					{
						// fare_denormalized.add(0.0);
						/*System.out.println("TESTING DATA PREPERATION ERROR: Fare");
						return null;*/
					}
					
					
					/* TRAINING: Embarked */
					if(line_raw.get(column_map.get("embarked")).compareTo("C") == 0)
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("embarked")).compareTo("Q") == 0)
					{
						line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("embarked")).compareTo("S") == 0)
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);
					}
					else
					{
						line_clean.add(0.0);
						line_clean.add(0.0);
						line_clean.add(1.0);
					}
					
					
					if(line_raw.get(column_map.get("survived")).compareTo("0") == 0)
					{
						answer_array.add(0.0);
					}
					else if(line_raw.get(column_map.get("survived")).compareTo("1") == 0)
					{
						answer_array.add(1.0);
					}
					else
					{
						System.out.println("TESTING DATA PREPERATION ERROR: Survivor");
						System.out.println(line_raw.get(column_map.get("survived")));
						return null;
					}
					training_array.add(line_clean);	
				}
			}
			br.close();
			
			/* NORTMALIZE AGE AND REPLACE MISSING VALUES WITH AVERAGE DEPEDNING ON MALE OR FEMALE */
			br = new BufferedReader(new FileReader("C:\\LOCAL\\ECLIPSE PROJECTS\\Titanic\\DATA_FILES\\train.csv"));
			if(br.ready())
			{
				br.readLine();
				for(int i = 0; i < age_denormalized.size(); i++)
				{
					line = br.readLine();
					ArrayList<String> line_raw = new ArrayList<String>(Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")));
					double age_normalized = -1000000.0;
					if(age_denormalized.get(i).compareTo(-1.0) != 0)
					{
						age_normalized = (age_denormalized.get(i)) / (age_max);
					}
					else if(age_denormalized.get(i).compareTo(-1.0) == 0)
					{
						if(line_raw.get(column_map.get("sex")).compareTo("male") == 0)
						{
							
							double mean_age_male = age_sum_male / male_count;
							age_normalized = mean_age_male / age_max;
						}
						else if(line_raw.get(column_map.get("sex")).compareTo("female") == 0)
						{
							double mean_age_female = age_sum_female / female_count;
							age_normalized = mean_age_female / age_max;
						}
						else
						{
							System.out.println("TRAINING DATA PREPERATION ERROR: Age Normalization 1");
							return null;
						}
					}
					else
					{
						System.out.println("TRAINING DATA PREPERATION ERROR: Age Normalization 2");
						return null;
					}
					training_array.get(i).add(age_normalized);
				}
				br.close();
			}
			else
			{
				System.out.println("TRAINING: File Open Error");
				return null;
			}
			
			for(int i = 0; i < fare_denormalized.size(); i++)
			{
				double fare_normalized = (fare_denormalized.get(i)) / (fare_max); 
				training_array.get(i).add(fare_normalized);
			}
			
			/* TRANSFER ARRAYLIST TO DOUBLE[][] FOR ENCOG */
			double[][][] return_array = new double[2][training_array.size()][training_array.get(0).size()];
			for(int i = 0; i < training_array.size(); i++)
			{
				for(int j = 0; j < training_array.get(i).size(); j++)
				{
					return_array[0][i][j] = training_array.get(i).get(j);
					System.out.print(return_array[0][i][j]+"  ");					
				}
				System.out.println();
			}
			
			for(int i = 0; i < answer_array.size(); i++)
			{
				return_array[1][i][0] = answer_array.get(i);
			}
			
			// PRINT TRAINING ARRAY
			/*System.out.println("TRAINING ARRAY:");
			for(int i = 0; i < return_array[0].length; i++)
			{
				for(int j = 0; j < return_array[0][i].length; j++)
				{
					System.out.print(return_array[0][i][j]+"  ");
					System.out.println();					
				}
			}
			
			// PRINT ANSWER ARRAY
			System.out.println("ANSWER ARRAY:");
			for(int i = 0; i < return_array[1].length; i++)
			{
				System.out.println(return_array[1][i][0]+"  ");
				// System.out.println();
			}*/
			
			return return_array;
		}
		catch(Exception e)
		{
			System.out.println("FILE NOT FOUND!!!");
			return null;
		}
	}
	
	
	/* ============================================================================================================
	   ============================================================================================================ */
	

	/**
	 * This function prepares the testing data. Also averages the passenger age and fare, separately based on gender.
	 * This exercise uses binary classifiers (one-hot encoding) for most inputs, using either a 0 or 1.
	 * 
	 * @param    none
	 * @return   nested array of type double, containing both the input and output (ideal) data.
	 *           [input/output][index][data set]
	 *
	 */
	public double[][][] prepTestingData()
	{
		System.out.println(System.getProperty("user.dir"));
		HashMap<String, Integer> column_map = new HashMap<String, Integer>();
		column_map.put("class", 1);
		column_map.put("sex", 3);
		column_map.put("age", 4);
		column_map.put("siblings", 5);
		column_map.put("par_child", 6);
		column_map.put("fare", 8);
		column_map.put("embarked", 10);
		
		System.out.println("BEGIN PREPARATION OF TESTING DATA...");
		try
		{
			String line;
			double age_max = 0;
			double fare_max = 0;
			ArrayList<Double> fare_denormalized = new ArrayList<Double>();
			ArrayList<Double> age_denormalized = new ArrayList<Double>();
			double age_sum_female = 0.0;
			double age_sum_male = 0.0;
			int age_count = 0;
			int female_count = 0;
			int male_count = 0;
			double fare_sum = 0;
			int fare_count = 0;
			ArrayList<Double> answer_array = new ArrayList<Double>();
			ArrayList<ArrayList<Double>> testing_input_array = new ArrayList<ArrayList<Double>>();
			
			BufferedReader br = new BufferedReader(new FileReader("C:\\LOCAL\\ECLIPSE PROJECTS\\Titanic\\DATA_FILES\\test.csv"));
			if(br.ready())
			{
				br.readLine();
				while((line = br.readLine()) != null)
				{							
					ArrayList<String> line_raw = new ArrayList<String>(Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")));
					ArrayList<Double> line_clean = new ArrayList<Double>();
					
					System.out.println(line_raw);
					for(int i = 0; i < line_raw.size(); i++)
						System.out.print(line_raw.get(i)+" ");
	
					// PASSENGER CLASS
					if(line_raw.get(column_map.get("class")).compareTo("1") == 0)
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					
					if(line_raw.get(column_map.get("class")).compareTo("2") == 0)
					{
						line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);
					}
					
					if(line_raw.get(column_map.get("class")).compareTo("3") == 0)
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);
					}
					
					
					/* SEX */
					int[] sex_array = new int[1];
					if(line_raw.get(column_map.get("sex")).compareTo("male") == 0)
					{
						line_clean.add(1.0);
					}
					
					else if(line_raw.get(column_map.get("sex")).compareTo("female") == 0)
					{
						line_clean.add(0.0);
					}
					else
					{
						System.out.println("TESTING DATA PREPERATION ERROR: Sex");
						System.out.println(line_raw.get(column_map.get("sex")));
						return null;
					}
										
					
					/* AGE: WILL BE NORMALIZED BEFORE USING (this is the purpose for calculating average)*/
					if(line_raw.get(column_map.get("age")).compareTo("") != 0)
					{
						double current_age = Double.valueOf(line_raw.get(column_map.get("age")));
						if(line_raw.get(column_map.get("sex")).compareTo("male") == 0)
						{
							if(current_age > age_max)
								age_max = current_age;
							age_sum_male += current_age;
							male_count++;
						}
						else if(line_raw.get(column_map.get("sex")).compareTo("female") == 0)
						{
							if(current_age > age_max)
								age_max = current_age;
							age_sum_female += current_age;
							female_count++;
						}
						else
							System.out.println("TESTING DATA PREPERATION ERROR: Age");

						age_count++;
						age_denormalized.add(current_age);
					}
					else  // MISING VALUE FOR AGE
					{
						age_denormalized.add(-1.0);
						System.out.println("MISSING AGE: "+line_raw.get(column_map.get("age")));
					}
					
					/* SIBLINGS */
					if(line_raw.get(column_map.get("siblings")) == "0")
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("siblings")) == "1")
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("siblings")) == "2")
					{
						line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("siblings")) == "3")
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("siblings")) == "4")
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);
					}
					else
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);
					}
					
					
					/* PARENTS / CHILDREN: 0, 1, 2 or more */
					if(line_raw.get(column_map.get("par_child")) == "0")
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("par_child")) == "1")
					{
						line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("par_child")) == "2")
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);
					}
					else // 3 OR MORE
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);
					}
					
					
					/* FARE: TO BE NORMALIZED BEFORE USE (this is the purpose of calculating average) */
					if(line_raw.get(column_map.get("fare")).compareTo("") != 0)
					{
						/*double denormalized_value = Double.valueOf(line_raw.get(column_map.get("fare")));
						double normalized_value = denormalized_value / (1 + Math.abs(denormalized_value));
						line_clean.add(normalized_value);*/
						fare_denormalized.add(Double.valueOf(line_raw.get(column_map.get("fare"))));
						if(Double.valueOf(line_raw.get(column_map.get("fare"))) > fare_max)
							fare_max = Double.valueOf(line_raw.get(column_map.get("fare")));
						fare_sum = Double.valueOf(line_raw.get(column_map.get("fare")));
						fare_count++;
					}
					else
					{
						/*System.out.println("TESTING DATA PREPERATION ERROR: Fare");
						return null;*/
					}
					
					
					/* EMBARKED */
					if(line_raw.get(column_map.get("embarked")).compareTo("C") == 0)
					{
						line_clean.add(1.0);	line_clean.add(0.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("embarked")).compareTo("Q") == 0)
					{
						line_clean.add(0.0);	line_clean.add(1.0);	line_clean.add(0.0);
					}
					else if(line_raw.get(column_map.get("embarked")).compareTo("S") == 0)
					{
						line_clean.add(0.0);	line_clean.add(0.0);	line_clean.add(1.0);
					}
					else
					{
						line_clean.add(0.0);
						line_clean.add(0.0);
						line_clean.add(1.0);
					}
					testing_input_array.add(line_clean);	
				}
			}
			br.close();
			System.out.println("TESTING FILE: Closed");
			
			/* COMPUTE AVERAGE OF MALE AND FEMALE PASSENGERS TO REPLACE MISSING VALUES */
			/* NORTMALIZE AGE AND REPLACE MISSING VALUES WITH AVERAGE DEPEDNING ON MALE OR FEMALE */
			br = new BufferedReader(new FileReader("C:\\LOCAL\\ECLIPSE PROJECTS\\Titanic\\DATA_FILES\\test.csv"));
			if(br.ready())
			{
				br.readLine();
				for(int i = 0; i < age_denormalized.size(); i++)
				{
					line = br.readLine();
					ArrayList<String> line_raw = new ArrayList<String>(Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")));
					double age_normalized = -1000000.0;
					if(age_denormalized.get(i).compareTo(-1.0) != 0)
					{
						age_normalized = (age_denormalized.get(i)) / (age_max);
					}
					else if(age_denormalized.get(i).compareTo(-1.0) == 0)
					{
						if(line_raw.get(column_map.get("sex")).compareTo("male") == 0)
						{
							double mean_age_male = age_sum_male / male_count;
							age_normalized = mean_age_male / age_max;
						}
						else if(line_raw.get(column_map.get("sex")).compareTo("female") == 0)
						{
							double mean_age_female = age_sum_female / female_count;
							age_normalized = mean_age_female / age_max;
						}
						else
						{
							System.out.println("TESTING DATA PREPERATION ERROR: Normalize Age 1");
							return null;
						}
					}
					else
					{
						System.out.println("TESTING DATA PREPERATION ERROR: Normalize Age 2");
						return null;
					}
					testing_input_array.get(i).add(age_normalized);
				}
				br.close();
			}
			else
			{
				System.out.println("TESTING DATA PREPERATION ERROR: File Open Error");
				return null;
			}
			
			for(int i = 0; i < fare_denormalized.size(); i++)
			{
				double fare_normalized = (fare_denormalized.get(i)) / (fare_max); 
				testing_input_array.get(i).add(fare_normalized);
			}
			
			/* TRANSFER ARRAYLIST TO DOUBLE[][] FOR ENCOG */
			double[][][] return_array = new double[2][testing_input_array.size()][testing_input_array.get(0).size()];
			for(int i = 0; i < testing_input_array.size(); i++)
			{
				for(int j = 0; j < testing_input_array.get(i).size(); j++)
				{
					return_array[0][i][j] = testing_input_array.get(i).get(j);
					System.out.print(return_array[0][i][j]+"  ");					
				}
				System.out.println();
			}
			
			br = new BufferedReader(new FileReader("C:\\LOCAL\\ECLIPSE PROJECTS\\Titanic\\DATA_FILES\\genderclassmodel.csv"));
			if(br.ready())
			{
				int index = 0;
				br.readLine();
				while((line = br.readLine()) != null)
				{
					ArrayList<String> line_raw = new ArrayList<String>(Arrays.asList(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")));
					return_array[1][index][0] = Double.valueOf(line_raw.get(1));
					index++;
				}
				br.close();
			}
			
			
			// PRINT TESTING INPUT ARRAY
			System.out.println("TESTING INPUT ARRAY:");
			for(int i = 0; i < return_array[0].length; i++)
			{
				for(int j = 0; j < return_array[0][i].length; j++)
				{
					System.out.print(return_array[0][i][j]+"  ");
					// System.out.println();					
				}
			}
			
			// PRINT TESTING IDEAL ARRAY
			System.out.println("TESTING IDEAL ARRAY:");
			for(int i = 0; i < return_array[1].length; i++)
			{
				System.out.println(return_array[1][i][0]+"  ");
			}

			return return_array;
		}
		catch(Exception e)
		{
			System.out.println("TESTING DATA PREPERATION ERROR: File Not Found");
			return null;
		}
	}	
}


/* Copyright 2017 - NorthBridge - All Rights Reserved */


/*


VARIABLE DESCRIPTIONS:
survival        Survival
                (0 = No; 1 = Yes)
pclass          Passenger Class
                (1 = 1st; 2 = 2nd; 3 = 3rd)
name            Name
sex             Sex
age             Age
sibsp           Number of Siblings/Spouses Aboard
parch           Number of Parents/Children Aboard
ticket          Ticket Number
fare            Passenger Fare
cabin           Cabin
embarked        Port of Embarkation
                (C = Cherbourg; Q = Queenstown; S = Southampton)

SPECIAL NOTES:
Pclass is a proxy for socio-economic status (SES)
 1st ~ Upper; 2nd ~ Middle; 3rd ~ Lower

Age is in Years; Fractional if Age less than One (1)
 If the Age is Estimated, it is in the form xx.5

With respect to the family relation variables (i.e. sibsp and parch)
some relations were ignored.  The following are the definitions used
for sibsp and parch.

Sibling:  Brother, Sister, Stepbrother, or Stepsister of Passenger Aboard Titanic
Spouse:   Husband or Wife of Passenger Aboard Titanic (Mistresses and Fiances Ignored)
Parent:   Mother or Father of Passenger Aboard Titanic
Child:    Son, Daughter, Stepson, or Stepdaughter of Passenger Aboard Titanic

Other family relatives excluded from this study include cousins,
nephews/nieces, aunts/uncles, and in-laws.  Some children travelled
only with a nanny, therefore parch=0 for them.  As well, some
travelled with very close friends or neighbors in a village, however,
the definitions do not support such relations.


*/