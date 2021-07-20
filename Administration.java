// Course Administration System. By- Saloni Ranka and Sanyam Jain

import java.sql.*;
import java.util.Scanner;
public class Administration {
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   static final String DB_URL = "jdbc:mysql://localhost/ADMINISTRATION";

   static final String USER = "root";
   static final String PASS = "root";
    
    static Connection con = null;
    static Statement st = null;
    static Scanner sc = new Scanner(System.in);
    static void course_reg(String r_no, String year, String dept) throws SQLException{
        String sql = "select course_name, credits from Courses where year = '" + year + "' and type = 'Core' and dept = '" + dept+"'";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("Your core courses for this year are:");
        while(rs.next()) {
            System.out.println(rs.getString(1) + " with Credits  = " + rs.getInt(2));
        }
        sql = "select course_name, credits from Courses where year = '" + year + "' and type = 'Elective' and dept = '" + dept+"'";
        rs = st.executeQuery(sql);
        int cnt = 0;
        System.out.println("Elective courses offered are");
        while(rs.next()) {
            System.out.println(rs.getString(1) + " with Credits  = " + rs.getInt(2));
            cnt++;
        }
        rs.first();
        if(cnt == 1) {
            System.out.println("You can opt to register this course if you wish to. Y or N");
            char op = sc.next().charAt(0);
            if(op == 'Y') {
                sql = "select prerequisite from Courses where course_name ='" + rs.getString(1)+"'";
                ResultSet rs1 = st.executeQuery(sql);
                sql = "select grade from "+rs1.getString(1)+"where roll_no = '"+ r_no+"'";
                rs1 = st.executeQuery(sql);
                if(rs1.next())
                {
                    if(rs1.getString(1) != "F")
                    {
                        sql = "select count(roll_no) from " + rs.getString(1);
                        rs1 = st.executeQuery(sql);
                        if(rs1.getInt(1) >= 80) {
                        System.out.println(" No seats available in this elective. Choose some other elective");
                        }
                        else {
                        sql = "insert into " + rs.getString(1) + " values ('" + r_no + "' , NULL, NULL)";
                        int m = st.executeUpdate(sql);
                        System.out.println(" You have been registered to this course");
                        }
                    }
                    else
                        System.out.println("You are not eligible for the course");
                }
                else
                    System.out.println("You are not eligible for the course");
            }
        }
        else {
            cnt /= 2;
            System.out.println("Select "+cnt+" courses from elective courses offered");
            while(cnt > 0)
            {
                System.out.println("Enter the name of the chosen elective");
                String elec = sc.nextLine();
                sql = "select prerequisite from Courses where course_name ='" + elec+"'";
                ResultSet rs1 = st.executeQuery(sql);
                sql = "select grade from "+rs1.getString(1)+"where roll_no = '"+ r_no+"'";
                rs1 = st.executeQuery(sql);
                
                if(rs1.next())
                {
                    if(rs1.getString(1) != "F")
                    {
                        sql = "select count(roll_no) from " + elec;
                        rs1 = st.executeQuery(sql);
                        if(rs1.getInt(1) >= 80) {
                        System.out.println(" No seats available in this elective. Choose some other elective");
                        cnt++;
                        }
                        else {
                        sql = "insert into " + rs.getString(1) + " values ('" + r_no + "' , NULL, NULL)";
                        int m = st.executeUpdate(sql);
                        System.out.println(" You have been registered to this course");
                        }
                    }
                    else
                        System.out.println("You are not eligible for the course");
                }
                cnt--;
            }
        }
    }
    static void suggest_course() throws SQLException{
        
        System.out.println("Please enter the name of the course you want to suggest :");
        String a = sc.nextLine();
        System.out.println("Which year is eligible to take this course :");
        String b = sc.nextLine();
        System.out.println("Which department the course comes under");
        String c = sc.nextLine();
        System.out.println("How much credits to complete this course");
        int d = sc.nextInt();
        System.out.println("Enter the type of the course : Core or Elective");
        String e = sc.nextLine();
        System.out.println("Enter the prerequisite for the course");
        String f = sc.nextLine();
        Statement st = con.createStatement();
        String sql = "select name from Courses where course_name = '"+a+"' and year = '"+b+"' and dept = '"+c+"' and credits = "+d+" and type = '"+e+"' and prerequisite = '"+f+"'";
        ResultSet rs = st.executeQuery(sql);
        if(rs.next())
        {
            System.out.println("The suggested course already exists"); 
        }
        else
        {
            sql = "insert into Courses values('"+a+"', '"+b+"', '"+c+"', "+d+", '"+e+"', '"+f+"')";
            int m = st.executeUpdate(sql);
            sql = "create table "+a+" roll_no varchar(10), teacher_ID varchar(10), grade varchar(2)"; 
            m = st.executeUpdate(sql);
        }
    }
    static void student_details(int i, String r_no)  throws SQLException {
        
        System.out.println("Enter the name of the course: ");
        String cou = sc.nextLine();
        if(i == 0)
        {
            String sql = "select * from " + cou + " where roll_no = '" + r_no+"'";
            ResultSet rs = st.executeQuery(sql);
            if(rs.next())
                System.out.println(cou + " : " + r_no + " ---- Grade: " + rs.getString(3));
            else
                System.out.println(" The student is not registered for this course");
        }
        if(i == 1)
        {
            System.out.println("Enter the updated grade of the student");
            String g = sc.nextLine(); 
            String sql = "update "+cou+" set grade = "+g+" where roll_no = '"+r_no+"'";
            int m = st.executeUpdate(sql);
            if(m == 0)
                System.out.println("The student is not registered for this course");
        }
    }
    
   public static void main(String[] args) {

    try {
		    Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(DB_URL,USER,PASS);
	        st = con.createStatement();
	        System.out.println("------------------------Welcome------------------------");
	        while(true) {
	            String ch = sc.nextLine();
	            if(ch.equals("I")) {
	                System.out.println("Please Enter you Login Credentials");
	                System.out.println("Teacher ID : ");
	                String ID = sc.nextLine();
	                System.out.println("Password :");
	                String Password = sc.nextLine();
	                String sql = "select name from Instructors where id = '"+ID+"' and password = '"+Password+"'";
	                ResultSet rs = st.executeQuery(sql);
	                if(rs.next())
	                {
	                    String name = rs.getString(1);
	                    System.out.println("Welcome " + name);
	                    while(true) {
	                        System.out.println("Press 1 to suggest a course");
	                        System.out.println("Press 2 to view student data");
	                        System.out.println("Press 3 to update student data");
	                        int c = sc.nextInt();
	                        String r = new String();
	                        switch(c)
	                        {
	                            case 1:
	                            	try {
	                                suggest_course();
	                            	}
	                            	catch(SQLException se) {
	                            		se.printStackTrace();
	                            	}
	                                break;
	                            case 2:
	                                System.out.println("Enter the rollno of student whose details are to be viewed");
	                                r = sc.nextLine();
	                                try {
	                                student_details(0,r);
	                                }
	                            	catch(SQLException se) {
	                            		se.printStackTrace();
	                            	}
	                                break;
	                            case 3:
	                                System.out.println("Enter the rollno of student whose grade is to be updated");
	                                r = sc.nextLine();
	                                try {
	                                	student_details(1,r);
	                                }
	                            	catch(SQLException se) {
	                            		se.printStackTrace();
	                            	}
	                                break;
	                            default:
	                                System.out.println("Invalid input");
	                                break;
	                        }
	                        System.out.println("Would you like to continue?");
	                        String ab1 = sc.nextLine();
	                        if(ab1.equals("Y"))
	                            continue;
	                        else
	                            break;
	                    }
	                }
	                else
	                {
	                    System.out.println("Invalid ID or password");
	                    System.out.println("Login again");
	                }
	            }
	            else if(ch.equals("S")) {
	                System.out.println("Please Enter you Login Credentials");
	                System.out.println("Roll No. : ");
	                String RollNo = sc.nextLine();
	                System.out.println("Password");
	                String Password = sc.nextLine();
	                String year = RollNo.substring(0,2);
	                String sql = "select * from Y" + RollNo.substring(0,2) + " where roll_no = '" + RollNo + "' and password = '" + Password+"'";
	                ResultSet rs = st.executeQuery(sql);
	                if(rs.next()) {
	                    while(true) {
	                        System.out.println("Press 1 for Course Registration");
	                        System.out.println("Press 2 for viewing your grades");
	                        int c = sc.nextInt();
	                        switch(c)
	                        {
	                            case 1:
	                            	try {
	                            		course_reg(RollNo, rs.getString(5), rs.getString(4));
	                            	}
	                            	catch(SQLException se) {
	                            		se.printStackTrace();
	                            	}
	                                break;
	                            case 2:
	                                try {
	                                	student_details(0, RollNo);
	                        		}
		                        	catch (SQLException se) {
		                        		se.printStackTrace();
		                        	}
	                                break;
	                                
	                            default:
	                                System.out.println("Invalid input");
	                                break;
	                        }
	                    System.out.println("Would you like to continue?");
	                    String ab1 = sc.nextLine();
                        if(ab1.equals("Y"))
                            continue;
                        else
                            break;
	                    }
	                }
	                else {
	                    System.out.println("Invalid Login Credentials");
	                    System.out.println("Please Try Again");
	                }
	            }
	            else {
	                System.out.println(" Invalid Input");
	            }
	            System.out.println("Would you like to continue?");
                String ab1 = sc.nextLine();
                if(ab1.equals("Y"))
                    continue;
                else
                    break;	        
	        }
    	}
	    catch(SQLException se) {
        	se.printStackTrace();
        }
        catch(Exception e) {
        	e.printStackTrace();
        }
        finally {
        	try {
        		if (con != null);
        			con.close();
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
        }
        System.out.println("Thank You");//end try
}
}
