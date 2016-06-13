/**
 * A register wrapper (allow for 'reference' argument passings)
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.gc;

public class RegisterWrapper {
    private Register register;

    public RegisterWrapper(Register reg) {
        this.register = reg;
    }

    public Register get() {
        return this.register;
    }

    public void set(Register reg) {
        this.register = reg;
    }
}


