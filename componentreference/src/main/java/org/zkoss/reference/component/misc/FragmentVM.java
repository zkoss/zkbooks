package org.zkoss.reference.component.misc;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.event.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FragmentVM {
    private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

    private String date = df.format(new Date());

    private Integer prev;

    private List<Expense> expenses;

    private Expense newExpense;

    private Expense editExpense;

    private boolean toggleEdit;

    private int totalExpenses;

    public FragmentVM() {
        expenses = new ArrayList<>(Arrays.asList(new Expense[]{
                new Expense(0, df.format(new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000)), "Breakfast", 40),
                new Expense(1, df.format(new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000)), "Lunch", 60),
                new Expense(2, df.format(new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000)), "Dinner", 120),
                new Expense(3, df.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)), "Breakfast", 45),
                new Expense(4, df.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)), "Lunch", 110),
                new Expense(5, df.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)), "Dinner", 70),
                new Expense(6, df.format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)), "Shopping", 87),
                new Expense(7, date, "Breakfast", 45)}));
        newExpense = new Expense(date, "", 0);
        for (Expense e : expenses) {
            totalExpenses += e.getAmount();
        }
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public boolean isToggleEdit() {
        return toggleEdit;
    }

    public void setToggleEdit(boolean toggleEdit) {
        this.toggleEdit = toggleEdit;
    }

    public Expense getNewExpense() {
        return newExpense;
    }

    public void setNewExpense(Expense newExpense) {
        this.newExpense = newExpense;
    }

    public Expense getEditExpense() {
        return editExpense;
    }

    public void setEditExpense(Expense editExpense) {
        this.editExpense = editExpense;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    @Command
    @NotifyChange({"editExpense", "toggleEdit"})
    public void toggleEditMethod(@BindingParam("id") Integer id, @BindingParam("evt") Event evt) {
        if (id != prev) {
            editExpense = getExpenses().get(id);
            toggleEdit = true;
            prev = id;
        }
    }

    @Command
    @NotifyChange({"expenses", "newExpense", "totalExpenses"})
    public void submitNewExpense() {
        expenses.add(this.newExpense);
        newExpense = new Expense(date, "", 0);
//		setTotalExpenses();
        totalExpenses = 0;
        for (Expense e : getExpenses()) {
            totalExpenses += e.getAmount();
        }
    }

    @Command
    @NotifyChange({"expenses", "editExpense", "toggleEdit", "totalExpenses"})
    public void submitEditExpense() {
        expenses.set(prev, editExpense);
        editExpense = null;
        toggleEdit = false;
        prev = null;
//		setTotalExpenses();
        totalExpenses = 0;
        for (Expense e : getExpenses()) {
            totalExpenses += e.getAmount();
        }
    }

    @Command
    @NotifyChange({"toggleEdit", "editExpense"})
    public void addExpense() {
        toggleEdit = false;
        prev = null;
        editExpense = null;
    }

    private class Expense {

        private Integer id;

        private String date;

        private String category;

        private int amount;

        public Expense(Integer id, String date, String category, int amount) {
            this.id = id;
            this.date = date;
            this.category = category;
            this.amount = amount;
        }

        public Expense(String date, String category, int amount) {
            this.id = getExpenses().size();
            this.date = date;
            this.category = category;
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }
    }

}
