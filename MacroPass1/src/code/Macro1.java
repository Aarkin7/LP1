package code;

//Reads text from a character-input stream, buffering characters so as toprovide for the efficient reading of characters, 
import java.io.BufferedReader;

//Reads text from character files using a default buffer size. Decoding from bytesto characters uses either a 
//specified charsetor the platform's default charset. 
//
//The FileReader is meant for reading streams of characters. For readingstreams of raw bytes, consider using a FileInputStream.
import java.io.FileReader;

//Writes text to character files using a default buffer size. Encoding from characters to bytes uses either a specified charsetor the platform's default charset. 
//
//Whether or not a file is available or may be created depends upon theunderlying platform. Some platforms, in particular, 
//allow a file to beopened for writing by only one FileWriter (or other file-writingobject) at a time. 
//In such situations the constructors in this classwill fail if the file involved is already open.
import java.io.FileWriter;

//Signals that an I/O exception of some sort has occurred. Thisclass is the general class of exceptions produced by failed orinterrupted I/O operations.
import java.io.IOException;

//An iterator over a collection. Iterator takes the place of Enumeration in the Java Collections Framework. 
//Iteratorsdiffer from enumerations in two ways: 
//• Iterators allow the caller to remove elements from theunderlying collection during the iteration with 
//well-definedsemantics. 
//• Method names have been improved. 
import java.util.Iterator;

//Hash table and linked list implementation of the Map interface,with predictable iteration order. This implementation differs from HashMap in that it maintains a doubly-linked list running throughall of its entries. This linked list defines the iteration ordering,which is normally the order in which keys were inserted into the map(insertion-order). Note that insertion order is not affectedif a key is re-inserted into the map. (A key k isreinserted into a map m if m.put(k, v) is invoked when m.containsKey(k) 
//would return true immediately prior tothe invocation.) 
import java.util.LinkedHashMap;


public class Macro1 {

public static void main(String[] args) throws IOException{


	//filereader for reading input file
	FileReader FR=new FileReader("input.txt");
	BufferedReader br=new BufferedReader(FR);
	
	//file writer
	FileWriter mnt=new FileWriter("mnt.txt");              //Macro Name Table
	FileWriter mdt=new FileWriter("mdt.txt");              //Macro Definition Table
	FileWriter kpdt=new FileWriter("kpdt.txt");            //Keyword Parameter Default Table
	FileWriter pnt=new FileWriter("pntab.txt");            //Parameter name table
    FileWriter op=new FileWriter("output.txt");             //output file

	//for storing the parameters name and position
	LinkedHashMap<String, Integer> pntab=new LinkedHashMap<>();
	
	 
	String line;                         //for reading line by line
	String Macroname = null;             //for storing the name of macro

	int mdtp=1;                    //MDT Pointer
	int kpdtp=0;                   //KPDT Pointer
	int paramNo=1;                 //keeping track of no. of parameters
	int pp=0;                      //no. of positional parameters
	int kp=0;                      //no. of keyword parameters
	int flag=0;                    //checking for MACRO & MEND statement

	while((line=br.readLine())!=null)
	{
		
		String tokens[]=line.split("\\s+");                      //spliting by whitespaces
		if(tokens[0].equalsIgnoreCase("MACRO"))
		{
			flag=1;                  
			line=br.readLine();
			tokens=line.split("\\s+");
			Macroname=tokens[0];


			if(tokens.length<=1)                      //if there are no parameters
			{
				mnt.write(tokens[0]+"\t"+pp+"\t"+kp+"\t"+mdtp+"\t"+(kp==0?kpdtp:(kpdtp+1))+"\n");
				continue;
			}
			for(int i=1;i<tokens.length;i++)         //processing of parameters
			{
				tokens[i]=tokens[i].replaceAll("[&,]", "");
		
			
				//for keyword parameters
				if(tokens[i].contains("="))
				{
					++kp;                           //increase no. of keyword parameters

					//for storing parameter and its default value
					String keywordParam[]=tokens[i].split("=");

					//Writing in LinkedHashMap---parameter name and no. of that parameter
					pntab.put(keywordParam[0], paramNo++);

					//writing in KDPT
                	if(keywordParam.length==2)                //if there is default parameter
					{
						kpdt.write(keywordParam[0]+"\t"+keywordParam[1]+"\n");
					}
					else
					{
						kpdt.write(keywordParam[0]+"\t-\n");
					}
				}

				//writing positional parameters
				else
				{
					pntab.put(tokens[i], paramNo++);
					pp++;                           //increasing no. of positional parameters
				}
			}

			mnt.write(tokens[0]+"\t"+pp+"\t"+kp+"\t"+mdtp+"\t"+(kp==0?kpdtp:(kpdtp+1))+"\n");
			kpdtp=kpdtp+kp;                       //increasing KDPT Pointer
			
			
			
		}

		//if MEND
		else if(tokens[0].equalsIgnoreCase("MEND"))
		{
			mdt.write(line+"\n");               //writing MEND in MDT

			flag=kp=pp=0;                       //reseting positional parameters and keyword parameters
			mdtp++;                 
			paramNo=1;
			pnt.write(Macroname+":\t");

			//iterator for keySet in HashMap
			Iterator<String> itr=pntab.keySet().iterator();

			//writing parameters in PNT
//			Returns true if the iteration has more elements.(In other words, returns true if next 
//			would return an element rather than throwing an exception.)
			while(itr.hasNext())
			{
//				Returns the next element in the iteration.
				pnt.write(itr.next()+"\t");
			}

			pnt.write("\n");

			//clearing PNTAB LinkedHashMap
//			Removes all of the mappings from this map (optional operation).The map will be empty after this call returns.
			pntab.clear();
		}

		//writing in MDT
		else if(flag==1)
		{
			for(int i=0;i<tokens.length;i++)
			{
				if(tokens[i].contains("&"))
				{
					tokens[i]=tokens[i].replaceAll("[&,]", "");
					
					//get
//					Returns the value to which the specified key is mapped,or null if this map contains no mapping for the key. 
					mdt.write("(P,"+pntab.get(tokens[i])+")\t");               //writing parameters and position
				}
				else
				{
					mdt.write(tokens[i]+"\t");          //writing mnemonic
				}
			}
			mdt.write("\n");
			mdtp++;
		}
		else{
			op.write(line+"\n");
		}
		
	}
	//close
//	Closes the stream and releases any system resources associated withit. Once the stream has been closed, further read(), ready(),mark(), reset(), or skip() 
//	invocations will throw an IOException.Closing a previously closed stream has no effect.
	br.close();
	mdt.close();
	mnt.close();
	pnt.close();
	kpdt.close();
	op.close();
	System.out.println("Macro Pass1 Processing done. :)");
}

}
