package mcs.gc;

/**
 * Cette classe décrit un registre d'une machine
 * 
 * @author marcel
 */
public class Register {
    public enum Status {
        Empty, Loaded, Used;

        public String toString() {
            switch (this) {
                case Empty:
                    return "E";
                case Loaded:
                    return "L";
                case Used:
                    return "U";
                default:
            }
            return "";
        }
    };

    private String name, alias;
    private int num;
    private Status status;
    private boolean lck;

    /**
     * @param name
     * @param num
     */
    public Register(String name, int num, String alias) {
        this.name = name;
        this.num = num;
        this.status = Status.Empty;
        this.alias = alias;
        this.lck = false;
    }

    public Register(String name, int num) {
        this(name, num, "");
    }

    public Register(String name) {
        this(name, -1);
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
        return this.alias;
    }

    public boolean hasAlias() {
        return this.alias.length() > 0;
    }

    public String name() {
        return this.name + (this.num == -1 ? "" : this.num);
    }

    public int num() {
        return this.num;
    }

    public Status status() {
        return this.status;
    }

    public void setStatus(Status s) {
        if (!this.lck){
            this.status = s;
						System.out.println("(" + this + ") Status changed.");
        } else {
				    System.out.println("(" + this + ") I can not change this status, you put a lock on it!");
				}
		}

    @Override
    public String toString() {
        return (alias.length() == 0 ? name + (num >= 0 ? num : "") : alias);
    }

    public String debug() {
        return "Register [name=" + name + "(" + alias + "), num=" + num + "]<" + this.status + ">" + (this.lck?"Locked":"Unlocked");
    }

    public boolean locked() {
        return this.lck;
    }

    public void lock() {
      System.out.println("I'm locking " + this);  
			this.lck = true;
    }

    public void unlock() {
			System.out.println("I'm unlocking " + this);
        this.lck = false;
    }

}


