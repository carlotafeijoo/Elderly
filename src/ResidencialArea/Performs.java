package ResidencialArea;

import java.util.Objects;

public class Performs {
	int staff_id;
	int task_id;
	int performs_id;
	
	public Performs() {
		super();
	}
	
	public Performs(int staff_id, int task_id, int performs_id) {
		super();
		this.staff_id = staff_id;
		this.task_id = task_id;
		this.performs_id = performs_id;
	}

	public int getStaff_id() {
		return staff_id;
	}
	
	public void setStaff_id(int staff_id) {
		this.staff_id = staff_id;
	}
	
	public int getTask_id() {
		return task_id;
	}
	
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	
	public int getPerforms_id() {
		return performs_id;
	}
	
	public void setPerforms_id(int performs_id) {
		this.performs_id = performs_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(performs_id, staff_id, task_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Performs other = (Performs) obj;
		return performs_id == other.performs_id && staff_id == other.staff_id && task_id == other.task_id;
	}

	@Override
	public String toString() {
		return "Performs [staff_id=" + staff_id + ", task_id=" + task_id + ", performs_id=" + performs_id + "]";
	}	

}
