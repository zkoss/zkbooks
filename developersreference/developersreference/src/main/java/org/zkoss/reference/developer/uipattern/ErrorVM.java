package org.zkoss.reference.developer.uipattern;

import org.zkoss.bind.annotation.Command;

public class ErrorVM{

    private String empty;

    public ErrorVM(){
        // throw NPE on pupose
//        empty.length();
    }

    @Command
    public void error(){
        empty.length(); // throw NPE on pupose
    }

}