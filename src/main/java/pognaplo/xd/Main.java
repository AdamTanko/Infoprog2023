package main.java.pognaplo.xd;

import main.java.pognaplo.frontend.WelcomePage;

import javax.swing.*;

public class Main
{
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        WelcomePage wp = new WelcomePage();

    }
}
