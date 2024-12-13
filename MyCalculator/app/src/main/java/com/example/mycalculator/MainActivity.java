package com.example.mycalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    double firstnum;
    String operation;
    boolean isNewInput = false; // Flag to track new input after operation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define buttons for numbers
        Button num0 = findViewById(R.id.num0);
        Button num1 = findViewById(R.id.num1);
        Button num2 = findViewById(R.id.num2);
        Button num3 = findViewById(R.id.num3);
        Button num4 = findViewById(R.id.num4);
        Button num5 = findViewById(R.id.num5);
        Button num6 = findViewById(R.id.num6);
        Button num7 = findViewById(R.id.num7);
        Button num8 = findViewById(R.id.num8);
        Button num9 = findViewById(R.id.num9);

        // Define buttons for operations and functionalities
        Button on = findViewById(R.id.on);
        Button off = findViewById(R.id.off);
        Button ac = findViewById(R.id.ac);
        Button del = findViewById(R.id.del);
        Button div = findViewById(R.id.div);
        Button times = findViewById(R.id.times);
        Button min = findViewById(R.id.min);
        Button plus = findViewById(R.id.plus);
        Button equal = findViewById(R.id.equal);
        Button point = findViewById(R.id.point);

        // Define the screen (TextView)
        TextView screen = findViewById(R.id.screen);

        // Reset screen on AC button press
        ac.setOnClickListener(view -> {
            firstnum = 0;
            screen.setText("0");
        });

        // Turn off calculator by hiding the screen
        off.setOnClickListener(view -> screen.setVisibility(android.view.View.GONE));

        // Turn on calculator by showing the screen
        on.setOnClickListener(view -> {
            screen.setVisibility(android.view.View.VISIBLE);
            screen.setText("0");
        });

        // Array of number buttons
        ArrayList<Button> nums = new ArrayList<>();
        nums.add(num0);
        nums.add(num1);
        nums.add(num2);
        nums.add(num3);
        nums.add(num4);
        nums.add(num5);
        nums.add(num6);
        nums.add(num7);
        nums.add(num8);
        nums.add(num9);

        // Handle number input
        for (Button b : nums) {
            b.setOnClickListener(view -> {
                if (isNewInput) {
                    screen.setText(b.getText().toString()); // New input starts after operation
                    isNewInput = false;
                } else if (!screen.getText().toString().equals("0")) {
                    screen.setText(screen.getText().toString() + b.getText().toString());
                } else {
                    screen.setText(b.getText().toString());
                }
            });
        }

        // Array of operation buttons
        ArrayList<Button> opers = new ArrayList<>();
        opers.add(div);
        opers.add(times);
        opers.add(plus);
        opers.add(min);

        // Handle operation input
        for (Button b : opers) {
            b.setOnClickListener(view -> {
                firstnum = Double.parseDouble(screen.getText().toString());
                operation = b.getText().toString();
                isNewInput = true; // Set flag for new input after operation
            });
        }

        // Handle delete button press
        del.setOnClickListener(view -> {
            String num = screen.getText().toString();
            if (num.length() > 1) {
                screen.setText(num.substring(0, num.length() - 1));
            } else if (num.length() == 1 && !num.equals("0")) {
                screen.setText("0");
            }
        });

        // Handle decimal point input
        point.setOnClickListener(view -> {
            if (!screen.getText().toString().contains(".")) {
                screen.setText(screen.getText().toString() + ".");
            }
        });

        // Handle equals button press for result
        equal.setOnClickListener(view -> {
            double secondNum = Double.parseDouble(screen.getText().toString());
            double result;
            switch (operation) {
                case "/":
                    result = firstnum / secondNum;
                    break;

                case "X":
                    result = firstnum * secondNum;
                    break;

                case "+":
                    result = firstnum + secondNum;
                    break;

                case "-":
                    result = firstnum - secondNum;
                    break;

                default:
                    result = firstnum + secondNum; // Fallback to addition if no operation
            }
            screen.setText(String.valueOf(result));
            firstnum = result; // Update firstnum for chaining operations
            isNewInput = true; // After result, flag new input
        });
    }
}
