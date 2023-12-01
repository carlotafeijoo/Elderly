package BITalino;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

import javax.bluetooth.RemoteDevice;

import POJOS.Elderly;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BitalinoDemo {

    public static Frame[] frame;

    //public static void main(String[] args) {
    public static File collectDataBitalino(String name, String mac, int recording_minutes) { //DNI will be the name of the file

        BITalino bitalino = null;
        
        /*FileOutputStream outh5 = null;
        DataOutputStream datah5 = null;*/
        
        FileOutputStream outtxt = null;
        DataOutputStream datatxt = null;
        
        File filetxt = null;
        
        try {
            bitalino = new BITalino();
            // Code to find Devices
            //Only works on some OS
            Vector<RemoteDevice> devices = bitalino.findDevices();
            System.out.println(devices);

            //You need TO CHANGE THE MAC ADDRESS
            //You should have the MAC ADDRESS in a sticker in the Bitalino
            //String macAddress = "20:17:11:20:51:54"; //Bitalino 51-54
            //String macAddress = "20:16:07:18:13:61"; //Bitalino 13-61
            String macAddress = mac; //Bitalino of the user (elderly)

            
            //Sampling rate, should be 10, 100 or 1000
            int SamplingRate = 10;
            bitalino.open(macAddress, SamplingRate);

            // Start acquisition on analog channels A2 and A6
            // For example, If you want A1, A3 and A4 you should use {0,2,3}
            int[] channelsToAcquire = {1, 5};
            bitalino.start(channelsToAcquire);

            //CREATE FILE TO SAVE DATA
            String diract = System.getProperty("user.dir"); // find where the program is executing
            String dirfolder = diract +"\\recordstxt";
            
            //TO HAVE THE CODE CLEAN MAYBE THE FILE DECLARATION SHOULD BE OUTSIDE THE TRY-CATCH
            //File fileh5 = new File(diract, "ejemplo2.h5"); //.h5 is how the files are save with open signals --> we tried open it with opensignals but it didnt work 
            
            /*LocalDate fecha = LocalDate.now();
            LocalTime hora = LocalTime.now();
            
            LocalDateTime fecha_hora = LocalDateTime.of(fecha, hora);
            
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy"); // 05 May 1988
            //String formattedString = localDate.format(formatter);
            String fecha_hora_string = fecha_hora.format(formatter);*/

            //String filename = dni + fecha_hora_string;
            
            //CREATION OF THE DATE AND TIME FOR OUR FILE
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            LocalDateTime date_time = LocalDateTime.of(date, time);
            
            //CREATION OF THE FORMAT FOR THE NAME
            DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); //28-11-2023
            DateTimeFormatter time_formatter = DateTimeFormatter.ofPattern("hh-mm-ss"); //12:28:08

            //CONVERSION FROM LOCALDATE/TIME TO STRING
            String date_string = date_time.format(date_formatter);
            String time_string = date_time.format(time_formatter);
            
            String date_time_string = "Date_"+date_string+"_Hour_"+time_string;
            String filename = name+ "_" + date_time_string + ".txt";

            filetxt = new File(dirfolder, filename);
            
            //socket for file .txt
            outtxt = new FileOutputStream(filetxt);
            datatxt = new DataOutputStream(outtxt);
            
            int recording_sec = recording_minutes*60;
            //pasamos los minutos por segundos. Cada bloque es un segundo
            	
            for (int j = 0; j < recording_sec; j++) { //WE PUT 10 SO IT WAS EASIER FOR US TO MAKE TRIALS --> it will read 160 samples: 16 samples/bloc, 10 blocks

                //Each time read a block of 10 samples 
                int block_size=16; //WE PUT 16 SINCE OPENSIGNAL WORKS WITH BLOCKS OF 16
                frame = bitalino.read(block_size);

                System.out.println("size block: " + frame.length);

                //Print the samples
                for (int i = 0; i < frame.length; i++) {
                    System.out.println((j * block_size + i) + " seq: " + frame[i].seq + " "
                            + frame[i].analog[0] + " "
                            + frame[i].analog[1] + " "
                    //  + frame[i].analog[2] + " "
                    //  + frame[i].analog[3] + " "
                    //  + frame[i].analog[4] + " "
                    //  + frame[i].analog[5]
                    );

                    //THE SAME IS SAVE AS .TXT
                    datatxt.writeBytes(/*(j * block_size + i)" seq: "*/ + frame[i].seq + "\t" 
                            + frame[i].analog[0] + "\t"
                            + frame[i].analog[1] + "\t"
                            + frame[i].analog[2] + "\t"
                            + frame[i].analog[3] + "\t"
                            + frame[i].analog[4] + "\t"
                            + frame[i].analog[5] + "\n");
                    
                }
            }
            //stop acquisition
            bitalino.stop();
            
        } catch (BITalinoException ex) {
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //close bluetooth connection
                if (bitalino != null) {
                    bitalino.close();
                }
            } catch (BITalinoException ex) {
                Logger.getLogger(BitalinoDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try { //close DataOutputStream(datatxt)
            	if(datatxt != null) {
            		datatxt.close();
            	}
        	} catch (IOException ex) {
        		ex.printStackTrace();
            }
            try { //close FileOutputStream(filetxt);
            	if(outtxt !=null) {
            		outtxt.close();
            	}
            } catch (IOException ex) {
        		ex.printStackTrace();
            }
            
        }
        return filetxt;

    }
    
    public static void main(String[] args) {
    	BitalinoDemo bitdemo = new BitalinoDemo();
    	
    	//String MAC = "20:17:11:20:51:54";
    	//String MAC = "20:16:07:18:13:61";
    	String MAC = "98:D3:41:FD:4E:E8";
    	
    	int rec_min = 1;
    	
    	File filetry = bitdemo.collectDataBitalino("AlbertoMarquez", MAC, rec_min);
    	System.out.println(filetry.getName());
//    	int file_size = (int) filetry.length();
//    	System.out.println(file_size);
    	
    	System.out.println("--------------");
    	//readAndSendrecord(filetry);
    	
    	
    	
    	
    }
    
	
    

}
