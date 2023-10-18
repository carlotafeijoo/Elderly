package ui;

import java.io.*;


import java.security.MessageDigest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exceptions.InputException;
import POJOS.*;

public class ElderlyMenuResidencialArea {

	private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

	//private static ElderlyManager elderlyManager;

	//private static DoctorManager DoctorManager;

	//private static UserManager userManager;

	//private static TaskManager tasksManager;
	public static void main(String[] args) {

		System.out.println("WELCOME TO THE RESIDENCIAL AREA DATA BASE");
		/*
		JDBCManager jdbcManager = new JDBCManager();

		// initialize database JDBC
		elderlyManager = new JDBCElderlyManager(jdbcManager);
		DoctorManager = new JDBCDoctorManager(jdbcManager);
		tasksManager = new JDBCTasksManager(jdbcManager);
		// initialize database JPA
		userManager = new JPAUserManager();*/
		mainMenu();

	}

	public static void mainMenu() {
		try {

			int option;
			do {
				System.out.println("MAIN MENU ");
				System.out.println("1. Enter ");
				System.out.println("2. Exit ");
				option = InputException.getInt("Introduce the number choice:  ");

				switch (option) {

				case 1:
					loginElderly();
					break;



				case 2:
					System.out.println("YOU HAVE EXIT THE RESIDENCIAL AREA DATA BASE");
					System.exit(3);
					break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private static void elderlyMenu(int User_id) {

		try {
			int choice;
			do {

				System.out.println("1. Record signal.  ");
				System.out.println("2. See my tasks");
				System.out.println("3. Back");

				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					//AQUI: FUNCION DE LLAMAR AL BITALINO
					break;
				case 2:
					int elderly_id= 0;//elderlyManager.searchElderlyIdfromUId(User_id);
                 List <Task> tasks=null;//elderlyManager.seeTasks(elderly_id);
                 for (int i=0;i<tasks.size();i++) {
                	 System.out.println(tasks.get(i).toStringtoElderly());
                 }
					break;				
					
				case 3:
						mainMenu();
						break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void registerElderly() throws Exception {

		System.out.println("Input the information of the new elderly: ");

		String name = InputException.getString(" Name: ");

		int day = InputException.getInt("Day of birth:  ");
		int month = InputException.getInt("Month of birth:  ");
		int year = InputException.getInt("Year of birth:  ");
		int dni = InputException.getInt("DNI without letter:  ");

		ArrayList<Doctor> doctores = null; //DoctorManager.searchAllDoctors();

		for (int i = 0; i < doctores.size(); i++) {
			System.out.println(doctores.get(i).toStringForPatients());
		}
		int doctor_id = InputException.getInt("put the id of your doctor:  ");

		System.out.println(" info: your username will be your dni ");
		Date dob = new Date(year, month, day);

		String username = "" + dni;
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		User u = new User(username, digest);
		Role role = null;//userManager.getRole("Elderly");
		u.setRole(role);
		role.addUser(u);
		//userManager.newUser(u);

		Elderly elderly = new Elderly(name, dni, dob, doctor_id);

		//elderlyManager.addElderly(elderly);
		elderlyMenu(u.getId());

	}



	public static void logIn() throws Exception {

		System.out.println("Username or dni without letter:");
		String username = read.readLine();
		String password = InputException.getString("Password: ");
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] digest = md.digest();

		User u = null;//userManager.checkPassword(username, digest);

		if (u == null) {
			System.out.println("User not found");
			mainMenu();
		}



		if (u != null && u.getRole().getName().equals("Elderly")) {
			Integer id = u.getId();

			int elderly_id = 0;//elderlyManager.searchElderlyIdfromUId(id);

			Elderly elderly =null;// elderlyManager.searchElderlyById(elderly_id);

			System.out.println(elderly);
			System.out.println("Login successful!");
			elderlyMenu(u.getId());

		}

	}



	private static void loginElderly() throws Exception {
		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Exit");
		int choice = InputException.getInt(" Introduce the number of your choice: ");

		switch (choice) {
		case 1:
			// Call method REGISTER
			registerElderly();
			break;

		case 2:
			// LOG IN as doctor member
			logIn();
			break;

		case 3:
			// EXIT
			mainMenu();
			break;

		default:
			break;
		}
	}



}
