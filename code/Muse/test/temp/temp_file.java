package com.example.amit.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    String dataLeAk9 = "";

	String dataLeAk8 = "";

	String dataLeAk7 = "";

	int intA = 0;

    int methodA() {
        dataLeAk7 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		return 1;
    }

    int intB = 0;

    int methodB() {
        dataLeAk8 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		dataLeAk9 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();

        this.intA = 1;
        this.methodA();
        this.intB = 2;
        this.methodB();
        setContentView(R.layout.activity_main);
    }
}
