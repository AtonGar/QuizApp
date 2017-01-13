package com.example.adroid.quizapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.type;


/**
 * This app testing the physical activity of the user in the different environments: work, travel and free time
 * The app have two languages: english and spanish
 * The resulting value is compared to a scale to show the level of physical activity of the user
 * The app testing that for each environment the values of days and minutes can only be entered if the check of the environment is checked
 * The app testing that if an environment is selected must also select the days of activity and the range of time dedicated to physical activity
 * The app testing that the number of days of the week does not exceed 7 nor less than 0
 * About the design, I do not like the large scroll.My first idea was to established 3 panels, one for each environment, with 3 buttons that showed and hid
 * the panels, showing only one on the screen. In doing so, you can see that unlike web design, by hiding a LinearLayout with visibility the LinearLayout
 * does not scroll up, leaving a blank space in the place it occupied. As I did not see how to solve this problem use the scroll
 */

public class MainActivity extends AppCompatActivity {

    //declare global variables
    private static final int minDaysReached = 0;
    private static final int maxDaysReached = 1;
    private static final int firstQuestionCheckRequerided = 2;
    private static final int nameIsRequerided = 3;
    private static final int dataIsRequerided = 4;
    private static final int chekAnyEnrionmentRequerided = 5;
    private int daysWork = 0;
    private int daysDespla = 0;
    private int daysOcio = 0;
    private int totalTime = 0;
    private EditText nameUserView;
    private TextView countDaysWork;
    private TextView countDaysDespla;
    private TextView countDaysOcio;
    private CheckBox workQuestions;
    private CheckBox desplaQuestions;
    private CheckBox ocioQuestions;
    private RadioGroup radGroupWork;
    private RadioGroup radGroupDespla;
    private RadioGroup radGroupOcio;
    private String ActNivel;
//    private String minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the views global variables
        nameUserView = (EditText) findViewById(R.id.name);
        countDaysWork = (TextView) findViewById(R.id.quantityDaysWork);
        countDaysDespla = (TextView) findViewById(R.id.quantityDaysDespla);
        countDaysOcio = (TextView) findViewById(R.id.quantityDaysOcio);
        workQuestions = (CheckBox) findViewById(R.id.EnvWork_checkbox);
        desplaQuestions = (CheckBox) findViewById(R.id.EnvDespla_checkbox);
        ocioQuestions = (CheckBox) findViewById(R.id.EnvOcio_checkbox);
        radGroupWork = (RadioGroup) findViewById(R.id.radGroupWork);
        radGroupDespla = (RadioGroup) findViewById(R.id.radGroupDespla);
        radGroupOcio = (RadioGroup) findViewById(R.id.radGroupOcio);

    }


    /*===================================================
Submit methods
====================================================*/

    // method to testing the phisical activity
    public void testClic(View view) {

        //  set if the checkbox was clicked
        boolean workQuestionsState = workQuestions.isChecked();
        boolean desplaQuestionsState = desplaQuestions.isChecked();
        boolean ocioQuestionsState = ocioQuestions.isChecked();

        // testing if there is name
        String nameUser = nameUserView.getText().toString();
        if (nameUser.isEmpty()) {
            // if there isn´t name show toast and out
            showToast(nameIsRequerided);
            return;
        }

        //if there is name continued


        if (workQuestionsState) {
            //if work environment is checked calculate their activity time
            int time = calculateTimeActivity(radGroupWork, daysWork);
            if (time == 0) {
                // if time is 0 show Toast and out
                showToast(dataIsRequerided);
                return;
            } else {
                // if all is right set the total time for work
                totalTime = totalTime + time;
            }

        }

        if (desplaQuestionsState) {
            //if desplacement environment is checked calculate their activity time
            int time = calculateTimeActivity(radGroupDespla, daysDespla);
            if (time == 0) {
                // if time is 0 show Toast and out
                showToast(dataIsRequerided);
                return;
            } else {
                // if all is right set the total time for desplacement
                totalTime = totalTime + time;
            }
        }

        if (ocioQuestionsState) {
            //if free time environment is checked calculate their activity time
            int time = calculateTimeActivity(radGroupOcio, daysOcio);
            if (time == 0) {
                // if time is 0 show Toast and out
                showToast(dataIsRequerided);
                return;
            } else {
                // if all is right set the total time for free time
                totalTime = (totalTime + time) * 2;
            }

        }

        if (totalTime == 0) {
            // if the time is 0 the user no selected any environment, show toast
            showToast(chekAnyEnrionmentRequerided);
            return;
        }
        // call to method to create a text summary for final result
        String summary = createOrderSummary();
        // reset the total time for the user click again
        totalTime = 0;
        // Finally show the toast with the result
        Toast.makeText(this, summary, Toast.LENGTH_SHORT).show();

    }

    // method to reset data
    public void reset(View view) {

        daysWork = 0;
        daysDespla = 0;
        daysOcio = 0;
        totalTime = 0;
        nameUserView.setText(String.valueOf(""));
        updateDaysCounter(countDaysWork, 0);
        updateDaysCounter(countDaysDespla, 0);
        updateDaysCounter(countDaysOcio, 0);
        workQuestions.setChecked(false);
        workQuestions.setChecked(false);
        desplaQuestions.setChecked(false);
        ocioQuestions.setChecked(false);
        radGroupWork.clearCheck();
        radGroupDespla.clearCheck();
        radGroupOcio.clearCheck();
    }

/*===================================================
Methods for the interaction of controls
====================================================*/

    //    Method for when the environment checkbox are unchecked to clean the corresponding environment
    public void onCheckBoxClicked(View view) {

        //  set if the checkbox was clicked
        boolean workQuestionsState = workQuestions.isChecked();
        boolean desplaQuestionsState = desplaQuestions.isChecked();
        boolean ocioQuestionsState = ocioQuestions.isChecked();

        // check which checkbox control are checked or unchecked
        switch (view.getId()) {
            case R.id.EnvWork_checkbox:

                if (!workQuestionsState) {
//                    if checkbox control are unchecked clean week days and their radiobutton
                    updateDaysCounter(countDaysWork, 0);
                    radGroupWork.clearCheck();
                    break;
                }

            case R.id.EnvDespla_checkbox:

                if (!desplaQuestionsState) {
                    // if checkbox control are unchecked clean week days and their radiobutton
                    updateDaysCounter(countDaysDespla, 0);
                    radGroupDespla.clearCheck();
                    break;
                }

            case R.id.EnvOcio_checkbox:

                if (!ocioQuestionsState) {
                    // if checkbox control are unchecked clean week days and their radiobutton
                    updateDaysCounter(countDaysOcio, 0);
                    radGroupOcio.clearCheck();
                    break;
                }
        }
    }

    //    Method for decrement the week days value
    public void decrementDays(View view) {

        //  set if the checkbox was clicked
        boolean workQuestionsState = workQuestions.isChecked();
        boolean desplaQuestionsState = desplaQuestions.isChecked();
        boolean ocioQuestionsState = ocioQuestions.isChecked();

        // check which button control are clicked
        switch (view.getId()) {
            case R.id.workBtnMinus:

                if (!workQuestionsState) {
                    // if corresponding environment checkbox control are unchecked show a toast and out method
                    showToast(firstQuestionCheckRequerided);
                    break;
                }
                // if corresponding environment days week is 0 show a toast and out method
                if (daysWork == 0) {
                    showToast(minDaysReached);
                    break;
                }
                // if all is right set the new days week value
                daysWork = daysWork - 1;
                updateDaysCounter(countDaysWork, daysWork);
                break;
            case R.id.desplaBtnMinus:

                if (!desplaQuestionsState) {
                    // if corresponding environment checkbox control are unchecked show a toast and out method
                    showToast(firstQuestionCheckRequerided);
                    break;
                }
                if (daysDespla == 0) {
                    // if corresponding environment days week is 0 show a toast and out method
                    showToast(minDaysReached);
                    break;
                }
                // if all is right set the new days week value
                daysDespla = daysDespla - 1;
                updateDaysCounter(countDaysDespla, daysDespla);
                break;
            case R.id.ocioBtnMinus:

                if (!ocioQuestionsState) {
                    // if corresponding environment checkbox control are unchecked show a toast and out method
                    showToast(firstQuestionCheckRequerided);
                    break;
                }
                if (daysOcio == 0) {
                    // if corresponding environment days week is 0 show a toast and out method
                    showToast(minDaysReached);
                    break;
                }
                // if all is right set the new days week value
                daysOcio = daysOcio - 1;
                updateDaysCounter(countDaysOcio, daysOcio);
                break;
        }
    }

    //    Method for increment the week days value
    public void incrementDays(View view) {
        //  set if the checkbox was clicked
        boolean workQuestionsState = workQuestions.isChecked();
        boolean desplaQuestionsState = desplaQuestions.isChecked();
        boolean ocioQuestionsState = ocioQuestions.isChecked();
// check which button control are clicked
        switch (view.getId()) {
            case R.id.workBtnMore:

                if (!workQuestionsState) {
                    // if corresponding environment checkbox control are unchecked show a toast and out method
                    showToast(firstQuestionCheckRequerided);
                    break;
                }
                if (daysWork == 7) {
                    // if corresponding environment days week is 7 show a toast and out method
                    showToast(maxDaysReached);
                    break;
                }
                daysWork = daysWork + 1;
                updateDaysCounter(countDaysWork, daysWork);
                break;
            case R.id.desplaBtnMore:

                if (!desplaQuestionsState) {
                    // if corresponding environment checkbox control are unchecked show a toast and out method
                    showToast(firstQuestionCheckRequerided);
                    break;
                }
                if (daysDespla == 7) {
                    // if corresponding environment days week is 7 show a toast and out method
                    showToast(maxDaysReached);
                    break;
                }
                daysDespla = daysDespla + 1;
                updateDaysCounter(countDaysDespla, daysDespla);
                break;
            case R.id.ocioBtnMore:

                if (!ocioQuestionsState) {
                    // if corresponding environment checkbox control are unchecked show a toast and out method
                    showToast(firstQuestionCheckRequerided);
                    break;
                }
                if (daysOcio == 7) {
                    // if corresponding environment days week is 7 show a toast and out method
                    showToast(maxDaysReached);
                    break;
                }
                // if all is right set the new days week value
                daysOcio = daysOcio + 1;
                updateDaysCounter(countDaysOcio, daysOcio);
                break;
        }
    }

    //    Method for testing if checkbox of corresponding environment is checked when a radiobutoon is clicked,
    //   if is unchecked clean the radiobutton
    public void onRadioButtonClicked(View view) {
        //  set if the checkbox was clicked
        boolean workQuestionsState = workQuestions.isChecked();
        boolean desplaQuestionsState = desplaQuestions.isChecked();
        boolean ocioQuestionsState = ocioQuestions.isChecked();

       /* For each unchecked checkbox control we look for which radiobutton control has been clicked and clean the control and break*/
        if (!workQuestionsState) {
            switch (view.getId()) {
                case R.id.radHalfHourWork:
                    radGroupWork.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
                case R.id.radOneHourWork:
                    radGroupWork.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
                case R.id.radTwoHourWork:
                    radGroupWork.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
            }
        }

        if (!desplaQuestionsState) {
            switch (view.getId()) {
                case R.id.radHalfHourDespla:
                    radGroupDespla.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
                case R.id.radOneHourDespla:
                    radGroupDespla.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
                case R.id.radTwoHourDespla:
                    radGroupDespla.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
            }
        }

        if (!ocioQuestionsState) {
            switch (view.getId()) {
                case R.id.radHalfHourOcio:
                    radGroupOcio.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
                case R.id.radOneHourOcio:
                    radGroupOcio.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
                case R.id.radTwoHourOcio:
                    radGroupOcio.clearCheck();
                    showToast(firstQuestionCheckRequerided);
                    break;
            }
        }
    }



/*===================================================
Miscellaneus Methods
====================================================*/

    /**
     * Method for calculate the total time to environment
     *
     * @param radioGroup is the RadioGroup that content the radiobutton of environment
     * @param days       is the total week days calculated for the environment
     *                   return total time of environment or a 0 value if there isn´t value chcecked
     */
    private int calculateTimeActivity(RadioGroup radioGroup, int days) {

        // set the id of radiobutton value checked
        int selectedId = radioGroup.getCheckedRadioButtonId();

        if (selectedId == -1 || days == 0) {
            // if any radiobutton value was checked or the days are 0 return a value 0
            return 0;
        } else {
            // get text of radiobutton valu checked
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            String minutes = radioButton.getText().toString();
            // remove of value the m character
            minutes = minutes.replace("m", "");
            // return the total time for the environment
            return days * Integer.parseInt(minutes);
        }

    }

    /**
     * Method for update testview to show the environment week days
     *
     * @param textView is the TextView control to update
     * @param count    is the total week days calculated for the environment
     */
    public static void updateDaysCounter(TextView textView, int count) {
        textView.setText(String.valueOf(count));
    }

    private String createOrderSummary() {

        String nameUser = nameUserView.getText().toString();
        if (totalTime < 300) {
            ActNivel = getString(R.string.bad);
        } else if (totalTime > 299 && totalTime < 600) {
            ActNivel = getString(R.string.notGood);
        } else if (totalTime > 599 && totalTime < 1200) {
            ActNivel = getString(R.string.good);
        } else if (totalTime > 1199 && totalTime < 1500) {
            ActNivel = getString(R.string.veryGood);
        } else if (totalTime > 1499) {
            ActNivel = getString(R.string.excelent);
        }

        String message = getString(R.string.nivel_activity);
        message += "\n" + getString(R.string.test_summary_name, nameUser);
        message += "\n" + getString(R.string.test_summary_text, ActNivel);

        return message;
    }

    /**
     * Method for update Toast to view
     *
     * @param type is value indicative for type to Toast show
     */
    public void showToast(int type) {
        if (type == minDaysReached) {
            Toast.makeText(this, getString(R.string.minDay), Toast.LENGTH_SHORT).show();
        } else if (type == maxDaysReached) {
            Toast.makeText(this, getString(R.string.maxDay), Toast.LENGTH_SHORT).show();
        } else if (type == firstQuestionCheckRequerided) {
            Toast.makeText(this, getString(R.string.checkLack), Toast.LENGTH_SHORT).show();
        } else if (type == nameIsRequerided) {
            Toast.makeText(this, getString(R.string.nameRequerided), Toast.LENGTH_SHORT).show();
        } else if (type == dataIsRequerided) {
            Toast.makeText(this, getString(R.string.dataRequerided), Toast.LENGTH_SHORT).show();
        } else if (type == chekAnyEnrionmentRequerided) {
            Toast.makeText(this, getString(R.string.checkAnyEnvironmentRequerided), Toast.LENGTH_SHORT).show();
        }

    }

}


