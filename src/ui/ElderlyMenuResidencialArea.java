package ui;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


	public static void main(String[] args) throws IOException {

		System.out.println("WELCOME TO THE RESIDENCIAL AREA DATA BASE");
		
		so = new Socket("localhost", 9009);
		//Client:reads and writes lines
		br = new BufferedReader(new InputStreamReader(so.getInputStream()));
		os = so.getOutputStream();
		pw = new PrintWriter(os, true);

		mainMenu();

	}
	
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
	
	private static void loginElderly() throws Exception {
		System.out.println("1. Register");
		System.out.println("2. Log in ");
		System.out.println("3. Back");
		int choice = InputException.getInt(" Introduce the number of your choice: ");

		switch (choice) {
		case 1:
			// Call method REGISTER
			registerElderly();
			loginElderly();
			break;

		case 2:
			//LOG IN
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
	
	public static void registerElderly() throws Exception {

		System.out.println("Input the information of the new elderly: ");

		String name = InputException.getString("Name: ");

		System.out.println("Enter the year of birth:");
		int year = Integer.parseInt(read.readLine());

		System.out.println("Enter the month of birth:");
		int month = Integer.parseInt(read.readLine());

		System.out.println("Enter the day of birth:");
		int day = Integer.parseInt(read.readLine());

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dobStr = String.format("%04d-%02d-%02d", year, month, day);
		java.util.Date utilDate = dateFormat.parse(dobStr);
		java.sql.Date dob = new java.sql.Date(utilDate.getTime());

		System.out.println("Your dni will be used as your username");
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
		
		System.out.println("Put the id of your doctor: ");
		String text_doctor_id = read.readLine();
		int doctor_id = Integer.parseInt(text_doctor_id);
		//Integer doctor_id = InputException.getInt("Put the id of your doctor: ");
		String username = "" + dni;
		System.out.println(doctor_id);
		
		Elderly elderly = new Elderly(name, dni, dob, doctor_id);
		System.out.println("Elderly main " + elderly);
		
		String password = InputException.getString("Password: ");
		
		pw.println("addElderly");
		pw.println(username);
		pw.println(password);
		pw.println(elderly.toString());

		br.readLine();

	}
	
	public static void logIn() throws Exception {

		System.out.println("Dni without letter:");
		String username = read.readLine();
		String password = InputException.getString("Password: ");

		pw.println("checkPassword");
		pw.println(username);
		pw.println(password);

		String role_text = br.readLine();
		String user_text = br.readLine();
		
		User u = null;

		if(user_text.equalsIgnoreCase("error")) {
			System.out.println("User not found");
			mainMenu();
		}
		else {
			u = new User(user_text);
			u.setRole(new Role(role_text));
		}
		
		/*User u = new User(user_text);
		u.setRole(new Role(role_text));

		if (u == null) {
			System.out.println("User not found");
			mainMenu();
		}*/

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

			
			System.out.println(elderly);
			System.out.println("Login successful!");
			elderlyMenu(u.getId());

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
					//find elderly id from user id
					pw.println("searchElderlyIdfromUId"); 
					pw.println(User_id);
					String eld_id_string = br.readLine();
					int eld_id = Integer.parseInt(eld_id_string);
					
					//find elderly name from elderly Id
					pw.println("searchElderlyNameById"); 
					pw.println(eld_id);
					String eld_name_string = br.readLine();
					
					
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
					
					System.out.println("List of tasks: " + tasks);
					
					
					System.out.println("Enter id of the task you want to perform");
					String task_id_text = read.readLine();
					int task_id = Integer.parseInt(task_id_text);
					
					
					//find task duration from elderly Id
					pw.println("searchTaskDurationByELDid"); 
					
					pw.println(task_id);
					String duration_text = br.readLine();
					int duration = Integer.parseInt(duration_text);
					
					
					//Calls the function of Bitalino to start reading data
					System.out.println("Enter MAC of Bitalino with ':' as the following structure xx:xx:xx:xx:xx:xx");
					String MACBitalino = read.readLine();
	
					File filetxt = BitalinoDemo.collectDataBitalino(eld_name_string, MACBitalino, duration);
					
					//propuesta mari: sabemos hacer sockets con string
					//leemos linea a linea el fichero y lo vamos mandando en sockets
					readAndSendrecord(filetxt);
					break;
					
				case 2:
					pw.println("searchElderlyIdfromUId"); //find id doctor from User id
					pw.println(User_id);
					String elderly_id_string = br.readLine();
					int elderly_id = Integer.parseInt(elderly_id_string);
					
					//List<Task> tasksList = null;// tasksManager.getListOfTasks(doctorAllTask_id);
					pw.println("seeTasks"); //find list task from doctor id
					pw.println(elderly_id);
					
					List <Task> tasks2 = new ArrayList<>();
					String task_size_txt = br.readLine();
					int task_size = Integer.parseInt(task_size_txt);
					
					for(int i = 0; i < task_size; i++) {
						
						String tasks_text=br.readLine();
						Task task2=new Task(tasks_text);
						tasks2.add(task2);
					}
					System.out.println("List of tasks: " + tasks2);
					
					
					
					break;
				

				case 3:
					loginElderly();
					break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static void readAndSendrecord(File filetxt) {
		//la funcion la he sacado del codigo de Java para leer ficheros
		//le mandamos el nombre del fichero
		String name = filetxt.getName();
		pw.println(name);
		
		//leemos el fichero linea a linea
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
                if (stringleido == null) {//si llega al final del fichero, se sale del bucle antes de que null se guarde en el string texto
                	break;
                }
                //texto = texto + stringleido + "\n"; //ponemos el \n para que por cada linea que lee readLine para que haya un caracter sobre el que pueda actuar splitPoblacion
                //no podemos poner un intro como separacion de las lineas porque el socket identifica \n como terminacion y solo se copia la primera linea del txt
                texto = texto + stringleido + ",";
                
            }
            //System.out.println(texto);
            //una vez hemos salido del bucle, texto contiene todo el contenido del fichero
            //lo mandamos en un socket
            pw.println(texto);
            
            
        } catch (IOException ioe) {
            System.out.println("Error durante el proceso\t" + ioe);
        } finally {
            try {  //se cierran en sentido contrario al que se han abierto
                if (bufferedreader != null) {
                    bufferedreader.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error durante el proceso\t" + ioe);
            }
            try {
                if (inputstreamreader != null) {
                    inputstreamreader.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error durante el proceso\t" + ioe);
            }
            try {
                if (fileinputstream != null) {
                    fileinputstream.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error durante el proceso\t" + ioe);
            }

        }
		
	}


	
}
