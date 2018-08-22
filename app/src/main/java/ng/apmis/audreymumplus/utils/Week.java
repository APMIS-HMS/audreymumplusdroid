package ng.apmis.audreymumplus.utils;

/**
 * Created by Thadeus-APMIS on 7/16/2018.
 */

public enum Week {
    ALL ("All"),
    Week0 ("Week 0"),
    Week1 ("Week 1"),
    Week2 ("Week 2"),
    Week3 ("Week 3"),
    Week4 ("Week 4"),
    Week5 ("Week 5"),
    Week6 ("Week 6"),
    Week7 ("Week 7"),
    Week8 ("Week 8"),
    Week9 ("Week 9"),
    Week10 ("Week 10"),
    Week11 ("Week 11"),
    Week12 ("Week 12"),
    Week13 ("Week 13"),
    Week14 ("Week 14"),
    Week15 ("Week 15"),
    Week16 ("Week 16"),
    Week17 ("Week 17"),
    Week18 ("Week 18"),
    Week19 ("Week 19"),
    Week20 ("Week 20"),
    Week21 ("Week 21"),
    Week22 ("Week 22"),
    Week23 ("Week 23"),
    Week24 ("Week 24"),
    Week25 ("Week 25"),
    Week26 ("Week 26"),
    Week27 ("Week 27"),
    Week28 ("Week 28"),
    Week29 ("Week 29"),
    Week30 ("Week 30"),
    Week31 ("Week 31"),
    Week32 ("Week 32"),
    Week33 ("Week 33"),
    Week34 ("Week 34"),
    Week35 ("Week 35"),
    Week36 ("Week 36"),
    Week37 ("Week 37"),
    Week38 ("Week 38"),
    Week39 ("Week 39"),
    Week40 ("Week 40"),
    Week41 ("Week 41"),
    Week42 ("Week 42");

    public final String week;

    Week(String week) {
        this.week = week;
    }

    public String getWeek() {
        return this.week;
    }

    public String getReverseWeek () {
        String [] reverseString = this.week.split(" ");
        return reverseString[1] + " " + reverseString [0];
    }

}
