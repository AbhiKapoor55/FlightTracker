
/* -----------------------------------------------------
 * Name of Software: FlightTracker  
 * Author/Programmer: Abhishek (Abhi) Kapoor 
 * Date: August 15th, 2018 
 * Location: University of Toronto - Canada 
 * Purpose and Description: A Java-based software which 
 * utilizes network programming and the JSoup library 
 * in order to display up-to-date information about 
 * particular flights 
 ------------------------------------------------------ */

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FTDEMO extends JPanel implements ActionListener 
{
	JFrame frame; 
	JLabel lblNewOne; 
	JLabel lblLogo; 
	JLabel imgPlane;
	static JLabel lblStatus; 
	static JLabel lblFlightTitle; 
	static JLabel lblDepartureTime; 
	static JLabel lblOriginCity; 
	static JLabel lblOriginAirport; 
	JLabel lblOrigin; 
	JLabel lblDestination; 
	static JLabel lblArrivalTime; 
	static JLabel lblDestinationCity; 
	static JLabel lblDestinationAirport; 
	static JTextField txtEnter; 
	JButton btnEnter; 
	JLabel lblCopyright; 
	
	/**
	 * This method is the constructor method, and it creates a new frame and manages 
	 * the elements of the Graphical User Interface (GUI)   
	 */
	
	public FTDEMO()   
	{
		frame = new JFrame(" =======| Welcome to FlightTracker |======== ");
		
		ImageIcon imgNewOne = new ImageIcon("blue1.jpg");
		lblNewOne = new JLabel(imgNewOne); 
	    
	    lblLogo = new JLabel(new ImageIcon("logo2.png"));
	    lblLogo.setBounds(120,0,500,90);
	    lblLogo.setFont(new Font("Comic Sans MS", Font.ITALIC, 18));
	    
	    lblCopyright = new JLabel("© Copyright Abhi Kapoor 2018");
	    lblCopyright.setBounds(240,60,500,30);
	    
	    //imgPlane = new JLabel(new ImageIcon("airplane.png")); 
	    //imgPlane.setBounds(230, 150, 200, 130);
	    
	    lblFlightTitle = new JLabel();
	    lblFlightTitle.setBounds(90,90,700,30);
	    lblFlightTitle.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
	    
	    lblStatus = new JLabel();
	    lblStatus.setBounds(90,130,400,30);
	    lblStatus.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		lblStatus.setForeground(Color.GREEN);
	    
	    lblOrigin = new JLabel("FLIGHT ORIGIN");
	    lblOrigin.setBounds(65,170,200,30);
	    lblOrigin.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
	    
	    lblDestination = new JLabel("FLIGHT DESTINATION");
	    lblDestination.setBounds(485,170,200,30);
	    lblDestination.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
	    
	    lblDepartureTime = new JLabel();
	    lblDepartureTime.setBounds(30,210,400,30);
	    
	    lblArrivalTime = new JLabel();
	    lblArrivalTime.setBounds(430,210,400,30);
	    
	    lblOriginCity = new JLabel();
	    lblOriginCity.setBounds(30,250,400,30);
	    
	    lblDestinationCity = new JLabel();
	    lblDestinationCity.setBounds(430,250,400,30);
	    
	    lblOriginAirport = new JLabel();
	    lblOriginAirport.setBounds(30,290,400,30);
	    
	    lblDestinationAirport = new JLabel();
	    lblDestinationAirport.setBounds(430,290,400,30);
	    
	    
	    txtEnter = new JTextField();
	    txtEnter.setBounds(10,350,600,20);	
	    
	    btnEnter = new JButton("Track");
	    btnEnter.setBounds(620,350,80,25);
	    btnEnter.addActionListener(this);
	   
	    frame.setContentPane(lblNewOne);
	    frame.add(lblLogo);
	    //frame.add(imgPlane); 
	    frame.add(lblStatus); 
	    frame.add(lblFlightTitle); 
	    frame.add(lblDepartureTime); 
	    frame.add(lblOriginCity); 
	    frame.add(lblOriginAirport); 
	    frame.add(lblOrigin);
	    frame.add(lblDestination); 
	    frame.add(lblArrivalTime); 
	    frame.add(lblDestinationCity); 
	    frame.add(lblDestinationAirport);
	    frame.add(txtEnter); 
	    frame.add(btnEnter); 
	    frame.add(lblCopyright); 
	    
	    frame.setResizable(true);
		frame.pack(); 
		frame.setSize(770,400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(200, 200);
		frame.setVisible(true);
	    
		frame.revalidate();
	}
	
	/**
	 * This method is invoked when the "Track" JButton is clicked and it 
	 * calls the beginFlightTracker() method 
	 * @param event An ActionEvent object   
	 */
	
	public void actionPerformed(ActionEvent event) 
	{
		if(event.getActionCommand() == "Track")
		{
			System.out.println("Connecting..."); 
			beginFlightTracker();
		}
	}
	
	/**
	 * This method first obtains the entered flight and uses regular expressions to validate 
	 * entered input and extract the airline code and the flight number. The validated user 
	 * input is then passed to the connect() method  
	 */
	
	public static void beginFlightTracker()
	{
		boolean flag = false; 
		String flightEntered = ""; 
		while(!flag)
		{
			
			flightEntered = txtEnter.getText(); 
			String mold = "[a-zA-Z0-9]+\\d+";
			flag = flightEntered.matches(mold); 
			if(!flag)
				System.out.println("Invalid Flight Number. Please Try Again");
		}
	
		Document doc = connect(flightEntered);
		System.out.println("Document Successfully Obtained");
		displayInformation(doc); 
	}
	
	/**
	 * This method takes a Document object, extracts important information from it, and displays the information 
	 * on the GUI. Useful information extracted include the airline & flight name, flight status, 
	 * origin and destination cities, and departure and arrival times. 
	 * @param doc A Document object obtained after successfully connecting to the URL 
	 */
	
	public static void displayInformation(Document doc)
	{
		// Display the Title 
		try
		{
			String title = doc.select("[class=sc-giadOv bowyid]").first().text();
			title = title.substring(0, title.indexOf("Flight Tracker"));
			lblFlightTitle.setText("NOW TRACKING: "+title);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Flight Not Found", "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		// Display the Status of the Flight 
		String statusTime = doc.select("[class=sc-ipXKqB bJRHTc]").text();
		lblStatus.setText("STATUS OF FLIGHT: "+statusTime); 
		System.out.println("STATUS: "+statusTime);
		
		// Create array of Origin City and Destination City 
		Elements originDestination = doc.select("[class=sc-ipXKqB hOVIhp]");
		String[] originDestinationArray = new String[2];
		int originDestinationCount = 0; 
				
		for(Element e: originDestination)
		{
			originDestinationArray[originDestinationCount] = e.text(); 
			originDestinationCount++; 
		}
				
		lblOriginCity.setText("ORIGIN: "+originDestinationArray[0]);
		lblDestinationCity.setText("DESTINATION: "+originDestinationArray[1]);
		
		// Create array of both Airports 
		Elements airports = doc.select("[class=sc-ipXKqB cTdJbD]");
		String[] airportsArray = new String[2]; 
		int airportCount = 0; 
		for(Element e: airports)
		{
			airportsArray[airportCount] = e.text(); 
			airportCount++; 
		}
				
		lblOriginAirport.setText("ORIGIN AIRPORT: "+airportsArray[0]);
		lblDestinationAirport.setText("DESTINATION AIRPORT: "+airportsArray[1]);
		
		// Create array of departure time (0th index) and arrival time (1st index)
		String times = doc.select("[class=sc-ipXKqB kzMLCP]").text();
		String[] timesArray = new String[2]; 
		if(times.contains("--"))
		{
			timesArray = times.split("--");
			//System.out.println(timesArray[0] + "  "+timesArray[1]);
			System.out.println("CONTAINED -- "+times);
			lblDepartureTime.setText("ESTIMATED DEPARTURE TIME: "+timesArray[0]);
			lblArrivalTime.setText("ESTIMATED ARRIVAL TIME: "+timesArray[1]);
		}
		else 
		{
			// 21:35 BST 20:35 BST 17:50 +08 17:50 +08
			String[] thing = times.split(" ");
			System.out.println("Did not contain --- "+times);
			String mold = "[A-Z]+"; 
			String timezone = " "; 
			for(int i = 0;i<thing.length;i++)
				if(thing[i].matches(mold) && !timezone.contains(thing[i]))
					timezone += thing[i] + " "; 
			
			timezone = timezone.trim(); 
			System.out.println("==== Did not contain + =====");
			String first, second = ""; 
			System.out.println(timezone);
			if(timezone.length() > 3)
			{
				first = timezone.split(" ")[0];
				second = timezone.split(" ")[1];
				lblDepartureTime.setText("ESTIMATED DEPARTURE TIME: "+thing[0] + " "+first);
				lblArrivalTime.setText("ESTIMATED ARRIVAL TIME: "+thing[4] + " "+second);
				//System.out.println("WE THINK: "+thing[0]  + first +"   and   "+thing[4] + second +"    "+timezone);
			}
			else 
			{
				lblDepartureTime.setText("ESTIMATED DEPARTURE TIME: "+thing[0] + " "+timezone);
				lblArrivalTime.setText("ESTIMATED ARRIVAL TIME: "+thing[4] + " "+timezone);
			}
					
		}
		
		// Departed, Scheduled, or Arrived 
		try
		{
			String depschedarr = doc.select("[class=sc-ipXKqB exFxRg]").first().text();
			if(depschedarr.contains("arrived"))
				lblStatus.setText("On Time: THIS FLIGHT HAS ARRIVED AT DESTINATION");
			else if(depschedarr.contains("delayed"))
			{
				lblStatus.setForeground(Color.RED);
				lblStatus.setText("THIS FLIGHT IS DELAYED");
			}
			
				
			System.out.println("DEPSCHEDARR: "+depschedarr);
		}
		catch(Exception e)
		{
			System.out.println("No depschedarr");
		}
		//"[class=sc-ipXKqB eizTVI]"
			
	}
	
	/**
	 * This method takes the entered flight and creates the correct URL. It then attempts to 
	 * connect to the FlightStats server and then returns a Document object 
	 * @param flightEntered A String object containing the flight name and number 
	 * @return doc A Document object obtained after successfully connecting to the FlightStats website, 
	 * or null otherwise 
	 */
	
	public static Document connect(String flightEntered)
	{
		String airlineCode = flightEntered.substring(0, 2);
		String airlineNumbers = flightEntered.substring(2,flightEntered.length());
		String year = getDate()[0];
		String month = getDate()[1];
		String day = getDate()[2];
		
		String part1 = "https://www.flightstats.com/v2/flight-tracker/"; 
		String part2 = airlineCode+"/"+airlineNumbers; 
		String part3 = "?year="+year+"&month="+month+"&date="+day; 
		String finalURL = part1 + part2 + part3; 
		System.out.println(finalURL); 
		
		System.out.println("Connecting...");
		try 
		{
			Document doc = Jsoup.connect(finalURL).get();
			return doc; 
		} 
		catch (IOException e) 
		{
			System.out.println("ERROR CONNECTING TO SERVER "+e.toString());
		}
		return null; 
		
	}
	
	/**
	 * This method obtains the current date in the YYYY/MM/DD format 
	 * and returns an Array containing the date. The date is used as part of the 
	 * URL-forming function 
	 * return dateArray A String Array containing the current year (index 0), current month (index 1), and 
	 * current day (index 2)  
	 */
	
	public static String[] getDate()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String[] dateArray = dateFormat.format(date).split("/");
		
		return dateArray; 
	}
	
	/**
	 * This is the main method - the entry point to the program. This method is executed first by the 
	 * Java Virtual Machine (JVM). This method instantiates a new object of this type, thus executing the constructor method, which 
	 * then initializes the GUI. 
	 */
	
	public static void main(String[] args)
	{
		FTDEMO obj = new FTDEMO();
	}

	
}

