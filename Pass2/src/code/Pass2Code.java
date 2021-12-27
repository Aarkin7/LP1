package code;

//Reads text from a character-input stream, buffering characters so as toprovide for the efficient reading of characters, 
//arrays, and lines. 
//The buffer size may be specified, or the default size may be used. Thedefault is large enough for most purposes. 
//
//In general, each read request made of a Reader causes a correspondingread request to be made 
//of the underlying character or byte stream. It istherefore advisable to wrap a BufferedReader 
//around any Reader whose read()operations may be costly, such as FileReaders and InputStreamReaders.
import java.io.BufferedReader;

//Writes text to a character-output stream, buffering characters so as toprovide for the efficient writing of single characters, arrays, and strings. 
//
//The buffer size may be specified, or the default size may be accepted.The default is large enough for most purposes. 
//
//A newLine() method is provided, which uses the platform's own notion ofline separator as defined by the system property line.separator.
//Not all platforms use the newline character ('\n') to terminate lines.Calling this method to terminate each output line is 
//therefore preferred towriting a newline character directly. 
import java.io.BufferedWriter;

//Reads text from character files using a default buffer size. Decoding from bytesto characters uses either a 
//specified charsetor the platform's default charset. 
//
//The FileReader is meant for reading streams of characters. For readingstreams of raw bytes, consider using a FileInputStream.
import java.io.FileReader;

//Writes text to character files using a default buffer size. Encoding from charactersto bytes uses either a specified charsetor the platform's default charset. 
//
//Whether or not a file is available or may be created depends upon theunderlying platform. Some platforms, in particular, 
//allow a file to beopened for writing by only one FileWriter (or other file-writingobject) at a time. 
//In such situations the constructors in this classwill fail if the file involved is already open.
import java.io.FileWriter;

//Resizable-array implementation of the List interface. Implementsall optional list operations, and permits all elements, 
//including null. In addition to implementing the List interface,this class provides methods to manipulate 
//the size of the array that isused internally to store the list. (This class is roughly equivalent to Vector, 
//		except that it is unsynchronized.) 
//
//The size, isEmpty, get, set, iterator, and listIterator operations run in constanttime. 
//The add operation runs in amortized constant time,that is, adding n elements requires O(n) time. 
//All of the other operationsrun in linear time (roughly speaking). The constant factor is low comparedto that for 
//the LinkedList implementation. 
import java.util.ArrayList;




public class Pass2Code {
	ArrayList<TableRow> SYMTAB,LITTAB; //Resizable-array implementation of the List interface

	public Pass2Code()
	{
		SYMTAB=new ArrayList<>();
		LITTAB=new ArrayList<>();
	}
	public static void main(String[] args) {
		Pass2Code pass2=new Pass2Code();
		
		try {
			pass2.generateCode("IC.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readtables()
	{
		BufferedReader br;
		String line;
		try
		{
			br=new BufferedReader(new FileReader("SYMTAB.txt"));
			while((line=br.readLine())!=null)
			{
				String parts[]=line.split("\\s+"); 
				/*split("\\s+"); This combines all-white spaces as a delimiter. 
				So if you have the string: "Hello[space][tab]World" This will yield the strings "Hello" and "World" 
				and eliminate the space among the [space] and the [tab]*/
				//System.out.print(parts[1]);  = A,L1 etc
				//System.out.println(parts[2]);= 100,103 etc (locations)
				
				
//				Parses the string argument as a signed decimal integer. Thecharacters in the string must all be decimal digits, exceptthat the first character may be an ASCII minus sign '-'('\u005Cu002D') 
//				to indicate a negative value or anASCII plus sign '+' ('\u005Cu002B') toindicate a positive value. The resulting integer value isreturned, exactly as if 
//				the argument and the radix 10 weregiven as arguments to the parseInt(java.lang.String, int) method.
				SYMTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]),Integer.parseInt(parts[0]) ));
				
			}
			br.close();
			br=new BufferedReader(new FileReader("LITTAB.txt"));
			while((line=br.readLine())!=null)
			{
				String parts[]=line.split("\\s+");
				LITTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]),Integer.parseInt(parts[0])));
			}
			br.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void generateCode(String filename) throws Exception
	{
		readtables();
		BufferedReader br=new BufferedReader(new FileReader(filename));

		BufferedWriter bw=new BufferedWriter(new FileWriter("PASS2.txt"));
		String line,code;
		while((line=br.readLine())!=null)
		{
			
//			Splits this string around matches of the given regular expression. 
//
//			This method works as if by invoking the two-argument split method with the given expression and a 
//			limitargument of zero. Trailing empty strings are therefore not included inthe resulting array. 
			String parts[]=line.split("\\s+");
			
			//contains
//			Returns true if and only if this string contains the specifiedsequence of char values.
			if(parts[0].contains("AD")||parts[0].contains("DL,01")) // assembler directive or a declarative statement
			{
				bw.write("\n");   //?
				continue;
			}
			else if(parts.length==2)
			{
				if(parts[0].contains("DL,02")) //declarative statement
				{
					
					
					parts[0]=parts[0].replaceAll("[^0-9]", ""); //removing everything except decimals
				
					
					//if(Integer.parseInt(parts[0])==1)  // DS
					
						
						int constant=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
						code="00\t0\t"+String.format("%03d", constant)+"\n"; // constant 
						bw.write(code);
						
						
					
				}
				else if(parts[0].contains("IS"))
				{
					int opcode=Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // replacing all non digits with a ""
					
					if(opcode==10)  // opcode for print
					{
						if(parts[1].contains("S"))  //symbol
						{
							int symIndex=Integer.parseInt(parts[1].replaceAll("[^0-9]", "")); //symindex is the index in the symbol table
							code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", SYMTAB.get(symIndex-1).getAddress())+"\n"; 
							// "%03d" it's padding. if the number has 1 or 2 digits than some leading zeros will be added to the number to make its width equal to 3.
							bw.write(code);
							//System.out.println(code);
						}
						else if(parts[1].contains("L"))
						{
							int symIndex=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
							//format
//							Returns a formatted string using the specified format string andarguments. 
							code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", LITTAB.get(symIndex-1).getAddress())+"\n";
							bw.write(code);
						}
						
					}
				}
			}
			else if(parts.length==1 && parts[0].contains("IS"))
			{
				int opcode=Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
			    
				code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", 0)+"\n"; //only 0s
				
				bw.write(code);
			}
			else if(parts[0].contains("IS") && parts.length==3) //All OTHER IS INSTR
			{
			int opcode=	Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // OPCODE given in MOT
			
			int regcode=Integer.parseInt(parts[1]);   // indicates whether a register is present
			
			if(parts[2].contains("S"))   //symbol
			{
				int symIndex=Integer.parseInt(parts[2].replaceAll("[^0-9]", "")); // the 2nd value in tuple contains the location e.g. (S,03)
				//System.out.println(SYMTAB.get(symIndex-1).getSymbol());
				code=String.format("%02d", opcode)+"\t"+regcode+"\t"+String.format("%03d", SYMTAB.get(symIndex-1).getAddress())+"\n";
				
				bw.write(code);
			}
			else if(parts[2].contains("L"))     // literal 
			{
				int symIndex=Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
				
				code=String.format("%02d", opcode)+"\t"+regcode+"\t"+String.format("%03d", LITTAB.get(symIndex-1).getAddress())+"\n";
				bw.write(code);
			}		
			}
			
		}
//		Closes the stream, flushing it first. Once the stream has been closed,further write() or flush() invocations will cause an IOException to bethrown. 
//		Closing a previously closed stream has no effect.
		bw.close();
		br.close();

	}



	
	
	
	
}
