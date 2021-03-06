package moocs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

//import java.sql.DriverManager;
//import java.sql.Statement;

/*

Cloud MySQL Login Information

mysql://bd887ba87d4a49:93a9097d@us-cdbr-east-05.cleardb.net/heroku_229e6c24d3396ae

Host: us-cdbr-east-05.cleardb.net
Username: bd887ba87d4a49
Password: 93a9097d
Database Name: heroku_229e6c24d3396ae

 */

public class Novoed {
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		// Need Jsoup jar files to run this sample program. You may also need to rebuild path, etc.

		// Used for writing query outputs to file
		File file = new File("dump.txt");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		ArrayList<String> month = new ArrayList<String>();
		month.add("January"); month.add("February"); month.add("March"); month.add("April"); month.add("May"); month.add("June"); 
		month.add("July"); month.add("August"); month.add("September"); month.add("October"); month.add("November"); month.add("December"); 
		int id = 1;
		int course_id = 1;

		String url1 = "https://novoed.com/courses";
		String url2 = "https://novoed.com/courses?page=2";
		 
		ArrayList<String> pgcrs = new ArrayList<String>(); //Array which will store each course URLs 
		pgcrs.add(url1);
		pgcrs.add(url2);

		//The following few lines of code are used to connect to a database so the scraped course content can be stored.
//		Class.forName("com.mysql.jdbc.Driver").newInstance();
//		java.sql.Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/novoed_udacity?useUnicode=true&amp;characterEncoding=UTF-16","root","novacity");
		//make sure you create a database named scrapedcourse in your local mysql database before running this code
		//default mysql database in your local machine is ID:root with no password
		//you can download scrapecourse database template from your Canvas account->modules->Team Project area
		for(int a=0; a<pgcrs.size();a++)
		{
			String furl = (String) pgcrs.get(a);
			Document doc = Jsoup.connect(furl).get();
			doc.outputSettings().charset("UTF-16");
			Elements ele = doc.select("ul[class=accessible]");
			//System.out.println(ele); 
			Elements crspg = ele.select("div[class=hovered row-fluid");
			//System.out.println("CoursePage " +crspg);
			Elements link = crspg.select("h2[class=coursetitle]").select("a[href]");
			//System.out.println("Link: " +link);

			for (int j=0; j<link.size();j++)
			{
                //TODO: Use cloud database in future
//				Statement statement = connection.createStatement();
				
				String crsurl = link.get(j).attr("href"); // Get Course URL
				System.out.println(crsurl);
				String CourseName = crspg.select("h2").get(j).text(); //Get the Course Name from h2 Tag
				CourseName = CourseName.replace("'", "''");
				CourseName = CourseName.replace(",", "");
				String SCrsDesrpTemp = crspg.select("p[class=description]").get(j).text();
				//System.out.println("De " +SCrsDesrpTemp);
				SCrsDesrpTemp = SCrsDesrpTemp.replace("?", "");
				//String SCrsDesrp = SCrsDesrpTemp.substring(0, (SCrsDesrpTemp.length()-5)); //To get the course description and remove the extra characters at the end.
				SCrsDesrpTemp = SCrsDesrpTemp.replace("'", "''");
				SCrsDesrpTemp = SCrsDesrpTemp.replace(",", "");
				
				String CrsImg = crspg.select("img[alt= ]").get(j).attr("src"); //To get the course image
            System.out.println(CrsImg);
				/*				if(a==0||a==1)
				{
					CrsImg  = ele.select("img[alt= ]").get(j).attr("src"); //To get the course image
				}
				else
				{
					CrsImg = ele.select("img[alt= ]").get(a).attr("src"); //To get the course image - FOR URL4
				} */
				Document crsdoc = Jsoup.connect(crsurl).get();
				crsdoc.outputSettings().charset("UTF-16");
				//System.out.println("Course Document: " +crsdoc);
				Elements crsheadele = crsdoc.select("header[class=row-fluid coursepage]");
				//System.out.println("Head Element: " + crsheadele);
				String youtube = crsheadele.select("div[class=span5 course-video]").select("iframe[title]").attr("src"); //Youtube link
				//System.out.println("YT: " + youtube);
				Elements crsbodyele = crsdoc.select("div[class=container main-container]");
				//System.out.println("Course Body: " +crsbodyele);
				String CrsDes = crsbodyele.select("article[class=span7]").text(); //Course Description Element
				CrsDes = CrsDes.replace("'", "''");
				CrsDes = CrsDes.replace(",", "");
				if(CrsDes.contains("?"))
				{
					CrsDes = CrsDes.replace("?", "");
				}
				
				//System.out.println("Course Description: " +CrsDes);

				/* get instructor names and images */
				String instructor = crsdoc.select("span[class=instructor_name]").text();
				String[] instructors = instructor.split(",");
				String selector;
				String[] instrimgs = new String[instructors.length];
				String[] instrqueries = new String[instructors.length];
				for (int k = 0; k < instructors.length; k++)
				{
				   instructors[k] = instructors[k].trim();
				   if (!instructors[k].startsWith("Ph.D."))
				   {
				      selector = "img[alt^=" + instructors[k] + "]";
			         instrimgs[k] = crsdoc.select(selector).attr("src");
			         instrqueries[k] = "insert into coursedetails values(" + id + ",'" + instructors[k] + "','" + instrimgs[k] + "', " + course_id + ")";
			         id++;
			         System.out.println(instrqueries[k]);
//			         System.out.println("Image: " + instrimgs[k]);
				   }
//				   System.out.println("Instructor: " + instructors[k]);
				}
				course_id++;
				
				String Date = crsdoc.select("div[class=timeline inline-block]").text();
				System.out.println("Date: " +Date);
				
				String StrDate;	// String Date for query
				String crsduration = "0";	// default course duration to 0 for self-paced
				
				if (Date.contains("Starting Fall")) {
					StrDate = Date.substring(Date.indexOf("l ")+2, Date.length());
					StrDate = StrDate + "-09-01";	// September 1 for courses starting in the Fall
					//System.out.println("Date: " +StrDate);
				} else if (Date.contains("Starting Spring")) {
					StrDate = Date.substring(Date.indexOf("g 2")+2, Date.length());
					StrDate = StrDate + "-02-01";	// February 1 for courses starting in the Spring
					//System.out.println("Date2: " +StrDate);
				} else if (Date.contains("Starting")){
					String tmp = Date.substring(Date.indexOf("g")+2);
					if (tmp.contains(",")) // tmp in format: MONTH DD, YYYY
					{
						int monthNumber = month.indexOf(tmp.substring(0, tmp.indexOf(" "))) + 1;
						String day = tmp.substring(tmp.indexOf(" ")+1, tmp.indexOf(" ")+3);
						if (monthNumber < 10) { // single digit months need that 0
							StrDate = tmp.substring(tmp.length()-4) + "-0" + monthNumber + "-" + day;
						} else { // two digit months are good to go
							StrDate = tmp.substring(tmp.length()-4) + "-" + monthNumber + "-" + day;
						}
					} else {	// tmp format: MONTH YYYY
						int monthNumber = month.indexOf(tmp.substring(0, tmp.indexOf(" "))) + 1;
						if (monthNumber < 10) { // single digit months need that 0
							StrDate = tmp.substring(tmp.indexOf(" ")+1) + "-0" + monthNumber + "-01";
						} else { // two digit months are good to go
							StrDate = tmp.substring(tmp.indexOf(" ")+1) + "-" + monthNumber + "-01";
						}
					}
					//System.out.println("Date3: " +StrDate);
				} else if (Date.contains("Registration closed") && !(Date.contains("Started"))) {
					String tmp = Date.substring(0, Date.length()-20);	// Date in format: MONTH DD, YYYY MONTH DD, YYYY
					String Date2 = tmp.substring(Date.indexOf(",")+7);
					int month1 = month.indexOf(tmp.substring(0, Date.indexOf(" "))) + 1; //System.out.println("month1: " + month1);
					int month2 =  month.indexOf(Date2.substring(0, Date2.indexOf(" "))) + 1; //System.out.println("month2: " + month2);
					String date1 = Date.substring(tmp.indexOf(" ")+1, tmp.indexOf(",")); //System.out.println("date1: " + date1);
					String date2 = Date2.substring(Date2.indexOf(" ")+1, Date2.indexOf(",")); //System.out.println("date2: " + date2);
					String year1 = Date.substring(tmp.indexOf(",")+2, tmp.indexOf(",")+6);
					String year2 = Date2.substring(Date2.indexOf(",")+2);
					
					if (month1 < 10) {	// StrDate only cares about the STaRt Date?
						StrDate = year1 + "-0" + month1 + "-" + date1;
					} else {
						StrDate = year1 + "-" + month1 + "-" + date1;
					}
					
					int y1 = Integer.parseInt(year1);
					int y2 = Integer.parseInt(year2);
					int d1 = Integer.parseInt(date1);
					int d2 = Integer.parseInt(date2);
					/* Stacked Overflowed how to calculate range
					 * http://stackoverflow.com/questions/3796841/getting-the-difference-between-date-in-days-in-java
					 */
					Calendar startDate = Calendar.getInstance();
					Calendar endDate = Calendar.getInstance();
					startDate.set(y1, month1, d1);
					endDate.set(y2, month2, d2);
					Date start = startDate.getTime();
					Date end = endDate.getTime();
					long stime = start.getTime();
					long etime = end.getTime();
					long timediff = etime - stime;
					long days = timediff / (1000 * 60 * 60 * 24);
					
					crsduration = "" + days;
					//System.out.println("Date4: " +StrDate);
				} else if (Date.contains("Started")) {
					// format: Started MONTH DD, YYYY
				   if (Date.contains("Registration closed"))
				      Date = Date.replace(" Registration closed", "");
					String tmp = Date.substring(Date.indexOf(" ")+1);	// MONTH DD, YYYY
					int monthNumber = month.indexOf(tmp.substring(0, tmp.indexOf(" "))) + 1;
					if (monthNumber < 10) {
						StrDate = tmp.substring(tmp.indexOf(",")+2) + "-0" + monthNumber + "-" + tmp.substring(tmp.indexOf(" ")+1, tmp.indexOf(","));
					} else {
						StrDate = tmp.substring(tmp.indexOf(",")+2) + "-" + monthNumber + "-" + tmp.substring(tmp.indexOf(" ")+1, tmp.indexOf(","));
					}
					//System.out.println("Date5: " +StrDate);
				} else {  // format of Date for parsing: MONTH DD, YYYY MONTH DD, YYYY
					String Date2 = Date.substring(Date.indexOf(",")+7);
					int month1 = month.indexOf(Date.substring(0, Date.indexOf(" "))) + 1; //System.out.println("month1: " + month1);
					int month2 =  month.indexOf(Date2.substring(0, Date2.indexOf(" "))) + 1; //System.out.println("month2: " + month2);
					String date1 = Date.substring(Date.indexOf(" ")+1, Date.indexOf(",")); //System.out.println("date1: " + date1);
					String date2 = Date2.substring(Date2.indexOf(" ")+1, Date2.indexOf(",")); //System.out.println("date2: " + date2);
					String year1 = Date.substring(Date.indexOf(",")+2, Date.indexOf(",")+6);
					String year2 = Date2.substring(Date2.indexOf(",")+2);
					
					if (month1 < 10) {	// StrDate only cares about the STaRt Date?
						StrDate = year1 + "-0" + month1 + "-" + date1;
					} else {
						StrDate = year1 + "-" + month1 + "-" + date1;
					}
					
					int y1 = Integer.parseInt(year1);
					int y2 = Integer.parseInt(year2);
					int d1 = Integer.parseInt(date1);
					int d2 = Integer.parseInt(date2);
					/* Stacked Overflowed how to calculate range
					 * http://stackoverflow.com/questions/3796841/getting-the-difference-between-date-in-days-in-java
					 */
					Calendar startDate = Calendar.getInstance();
					Calendar endDate = Calendar.getInstance();
					startDate.set(y1, month1, d1);
					endDate.set(y2, month2, d2);
					Date start = startDate.getTime();
					Date end = endDate.getTime();
					long stime = start.getTime();
					long etime = end.getTime();
					long timediff = etime - stime;
					long days = timediff / (1000 * 60 * 60 * 24);
					
					crsduration = "" + days;
					//System.out.println("Duration: " +crsduration);
					//System.out.println("Date6: " +Date);
				}
				
				String crsfee = ele.select("figure[class=pricetag]").get(j).text();
				if (crsfee.equals("Free")) { crsfee = "0"; }
				else { crsfee = crsfee.substring(1); }
				//System.out.println("Course fee: " +crsfee);

				String lang = "English"; // default language is English
				String cert = "No"; 	// default certification is NO.
				boolean stateofA = crsdoc.select("div[class=more-info-expandable]").text().contains("Statement of Accomplishment");
				if (stateofA) { cert = "Yes"; }
				String univ = crsdoc.select("header[class=row-fluid coursepage]").select("div[class=row-fluid university-branding] > img").attr("alt");
				univ = univ.replace("'", "''");
				univ = univ.replace(",", "");
				//System.out.println("Univeristy: " +univ);
				
				// For Date parsed
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 1);
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
				String dscrap = format1.format(cal.getTime());
				//System.out.println("Date scraped: " +dscrap);
				
				//The following is used to insert scraped data into your database table. Need to uncomment all database related code to run this.
				String query = "insert into course_data values(null,'"+CourseName+"','"+SCrsDesrpTemp+"','"+CrsDes+"','"+crsurl+"','"+youtube+"','"+StrDate+"','"+crsduration+"','"+CrsImg+"','','NovoEd','"+crsfee+"','"+lang+"','"+cert+"','"+univ+"','"+dscrap+"')";
				
				System.out.println(query);   
				
//				statement.executeUpdate(query); // skip writing to database; focus on data printout to a text file instead.
            for (int m = 0; m < instructors.length; m++)
            {
               if (!instructors[m].startsWith("Ph.D."))
               {
//                  statement.executeUpdate(instrqueries[m]);
               }
            }

//				statement.close();
				
				try {								
					// if file doesn't exists, then create it
					if (!file.exists()) {
						file.createNewFile();
					}
					
					for (int l = 0; l < instructors.length; l++)
					{
					   if (!instructors[l].startsWith("Ph.D."))
					   {
					      bw.write(instrqueries[l]);
					      bw.newLine(); bw.newLine();
					   }
					}
					bw.write(query);
					bw.newLine(); bw.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
		}
//		connection.close();
		bw.close();
	}
}
