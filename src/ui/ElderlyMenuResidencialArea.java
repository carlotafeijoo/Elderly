package ui;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BITalino.BitalinoDemo;
import exceptions.InputException;
import POJOS.*;

public class ElderlyMenuResidencialArea {
	static OutputStream os = null;
	static PrintWriter pw = null;
	static FileInputStream fis = null;
	static DataOutputStream dos = null;
	static InputStreamReader isr = null;
	static BufferedInputStream bis = null;
	static BufferedOutputStream bos = null;

	static BufferedReader br = null;
	static Socket so = null;
	private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

	/**
	The main method to start the client application.
	@param args the command line arguments
	@throws IOException if an I/O error occurs
	This method establishes a connection to the server, sets up input and output streams, and displays the main menu for the client application.
	 */
	public static void main(String[] args) throws IOException {

		so = new Socket("localhost", 9009); //cambiar localhost x IP_server
		//Client:reads and writes lines
		br = new BufferedReader(new InputStreamReader(so.getInputStream()));
		os = so.getOutputStream();
		pw = new PrintWriter(os, true);

		System.out.println("\nPATIENT! WELCOME TO THE RESIDENCIAL AREA DATA BASE");
		mainMenu();

	}
	/**
	Releases the resources associated with the PrintWriter, BufferedReader, OutputStream, and Socket.
	@param printWriter the PrintWriter to be closed
	@param br the BufferedReader to be closed
	@param outputStream the OutputStream to be closed
	@param so2 the Socket to be closed
	 */
	private static void releaseResources(PrintWriter printWriter, BufferedReader br, OutputStream outputStream,
			Socket so2) {
		printWriter.close();
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			so2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	Displays the main menu and handles user input.
	 */
	public static void mainMenu() {
		try {


			int option;
			do {
				System.out.println("\nMAIN MENU ");
				System.out.println("1. Enter ");
				System.out.println("2. Exit ");
				option = InputException.getInt("\nIntroduce the number choice:  ");
				System.out.println("\n--------------------------------------------------------------------------------");

				switch (option) {

				case 1:
					loginElderly();
					break;

				case 2:
					System.out.println("YOU HAVE EXIT THE RESIDENCIAL AREA DATA BASE");
					pw.println("stop");
					releaseResources(pw, br, os, so);

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
	/**
	Handles the login process for the elderly users.
	@throws Exception if an error occurs
	 */
	private static void loginElderly() throws Exception {
		System.out.println("\nMENU");
		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Back");
		int choice = InputException.getInt("\nIntroduce the number of your choice: ");
		System.out.println("\n--------------------------------------------------------------------------------");
		
		switch (choice) {
		case 1:
			// Call method REGISTER
			System.out.println("\n\tREGISTER");
			registerElderly();
			loginElderly();
			break;

		case 2:
			//LOG IN
			System.out.println("\n\tLOGIN");
			logIn();
			break;

		case 3:
			// BACK
			loginElderly();
			break;

		default:
			break;
		}
	}
	/**
	Handles the registration process for elderly users.
	@throws Exception if an error occurs
	 */
	public static void registerElderly() throws Exception {

		System.out.println("\tREGISTER ELDERELY ");

		System.out.println("\nInput the information of the new elderly: ");

		String name = InputException.getString("Name: ");

		System.out.println("Enter the year of birth:");
		int year = Integer.parseInt(read.readLine());

		System.out.println("Enter the month of birth:");
		int month = Integer.parseInt(read.readLine());

		System.out.println("Enter the day of birth:");
		int day = Integer.parseInt(read.readLine());

		if (checkDate(year, month, day)==false) {
			System.out.println("\nSorry your date of birth is worng, try again \n");
			System.out.println("\n--------------------------------------------------------------------------------");
			
		}else {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String dobStr = String.format("%04d-%02d-%02d", year, month, day);
			java.util.Date utilDate = dateFormat.parse(dobStr);
			java.sql.Date dob = new java.sql.Date(utilDate.getTime());

			System.out.println("\nYour dni will be used as your username");
			System.out.println("DNI without letter:  ");
			int dni = Integer.parseInt(read.readLine());

			pw.println("searchAllDoctors");

			ArrayList <Doctor>doctores = new ArrayList<>();
			String cantidad_doctores_text=br.readLine();
			int cantidad_doctores=Integer.parseInt(cantidad_doctores_text);
			for(int i = 0; i < cantidad_doctores; i++) {

				String doctor_text=br.readLine();
				Doctor doctor=new Doctor(doctor_text);
				doctores.add(doctor);

			}


			for (int i = 0; i < doctores.size(); i++) {
				System.out.println(doctores.get(i).toStringForPatients());
			}

			System.out.println("\nWrite the id of your doctor: ");
			String text_doctor_id = read.readLine();
			int doctor_id = Integer.parseInt(text_doctor_id);
			if(checklist(doctor_id, doctores)==true) {
				//Integer doctor_id = InputException.getInt("Put the id of your doctor: ");
				String username = "" + dni;
				System.out.println(doctor_id);

				Elderly elderly = new Elderly(name, dni, dob, doctor_id);			
				String password = InputException.getString("Password: ");

				pw.println("addElderly");
				pw.println(username);
				pw.println(password);
				pw.println(elderly.toString());

				System.out.println(br.readLine());
				System.out.println("\n--------------------------------------------------------------------------------");
				
			}else {
				System.out.println("\nSorry, the doctor id that you introduced is not valid");
				System.out.println("\n--------------------------------------------------------------------------------");
			}
		}

	}
	/**
	Handles the login process for elderly users.
	@throws Exception if an error occurs
	 */
	public static void logIn() throws Exception {

		System.out.println("\tLOG IN ELDELY ");

		System.out.println("\nDni without letter:");
		String username = read.readLine();
		String password = InputException.getString("Password: ");

		pw.println("checkPassword");
		pw.println(username);
		pw.println(password);

		String role_text = br.readLine();
		String user_text = br.readLine();

		User u = null;

		if(user_text.equalsIgnoreCase("error")) {
			System.out.println("\nUser not found");
			System.out.println("\n--------------------------------------------------------------------------------");
			mainMenu();
		}
		else {
			u = new User(user_text);
			u.setRole(new Role(role_text));
		}


		if (u != null && u.getRole().getName().equals("Elderly")) {
			Integer id = u.getId();

			pw.println("searchElderlyIdfromUId");
			pw.println(""+id);
			String elderly_id_text = br.readLine();

			int elderly_id = Integer.parseInt(elderly_id_text);

			pw.println("searchElderlyById");

			pw.println(elderly_id);

			String elderly_text = br.readLine();
			Elderly elderly = new Elderly(elderly_text);


			System.out.println("\nThis is your user:");
			System.out.println( "\tName = " + elderly.getName() +
					"\n\tDNI = " + elderly.getDni() + 
					"\n\tdoctor_id = " + elderly.getDoctor_id() + 
					"\n\tdob = " + elderly.getDob() + 
					"\n\tsymptoms = " + elderly.getSymptoms());

			System.out.println("\nLogin successful!");
			System.out.println("\n--------------------------------------------------------------------------------");
			elderlyMenu(u.getId());

		}

	}
	/**
	Displays the menu for elderly users and handles user input.
	@param User_id the ID of the user
	 */
	private static void elderlyMenu(int User_id) {

		try {
			int choice;
			do {

				System.out.println("\nMENU");
				System.out.println("1. Record signal.  ");
				System.out.println("2. See my tasks");
				System.out.println("3. Add symptoms");
				System.out.println("4. Back");

				choice = InputException.getInt("\nIntroduce your choice: ");
				System.out.println("\n--------------------------------------------------------------------------------");
				
				switch (choice) {

				case 1:
					System.out.println("\n\tRECORDING SIGNAL");
					//find elderly id from user id
					pw.println("searchElderlyIdfromUId"); 
					pw.println(User_id);
					String eld_id_string = br.readLine();
					int eld_id = Integer.parseInt(eld_id_string);
					System.out.println(eld_id);

					//find elderly name from elderly Id
					pw.println("searchElderlyNameById"); 
					pw.println(eld_id);
					String eld_name_string = br.readLine();
					System.out.println("\n" +eld_name_string);


					//PRINTS LISTS OF TASK AND THEIR ID TO ENTER DESIRED TASK
					pw.println("seeTasksandId"); 
					pw.println(eld_id);

					ArrayList <Task> tasks = new ArrayList<>();
					String cantidad_tasks_text=br.readLine();
					int cantidad_tasks=Integer.parseInt(cantidad_tasks_text);

					for(int i = 0; i < cantidad_tasks; i++) {

						String tasks_text=br.readLine();
						Task task=new Task(tasks_text);
						tasks.add(task);
					}

					System.out.println("\nList of tasks: ");
					for (Task t : tasks) {
						System.out.println(t.getTask_id()+", " + t.getDescription());
					}

					if (tasks.isEmpty()) {
						System.out.println("\nYou don't have any tasks assigned");
						System.out.println("\n--------------------------------------------------------------------------------");
						break;
					}

					System.out.println("\nEnter id of the task you want to perform");
					System.out.println("\n--------------------------------------------------------------------------------");
					String task_id_text = read.readLine();
					int task_id = Integer.parseInt(task_id_text);


					//find task duration from elderly Id
					pw.println("searchTaskDurationByELDid"); 

					pw.println(task_id_text);
					String duration_text = br.readLine();
					int duration = Integer.parseInt(duration_text);


					//Calls the function of Bitalino to start reading data
					System.out.println("\nEnter MAC of Bitalino");
					String MACBitalino = " ";

					boolean pattern = false;

					while (pattern==false) {
						System.out.println("Please, introduce a correct Bitalino MAC as the following structure xx:xx:xx:xx:xx:xx");
						MACBitalino = read.readLine();
						pattern = checkMAC(MACBitalino);
					}

					File filetxt = BitalinoDemo.collectDataBitalino(eld_name_string, MACBitalino, duration);
					readAndSendrecord(filetxt,task_id,eld_id);
					break;




				case 2:
					System.out.println("\n\tSEE MY TASKS");
					pw.println("searchElderlyIdfromUId"); //find id doctor from User id
					pw.println(User_id);
					String elderly_id_string = br.readLine();

					//List<Task> tasksList = null;// tasksManager.getListOfTasks(doctorAllTask_id);
					pw.println("seeTasks"); //find list task from doctor id
					pw.println(elderly_id_string);

					List <Task> tasks2 = new ArrayList<>();
					String task_size_txt = br.readLine();
					int task_size = Integer.parseInt(task_size_txt);

					for(int i = 0; i < task_size; i++) {

						String tasks_text=br.readLine();
						Task task2=new Task(tasks_text);
						tasks2.add(task2);
					}
					if(tasks2.isEmpty()==true) {
						System.out.println("\nSorry, at this time you dont have any task associated");
						break;
					}else {
						System.out.println("\nList of tasks: ");
						for (Task t : tasks2) {
							System.out.println(t);
						}
						break;
					}

				case 3:
					System.out.println("\n\tADD SYMPTOMS");
					pw.println("searchElderlyIdfromUId"); 
					pw.println(User_id);
					String e_id_string = br.readLine();


					System.out.println("\nIntroduce all your symptoms:");
					String symptom = read.readLine();

					pw.println("addSymptoms");
					pw.println(e_id_string);
					pw.println(symptom);
					System.out.println("\nSymptoms updated");

					break;

				case 4:
					loginElderly();
					break;

				default:
					System.out.println("\nPlease, introduce option from 1 to 4.\n");
					elderlyMenu(User_id);

				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	Checks if a doctor ID is present in the list of doctors.
	@param doc_id the doctor ID to be checked
	@param doc the list of doctors
	@return true if the doctor ID is present, otherwise false
	 */
	public static boolean checklist(int doc_id,  List <Doctor> doc) {
		boolean check= false;
		for (int i=0; i < doc.size();i++ ) {
			if (doc.get(i).getdoctor_id() == doc_id) {
				check = true;
			}
		}
		return check;
	}

	/**
	Checks if the provided MAC address matches the specified pattern.
	@param mac the MAC address to be checked
	@return true if the MAC address matches the pattern, otherwise false
	 */
	public static boolean checkMAC(String mac) {

		String pattern_str = "\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}";
		Pattern pattern = Pattern.compile(pattern_str);
		Matcher matcher = pattern.matcher(mac);

		return matcher.matches();

	}
	/**
	Checks if the provided year, month, and day form a valid date.
	@param year the year
	@param month the month
	@param day the day
	@return true if the date is valid, otherwise false
	 */
	public static boolean checkDate(int year, int month, int day) {
		if (year < 1900 || year > 2024) {
			return false;
		}
		if (month < 1 || month > 12) {
			return false;
		}
		if (day < 1 || day > 31) {
			return false;
		}
		if (month == 2) {
			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
				if (day > 29) {
					return false;
				}
			} else {
				if (day > 28) {
					return false;
				}
			}
		}
		else if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (day > 30) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Reads the contents of a file and sends them over a socket.
	 * 
	 * @param filetxt the File object representing the file to be read and sent
	 * @param task_id the ID of the task
	 * @param elderly_id the ID of the elderly person
	 */
	private static void readAndSendrecord(File filetxt, int task_id, int elderly_id) {	
		pw.println("storeRecord");
		String name = filetxt.getName();
		pw.println(name);
		pw.println(task_id);
		pw.println(elderly_id);

		FileInputStream fileinputstream = null;
		InputStreamReader inputstreamreader = null;
		BufferedReader bufferedreader = null;
		try {
			fileinputstream = new FileInputStream(filetxt);
			inputstreamreader = new InputStreamReader(fileinputstream);
			bufferedreader = new BufferedReader(inputstreamreader);
			String texto = "";
			String stringleido;
			while (true) {
				stringleido = bufferedreader.readLine();
				if (stringleido == null) {
					break;
				}
				texto = texto + stringleido + ",";

			}
			pw.println(texto);


		} catch (IOException ioe) {
			System.out.println("\nError durante el proceso\t" + ioe);
		} finally {
			try {  
				if (bufferedreader != null) {
					bufferedreader.close();
				}
			} catch (IOException ioe) {
				System.out.println("\nError durante el proceso\t" + ioe);
			}
			try {
				if (inputstreamreader != null) {
					inputstreamreader.close();
				}
			} catch (IOException ioe) {
				System.out.println("\nError durante el proceso\t" + ioe);
			}
			try {
				if (fileinputstream != null) {
					fileinputstream.close();
				}
			} catch (IOException ioe) {
				System.out.println("\nError durante el proceso\t" + ioe);
			}

		}

	}



}
