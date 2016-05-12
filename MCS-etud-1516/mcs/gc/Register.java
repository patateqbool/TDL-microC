package mcs.gc;

/**
 * Cette classe décrit un registre d'une machine
 * 
 * @author marcel
 */
public class Register {
	public enum Status {
		Empty, Loaded, Used
	};

	private String name;
	private int num;
	private Status status;

	/**
	 * @param name
	 * @param num
	 */
	public Register(String name, int num) {
		super();
		this.name = name;
		this.num = num;
		this.status = Status.Empty;
	}

	public Register() {
		this("", -1);
	}

	public void copy(Register other) {
		this.name = other.name;
		this.num = other.num;
		this.status = other.status;
	}

	public String getName() {
		return name;
	}

	public int getNum() {
		return num;
	}

	public Status status() {
		return this.status;
	}

	public void setStatus(Status s) {
		this.status = s;
	}

	@Override
	public String toString() {
		return name + (num >= 0 ? num : "");
	}

	public String debug() {
		return "Register [name=" + name + ", num=" + num + "]";
	}

}


