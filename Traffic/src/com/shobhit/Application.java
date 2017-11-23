package com.shobhit;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

@Path("/app")
public class Application {
	
	/*
	 * To create new record
	 */
	@Path("/newUser")
	@POST
	@Produces(MediaType.TEXT_HTML)
	public InputStream newUser(@FormParam("id") String id,@FormParam("name") String name,@FormParam("fname") String fname,@FormParam("address") String address,@FormParam("balance") float balance,@FormParam("challans") int challans,@FormParam("status") String status)
	{
		Configuration cfg = new Configuration();
		cfg.configure("/resources/hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		
		Session session = factory.openSession();
		
		Transaction transaction = session.beginTransaction();
		
		Citizen citizen = new Citizen();
		citizen.setId(id.toUpperCase());
		citizen.setName(name.toUpperCase());
		citizen.setFname(fname.toUpperCase());
		citizen.setAddress(address);
		citizen.setBalance(balance);
		citizen.setChallans(challans);
		citizen.setStatus(status.toUpperCase());
		session.persist(citizen);
		transaction.commit();
		session.close();
		
		try{
			File f = new File("C:\\Users\\USER\\workspace\\Traffic\\WebContent\\index.html");
			return new FileInputStream(f);
		}
		catch(FileNotFoundException e)
		{
			return null;
		}
	}
	
	@GET
	@Path("/getUser/{plateNo}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Citizen> getCitizen(@PathParam("plateNo") String plateNo)
	{
		Configuration cfg = new Configuration();
		cfg.configure("/resources/hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		
		Session session = factory.openSession();
		
		Transaction transaction = session.beginTransaction();
		
		String q = "from Citizen where id=:id";
		Query query = session.createQuery(q);
		query.setParameter("id",plateNo.toUpperCase());
		List<Citizen> list = query.list();
		session.getTransaction().commit();
		session.close();
		
		System.out.println("inside USER");
		
		return list;
	}
	
	/*
	 * Method to return the plate number from the file output.txt
	 */
	@GET
	@Path("/getPlate")
	
	public JsonObject getPlate() throws IOException{
		
		// pass the path to the file as a parameter
	    //File file = new File("C:\\Users\\USER\\Documents\\licence plate recognition\\output.txt");
	    BufferedReader brTest = new BufferedReader(new FileReader("C:\\Users\\USER\\Documents\\licence plate recognition\\output.txt"));
	    //Scanner sc = new Scanner(file);
	 	//String plateNo=sc.nextLine();
	 	String plateNo = brTest.readLine();
	 	plateNo = plateNo.replaceAll("\\s","");

	 	Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
	    Matcher match= pt.matcher(plateNo);
	    while(match.find())
	    {
	      String s= match.group();
	      plateNo=plateNo.replaceAll("\\"+s, "");
	    }
	    plateNo=plateNo.toUpperCase();
	 	System.out.println(plateNo);
	 	JsonObject object = Json.createObjectBuilder().add("value",plateNo).build();
	 	return object;
	}
	
	/*
	 * Method for deducting toll/challan
	 */
	@POST
	@Path("/tollChallan")
	@Produces(MediaType.TEXT_HTML)
	public InputStream tollChallan(@FormParam("plateNo") String plateNo,@FormParam("amount") float amount, @FormParam("type") String type )
	{
		amount = (int) amount;
		int challans=0,balance=0;
		plateNo = plateNo.toUpperCase();
		type = type.toUpperCase();
		
		Configuration cfg = new Configuration();
		cfg.configure("/resources/hibernate.cfg.xml");
		SessionFactory factory = cfg.buildSessionFactory();
		
		Session session = factory.openSession();
		
		Transaction transaction = session.beginTransaction();
		
		//getting data
		String q = "from Citizen where id=:id";
		Query query = session.createQuery(q);
		query.setParameter("id",plateNo);
		List<Citizen> list = query.list();
		
		
		
		//storing challans and balance
		for(Citizen c1 : list)
		{
			challans=c1.getChallans();
			balance=(int) c1.getBalance();
		}
		
		//checking if toll or challan
		if(type.equals("TOLL"))
		{
			String qString="update Citizen set balance = :balance where id=:id";
			Query query1 = session.createQuery(qString);
			query1.setParameter("balance",balance-amount);
			query1.setParameter("id",plateNo);
			query1.executeUpdate();
		}
		else if(type.equals("CHALLAN"))
		{
			if(challans == 2)
			{
				String q1 = "update Citizen set balance = :balance , challans = :newchallan ,status = :newStatus where id=:id ";
				Query qq1 = session.createQuery(q1);
				qq1.setParameter("balance",balance - amount);
				qq1.setParameter("newchallan",3);
				qq1.setParameter("newStatus","DENIED");
				qq1.setParameter("id",plateNo);
				qq1.executeUpdate();
			}
			else if(challans <2)
			{
				String q2 = "update Citizen set balance = :balance , challans = :newchallan where id=:id ";
				Query qq2 = session.createQuery(q2);
				qq2.setParameter("balance",balance - amount);
				qq2.setParameter("newchallan",++challans);
				qq2.setParameter("id",plateNo);
				qq2.executeUpdate();
			}
		}
		transaction.commit();
		session.close();
		
		try{
			File f = new File("C:\\Users\\USER\\workspace\\Traffic\\WebContent\\index.html");
			return new FileInputStream(f);
		}
		catch(FileNotFoundException e)
		{
			return null;
		}
		
	}

}
