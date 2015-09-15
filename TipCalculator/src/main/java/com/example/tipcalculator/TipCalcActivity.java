package com.example.tipcalculator;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class TipCalcActivity extends AppCompatActivity {

    private static final String TOTAL_BILL = "TOTAL_BILL";
    private static final String CURRENT_TIP= "CURRENT_TIP";
    private static final String BILL_WITHOUT_TIP = "BILL_WITHOUT_TIP";

    private double billBeforeTip;
    private double tipAmount;
    private double finalBill;

    private EditText billBeforeTipET;
    private EditText tipAmountET;
    private EditText finalBillET;

    private SeekBar tipSeekBar;

    private int[] checkListValues = new int[12];
    private CheckBox friendlyCheckBox;
    private CheckBox specialsCheckBox;
    private CheckBox opinionCheckBox;

    private RadioGroup availableRadioGroup;
    private RadioButton availableBadRadio;
    private RadioButton availableOkRadio;
    private RadioButton availableGoodRadio;

    private Spinner problemsSpinner;

    private Button startChronometerButton;
    private Button pauseChronometerButton;
    private Button resetChronometerButton;

    private Chronometer timeWaitingChronometer;

    private long secondsYouWaited = 0;

    private TextView timeWaitingTextView;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calc);

        // Just starting the application
        if(savedInstanceState == null) {
            billBeforeTip = 0.0;
            tipAmount = .15;
            finalBill = 0.0;
        } else {
            // We came back from a different state
            billBeforeTip = savedInstanceState.getDouble(BILL_WITHOUT_TIP);
            tipAmount = savedInstanceState.getDouble(CURRENT_TIP);
            finalBill = savedInstanceState.getDouble(TOTAL_BILL);
        }

        billBeforeTipET = (EditText) findViewById(R.id.billEditText);
        tipAmountET = (EditText) findViewById(R.id.tipEditText);
        finalBillET = (EditText) findViewById(R.id.finalBillEditText);
        tipSeekBar = (SeekBar) findViewById(R.id.changeTipSeekBar);

        tipSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tipAmount = (tipSeekBar.getProgress()) * .01;
                tipAmountET.setText(String.format("%.02f", tipAmount));
                updateTipAndFinalBill();
            }
        });

        billBeforeTipET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    billBeforeTip = Double.parseDouble(s.toString());
                } catch (NumberFormatException e) {
                    billBeforeTip = 0.0;
                }
                updateTipAndFinalBill();
            }
        });

        friendlyCheckBox = (CheckBox) findViewById(R.id.friendlyCheckBox);
        specialsCheckBox = (CheckBox) findViewById(R.id.specialsCheckBox);
        opinionCheckBox = (CheckBox) findViewById(R.id.opinionCheckBox);

        setUpIntroCheckBoxes();

        availableRadioGroup = (RadioGroup) findViewById(R.id.availabilityRadioGroup);
        availableBadRadio = (RadioButton) findViewById(R.id.availBadRadioButton);
        availableOkRadio = (RadioButton) findViewById(R.id.availOkRadioButton);
        availableGoodRadio = (RadioButton) findViewById(R.id.availGoodRadioButton);

        addChangeListenerToRadios();

        problemsSpinner = (Spinner) findViewById(R.id.problemsSpinner);

        addItemSelectedListenerToSpinner();

        startChronometerButton = (Button) findViewById(R.id.startChronoButton);
        pauseChronometerButton = (Button) findViewById(R.id.pauseChronoButton);
        resetChronometerButton = (Button) findViewById(R.id.resetChronoButton);

        setButtonOnClickListeners();

        timeWaitingChronometer = (Chronometer) findViewById(R.id.timeWaitingChronometer);
        timeWaitingTextView = (TextView) findViewById(R.id.timeWaitingTextView);
    }

    private void setButtonOnClickListeners() {
        startChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                int stoppedMilliseconds = 0;
                String chronoText = timeWaitingChronometer.getText().toString();
                String array[] = chronoText.split(":");
                if(array.length == 2) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 +
                            Integer.parseInt(array[1]) * 1000;
                } else if(array.length == 3) {
                    stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 +
                            Integer.parseInt(array[1]) * 60 * 1000 +
                            Integer.parseInt(array[2]) * 1000;
                }

                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
                secondsYouWaited = Long.parseLong(array[1]);
                updateTipBasedOnTimeWaited(secondsYouWaited);
                timeWaitingChronometer.start();
            }
        });

        pauseChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                timeWaitingChronometer.stop();
            }
        });

        resetChronometerButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                timeWaitingChronometer.setBase(SystemClock.elapsedRealtime());
                secondsYouWaited = 0;
            }
        });
    }

    private void updateTipBasedOnTimeWaited(long secondsYouWaited) {
        checkListValues[9] = (secondsYouWaited > 10) ? -2 : 2;
        setTipFromWaitressChecklist();
        updateTipAndFinalBill();
    }

    private void addItemSelectedListenerToSpinner() {
        problemsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkListValues[6] = (problemsSpinner.getSelectedItem().equals("Bad")) ? -1 : 0;
                checkListValues[7] = (problemsSpinner.getSelectedItem().equals("OK")) ? 3 : 0;
                checkListValues[8] = (problemsSpinner.getSelectedItem().equals("Good")) ? 6 : 0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void addChangeListenerToRadios() {
        availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkListValues[3] = (availableBadRadio.isChecked()) ? -1 : 0;
                checkListValues[4] = (availableOkRadio.isChecked()) ? 2 : 0;
                checkListValues[5] = (availableGoodRadio.isChecked()) ? 4 : 0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }
        });
    }

    private void setUpIntroCheckBoxes() {
        friendlyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkListValues[0] = (friendlyCheckBox.isChecked()) ? 4 : 0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }
        });

        specialsCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkListValues[1] = (specialsCheckBox.isChecked()) ? 4 : 0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }
        });

        opinionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkListValues[2] = (opinionCheckBox.isChecked()) ? 4 : 0;
                setTipFromWaitressChecklist();
                updateTipAndFinalBill();
            }
        });
    }

    private void setTipFromWaitressChecklist() {
        int checkListTotal = 0;
        for(int item : checkListValues) checkListTotal += item;
        tipAmountET.setText(String.format("%.02f", checkListTotal * .01));
    }

    private void updateTipAndFinalBill() {
        double tipAmount = Double.parseDouble(tipAmountET.getText().toString());
        double finalBill = billBeforeTip + (billBeforeTip * tipAmount);
        finalBillET.setText(String.format("%.02f", finalBill));
    }

    // Important custom method to save state when app change its state
    // This way we could restore our current working values
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(TOTAL_BILL, finalBill);
        outState.putDouble(CURRENT_TIP, tipAmount);
        outState.putDouble(BILL_WITHOUT_TIP, billBeforeTip);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tip_calc, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
