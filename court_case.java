package dbms;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;
import java.math.BigDecimal;

public class court_case
{
	public static void main(String args[]) 
	{
		Connection c = null;
		try
		{
			// Load Postgresql Driver class
			Class.forName("org.postgresql.Driver");
			// Using Driver class connect to databased on localhost, port=5432, database=postgres, user=postgres, password=postgres. If cannot connect then exception will be generated (try-catch block)
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres", "1234");
			System.out.println("Opened database successfully");
			
			// Create instance of this class to call other methods
			court_case p = new court_case();
			
			c.setAutoCommit(false);
			// Call method createCompanyTable to Create Company Table
			//p.createCompanyTable(c);
			
			
		//	System.out.println("Opened database successfully");
			Scanner sc=new Scanner(System.in);
			int num=0;
			while(num!=6)
			{
			System.out.println("!!..Select the Number of Query you want to Execute..!!");
			System.out.println("1. Retrieve number of cases an advocate has handled.");
			System.out.println("2. Retrieve names of judge and advocate of Gujarat.");
			System.out.println("3. Retrieve details of advocate who are victim. ");
			System.out.println("4. Retrieve name of judge judging a ‘Civil’ case type in ‘Ahmedabad’. ");
			System.out.println("5. Display Case type with Maximum no. of cases registered. ");
			System.out.println("6. Insert into table case_type. ");
			System.out.println("7. EXIT..!!");
			num = sc.nextInt();
			
			switch(num)
			{
			case 1: p.query1(c);
				break;
			case 2: p.query2(c);
				break;
			case 3: p.query3(c);
				break;
			case 4: p.query4(c);
				break;
			case 5: p.query5(c);
				break;
			case 6: p.insertCasetype(c);
					c.setAutoCommit(true);
				break;
			case 7: System.exit(0);
				break;
			default: System.out.println("!!..INVALID CHOICE..!!");
			}
			}
			// Call method updEmployeeTable to Create Run UPDATE Query in Employee Table
			//p.updEmployeeTable(c);
			
			c.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}
	void insertCasetype(Connection c)
	{
		Scanner sc=new Scanner(System.in);
		int num=0;
		String type;
		PreparedStatement stmt = null;
		String sql = "INSERT INTO court_case.case_type VALUES (?,?)";
		try
		{
			stmt = c.prepareStatement(sql);
			System.out.println( "give id:-"); 
			stmt.setInt(1, num = sc.nextInt());
			System.out.println( "case type :-"); 
			stmt.setString(2, type = sc.next());
			
			stmt.executeUpdate();
			stmt.close();
			System.out.println( "!!..inserted successfully..!!");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}
	
	void query1(Connection c)
	{
		Statement stmt = null;
		try
		{
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select u.adv_id,x.adv_name,count(case_no)as total_cases from court_case.user_adv_cases as u,court_case.advocate as x where u.adv_id=x.adv_id and case_no in (select case_no from court_case.cases where extract(year from start_date)='2017') group by u.adv_id,x.adv_name");
			System.out.format("+-----------------+-----------------+-----------------+%n");
			System.out.format("| advocate ID     | Advocate name   | Total cases     |%n");
			System.out.format("+-----------------+-----------------+------------------%n");
			while(rs.next())
			{
				// Datatype mapping and functions to use with JDBC is found at https://www.tutorialspoint.com/jdbc/jdbc-data-types.htm
				String name;
				int id, total;
				//String esalary, ssalary;
				// Employee Infor
				id = rs.getInt("adv_id");
				name = rs.getString("adv_name");
				total = rs.getInt("total_cases");
				
		
				
				System.out.println( "\t"+ id + "\t\t" + name + "\t\t" + total);
			}
			
			stmt.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}

   }
	void query2(Connection c)
	{
		Statement stmt = null;
		try
		{
			
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select judge.judge_name,'judge' as post from court_case.judge where judge.court_id in("
					+ "select court_id from court_case.court where court_name like 'Gujarat%')"
					+ "union "
					+ "select advocate.adv_name,'advocate' from court_case.advocate where advocate.court_id in("
					+ "select court_id from court_case.court where court_name like 'Gujarat%')");
			while(rs.next())
			{
				// Datatype mapping and functions to use with JDBC is found at https://www.tutorialspoint.com/jdbc/jdbc-data-types.htm
				String name, post;
				name = rs.getString("judge_name");
				post = rs.getString("post");
				
				System.out.println("name-" + name + ", \tpost-" + post);
			}
			
			stmt.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}

   }
   
	void query3(Connection c)
	{
		Statement stmt = null;
		try
		{
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from court_case.user_det as u where user_type='v' and contact =(\r\n" + 
					"	select contact from court_case.advocate as e where e.contact=u.contact and e.adv_name=u.user_name);");
			
			while(rs.next())
			{
				// Datatype mapping and functions to use with JDBC is found at https://www.tutorialspoint.com/jdbc/jdbc-data-types.htm
				String city, type, name, gender;
				long id,contact,aadhar,year,a_id;
				// Employee Infor
				id = rs.getInt("user_id");
				name= rs.getString("user_name");
				city = rs.getString("city");
				type = rs.getString("user_type");
				gender = rs.getString("gender");
				contact = rs.getLong("contact");
				aadhar = rs.getLong("aadhar_nu");
				year = rs.getInt("birth_year");
				a_id = rs.getInt("adv_id");
				
				System.out.println(" user ID-" + id + ", usr name-" + name + ", city-" + city +", assussed/victim- " + type + ", gender-" + gender + ", contact-" + contact+ ",aadhar number- "+ aadhar +",birth year- "+year+",advocate id- "+a_id);
			}
			
			stmt.close();
			System.out.println("Table Queried successfully");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}

   }
	
	void query4(Connection c)
	{
		Statement stmt = null;
		try
		{
			int flag=1;
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select * from court_case.judge where judge_id in (\r\n" + 
					"select judge_id from court_case.examines where case_no in (\r\n" + 
					"	select case_no from court_case.cases where city ='Ahmedabad' and case_type_id=(\r\n" + 
					"			select case_type_id from court_case.case_type where type='Civil')))");
			
			while(rs.next())
			{
				// Datatype mapping and functions to use with JDBC is found at https://www.tutorialspoint.com/jdbc/jdbc-data-types.htm
				String city, edu, name, ass;
				long id,contact,active,ex,c_id;
				// Employee Infor
				id = rs.getInt("judge_id");
				name= rs.getString("judge_name");
				ex = rs.getLong("experience");
				city = rs.getString("city");
				contact = rs.getLong("contact");
				active = rs.getLong("active_cases");
				edu = rs.getString("edu_degree");
				ass = rs.getString("assistant_name");
				c_id = rs.getInt("court_id");
				
				System.out.println(" judge ID-" + id + ", judge name-" + name + ", experience-" + ex +", city- " + city + ", contact-" + contact + ", active cases-" + active+ ",degree- "+ edu +",assistant name- "+ass+",court id- "+c_id);
			}
			
			stmt.close();
			System.out.println("Table Queried successfully"+flag);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}

   }
	
	void query5(Connection c)
	{
		Statement stmt = null;
		try
		{
			 
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("select type from court_case.case_type where case_type_id in(\r\n" + 
					"	select cs.case_type_id from(\r\n" + 
					"		select case_type_id,count(fir_no) as fir_count from court_case.cases group by case_type_id) as cs \r\n" + 
					"			 where cs.fir_count>3 group by  cs.case_type_id,cs.fir_count);");
			
			while(rs.next())
			{
				// Datatype mapping and functions to use with JDBC is found at https://www.tutorialspoint.com/jdbc/jdbc-data-types.htm
				String type;
				
				type = rs.getString("type");
				
				System.out.println("case type-" + type);
			}
			
			stmt.close();
			System.out.println("Table Queried successfully");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}

   }
}



