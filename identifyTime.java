import java.io.*;
import java.util.*;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;

import org.joda.time.DateTime;
/*
   
   
   
saurav kushwaha
used opennlp
used java regex expressions
*/
public class identifyTime 
{
	// using hash set i.e hash table as data structure
	 static Set<String> number = new HashSet<String>();
	 static Set<String> timexSet = new HashSet<String>();
	 static Set<String> date = new HashSet<String>();
	 static Set<Integer> arr=new HashSet<Integer>();
	 static String[] whitespaceTokenizerLine;
	 public static final String[] DIGITS = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
	 public static final String[] TENS = {null, "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
	 public static final String[] TEENS = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
	 public static final String[] MAGNITUDES = {"hundred", "thousand", "million", "point"};
	 public static final String[] ZERO = {"zero", "oh"};
	 public static void numEx(String str) throws IOException {
		   
		 POSModel model = new POSModelLoader().load(new File("en-pos-maxent.zip"));
		 POSTaggerME tagger = new POSTaggerME(model);
		    @SuppressWarnings("deprecation")
		 String sd="";
		 StringTokenizer t=new StringTokenizer(str,".;,:");
		 while (t.hasMoreElements()) 
		 {
			 sd=sd+" "+t.nextToken().trim().toLowerCase();
		 }
		 //System.out.println(sd);
		 ObjectStream<String> lineStream =new PlainTextByLineStream(new StringReader(sd));
		 while ((sd = lineStream.read()) != null) 
		 {
			
		        whitespaceTokenizerLine = WhitespaceTokenizer.INSTANCE.tokenize(sd);
		        String[] tags = tagger.tag(whitespaceTokenizerLine);
		         int i=0;
		                for(String s:tags)
		                {
		                	
		                	if(s.compareTo("CD")==0)
		                		number.add(whitespaceTokenizerLine[i]);  //populates the cardinal number
		                	i++;
		                }
		    }  
		}
	 public static void processor(String str)
	 { 
		 int num=-1;
		 DateTime dt = new DateTime();
		 int hours = dt.getHourOfDay();
	     int diffh;
	     String save=null;
		 int min=-1*(dt.getMinuteOfHour());
		 int sec=-1*(dt.getSecondOfMinute());
		 Date ds=new Date(); 
		 for (String s : number) 
		 {
			 getPos(s);
		 }
		 //System.out.println(dt.minusHours(30).getDayOfWeek());
			// System.out.println(s);
		 if(date.size()>0||timexSet.size()>0)
		 {
			 String ch=null;
			 int chk=-1;
			 boolean flag=false;
			 if(date.size()>0)
			 {
				 if(date.size()>0&&timexSet.size()==0)
				 {
					 
					 for(String s:date)
					 {
						 for(String sn:number)
						 {
							 if(s.matches(".*"+sn+".*"))
							 {
								 num=getNum(sn);
							 }
						 }
						 while(true)
						 {
						 if(chk<0&&num>0)
						 {
							 flag=true;
							 if(s.matches("(.*)last(.*)"))
							{
								 chk=0;
							}else
							 if(s.matches("(.*)next(.*)"))
							 {
						        chk=1;
							 }else
							 if(s.matches("(.*)ago(.*)"))
							 {
						         chk=2;
							 }else
							 if(s.matches("(.*)since(.*)"))
							 {
						         chk=3;
							 }else if(str.matches("(.*)since+(.*)"+" "+num))
							 
							 {
								 System.out.println(num+"-"+dt.getYear());
								 return;
							 }
							 //System.out.println("hello"+num);
						   }
						 
						 if(chk>=0&&flag&&num>0)
						 {
							 System.out.println("tPeriod");
							 if(chk==0||chk==2||chk==3)
							 {
					    		 if(s.matches("(.*)day(.*)"))
							       { 
								      System.out.println(dt.minusDays(num).toLocalDate()+" - "+dt.toLocalDate());
								      return ;
							       }else
							       if(s.matches("(.*)hour(.*)"))
							       {
								     
							    	   System.out.println(dt.minusHours(num).toLocalTime()+" - "+dt.toLocalTime());
								      return ;
							       }else
							       if(s.matches("(.*)minute(.*)"))
							       {
							    	   System.out.println(dt.minusMinutes(num).toLocalTime()+" - "+dt.toLocalTime());
								      return ;
							       }else
							       if(s.matches("(.*)month(.*)"))
							       {
							    	   System.out.println(dt.minusMonths(num).toLocalDate()+" - "+dt.toLocalDate());
								      return ;
							       }else
							       if(s.matches("(.*)year(.*)"))
							       {
							    	   System.out.println(dt.minusYears(num).getYear()+" - "+dt.getYear());
								      return ;
							       }else
							       if(chk==3)
							       {
							    	   System.out.println(num+" - "+dt.getYear());
							    	   return ;
							       }
							 }
							 if(chk==1)
							 {

								 if(s.matches("(.*)day(.*)"))
							       { 
					    			
								      System.out.println(dt.minusDays(num).toLocalDate()+" - "+dt.toLocalDate());
								      return ;
							       }
							       if(s.matches("(.*)hour(.*)"))
							       {
							    	   System.out.println(dt.minusHours(num).toLocalTime()+" - "+dt.toLocalTime());
								      return ;
							       }
							       if(s.matches("(.*)minute(.*)"))
							       {
							    	   System.out.println(dt.minusMinutes(num).toLocalTime()+" - "+dt.toLocalTime());
								      return ;
							       }
							       if(s.matches("(.*)month(.*)"))
							       {
							    	   System.out.println(dt.minusMonths(num).toLocalDate()+" - "+dt.toLocalDate());
								      return ;
							       }
							       if(s.matches("(.*)year(.*)"))
							       {
							    	   System.out.println(dt.minusYears(num).getYear()+" - "+dt.getYear());
								      return ;
							       }
							 }
							 
						 }else {
							break;
						       }
                       }	 
					}
				 }
			 }
			 if(date.size()>0)
			 {
				 for(String s:date)
				 {
					 if(s.matches("tomorrow"))
					 {
						 if(str.matches("(.*)day(.*\\s.*)after(.*\\s.*)"+s+"(.*)"))
						 {
							 chk=0;
							 break;
						 }
						 chk=1;
					 }else
					 if(s.matches("today"))
					 {
						 chk=2;
						 break;
					 }
					 if(s.matches("yesterday"))
					 {
						 if(str.matches("(.*)day(.*\\s.*)before(.*\\s.*)"+s+"(.*)"))
						 {
							 chk=4;
							 break;
						 }
						 chk=3;
						 break;
					 }
				 }
					 for(String  n:number)
					 {
						 if(str.matches("(.*)day(.*\\s.*)after(.*\\s.*)tomorrow(.*\\s.*)at(.*\\s.*)"+n+"(.*)"))
						 { num=getNum(n);save=n;}
						 else
						 if(str.matches("(.*)tomorrow(.*\\s.*)at(.*\\s.*)"+n+"(.*)"))
						 { num=getNum(n);save=n;}
						 else 
					     if(str.matches("(.*)today(.*\\s.*)at(.*\\s.*)"+n+"(.*)"))
					     { num=getNum(n);save=n;}
					     else
						 if(str.matches("(.*)day(.*\\s.*)before(.*\\s.*)yesterday(.*\\s.*)"+n+"(.*)"))
						 { num=getNum(n);save=n;}
						 else
						 if(str.matches("(.*)yesterday(.*\\s.*)at(.*\\s.*)"+n+"(.*)"))
						 { num=getNum(n);save=n;}
						 
					 }
					 if(timexSet.size()==0&&num>0)
						{
						 System.out.println("tStamp");
						 if(chk==0)
						 {
							 if(str.matches("(.*)"+save+"(.*\\s.*)am(.*)"))
							 {System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(2).plusHours(num).toDateTime());
							 return ;
							 }else if(str.matches("(.*)"+save+"(.*\\s.*)pm(.*)")){
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(2).plusHours(num+12).toDateTime());
								 return ;
							}
							 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(2).plusHours(num).toDateTime());
							 return ;
						 }else if(chk==1)
						 {
							 if(str.matches("(.*)"+save+"(.*\\s.*)am(.*)"))
							 {System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(1).plusHours(num).toDateTime());
							 return ;
							 }else if(str.matches("(.*)"+save+"(.*\\s.*)pm(.*)")){
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(1).plusHours(num+12).toDateTime());
								 return ;
							}
							 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(1).plusHours(num).toDateTime());
							 return ;
						 }else if(chk==2)
						 {
							 if(str.matches("(.*)"+save+"(.*\\s.*)am(.*)"))
							 {System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).toDateTime());
							 return ;
							 }else if(str.matches("(.*)"+save+"(.*\\s.*)pm(.*)")){
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num+12).toDateTime());
								 return ;
							}
							 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).toDateTime());
						     return ;
						 } else if(chk==3)
						 {
							 if(str.matches("(.*)"+save+"(.*\\s.*)am(.*)"))
							 {System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).minusDays(1).plusHours(num).toDateTime());
							 return ;
							 }else if(str.matches("(.*)"+save+"(.*\\s.*)pm(.*)")){
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).minusDays(1).plusHours(num).toDateTime());
								 return ;
							}
							 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).minusDays(1).toDateTime());
							 return ;
						 }else if(chk==4)
						 {
							 if(str.matches("(.*)"+save+"(.*\\s.*)am(.*)"))
							 {System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).minusDays(2).plusHours(num).toDateTime());
							 return ;
							 }else if(str.matches("(.*)"+save+"(.*\\s.*)pm(.*)")){
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).minusDays(2).plusHours(num).toDateTime());
								 return ;
							}
							 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).minusDays(2).toDateTime());
							 return ;
						 }
						 return ;
					    }
					 for(String s:timexSet)
					 {
						 //System.out.println(chk);
						 System.out.println("tStamp");
						 if(num>0)
						 {
						 if(chk==0)
						 {
							//(str.matches("(.*)"+s+" "))
							 if(s.matches("evening|night"))
								 
							 { 
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(2).plusHours(12+num).toDateTime());
								 return ;
							 }else
							 if(s.matches("morning"))
							 { 
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(2).plusHours(num).toDateTime());
								 return ;
							 }else
							 if(s.matches("noon"))
							 {
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(2).plusHours(12).toDateTime());
								 return ;
							 }else
							 if(s.matches("midnight"))
							 {
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(2).plusHours(0).toDateTime());
								 return ;
							 }
							 return ;
						 }
						 if(chk==1)
						 {
							 if(s.matches("evening|night"))
							 { 
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(1).plusHours(12+num).toDateTime());
								 return ;
							 }
							 if(s.matches("morning"))
							 { 
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(1).plusHours(num).toDateTime());
								 return ;
							 }
							 if(s.matches("noon"))
							 {
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(1).plusHours(12).toDateTime());
								 return ;
							 }
							 if(s.matches("midnight"))
							 {
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusDays(1).plusHours(0).toDateTime());
								 return ;
							 }
							 return ;
						 }
						 if(chk==2)
						 {
							 if(s.matches("evening|night"))
							 { 
								 if(num+12>hours)
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(12+num).toDateTime());
								 else 
								 {	System.out.println("Time has gone");
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(12+num).toDateTime());}
								 return ;
							 }
							 if(s.matches("morning"))
							 { 
								 if(num>hours)
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).toDateTime());
								 else 
								 {	System.out.println("Time has gone");
								    System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).toDateTime());}
								 return ;
							 }
							 if(s.matches("noon"))
							 {
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(12).toDateTime());
								 return ;
							 }
							 if(s.matches("midnight"))
							 {
								 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(0).toDateTime());
								 return ;
							 }
							 return ;
						 }
							 if(chk==3)
							 {
								 if(s.matches("evening|night"))
								 { 	 
									 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(12+num).minusDays(1).toDateTime());
									 return ;
								 }
								 if(s.matches("morning"))
								 { 
									 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).minusDays(1).toDateTime());
									 return ;
								 }
								 if(s.matches("noon"))
								 {
									 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).minusDays(1).plusHours(12).toDateTime());
									 return ;
								 }
								 if(s.matches("midnight"))
								 {
									 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).minusDays(1).plusHours(0).toDateTime());
									 return ;
								 }
								 return ;
							 }
							 if(chk==4)
							 {
								 if(s.matches("evening|night"))
								 { 	 
									 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(12+num).minusDays(2).toDateTime());
									 return ;
								 }
								 if(s.matches("morning"))
								 { 
									 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).minusDays(2).toDateTime());
									 return ;
								 }
								 if(s.matches("noon"))
								 {
									 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).minusDays(2).plusHours(12).toDateTime());
									 return ;
								 }
								 if(s.matches("midnight"))
								 {
									 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).minusDays(2).plusHours(0).toDateTime());
									 return ;
								 }
								 return ;
							 }
						 }   }
					 }
		 }
				 //System.out.println(chk+num);
			 

		for(Integer i:arr)
		{
			 num=getNum(whitespaceTokenizerLine[i]);
			 //System.out.println(num);
			 String pre=prev_word(str,i);
			 String aft=after_word(str,i);
			 //System.out.println(pre+aft);
		 if(( aft.compareTo("is")==0))
		 {
			 System.out.println("tTrigger");
			 if((pre.matches("after")))
			 {
				System.out.print("start ");
				if(hours>num)
				{
					diffh=(24-hours)+num;
				}else {
					
					diffh =num-hours;
				}
				ds=time(0, min, diffh, 0, 0,sec);
				System.out.println(ds.toString());
				return ;
			 }
			 if((pre.matches("before")))
			 {
				System.out.print("start ");
                System.out.println(ds.toString());
                if(hours>num)
				{
					diffh=(24-hours)+num;
				}else {
					
					diffh =num-hours;
				}
				ds=time(0, min, diffh, 0, 0,sec);
				System.out.print(" end ");
				System.out.println(ds.toString());
				return;
			 }
		 }
		 String aft_aft=after_word(str, i+1);
		 if(aft_aft.matches("from|after|before"))
			{
			 System.out.println("tStamp");
			 save=after_word(str, i+2);
			 if((save.compareTo("now")==0)&&aft_aft.matches("from"))
			 {
		       if(aft.matches("d(.*)y|d(.*)ys"))
		       {
			      ds=time(num, 0, 0, 0, 0, 0);
			      System.out.println(ds.toString());
			      return ;
		       }
		       if(aft.matches("h(.*)r|h(.*)rs"))
		       {
			      ds=time(0, 0, num, 0, 0, 0);
			      System.out.println(ds.toString());
			      return ;
		       }
		       if(aft.matches("min(.*)"))
		       {
			      ds=time(0, num, 0, 0, 0, 0);
			      System.out.println(ds.toString());
			      return ;
		       }
		       if(aft.matches("month|months"))
		       {
			      ds=time(0, 0, 0, num, 0, 0);
			      System.out.println(ds.toString());
			      return ;
		       }
		       if(aft.matches("y(.*)r|y(.*)rs"))
		       {
			      ds=time(0, 0, 0, 0, num, 0);
			      System.out.println(ds.toString());
			      return ;
		       }
			 }
			 if(aft_aft.matches("before"))
				 num=-1*num;
			 if(save.matches("midnight"))
			 {
				 if(aft.matches("h(.*)r|h(.*)rs"))
			       {
					 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(num).toDateTime());
					 return ;
			       }
			       if(aft.matches("min(.*)"))
			       {
			    	   System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusMinutes(num).toDateTime());
						 return ;
			       }
			       if(aft.matches("sec(.*)"))
			       {
			    	   System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusSeconds(num).toDateTime());
						 return ;
			       }
			 }
			 if(save.matches("noon"))
			 {
				 if(aft.matches("h(.*)r|h(.*)rs"))
			       {
					 System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(12+num).toDateTime());
					 return ;
			       }
			       if(aft.matches("min(.*)"))
			       {
			    	   System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(12).plusMinutes(num).toDateTime());
			    	   return ;
			       }
			       if(aft.matches("sec(.*)"))
			       { 
			    	   System.out.println(dt.minusHours(hours).minusMinutes(-1*min).minusSeconds(-1*sec).plusHours(12).plusSeconds(num).toDateTime());
			    	   return ;
			       }
			 }
		     }
		 }
		System.out.println("not understandable");
	 }
	 public static int getNum(String input)
	 {
		 if(input.matches("ten|10"))
			 return 10;
		 else if(input.matches("eleven|11"))
			 return 11;
		 else if(input.matches("twelve|12"))
			 return 12;
		 if(input.matches("thirteen|13"))
			 return 13;
		 else if(input.matches("fourteen|14"))
			 return 14;
		 else if(input.matches("fifteen|15"))
			 return 15;
		 else if(input.matches("sixteen|16"))
			 return 16;
		 else if(input.matches("seventeen|17"))
			 return 17;
		 else if(input.matches("eighteen|18"))
			 return 18;
		 else if(input.matches("nineteen|19"))
			 return 19;
		 else if(input.matches("twenty|20"))
			 return 20;
		 else if(input.matches(".*[a-zA-Z]+.*"))
			 {
		   String result = "";
		    String[] decimal = input.split(MAGNITUDES[3]);
		    String[] millions = decimal[0].split(MAGNITUDES[2]);

		    for (int i = 0; i < millions.length; i++) {
		        String[] thousands = millions[i].split(MAGNITUDES[1]);

		        for (int j = 0; j < thousands.length; j++) {
		            int[] triplet = {0, 0, 0};
		            StringTokenizer set = new StringTokenizer(thousands[j]);

		            if (set.countTokens() == 1) { 
		                String uno = set.nextToken();
		                triplet[0] = 0;
		                for (int k = 0; k < DIGITS.length; k++) {
		                    if (uno.equals(DIGITS[k])) {
		                        triplet[1] = 0;
		                        triplet[2] = k + 1;
		                    }
		                    if (uno.equals(TENS[k])) {
		                        triplet[1] = k + 1;
		                        triplet[2] = 0;
		                    }
		                }
		            }
		            else if (set.countTokens() == 2) {  
		                String uno = set.nextToken();
		                String dos = set.nextToken();
		                if (dos.equals(MAGNITUDES[0])) {  
		                    for (int k = 0; k < DIGITS.length; k++) {
		                        if (uno.equals(DIGITS[k])) {
		                            triplet[0] = k + 1;
		                            triplet[1] = 0;
		                            triplet[2] = 0;
		                        }
		                    }
		                }
		                else {
		                    triplet[0] = 0;
		                    for (int k = 0; k < DIGITS.length; k++) {
		                        if (uno.equals(TENS[k])) {
		                            triplet[1] = k + 1;
		                        }
		                        if (dos.equals(DIGITS[k])) {
		                            triplet[2] = k + 1;
		                        }
		                    }
		                }
		            }

		            else if (set.countTokens() == 3) {  
		                String uno = set.nextToken();
		                String dos = set.nextToken();
		                String tres = set.nextToken();
		                for (int k = 0; k < DIGITS.length; k++) {
		                    if (uno.equals(DIGITS[k])) {
		                        triplet[0] = k + 1;
		                    }
		                    if (tres.equals(DIGITS[k])) {
		                        triplet[1] = 0;
		                        triplet[2] = k + 1;
		                    }
		                    if (tres.equals(TENS[k])) {
		                        triplet[1] = k + 1;
		                        triplet[2] = 0;
		                    }
		                }
		            }

		            else if (set.countTokens() == 4) {  
		                String uno = set.nextToken();
		                String dos = set.nextToken();
		                String tres = set.nextToken();
		                String cuatro = set.nextToken();
		                for (int k = 0; k < DIGITS.length; k++) {
		                    if (uno.equals(DIGITS[k])) {
		                        triplet[0] = k + 1;
		                    }
		                    if (cuatro.equals(DIGITS[k])) {
		                        triplet[2] = k + 1;
		                    }
		                    if (tres.equals(TENS[k])) {
		                        triplet[1] = k + 1;
		                    }
		                }
		            }
		            else {
		                triplet[0] = 0;
		                triplet[1] = 0;
		                triplet[2] = 0;
		            }

		            result = result + Integer.toString(triplet[0]) + Integer.toString(triplet[1]) + Integer.toString(triplet[2]);
		        }
		    }

		    if (decimal.length > 1) {  
		        StringTokenizer decimalDigits = new StringTokenizer(decimal[1]);
		        result = result + ".";
		        System.out.println(decimalDigits.countTokens() + " decimal digits");
		        while (decimalDigits.hasMoreTokens()) {
		            String w = decimalDigits.nextToken();
		            System.out.println(w);

		            if (w.equals(ZERO[0]) || w.equals(ZERO[1])) {
		                result = result + "0";
		            }
		            for (int j = 0; j < DIGITS.length; j++) {
		                if (w.equals(DIGITS[j])) {
		                    result = result + Integer.toString(j + 1);
		                }   
		            }

		        }
		    }
		    return Integer.parseInt(result);
		 }
		return Integer.parseInt(input);
		}
	 
	 public static String prev_word(String main,Integer i)
	 {
		 if(i<1)
				return "";
			else
		return whitespaceTokenizerLine[i-1];
		 
	 }
	 public static String after_word(String main,Integer i)
	 {
		if(i>(whitespaceTokenizerLine.length-2))
			return "";
		else
		return whitespaceTokenizerLine[i+1];
		 
	 }
	 public static void getPos(String main)
	 {
		 int i=0;
		 for(String s:whitespaceTokenizerLine)
		 {
			 if(s.compareTo(main)==0)
			 {
				arr.add(i);
			 }
			 i++;
		 }
		 //System.out.println(arr);
	 }
	 
		 public static Date time(int day,int min,int hrs,int month,int year,int sec)
		 {
			 Date dts = new Date( ); // Instantiate a Date object
		     Calendar cal = Calendar.getInstance();
		     cal.setTime(dts);
		     cal.add(Calendar.MINUTE, min);
		     cal.add(Calendar.DATE, day);
		     cal.add(Calendar.HOUR, hrs);
		     cal.add(Calendar.MONTH, month);
		     cal.add(Calendar.YEAR, year);
		     cal.add(Calendar.SECOND, sec);
		     dts = cal.getTime();
		     //System.out.printf(dts.toString() );
			return dts;		 
		 }	
	//to extract date related information and then populates into hashset
	public static void dateEx(String str) throws InvalidFormatException, IOException {
		TokenNameFinderModel model=new TokenNameFinderModel(new File("en-ner-date.zip"));
		NameFinderME finder=new NameFinderME(model);
		Tokenizer tokenizer=SimpleTokenizer.INSTANCE;
		Span[] namespans = null;
		String[] tokens;
		@SuppressWarnings("deprecation")
		ObjectStream<String> lineStream =new PlainTextByLineStream(new StringReader(str));
		String line,s="";
		while ((line = lineStream.read()) != null)	
		{
			tokens=tokenizer.tokenize(line);
			 namespans=finder.find(tokens);
			 StringTokenizer t=new StringTokenizer(Arrays.toString(Span.spansToStrings(namespans, tokens)),"[],");
			 while (t.hasMoreElements()) 
			 {
				 date.add(t.nextToken().trim().toLowerCase());
			 }
			 //System.out.println(Arrays.toString(Span.spansToStrings(namespans, tokens)));
			//date.add(Arrays.toString(Span.spansToStrings(namespans, tokens)));
		}
		
	}
	//to extract time related information and then populates into hashset
	public static void timeEx(String str) throws InvalidFormatException, IOException {
		TokenNameFinderModel model=new TokenNameFinderModel(new File("en-ner-time.zip"));
		NameFinderME finder=new NameFinderME(model);
		Tokenizer tokenizer=SimpleTokenizer.INSTANCE;
		Span[] namespans = null;
		String[] tokens = null;
		@SuppressWarnings("deprecation")
		ObjectStream<String> lineStream =new PlainTextByLineStream(new StringReader(str));
		String line;
		while ((line = lineStream.read()) != null)
		{
			tokens=tokenizer.tokenize(line);
			 namespans=finder.find(tokens);
			 StringTokenizer t=new StringTokenizer(Arrays.toString(Span.spansToStrings(namespans, tokens)),"[],");
			 while (t.hasMoreElements()) 
			 {
				 timexSet.add(t.nextToken().trim().toLowerCase());
			 }
			 //System.out.println(Arrays.toString(Span.spansToStrings(namespans, tokens));
			//timexSet.add(Arrays.toString(Span.spansToStrings(namespans, tokens)));
		}	
		 
	}
	
public static void main(String[] args) throws Exception
{
	Scanner sc=new Scanner(System.in); 
	String string=sc.nextLine();
	
	numEx(string);
	System.out.println("number:-"+number);
	dateEx(string);
	timeEx(string);
	
	System.out.println("date related"+date);
	System.out.println("time related"+timexSet);

	//output the relevant data	
	processor(string);
}
}
