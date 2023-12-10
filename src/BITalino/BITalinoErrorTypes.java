package BITalino;

public enum BITalinoErrorTypes {
	BT_DEVICE_NOT_CONNECTED (0, "Bluetooth Device not connected"),
	PORT_COULD_NOT_BE_OPENED (1,"The communication port could not be initialized. The provided parameters could not be set."),
	DEVICE_NOT_IDLE (2,"Device not in idle mode"),
	DEVICE_NOT_IN_ACQUISITION_MODE (3,"Device not is acquisition mode"),
	SAMPLING_RATE_NOT_DEFINED (4,"The Sampling Rate chose cannot be set in BITalino. Choose 1000,100,10 or 1"),
	LOST_COMMUNICATION (5, "The Computer lost communication"),
	INVALID_PARAMETER (6,"INVALID_PARAMETER"),
	THRESHOLD_NOT_VALID (7, "The threshold value must be between 0 and 63"),
	ANALOG_CHANNELS_NOT_VALID (8, "The number of analog channels available are between 0 and 5"),
	INCORRECT_DECODE (9, "Incorrect data to be decoded"),
	DIGITAL_CHANNELS_NOT_VALID (10, "To set the digital outputs, the input array must have 4 items, one for each channel."),
	MACADDRESS_NOT_VALID (11, "MAC address not valid."),
	UNDEFINED (12,"UNDEFINED ERROR");


	private final int value;
	private final String name;

	/**
	Constructs a BITalinoErrorTypes enum constant with the specified integer value and name.
	@param value the integer value associated with the enum constant
	@param name the name of the enum constant
	 */
	BITalinoErrorTypes (int value, String name)
	{
		this.value = value;
		this.name = name;
	}
	/**
	Returns the integer value associated with the BITalinoErrorTypes enum constant.
	@return the integer value of the enum constant
	 */
	public int getValue()
	{
		return value;
	}
	/**
	Returns the name associated with the BITalinoErrorTypes enum constant.
	@return the name of the enum constant
	 */
	public String getName()
	{
		return name; 
	}
	/**
	Returns the BITalinoErrorTypes enum value based on the given integer.
	@param val the integer value to match with an enum
	@return the BITalinoErrorTypes enum value
	 */
	public static final BITalinoErrorTypes getType(int val)
	{
		for (BITalinoErrorTypes t : BITalinoErrorTypes.values())
		{
			if (t.getValue()==val)
				return t;
		}
		return UNDEFINED;
	}
	/**
	Returns the BITalinoErrorTypes enum value based on the given string.
	@param val the string value to match with an enum
	@return the BITalinoErrorTypes enum value
	 */
	public static final BITalinoErrorTypes getType(String val)
	{
		for (BITalinoErrorTypes t : BITalinoErrorTypes.values())
		{
			if (t.getName().equals(val))
				return t;
		}
		return UNDEFINED;
	}
}