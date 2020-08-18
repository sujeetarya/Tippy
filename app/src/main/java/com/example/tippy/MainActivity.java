package com.example.tippy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int INITIAL_TIP_PERCENTAGE = 15;

    private EditText basePriceEditText;
    private SeekBar seekBar;
    private TextView percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basePriceEditText = findViewById(R.id.base_edit_text);
        percentage = findViewById(R.id.percent_txt);

        seekBar = findViewById(R.id.seekBar);
        //  Set the Initial value to match hard code value to percentage[TextView]
        seekBar.setProgress(INITIAL_TIP_PERCENTAGE);
        //  For Initial animation of text
        tipAnimation(INITIAL_TIP_PERCENTAGE);
        //  Set SeekBar ChangeListener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //  Toast.makeText(getApplicationContext(),"seekBar progress: "+progress, Toast.LENGTH_SHORT).show();
                //  Set text of percentage in textView
                percentage.setText(seekBar.getProgress() + "%");
                //  For Text Animation
                tipAnimation(seekBar.getProgress());

                //check if base price is empty
                if (basePriceIsEmpty()){
                    //If text box is empty
                }
                else {
                    // Call calculate method to perform calculations @ parm=> progress value
                    calculate(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });

        basePriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (basePriceIsEmpty()){

                }
                else {
                    calculate(seekBar.getProgress());
                }
            }
        });
    }

    private void calculate(double progress){

        TextView tipPrice = findViewById(R.id.tip);
        TextView totalPrice = findViewById(R.id.total);

        //We get the value of Text field in String so type cast into integer
        int basePrice = Integer.parseInt(basePriceEditText.getText().toString());
        // Calculate tip
        double tip = (progress/100)*basePrice;
        //  format the double value for 2 place after decimal value set to tipPrice text view
        tipPrice.setText("₹"+String.format("%.2f",tip));

        //  Calculate total prie
        double total = tip+basePrice;
        //  set Total price
        totalPrice.setText("₹"+String.format("%.2f",total));
    }
    //To check base price is empty or note
    private boolean basePriceIsEmpty()
    {
        EditText basePriceEditText = findViewById(R.id.base_edit_text);
        //Check that base price must have value
        String base_price = basePriceEditText.getText().toString();
        //  show the error msg if the base amount is not entered
        if (TextUtils.isEmpty(base_price)) {
            basePriceEditText.setError("Value ?");
            basePriceEditText.requestFocus();
            return true;
        }
        else
            return false;
    }

    private void tipAnimation(int progress){

        TextView animationTxt = findViewById(R.id.animation_txt);
        String tipDescription;

        if (progress >= 0 && progress <= 9 ){
            tipDescription ="Poor";
            animationTxt.setTextColor(Color.RED);
        }
        else if (progress >= 10 && progress <= 14){
            tipDescription = "Acceptable";
            animationTxt.setTextColor(Color.BLUE);
        }
        else if (progress >= 15 && progress <= 19){
            tipDescription = "Good";
            animationTxt.setTextColor(Color.MAGENTA);
        }
        else if (progress >= 20 && progress <= 23){
            tipDescription = "Great";
            animationTxt.setTextColor(Color.YELLOW);
        }
        else {
            tipDescription = "Amazing";
            animationTxt.setTextColor(Color.GREEN);
        }
        animationTxt.setText(tipDescription);
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int color =(int)argbEvaluator.evaluate((float)progress/seekBar.getMax(),
                ContextCompat.getColor(this,R.color.colorWorstTip),
                ContextCompat.getColor(this,R.color.colorBestTip));
        animationTxt.setTextColor(color);
    }
}
