package fnadaud.checkdebts;

/**
 * Created by florian on 12/03/17.
 */
public class Debts {
    private String name;
    private String debt;

    public Debts(String name, String debt) {
        super();
        this.name = name;
        this.debt = debt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
    }
}
