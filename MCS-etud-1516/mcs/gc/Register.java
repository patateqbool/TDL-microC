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

	private String name, alias;
	private int num;
	private Status status;

	/**
	 * @param name
	 * @param num
	 */
  public Register(String name, int num, String alias) {
    this.name = name;
		this.num = num;
		this.status = Status.Empty;
    this.alias = alias;
  }

	public Register(String name, int num) {
    this(name, num, "");
	}

	public Register() {
		this("", -1);
	}

	public void copy(Register other) {
		this.name = other.name;
		this.num = other.num;
		this.status = other.status;
	}

  public String alias() {
    return alias;
  }

	public String name() {
		return name;
	}

	public int num() {
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
		return (alias.length() == 0 ? name + (num >= 0 ? num : "") : alias);
	}

	public String debug() {
		return "Register [name=" + name + "(" + alias + "), num=" + num + "]<" +
      (status == Status.Empty ? "E" :
       (status == Status.Loaded ? "L" :
        "U")) + ">";
	}

}


