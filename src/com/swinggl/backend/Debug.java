package com.swinggl.backend;

import java.util.ArrayList;

/**
 * Created on 12/13/2015.
 */
public class Debug {

    public static ArrayList<String> debuggedVariables = new ArrayList<String>();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void println(String text, String color) {
        System.out.println(color + "[SwingGL] " + text + ANSI_RESET);
    }

    public static void print(String text, String color) {
        System.out.println(color + "[SwingGL]" + text + ANSI_RESET);
    }

    public static void renderDebugVariables() {
        for(int i = 0; i < debuggedVariables.size(); i++) {

        }
    }
}
