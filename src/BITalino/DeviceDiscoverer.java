package BITalino;

import java.util.Vector;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;

public class DeviceDiscoverer implements DiscoveryListener {


	public Vector<RemoteDevice> remoteDevices = new Vector<RemoteDevice>();
	DiscoveryAgent discoveryAgent;
	public String deviceName;
	String inqStatus = null;
	/**
    Constructs a new DeviceDiscoverer and initiates the device discovery process.
    The constructor initializes the local device and starts the inquiry process to discover nearby devices.
	 */
	public DeviceDiscoverer() {
		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			System.err.println(LocalDevice.getLocalDevice());
			discoveryAgent = localDevice.getDiscoveryAgent();
			discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);            

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
    Called when a remote device is discovered during the inquiry process.
    @param remoteDevice the discovered remote device
    @param cod the device class
    This method records the names of discovered devices and adds the BITalino device to the list of remote devices.
	 */
	public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass cod) {

		try
		{
			deviceName=remoteDevice.getFriendlyName(false); //Records devices names
			if (deviceName.equalsIgnoreCase("bitalino")) 
			{
				remoteDevices.addElement(remoteDevice);
			}

		} 
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
    Called when the inquiry process is completed.
    @param discType the type of the completed inquiry
    This method updates the inquiry status based on the type of completion.
	 */
	public void inquiryCompleted(int discType) 
	{

		if (discType == DiscoveryListener.INQUIRY_COMPLETED) 
		{
			inqStatus = "Scan completed.";
		}
		else if (discType == DiscoveryListener.INQUIRY_TERMINATED) 
		{
			inqStatus = "Scan terminated.";
		}
		else if (discType == DiscoveryListener.INQUIRY_ERROR) 
		{
			inqStatus = "Scan with errors.";
		}
	}
	/**
    Called when services are discovered.
    @param transID the transaction ID
    @param servRecord the service records
    This method handles the discovered services.
	 */
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord){}
	/**
    Called when the service search is completed.
    @param transID the transaction ID
    @param respCode the response code
    This method handles the completion of the service search.
	 */
	public void serviceSearchCompleted(int transID, int respCode) {}
}
