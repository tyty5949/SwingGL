package com.swinggl.backend;

/*
 * (C) Copyright 2015 Tyler Hunt

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 */

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created on 12/13/2015.
 */
public class Debug {

    public static boolean enabled;

    public static final NumberFormat noNotation = new DecimalFormat("###.#####");

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

    public static void println(String text) {
        System.out.println(ANSI_BLUE + "[SwingGL] " + text + ANSI_RESET);
    }

    public static void print(String text, String color) {
        System.out.println(color + "[SwingGL]" + text + ANSI_RESET);
    }

    public static void print(String text) {
        System.out.println(ANSI_BLUE + "[SwingGL]" + text + ANSI_RESET);
    }
}
