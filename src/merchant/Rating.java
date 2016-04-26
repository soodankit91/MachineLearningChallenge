package merchant;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class Rating
{
	HashMap<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, Transaction >>>>> transactionRecord = new HashMap<>(); // to store transaction information
	HashMap<String, HashMap<String, HashMap<String, HashMap<String, OverallRating>>>> completeRecord = new HashMap<>(); // to store information from transaction, profits and cancellation data
	HashMap<String, HashMap<String, HashMap<String, HashMap<String, OverallRating>>>> merchantRating = new HashMap<>(); // to store merchant rating, categorically 
	HashMap<String, Double> finalRating= new HashMap<>();
	
	
	// method to read transactions data
	void readTransaction(String filePath) throws IOException
	{
		BufferedReader br=new BufferedReader(new FileReader(filePath) );
		int itr=0;
		
		
			
		String line=br.readLine();
				
		while((line=br.readLine()) != null)
		{		
			itr++;
			String [] currentTransaction=line.split(",");
			int create, fulfill;
			
			
			
			if(transactionRecord.containsKey(currentTransaction[9])) // check if 'transcationRecord' contains entry for the merchant
			{
				if(transactionRecord.get(currentTransaction[9]).containsKey(currentTransaction[10]) ) // check if 'transcationRecord' contains entry for T1
				{
					if(transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).containsKey(currentTransaction[11])) // check if 'transcationRecord' contains entry for T2
					{
						if(transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).containsKey(currentTransaction[12])) // check if 'transcationRecord' contains entry for T4
						{
							if(transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).containsKey(currentTransaction[13])) // check if 'transcationRecord' contains entry for the product
							{
								int tempQuantity =  transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).get(currentTransaction[13]).quantity;								
								
								transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).get(currentTransaction[13]).quantity +=Integer.parseInt(currentTransaction[2].trim());
								
								create = (int) countDays(currentTransaction[8],currentTransaction[0]); // rating for creating shipment on time
								fulfill= (int) countDays(currentTransaction[7],currentTransaction[1]); // rating for fulfilling shipment on time
								
								
								transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).get(currentTransaction[13]).fullfillmentCreated += (create)*0.5 ;
								transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).get(currentTransaction[13]).fullfillmentShipped += fulfill ;
							}
							else
							{
								
								create = (int) countDays(currentTransaction[8],currentTransaction[0]); // rating for creating shipment on time
								fulfill= (int) countDays(currentTransaction[7],currentTransaction[1]); // rating for fulfilling shipment on time
								
								Transaction newTransaction = new Transaction();
								newTransaction.quantity=Integer.parseInt(currentTransaction[2].trim());
								newTransaction.fullfillmentCreated =  (int) ((create)*0.5) ;
								newTransaction.fullfillmentShipped = (int) fulfill;
								
								
								transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).put(currentTransaction[13], newTransaction);
							}
						}
						else
						{
							transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).put(currentTransaction[12], new HashMap<String, Transaction>());
							create = (int) countDays(currentTransaction[8],currentTransaction[0]); // rating for creating shipment on time
							fulfill= (int) countDays(currentTransaction[7],currentTransaction[1]); // rating for fulfilling shipment on time
							
							Transaction newTransaction = new Transaction();
							newTransaction.quantity=Integer.parseInt(currentTransaction[2].trim());
							newTransaction.fullfillmentCreated =  (int) ((create)*0.5) ;
							newTransaction.fullfillmentShipped = (int) fulfill;
							
							transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).put(currentTransaction[13], newTransaction);
						}
					}
					else
					{
						transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).put(currentTransaction[11], new HashMap<String, HashMap<String, Transaction>>());
						transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).put(currentTransaction[12], new HashMap<String, Transaction>());
						
						create = (int) countDays(currentTransaction[8],currentTransaction[0]); // rating for creating shipment on time
						fulfill= (int) countDays(currentTransaction[7],currentTransaction[1]); // rating for fulfilling shipment on time
						
						Transaction newTransaction = new Transaction();
						newTransaction.quantity=Integer.parseInt(currentTransaction[2].trim());
						newTransaction.fullfillmentCreated =  (int) ((create)*0.5) ;
						newTransaction.fullfillmentShipped = (int) fulfill;
						
						transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).put(currentTransaction[13], newTransaction);
					}
				}
				else
				{
					transactionRecord.get(currentTransaction[9]).put(currentTransaction[10], new HashMap<String, HashMap<String, HashMap<String, Transaction>>>());
					transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).put(currentTransaction[11], new HashMap<String, HashMap<String, Transaction>>());
					transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).put(currentTransaction[12], new HashMap<String, Transaction>());
					create = (int) countDays(currentTransaction[8],currentTransaction[0]); //rating for creating shipment on time
					fulfill= (int) countDays(currentTransaction[7],currentTransaction[1]); // rating for fulfilling shipment on time
					
					Transaction newTransaction = new Transaction();
					newTransaction.quantity=Integer.parseInt(currentTransaction[2].trim());
					newTransaction.fullfillmentCreated =  (int) ((create)*0.5) ;
					newTransaction.fullfillmentShipped = (int) fulfill;
					
					transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).put(currentTransaction[13], newTransaction);
					
				}
			}
			else
			{
				transactionRecord.put(currentTransaction[9], new HashMap<String,HashMap<String, HashMap<String, HashMap<String, Transaction>>>>());
				transactionRecord.get(currentTransaction[9]).put(currentTransaction[10], new HashMap<String, HashMap<String, HashMap<String, Transaction>>>());
				transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).put(currentTransaction[11], new HashMap<String, HashMap<String, Transaction>>());
				transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).put(currentTransaction[12], new HashMap<String, Transaction>());
				
				create = (int) countDays(currentTransaction[8],currentTransaction[0]); // rating for creating shipment on time
				fulfill= (int) countDays(currentTransaction[7],currentTransaction[1]); // rating for fulfilling shipment on time
				
				Transaction newTransaction = new Transaction();
				newTransaction.quantity=Integer.parseInt(currentTransaction[2].trim());
				newTransaction.fullfillmentCreated =  (int) ((create)*0.5) ;
				newTransaction.fullfillmentShipped = (int) fulfill;
				
				transactionRecord.get(currentTransaction[9]).get(currentTransaction[10]).get(currentTransaction[11]).get(currentTransaction[12]).put(currentTransaction[13], newTransaction);
			}
			
		}
		
		br.close();
		
	}
	
	// method to read profits data
	
	void readProfits(String filePath) throws IOException
	{
		BufferedReader br=new BufferedReader(new FileReader(filePath) );
		
		String line= br.readLine();
		
		while((line=br.readLine()) != null)
		{
			String [] currentTransaction=line.split(",");
			
			OverallRating insertRecord = new OverallRating();
			
			insertRecord.commissionPercent=Double.parseDouble(currentTransaction[0])*10; // commission rating is 10 times the commission offered by the merchant
			
			insertRecord.discountPercent = Double.parseDouble(currentTransaction[2])*10; // discount rating is 10 times the discount offered by the merchant
			
			insertRecord.profitRating = insertRecord.discountPercent+ insertRecord.commissionPercent;
			
			//populate data in 'compeleteRecord'
			populateRecords(currentTransaction[6], currentTransaction[5], currentTransaction[4], currentTransaction[3], insertRecord, completeRecord);
			
			
		}
		br.close();
		
	}
	
	// method to read cancellation data 
	void readCancellations(String filePath) throws IOException
	{
		BufferedReader br=new BufferedReader(new FileReader(filePath) );
				
		String line= br.readLine();
		
		while((line=br.readLine()) != null)
		{
			String [] currentTransaction=line.split(",");

			OverallRating insertRecord = new OverallRating();
			insertRecord.cancelNumber=Integer.parseInt(currentTransaction[0])*(-2); // cancellation number is double the negative of number of cancellations
			insertRecord.returnNumber=Integer.parseInt(currentTransaction[1])*(-3); // return number is thrice the negative of the number of returns
			insertRecord.cancelRating = insertRecord.cancelNumber + insertRecord.returnNumber; // cancellation rating is the sum of the above 2
			
			if(completeRecord.containsKey(currentTransaction[2])) // check if 'completeRecord' contains entry for merchant
			{
				if(completeRecord.get(currentTransaction[2]).containsKey(currentTransaction[3])) //check if 'completeRecord' contains entry for merchant & T1
				{
					if(completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).containsKey(currentTransaction[4])) //check if 'completeRecord' contains entry for merchant, T1 & T2
					{
						if(completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).get(currentTransaction[4]).containsKey(currentTransaction[5])) //check if 'completeRecord' contains entry for merchant, T1, T2 & T4
						{
							completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).get(currentTransaction[4]).get(currentTransaction[5]).cancelNumber=Integer.parseInt(currentTransaction[0])*(-2);
							completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).get(currentTransaction[4]).get(currentTransaction[5]).returnNumber=Integer.parseInt(currentTransaction[1])*(-3);
							insertRecord.cancelRating = insertRecord.cancelNumber + insertRecord.returnNumber;
						}
						else
						{
							completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).get(currentTransaction[4]).put(currentTransaction[5], insertRecord);
						}
					}
					else
					{
						completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).put(currentTransaction[4], new HashMap<String, OverallRating>());
						completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).get(currentTransaction[4]).put(currentTransaction[5], insertRecord);
					}
				}
				else
				{
					completeRecord.get(currentTransaction[2]).put(currentTransaction[3], new HashMap<String, HashMap<String, OverallRating>>());
					completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).put(currentTransaction[4], new HashMap<String, OverallRating>());
					completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).get(currentTransaction[4]).put(currentTransaction[5], insertRecord);
				}
			}
			else
			{
				completeRecord.put(currentTransaction[2], new HashMap<String, HashMap<String, HashMap<String, OverallRating>>>());
				completeRecord.get(currentTransaction[2]).put(currentTransaction[3], new HashMap<String, HashMap<String, OverallRating>>());
				completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).put(currentTransaction[4], new HashMap<String, OverallRating>());
				completeRecord.get(currentTransaction[2]).get(currentTransaction[3]).get(currentTransaction[4]).put(currentTransaction[5], insertRecord);
			}	
			
		}
	}
	
	// method to count the number of days passed
	
	long countDays(String inputString1 , String inputString2)
	{
		if(inputString1.equals(null) || inputString2.equals(null))
			return 0;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		long NoOfDays=0;

		try
		{
		    Date date1 = format.parse(inputString1);
		    Date date2 = format.parse(inputString2);
		     long diff = date2.getTime() - date1.getTime();
		     NoOfDays =TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);		     
		  
		} 
		catch (ParseException e)
		{
			return 0;
		}
		
		if(NoOfDays <0 )
			return NoOfDays;
		
		else
			return 0;
		
	}
	
	// common method to populate 'completeRecord' and 'merchantRating'
	
	void populateRecords(String str1, String str2, String str3, String str4, OverallRating rating, HashMap<String, HashMap<String, HashMap<String, HashMap<String, OverallRating>>>> ratingRecord)
	{
		if(ratingRecord.containsKey(str4)) //check if 'ratingRecord' contains entry for str4
		{
			if(ratingRecord.get(str4).containsKey(str3)) //check if 'ratingRecord' contains entry for str4 & str3
			{
				if(ratingRecord.get(str4).get(str3).containsKey(str2)) //check if 'ratingRecord' contains entry for str4, str3 & str2
				{
					ratingRecord.get(str4).get(str3).get(str2).put(str1, rating);
				}
				else
				{
					ratingRecord.get(str4).get(str3).put(str2, new HashMap<String, OverallRating>());
					ratingRecord.get(str4).get(str3).get(str2).put(str1, rating);					
				}
			}
			else
			{
				ratingRecord.get(str4).put(str3, new HashMap<String, HashMap<String, OverallRating>>());
				ratingRecord.get(str4).get(str3).put(str2, new HashMap<String, OverallRating>());
				ratingRecord.get(str4).get(str3).get(str2).put(str1, rating);
			}
			
		}
		else
		{
			ratingRecord.put(str4 , new HashMap<String,  HashMap<String,  HashMap<String, OverallRating>>>());
			ratingRecord.get(str4).put(str3, new HashMap<String, HashMap<String, OverallRating>>());
			ratingRecord.get(str4).get(str3).put(str2, new HashMap<String, OverallRating>());
			ratingRecord.get(str4).get(str3).get(str2).put(str1, rating);
		}
	}
	
	//method to return rating for a particular merchant
	OverallRating getRating(String merchant, String T1, String T2, String T4)
	{
		return completeRecord.get(merchant).get(T1).get(T2).get(T4);
	}
	
	// method to merge data from 'transactionRecord' to 'completeRecord' 
	void mergeData()
	{
		OverallRating overall = new OverallRating();
		
		for(Entry<String, HashMap<String, HashMap<String, HashMap<String, HashMap<String, Transaction>>>>> t1:transactionRecord.entrySet())
		{
			String key1 = t1.getKey();
			
			for (Entry<String, HashMap<String, HashMap<String, HashMap<String, Transaction>>>> t2 : t1.getValue().entrySet())
			{
				String key2 = t2.getKey();
				
				for (Entry<String, HashMap<String, HashMap<String, Transaction>>> t3 : t2.getValue().entrySet())
				{
					String key3=t3.getKey();
					
					for (Entry<String, HashMap<String, Transaction>> t4 : t3.getValue().entrySet())
					{
						String key4 = t4.getKey();
						
						double totalTransactionRating =0;
						int totalQuantity=0;
						for (Entry<String, Transaction> t5 : t4.getValue().entrySet())
						{							
							totalTransactionRating +=t5.getValue().fullfillmentCreated+t5.getValue().fullfillmentShipped;
							totalQuantity += t5.getValue().quantity;
						}
						
						overall=completeRecord.get(key1).get(key2).get(key3).get(key4);
						overall.transactionRating=totalTransactionRating;
						overall.quantity = totalQuantity;
						overall.totalRating = overall.cancelRating + overall.profitRating + overall.transactionRating;
						completeRecord.get(key1).get(key2).get(key3).put(key4, overall);
						populateRecords(key1,key4,key3,key2, overall, merchantRating);
																		
					}
				}
			}
		}
	}
	
	// method to print the information stored in 'completeRecord'
	void printResults(String filePath) throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter(filePath);
		double rate;
		
		for(Entry<String, HashMap<String, HashMap<String, HashMap<String, OverallRating>>>> t1:completeRecord.entrySet())
		{
			rate=0;
			String key1 = t1.getKey();
			out.println("merchant id= "+ key1);
			for (Entry<String, HashMap<String, HashMap<String, OverallRating>>> t2 : t1.getValue().entrySet())
			{
				String key2 = t2.getKey();
				out.println("T1= "+ key2);
				for (Entry<String, HashMap<String, OverallRating>> t3 : t2.getValue().entrySet())
				{
					String key3=t3.getKey();
					out.println("T2= "+ key3);
					for (Entry<String, OverallRating> t4 : t3.getValue().entrySet())
					{						
						out.println("transcation= "+t4.getValue().transactionRating+" profit= "+t4.getValue().profitRating+" cancel= "+t4.getValue().cancelRating+" total= "+t4.getValue().totalRating);
						rate += t4.getValue().totalRating;
					}
				}
			}
			finalRating.put(key1, rate);
		}
		
	}	
	void printMerchantRating(String filePath) throws FileNotFoundException
	{
		PrintWriter out = new PrintWriter(filePath);
		
		for(Entry<String, Double> t1: finalRating.entrySet() )
		{			
			out.println(" for merchant "+ t1.getKey()+" rating is "+t1.getValue());
		}
	}
	public static void main(String args[]) throws IOException
	{
		Rating rating=new Rating();
		rating.readTransaction(args[0]);
		
		rating.readProfits(args[1]);
		rating.readCancellations(args[2]);
				
		rating.mergeData();
		rating.printResults(args[3]);
		rating.printMerchantRating(args[4]);
		
	}

}
