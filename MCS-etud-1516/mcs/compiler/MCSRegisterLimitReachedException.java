/**
 * An exception for indicating that there is no more register available
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.compiler;

public class MCSRegisterLimitReachedException extends MCSException {
  private static final long serialVersionUID = 1l;
  public MCSRegisterLimitReachedException() {
    super("No more free register to use !");
  }
}


