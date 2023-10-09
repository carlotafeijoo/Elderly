package ui;

import java.io.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.List;
import exceptions.InputException;
import POJOS.*;

public class MenuResidencialArea {

	private static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
	
	//private static ElderlyManager elderlyManager;
	
	//private static FamilyContactManager familyContactManager;
	
	//private static ScheduleManager scheduleManager;
	
	//private static StaffManager staffManager;
	
	//private static UserManager userManager;
	
	//private static TaskManager tasksManager;
	
	//private static XMLManager xmlmanager;
	
	
	public static void main(String[] args) {

		System.out.println("WELCOME TO THE RESIDENCIAL AREA DATA BASE");
		//JDBCManager jdbcManager = new JDBCManager();

		//initialize database JDBC
		//elderlyManager = new JDBCElderlyManager(jdbcManager);
		//familyContactManager = new JDBCFamilyContactManager(jdbcManager);
		//staffManager = new JDBCStaffManager(jdbcManager);
		//tasksManager = new JDBCTasksManager(jdbcManager);
		//scheduleManager = new JDBCScheduleManager (jdbcManager);
		//initialize database JPA
		//userManager = new JPAUserManager();
		
		mainMenu();
		
	}
	
	public static void mainMenu() {
		try {
			
			int option;
			do {
				System.out.println("MAIN MENU ");
				System.out.println("1. I am an administrator ");
	
				System.out.println("4. Exit ");
				option = InputException.getInt("Introduce the number choice:  ");

				switch (option) {

				case 1:
					administratorMenu();
					break;
					
				
					
				case 4:
					System.out.println("YOU HAVE EXIT THE RESIDENCIAL AREA DATA BASE");
					System.exit(4);
					break;

				default:
					break;
				}
			} while (true);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static void administratorMenu() {
		try {
			int opcion;
			do {
				System.out.println("1. Elderly management. ");
				System.out.println("2. Schedule management. ");
				System.out.println("3. Back. ");
				opcion = InputException.getInt("Introduce the number choice:  ");
				
				switch(opcion) {
				case 1: 
					elderlyMenu();
					break;
					
				case 2:
					scheduleMenu();
					break;
					
				case 3:
					mainMenu();
					break;
					
				}
			}while (true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void elderlyMenu() {
		
		try {
			int choice;
			do {
				//arreglar menu los numeros
				System.out.println("1. Add an elderly to the database.  ");
				System.out.println("2. Update the information of an elderly. ");
				System.out.println("3. Get the list of all elderlies. ");
				System.out.println("4. Load new elderlies. ");
				System.out.println("5. Export elderlies to xml"); 
				System.out.println("6. Back");
				
				choice = InputException.getInt("Introduce your choice: ");

				switch (choice) {

				case 1:
					addElderly();
					break;
					
				case 2:
					int elderly_id = InputException.getInt("Introduce the id of the elderly that is going to be updated:");
					Elderly elderlyToUpdate = null;
					//Elderly elderlyToUpdate = elderlyManager.searchElderlyById(elderly_id);
					if (elderlyToUpdate != null) {
					    int newAge = InputException.getInt("Enter the new age for the elderly: ");
					    elderlyToUpdate.setAge(newAge);
					    //elderlyManager.updateInfo(elderlyToUpdate);
					    System.out.println("Elderly information updated successfully.");
					} else {
					    System.out.println("Elderly not found with the provided id.");
					}
				    break;
				    
				case 3:
					getListOfElderlies();
					break;	
	
					
				case 6:
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
	
	public static void addElderly() throws Exception {
	
		System.out.println("Input the information of the new elderly: ");
	
		String name = InputException.getString(" Name: ");
		
		int age=InputException.getInt("Age:  ");
	
		Elderly elderly = new Elderly(name, age);
	
		//elderlyManager.addElderly(elderly);
		elderlyMenu();
	
	}
	
	private static void getListOfElderlies() throws IOException, Exception {
		System.out.println("The list of elderlies is: ");
		//List<Elderly> resultado = elderlyManager.getListOfElderly();
		List<Elderly> resultado = null;
		System.out.println(resultado);
		elderlyMenu();
	}

	
	private static void scheduleMenu() {
		try {
			int choice;
			do {
				
				System.out.println("1. Add a schedule.  ");
				System.out.println("2. Update a schedule");
				System.out.println("3. List schedules.  ");
				System.out.println("4. Back");
				
				choice = InputException.getInt("Introduce the number of your choice: ");

				switch (choice) {

				case 1:
					addSchedule();
					System.out.println("Schedule added sucessfully! ");
					break;
					
				case 2:
					int schedule_id =InputException.getInt("Enter the id of the schedule that is going to be updated: ");
					//Schedule scheduleToUpdate = scheduleManager.searchScheduleById(schedule_id);
					Schedule scheduleToUpdate = null;
					if(scheduleToUpdate != null) {
						String newDay = InputException.getString("Enter the new day of the week: ");
						scheduleToUpdate.setWeekDay(newDay);
						//scheduleManager.scheduleUpdate(scheduleToUpdate);
						System.out.println("Schedule day updated successfully.");
					}else {
						System.out.println("Schedule not found with the provided id.");
					}
					break;
					
				case 3:
					System.out.println("What day of the week do you want to see the schedule of? ");
					System.out.println("Monday  ");
					System.out.println("Tuesday ");
					System.out.println("Wednesday  ");
					System.out.println("Thursday ");
					System.out.println("Friday ");
					
					String day = InputException.getString("Introduce the day name: " );
					
					System.out.println("The schedule for " +day +" is: ");
					//scheduleManager.getDaySchedule(day);					
					
					System.out.println("List succesfully provided! ");
			
					break;	
					
				case 4:
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
	
	private static void addSchedule ()throws Exception {
		
		 System.out.println("Input the information of the new schedule: ");
		 
		 String weekDay = InputException.getString("New week day: ");
			
		 int staff_id=InputException.getInt("New staff_id:  ");
		 
		 int task_id=InputException.getInt("New task_id:  ");
		 
		 int elderly_id=InputException.getInt("New elderly_id:  ");
		
		 Schedule schedule = new Schedule (weekDay, staff_id, task_id, elderly_id);
	 
		 //scheduleManager.addSchedule(schedule);	
		
		 scheduleMenu();	
		
	}
	
	

}
