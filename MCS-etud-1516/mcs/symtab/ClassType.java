/**
 * ClassType -- class representing a class (ha ha ha).
 *
 * Note on classID:
 *  Each class have an unique ID that identifies it. A mother class knows
 *  its daughters'; when instanciated, the resulting object also have its
 *  classID, no matter which is the type it is registered under.
 *  This allows, by classID comparison, for late linking : we actually
 *  call the method of a corresponding class ID, and not of the actuall
 *  class 
 * @author G.Dupont
 * @version 0.1
 */
package mcs.symtab;

class ClassType extends Type {
  enum AccessSpecifier {
    APublic, APrivate
  };

  private static int nextID = 0;

  private String name;
  private int id;
  private ClassType parent;
  private FunctionTable methodTable;
  private VariableTable attributeTable;
 
  // public ClassType(String name, List<ClassType> parents) {
  /**
   * Constructor
   * Build a class from its name and its parent; it also set the
   * class's unique id.
   * @param name name of the class
   * @param parent parent class (can be null)
   */
  public ClassType(String name, ClassType parent) {
    super(4);
    this.name = name;
    this.parent = parent;

    // Class id
    this.id = ClassType.nextID;
    ClassType.nextID++;

    // Internal stuff
    this.methodTable = new FunctionTable();
    this.attributeTable = new VariableTable();
  }

  /**
   * Proxy methods
   */
  /**
   * Append a method to the class's function table
   * @param mi the method info
   */
  public void addMethod(MethodInfo mi) {
    mi.pushFront();
  }


  /**
   * Accessors
   */
  public ClassType parent() {
    return this.parent;
  }

  public int classId() {
    return this.id;
  }

  /**
   * Inherited abstract methods
   */
  public String toString() {
    return "Class";
  }

  public boolean isCompatible(Type other) {
    return false;
  }

  public Object getDefault() {
    return null;
  }
}


