package mcs.gc;

/**
 * Cette classe décrit un registre d'une machine
 * 
 * @author marcel
 */
public class Register {
    public enum Status {
        Empty(0), Loaded(1), Used(2);

        private final int value;

        private Status(int v) {
            this.value = v;
        }

        public int value() {
            return this.value;
        }

        public static Status fromInt(int value) {
            switch (value) {
                case 0:
                    return Empty;
                case 1:
                    return Loaded;
                case 2:
                    return Used;
                default:
                    return Empty;
            }
        }

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
    private int usability;

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
        Status old = this.status;;
        if (!this.lck){
            this.status = s;
						System.out.println("(" + this + ") Status changed : " + old + " => " + s);
        } else {
				    System.out.println("(" + this + ") I can not change this status, you put a lock on it! My status is still " + this.status);
				}
		}

    public void setStatus(int s) {
        this.setStatus(Status.fromInt(s));
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


