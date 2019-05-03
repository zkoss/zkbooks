package org.zkoss.mvvm.viewmodel;

import org.zkoss.bind.annotation.*;

public class FullnameViewModel {

    private String firstname;
    private String lastname;

    @NotifyChange("fullname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /* By default setLastname() will notify "lastname" property's change.
    Becuase we apply NotifyChange on it, this default notification is overridden.
    We have to notify it explicitly. */
    @NotifyChange({"lastname", "fullname"})
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullname() {
        return (firstname == null ? "" : firstname)   + " "
                + (lastname == null ? "" : lastname);
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }
}
