package com.zola.bmi;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;


public class BMIMain extends AppCompatActivity {

    //private Spinner weightSpinner, heightSpinner;

    String dataLeAk0 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
	Object throwawayLeAk0 = android.util.Log.d("leak-0", dataLeAk0);

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		String dataLeAk1 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		Object throwawayLeAk1 = android.util.Log.d("leak-1", dataLeAk1);
        setContentView(R.layout.activity_bmimain);

        if (savedInstanceState == null) {
            String dataLeAk2 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk2 = android.util.Log.d("leak-2", dataLeAk2);
			getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

    }

    public void calculateClickHandler(View view) {
        String dataLeAk3 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		Object throwawayLeAk3 = android.util.Log.d("leak-3", dataLeAk3);
		if (view.getId() == R.id.calcBMI) {

            // get the references to the widgets


            String dataLeAk4 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk4 = android.util.Log.d("leak-4", dataLeAk4);
			EditText weightNum = (EditText)findViewById(R.id.weightNum);
            EditText heightNum = (EditText)findViewById(R.id.heightNum);
            TextView resultLabel = (TextView)findViewById(R.id.resultLabel);

            Spinner weightSpinner = (Spinner)findViewById(R.id.weightSpinner);
            Spinner heightSpinner = (Spinner)findViewById(R.id.heightSpinner);
            String weightSpinnerString = weightSpinner.getSelectedItem().toString();
            String heightSpinnerString = heightSpinner.getSelectedItem().toString();

            double weight;
            weight = 0;
            double height;
            height = 0;

            // get the users values from the widget references
            if (!(weightNum.getText().toString().equals(""))) {
                String dataLeAk5 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				Object throwawayLeAk5 = android.util.Log.d("leak-5", dataLeAk5);
				weight = Double.parseDouble(weightNum.getText().toString());
            }

            if (!(heightNum.getText().toString().equals(""))) {
                String dataLeAk6 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				Object throwawayLeAk6 = android.util.Log.d("leak-6", dataLeAk6);
				height = Double.parseDouble(heightNum.getText().toString());
            }

            double bmi;

            // calculate bmi value - pounds and inch
            if (weightSpinnerString.equals("Pounds") && heightSpinnerString.equals("Inch")) {
                String dataLeAk7 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				Object throwawayLeAk7 = android.util.Log.d("leak-7", dataLeAk7);
				bmi = calculateBMI(weight, height);
            } else if (weightSpinnerString.equals("Kilograms") &&
                    heightSpinnerString.equals("Inch")){
                String dataLeAk8 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
						Object throwawayLeAk8 = android.util.Log.d("leak-8", dataLeAk8);
				weight = weight * 2.205;
                bmi = calculateBMI(weight, height);
            } else if (weightSpinnerString.equals("Pounds") && heightSpinnerString.equals("CM")){
                String dataLeAk9 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				Object throwawayLeAk9 = android.util.Log.d("leak-9", dataLeAk9);
				height = height / 2.54;
                bmi = calculateBMI(weight, height);
            } else {
                String dataLeAk10 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
				Object throwawayLeAk10 = android.util.Log.d("leak-10", dataLeAk10);
				weight = weight * 2.205;
                height = height / 2.54;
                bmi = calculateBMI(weight, height);
            }

            // round to 2 digits
            double newBMI = Math.round(bmi*100.0)/100.0;
            DecimalFormat f = new DecimalFormat("##.00");

            // interpret the meaning of the bmi value
            String bmiInterpretation = interpretBMI(bmi);

            // now set the value in the results text
            resultLabel.setText("BMI Score = " + f.format(newBMI) + "\n" + bmiInterpretation);
        }
    }

    // the formula to calculate the BMI index
    private double calculateBMI (double weight, double height) {
        String dataLeAk11 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		Object throwawayLeAk11 = android.util.Log.d("leak-11", dataLeAk11);
			// convert values to metric
            return (double) (((weight / 2.2046) / (height * 0.0254)) / (height * 0.0254));
    }

    // interpret what BMI means
    private String interpretBMI(double bmi) {

        String dataLeAk12 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		Object throwawayLeAk12 = android.util.Log.d("leak-12", dataLeAk12);
		if (bmi < 16) {
            String dataLeAk13 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk13 = android.util.Log.d("leak-13", dataLeAk13);
			return "You are Severely Underweight";
        } else if (bmi < 18.5) {
            String dataLeAk14 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk14 = android.util.Log.d("leak-14", dataLeAk14);
			return "You are Underweight";
        } else if (bmi < 25) {
            String dataLeAk15 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk15 = android.util.Log.d("leak-15", dataLeAk15);
			return "You are Normal";
        }else if (bmi < 30) {
            String dataLeAk16 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk16 = android.util.Log.d("leak-16", dataLeAk16);
			return "You are Overweight";
        }else if (bmi < 40) {
            String dataLeAk17 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk17 = android.util.Log.d("leak-17", dataLeAk17);
			return "You are Obese";
        }else if (bmi >= 40) {
            String dataLeAk18 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk18 = android.util.Log.d("leak-18", dataLeAk18);
			return "You are Morbidly Obese";
        }else {
            String dataLeAk19 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk19 = android.util.Log.d("leak-19", dataLeAk19);
			return "Enter your Details";
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bmimain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        String dataLeAk20 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
		Object throwawayLeAk20 = android.util.Log.d("leak-20", dataLeAk20);

		public PlaceholderFragment() {
			String dataLeAk21 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
			Object throwawayLeAk21 = android.util.Log.d("leak-21", dataLeAk21);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            String dataLeAk22 = java.util.Calendar.getInstance().getTimeZone().getDisplayName();
					Object throwawayLeAk22 = android.util.Log.d("leak-22", dataLeAk22);
			View rootView = inflater.inflate(R.layout.fragment_bmimain, container, false);
            return rootView;
        }
    }

}
