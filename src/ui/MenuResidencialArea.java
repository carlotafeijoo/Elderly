package ui;

import java.io.*;
import java.security.MessageDigest;

import java.text.SimpleDateFormat;

import java.util.List;

import Interfaces.*;
import exceptions.InputException;
import jdbc.*;
import jpa.JPAUserManager;
import POJOS.*;
import ResidencialAreaXML.XMLManagerImpl;

public class MenuResidencialArea {

	private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	
	private static ElderlyManager elderlyManager;
	
	private static FamilyContactManager familyContactManager;
	
	
	
	private static StaffManager staffManager;
	
	private static UserManager userManager;
	
	private static TaskManager tasksManager;
	
	private static XMLManager xmlmanager;
	
	
	
	
	
	public static void main(String[] args) {

		System.out.println("WELCOME TO THE RESIDENCIAL AREA DATA BASE");
		JDBCManager jdbcManager = new JDBCManager();

		//initialize database JDBC
		elderlyManager = new JDBCElderlyManager(jdbcManager);
		familyContactManager = new JDBCFamilyContactManager(jdbcManager);
		staffManager = new JDBCStaffManager(jdbcManager);
		tasksManager = new JDBCTasksManager(jdbcManager);
		// initialize database JPA
		userManager = new JPAUserManager();
		xmlmanager= new XMLManagerImpl();
		

		mainMenu();
	}
	
	public static void mainMenu() {
		try {
			
		
			int option;
			do {
				System.out.println("MAIN MENU ");
				System.out.println("1. I am an administrator ");
				System.out.println("2. I am a family contact ");
				System.out.println("3. I am a staff member  ");
				System.out.println("4. Exit ");
				option = InputException.getInt("Introduce the number choice:  ");

				switch (option) {

				case 1:
					elderlyMenu();
					break;
					
				case 2:
					loginFC();
					
					break;
					
				case 3:
					loginStaff();
					break;
					
				case 4:
					
					//JDBCManager.disconnect();
					System.exit(4);

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	private static void loginFC() throws Exception {
		

		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Exit");
		int choice = InputException.getInt(" Introduce the number of your choice: ");
	
		switch (choice) {
			case 1:
				// Call method REGISTER
				registerFamilyContact();
				loginFC();
	
				break;
			case 2:
				// LOG IN as family contact
				logIn();
	
				break;
	
			case 3:
				// EXIT
				mainMenu();
			default:
				break;
		}
		
	}

	private static void loginStaff() throws Exception {
	

		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Exit");
		int choice = InputException.getInt(" Introduce the number of your choice: ");
	
		switch (choice) {
			case 1:
				// Call method REGISTER
				registerStaff();
				loginStaff();
	
				break;
			case 2:
				// LOG IN as staff member
				logIn();
	
				break;
	
			case 3:
				// EXIT
				mainMenu();
			default:
				break;
		}
	
	}



	public static void registerFamilyContact() throws Exception {
	
		System.out.println("Input information: ");
	
		String name = InputException.getString("Name: ");
		String email = InputException.getString("Email: ");
		Integer phone = InputException.getInt("Phone: ");
		String address = InputException.getString("Address: ");
		
		String username = InputException.getString("Username: ");
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
	
	
		// CREATE FC AND ADD TO JPA
		User u = new User(username, digest);
		Role role = userManager.getRole("FamilyContact");
		u.setRole(role);
		role.addUser(u);
		userManager.newUser(u);
	
		// CREATE FAMILY CONTACT AND ADD TO JDBD
		FamilyContact fc = new FamilyContact(name, phone, email, address);
	
		familyContactManager.addFamilyContact(fc);
		System.out.println("Register sucessfull!");
	}
	
	public static void registerStaff() throws Exception {
		
		System.out.println("Input information: ");
	
		String name = InputException.getString("Name: ");
		
		String field = null;
		int choice;
		do {

			System.out.println("1.Carer");
			System.out.println("2.Cleaner");
			System.out.println("3.Chef");
			System.out.println("4.Animator");

			choice = InputException.getInt("Chose field: ");
			switch (choice) {

			case 1:
				field = "Carer";
				break;
			case 2:
				field = "Cleaner";
				break;
			case 3:
				field = "Chef";
			default:
				break;
			}
		} while (choice < 1 || choice > 4);
		
		String address = InputException.getString("Address: ");
		String email = InputException.getString("Email: ");
		Integer phone = InputException.getInt("Phone: ");
		System.out.println("Enter the year of birth:");
        int year = Integer.parseInt(read.readLine());

        System.out.println("Enter the month of birth:");
        int month = Integer.parseInt(read.readLine());

        System.out.println("Enter the day of birth:");
        int day = Integer.parseInt(read.readLine());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dobStr = String.format("%04d/%02d/%02d", year, month, day);
        java.util.Date utilDate = dateFormat.parse(dobStr);
        java.sql.Date dob = new java.sql.Date(utilDate.getTime());

		
		String username = InputException.getString("Username: ");
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
	
		// CREATE STAFF AND ADD TO JPA
		User u = new User(username, digest);
		Role role = userManager.getRole("Staff");
		u.setRole(role);
		role.addUser(u);
		userManager.newUser(u);
	
		// CREATE STAFF AND ADD TO JDBD
		Staff staff = new Staff(name, phone,dob, address, field, email);
		staff.setField(field); 
		
		
		staffManager.addStaffMember(staff);
		System.out.println("Register sucessfull!");
		
	}

	public static void logIn() throws Exception {
	
		System.out.println("Username :");
		String username = read.readLine();
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();
		
	
		
		User u = userManager.checkPassword(username, digest);
	
		if (u == null) {
			System.out.println("User not found");
			mainMenu(); 
		}
		
		//depending on the type of user we open a different menu
		if(u!= null && u.getRole().getName().equals("Staff")) {
			Integer id=u.getId();
			
			int staff_id = staffManager.searchStaffIdfromUId(id);
			Staff staff = staffManager.searchStaffbyId(staff_id);
			System.out.println(staff);
			System.out.println("Login successful!");
			staffMenu(u.getId());
			
		}
		if (u != null && u.getRole().getName().equals("FamilyContact")) {
			Integer id = u.getId();
			
			int familycontact_id = familyContactManager.searchFamilycontacIdfromUId(id);
			System.out.println(familycontact_id);
			FamilyContact fc = familyContactManager.searchFamilyContactbyId(familycontact_id);
			System.out.println(fc);
			System.out.println("Login successful!");
			familyContactMenu(u.getId());
	
		}
	}


	
	private static void familyContactMenu(int User_id) {
		
		try {
			int choice;
			do {
				System.out.println("1. Update information. ");
				System.out.println("2. Exit. ");
				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					
					int family_id = familyContactManager.searchFamilycontacIdfromUId(User_id);
					System.out.println(family_id);
					FamilyContact familyToUpdate = familyContactManager.searchFamilyContactbyId(family_id);
					System.out.println(familyToUpdate);
					if (familyToUpdate != null) {
					    int newPhone = InputException.getInt("Enter your new phone number: ");
					    familyToUpdate.setPhone(newPhone);
					    String newAddress = InputException.getString("Enter your new address: ");
					    familyToUpdate.setAddress(newAddress);
					    familyContactManager.updateFamilyContactInfo(familyToUpdate);
					    System.out.println("Information updated successfully.");
					} else {
					    System.out.println("Family contact update fail.");
					}
				    break;
					
				case 2:
					mainMenu();
				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	private static void elderlyMenu() {
		
		try {
			int choice;
			do {
				
				System.out.println("1. Add an elderly to the database.  ");
				System.out.println("2. Update the information of an elderly. ");
				System.out.println("3. Get the list of all elderlies. ");
				System.out.println("4. Load new elderlies. ");
				System.out.println("5. Print me");
				System.out.println("6. Back");
				

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					
					addElderly();
					break;
					
				case 2:
					int elderly_id = InputException.getInt("Introduce the id of the elderly that is going to be updated:");
					Elderly elderlyToUpdate = elderlyManager.searchElderlyById(elderly_id);
					if (elderlyToUpdate != null) {
					    int newAge = InputException.getInt("Enter the new age for the elderly: ");
					    elderlyToUpdate.setAge(newAge);
					    elderlyManager.updateInfo(elderlyToUpdate);
					    System.out.println("Elderly information updated successfully.");
					} else {
					    System.out.println("Elderly not found with the provided id.");
					}
				    break;
				case 3:
					
					getListOfElderlies();
					break;	
					
				case 4: 
					loadElderly(); //xml
				
				case 5:
					printMe(); //xml
				case 6:
					mainMenu();
				default:
					break;
					
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static void loadElderly() {
		File file = new File("./xmls/External-Elderly.xml");
		System.out.println(xmlmanager.xml2Elderly(file));
		
	}

	private static void printMe() {
		int id=InputException.getInt("Introduce the id of the elderly wanted to be printed:" );
		xmlmanager.staff2xml(id);
		
	}

	public static void addElderly() throws Exception {

		System.out.println("Input the information of the new elderly: ");
 
		String name = InputException.getString(" Name: ");
		
		int age=InputException.getInt("Age:  ");

		Elderly elderly = new Elderly(name, age);
 
		
		elderlyManager.addElderly(elderly);
		elderlyMenu();

	}
	
	

	
	private static void getListOfElderlies() throws IOException, Exception {
		
		System.out.println("The list of elderlies is: ");
		List<Elderly> resultado = elderlyManager.getListOfElderly();
		System.out.println(resultado);
		elderlyMenu();
		
	}
	
	
    private static void staffMenu(int User_id) {
    
		
		try {
			
			
			int choice;
			do {
				System.out.println("1.Update information. ");
				System.out.println("2.Register new task. ");
				System.out.println("3.List all the tasks. ");
				
				System.out.println("4.Print me.  ");
				System.out.println("5.Load new staff members. ");
				System.out.println("6.Back.  ");
			

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				
				case 1:
					int staff_id = staffManager.searchStaffIdfromUId(User_id);
					Staff staffToUpdate =staffManager.searchStaffbyId(staff_id);
					if(staffToUpdate !=null) {
						int newPhone = InputException.getInt("Enter your new phone number: ");
						staffToUpdate.setPhone(newPhone);
						String newAddress = InputException.getString("Enter your new address: ");
						staffToUpdate.setAddress(newAddress);
						staffManager.updateStaffMemberInfo(staffToUpdate);
						 System.out.println("Information updated successfully! ");
					} else {
					    System.out.println("Staff update fail.");
					}
				    break;
					
					
				case 2:
					
					int staffToAssignNewTask_id = staffManager.searchStaffIdfromUId(User_id);
					
					addTask(staffToAssignNewTask_id);
					
					System.out.println("Task added sucessfully!");
					
					
					break;
					
				case 3:
					int staffAllTask_id = staffManager.searchStaffIdfromUId(User_id);
					
					List <Task> tasksList = tasksManager.getListOfTasks(staffAllTask_id);
					
					System.out.println("List of tasks: " +tasksList);
					
					break;
				case 4: 
					printMe();
				break;
				case 5:
					loadStaff();
					
				case 6:
					mainMenu();
					
				default:
					break;
					
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private static void loadStaff() {
    	File file = new File("./xmls/External-Staff.xml");
		System.out.println(xmlmanager.xml2Staff(file));
		
	}
    
    public static void addTask(int staffToAssignNewTask_id) throws Exception {

		System.out.println("Input the information of the new task: ");
 
		String description = InputException.getString("Description: ");
		

		Task task = new Task(description,staffToAssignNewTask_id );
 
		
		tasksManager.addTask(task);
		

	}

	
    
    

	
	
}
