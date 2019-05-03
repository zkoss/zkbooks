package org.zkoss.mvvm.viewmodel;

import org.zkoss.bind.annotation.*;

/**
 * The example is functional equivalent to {@link FullnameViewModel} one, but written in
 * reversed meaning. It means when any one of 2 properties (firstname, lastname) change,
 * fullname should be reloaded.
 */
public class FullnameViewModel2 {

    private String firstname;
    private String lastname;

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullname() {
        return (firstname == null ? "" : firstname) + " "
                + (lastname == null ? "" : lastname);
    }

    @DependsOn({"firstname", "lastname"})
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
