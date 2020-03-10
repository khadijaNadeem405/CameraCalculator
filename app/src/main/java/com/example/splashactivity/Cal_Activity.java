 package com.example.splashactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Cal_Activity extends AppCompatActivity {
    TextView calculator, answer;

    String sCalculator = "", sAnswer = "", number_one = "", number_two = "", current_operator = "", prev_ans = "";
    String RorD = "RAD", sin_inv, cos_inv, tan_inv, function;
    Double Result = 0.0, numberOne = 0.0, numberTwo = 0.0, temp = 0.0;
    Boolean dot_present = false, number_allow = true, root_present = false, invert_allow = true, power_present = false;
    Boolean factorial_present = false, constant_present = false, function_present = false, value_inverted = false;
    NumberFormat format, longformate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_);

        calculator = findViewById(R.id.calculator);

        Intent intent = getIntent();
        String str = intent.getStringExtra("message");
        calculator.setText(str);

        calculator.setMovementMethod(new ScrollingMovementMethod());
        answer = findViewById(R.id.answer);


        format = new DecimalFormat("#.####");
        //we need to reformat answer if it's long
        longformate = new DecimalFormat("0.#E0");

        sin_inv = String.valueOf(Html.fromHtml("sin<sup><small>-1</small></sup>"));
        cos_inv = String.valueOf(Html.fromHtml("cos<sup><small>-1</small></sup>"));
        tan_inv = String.valueOf(Html.fromHtml("tan<sup><small>-1</small></sup>"));


        final Button btn_RorD = findViewById(R.id.btn_RorD);
        btn_RorD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RorD = btn_RorD.getText().toString();
                RorD = RorD.equals("RAD") ? "DEG" : "RAD";
                btn_RorD.setText(RorD);
            }
        });

    }

    public void onClickNumber(View v) {
        if (number_allow) {
            Button bn = (Button) v;
            sCalculator += bn.getText();
            number_one += bn.getText();
            numberOne = Double.parseDouble(number_one);

            if (function_present) {
                calculateFunction(function);
                return;
            }

            if (root_present) {
                numberOne = Math.sqrt(numberOne);
            }
            switch (current_operator) {
                case "":
                    if (power_present) {
                        temp = Result + Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result + numberOne;
                    }
                    break;

                case "+":
                    if (power_present) {
                        temp = Result + Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result + numberOne;
                    }
                    break;

                case "-":
                    if (power_present) {
                        temp = Result - Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result - numberOne;
                    }
                    break;

                case "x":
                    if (power_present) {
                        temp = Result * Math.pow(numberTwo, numberOne);
                    } else {
                        temp = Result * numberOne;
                    }
                    break;

                case "/":
                    try {
                        // divided by 0 cause execption
                        if (power_present) {
                            temp = Result / Math.pow(numberTwo, numberOne);
                        } else {
                            temp = Result / numberOne;
                        }
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    break;
            }
            sAnswer = format.format(temp).toString();
            updateCalculator();
        }

    }

    public void onClickOperator(View v) {
        Button ob = (Button) v;
        if (sAnswer != "") {

            if (current_operator != "") {
                char c = getcharfromLast(sCalculator, 2);
                if (c == '+' || c == '-' || c == 'x' || c == '/') {
                    sCalculator = sCalculator.substring(0, sCalculator.length() - 3);
                }
            }
            sCalculator = sCalculator + "\n" + ob.getText() + " ";
            number_one = "";
            number_two = "";
            Result = temp;
            current_operator = ob.getText().toString();
            updateCalculator();
            numberTwo = 0.0;
            dot_present = false;
            number_allow = true;
            root_present = false;
            invert_allow = true;
            power_present = false;
            factorial_present = false;
            function_present = false;
            constant_present = false;
            value_inverted = false;

        }
    }

    private char getcharfromLast(String s, int i) {
        char c = s.charAt(s.length() - i);
        return c;
    }

    public void onClickClear(View v) {
        cleardata();
    }
    public void cleardata(){
        sCalculator = "";
        sAnswer = "";
        current_operator = "";
        number_one = "";
        number_two = "";
        Result = 0.0;
        numberOne = 0.0;
        numberTwo = 0.0;
        temp = 0.0;
        updateCalculator();
        dot_present = false;
        number_allow = true;
        prev_ans = "";
        root_present = false;
        invert_allow = true;
        power_present = false;
        factorial_present = false;
        constant_present = false;
        function_present = false;
        value_inverted = false;

    }

    public void updateCalculator() {
        calculator.setText(sCalculator);
        answer.setText(sAnswer);

    }

    public void onDotClick(View view) {
        if (!dot_present) {
            if (number_one.length() == 0) {
                number_one = "0.";
                sCalculator = "0.";
                sAnswer = "0.";
                dot_present = true;
                updateCalculator();
            } else {
                number_one += ".";
                sCalculator += ".";
                sAnswer += ".";
                dot_present = true;
                updateCalculator();

            }
        }
    }

    public void onClickEqual(View view) {
        showresult();
    }

    public void showresult() {
        if (sAnswer != "" && sAnswer != prev_ans) {
            sCalculator += "\n=" + sAnswer + "\n--------\n" + sAnswer + "";
            number_one = "";
            number_two = "";
            numberOne = 0.0;
            numberTwo = 0.0;
            Result = temp;
            power_present = false;
            prev_ans = sAnswer;
            updateCalculator();
            dot_present = true;
            number_allow = false;
            factorial_present = false;
            constant_present = false;
            value_inverted = false;
            function_present = false;

        }
    }

    public void onModuloClick(View view) {
        if (sAnswer != "" && getcharfromLast(sCalculator, 1) != ' ') {
            sCalculator += "% ";
            //value of temp will change according to the operator
            switch (current_operator) {
                case "":
                    temp = temp / 100;
                    break;
                case "+":
                    temp = Result + ((Result * numberOne) / 100);
                    break;
                case "-":
                    temp = Result - ((Result * numberOne) / 100);
                    break;
                case "x":
                    temp = Result * (numberOne / 100);
                    break;
                case "/":
                    try {
                        temp = Result / (numberOne / 100);
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    break;
            }
            sAnswer = format.format(temp).toString();
            if (sAnswer.length() > 9) {
                sAnswer = longformate.format(temp).toString();
            }

            Result = temp;
            showresult();
        }
    }

    public void onPorMClick(View view) {
        if (invert_allow) {
            if (sAnswer != "" && getcharfromLast(sCalculator, 1) != ' ') {
                numberOne = numberOne * (-1);
                number_one = format.format(numberOne).toString();
                switch (current_operator) {
                    case "":
                        temp = numberOne;
                        sCalculator = number_one;
                        break;
                    case "+":
                        temp = Result + numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(sCalculator, ' ');
                        sCalculator += number_one;
                        break;
                    case "-":
                        temp = Result - numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(sCalculator, ' ');
                        sCalculator += number_one;
                        break;
                    case "*":
                        temp = Result * numberOne;
                        //we need to add - sign in the starting of the string
                        removeuntilchar(sCalculator, ' ');
                        sCalculator += number_one;
                        break;
                    case "/":
                        try {
                            temp = Result / numberOne;
                            //we need to add - sign in the starting of the string
                            removeuntilchar(sCalculator, ' ');
                            sCalculator += number_one;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }
                        break;
                }

                sAnswer = format.format(temp).toString();
                updateCalculator();
            }
        }
    }

    public void removeuntilchar(String str, char chr) {
        char c = getcharfromLast(str, 1);
        if (c != chr) {
            //remove last char

            str = removechar(str, 1);
            sCalculator = str;
            updateCalculator();
            removeuntilchar(str, chr);
        }

    }

    public String removechar(String str, int i) {
        char c = str.charAt(str.length() - 1);
        if (c == '.' && !dot_present) {
            dot_present = false;
        }
        if (c == '^') {
            power_present = false;
        }
        if (c == ' ') {
            return str.substring(0, str.length() - (i - 1));
        }
        return str.substring(0, str.length() - i);
    }

    public void onRootClick(View view) {
        Button root = (Button) view;
        //first we check if root is present or not
        if (sAnswer == "" && Result == 0 && !root_present && !function_present) {
            sCalculator = root.getText().toString();
            root_present = true;
            invert_allow = false;
            updateCalculator();
        } else if (getcharfromLast(sCalculator, 1) == ' ' && current_operator != "" && !root_present) {
            sCalculator += root.getText().toString();
            root_present = true;
            invert_allow = false;
            updateCalculator();

        }
    }

    public void onPowerClear(View view) {
        Button power = (Button) view;
        if (sCalculator != "" && !root_present && !power_present && !function_present) {
            if (getcharfromLast(sCalculator, 1) != ' ') {
                sCalculator += power.getText().toString();
                //we need second variable for the power
                number_two = number_one;
                numberTwo = numberOne;
                number_one = "";
                power_present = true;
                updateCalculator();
            }
        }
    }

    public void onSquareClick(View view) {
        if (sCalculator != "" && sAnswer != "") {
            if (!root_present && !function_present && !power_present && getcharfromLast(sCalculator, 1) != ' ' && getcharfromLast(sCalculator, 1) != ' ') {
                numberOne = numberOne * numberOne;
                number_one = format.format(numberOne).toString();
                if (current_operator == "") {
                    if (number_one.length() > 9) {
                        number_one = longformate.format(numberOne);
                    }
                    sCalculator = number_one;
                    temp = numberOne;
                } else {
                    switch (current_operator) {
                        case "+":
                            temp = Result + numberOne;
                            break;
                        case "-":
                            temp = Result - numberOne;
                            break;
                        case "x":
                            temp = Result * numberOne;
                            break;
                        case "/":
                            try {
                                temp = Result / numberOne;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                    }
                    removeuntilchar(sCalculator, ' ');
                    if (number_one.length() > 9) {
                        number_one = longformate.format(numberOne);
                    }
                    sCalculator += number_one;
                }
                sAnswer = format.format(temp);
                if (sAnswer.length() > 9) {
                    sAnswer = longformate.format(temp);
                }
                updateCalculator();
            }
        }
    }

    public void onClickFactorial(View view) {
        if (!sAnswer.equals("") && !factorial_present && !root_present && !dot_present && !power_present && !function_present) {
            if (getcharfromLast(sCalculator, 1) != ' ') {
                for (int i = 1; i < Integer.parseInt(number_one); i++) {
                    numberOne *= i;
                }
                if (numberOne.equals(0.0)) {
                    numberOne = 1.0;
                }
                number_one = format.format(numberOne).toString();
                switch (current_operator) {
                    case "":
                        Result = numberOne;
                        break;
                    case "+":
                        Result += numberOne;
                        break;
                    case "-":
                        Result -= numberOne;
                        break;
                    case "x":
                        Result *= numberOne;
                        break;
                    case "/":
                        try {
                            Result /= numberOne;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }

                        break;
                }
                sAnswer = Result.toString();
                temp = Result;
                sCalculator += "! ";
                factorial_present = true;
                number_allow = false;
                updateCalculator();
            }
        }
    }

    public void onClickInverse(View view) {
        if (!sAnswer.equals("") && !factorial_present && !root_present && !dot_present && !power_present && !function_present) {
            if (getcharfromLast(sCalculator, 1) != ' ') {
                numberOne = Math.pow(numberOne, -1);
                number_one = format.format(numberOne).toString();
                switch (current_operator) {
                    case "":
                        temp = numberOne;
                        sCalculator = number_one;
                        break;
                    case "+":
                        temp = Result + numberOne;
                        removeuntilchar(sCalculator, ' ');
                        sCalculator += number_one;
                        break;
                    case "-":
                        temp = Result - numberOne;
                        removeuntilchar(sCalculator, ' ');
                        sCalculator += number_one;
                        break;
                    case "x":
                        temp = Result * numberOne;
                        removeuntilchar(sCalculator, ' ');
                        sCalculator += number_one;
                        break;
                    case "/":
                        try {
                            temp = Result / numberOne;
                            removeuntilchar(sCalculator, ' ');
                            sCalculator += number_one;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }

                        break;
                }
                sAnswer = format.format(temp).toString();
                updateCalculator();
            }
        }
    }

    public void onClickPIorE(View view) {
        Button btn_PIorE = (Button) view;
        number_allow = false;
        if (!root_present && !dot_present && !power_present && !factorial_present && !constant_present && !function_present) {
            String str_PIorE = btn_PIorE.getText().toString() + " ";
            if (!str_PIorE.equals("e ")) {
                str_PIorE = "\u03A0" + " ";
            }
            if (sCalculator == "") {
                number_one = str_PIorE;
                if (str_PIorE.equals("e ")) {
                    numberOne = Math.E;
                } else {
                    numberOne = Math.PI;
                }
                temp = numberOne;
                sCalculator += number_one;
                sAnswer = format.format(temp).toString();
                updateCalculator();
            } else {
                if (str_PIorE.equals("e ")) {
                    //use ternary operation
                    numberOne = getcharfromLast(sCalculator, 1) == ' ' ? Math.E : Double.parseDouble(number_one) * Math.E;
                } else {
                    numberOne = getcharfromLast(sCalculator, 1) == ' ' ? Math.PI : Double.parseDouble(number_one) * Math.PI;
                }
                switch (current_operator) {

                    case "":
                        temp = Result + numberOne;
                        break;

                    case "+":
                        temp = Result + numberOne;
                        break;

                    case "-":
                        temp = Result - numberOne;
                        break;

                    case "x"://we use x instedof * so change it in another function if you not.
                        temp = Result * numberOne;
                        break;

                    case "/":
                        try {
                            temp = Result / numberOne;
                        } catch (Exception e) {
                            sAnswer = e.getMessage();
                        }
                        break;
                }
                sCalculator += str_PIorE;
                sAnswer = format.format(temp).toString();
                value_inverted = value_inverted ? false:true;
                updateCalculator();
            }
            constant_present = true;
        }
    }

    public void onClickFunction(View view) {
        Button func = (Button) view;
        function = func.getHint().toString();
        if (!function_present && !root_present && !power_present && !factorial_present && !dot_present) {
            calculateFunction(function);
        }
    }

    public void calculateFunction(String function) {
        function_present = true;
        if (current_operator != "" && getcharfromLast(sCalculator, 1) == ' ') {
            switch (function) {
                case "sin_inv":
                    sCalculator += sin_inv + "(";
                    break;
                case "cos_inv":
                    sCalculator += cos_inv + "(";
                    break;
                case "tan_inv":
                    sCalculator += tan_inv + "(";
                    break;
                default:
                    sCalculator += function + "(";
                    break;
            }
            updateCalculator();
        } else {
            switch (current_operator) {
                case "":
                    if (sCalculator.equals("")) {
                        switch (function) {
                            case "sin_inv":
                                sCalculator += sin_inv + "( ";
                                break;
                            case "cos_inv":
                                sCalculator += cos_inv + "( ";
                                break;
                            case "tan_inv":
                                sCalculator += tan_inv + "( ";
                                break;
                            default:
                                sCalculator += function + "( ";
                                break;
                        }
                    } else {
                        switch (function) {
                            case "log":
                                temp = Result + Math.log10(numberOne);
                                sCalculator = "log( " + number_one;
                                break;

                            case "ln":
                                temp = Result + Math.log(numberOne);
                                sCalculator = "ln( " + number_one;
                                break;

                            case "sin":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.sin(numberOne);
                                sCalculator = "sin( " + number_one;
                                break;

                            case "sin_inv":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.asin(numberOne);
                                sCalculator = sin_inv + "( " + number_one;
                                break;

                            case "cos":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.cos(numberOne);
                                sCalculator = "cos( " + number_one;
                                break;
                            case "cos_inv":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.acos(numberOne);
                                sCalculator = cos_inv + "( " + number_one;
                                break;

                            case "tan":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.tan(numberOne);
                                sCalculator = "tan( " + number_one;
                                break;

                            case "tan_inv":
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result + Math.atan(numberOne);
                                sCalculator = tan_inv + "( " + number_one;
                                break;
                        }
                    }
                    sAnswer = temp.toString();
                    updateCalculator();
                    break;
                case "+":
                    removeuntilchar(sCalculator, ' ');
                    switch (function) {
                        case "log":
                            temp = Result + Math.log10(numberOne);
                            sCalculator += "log(" + number_one;
                            break;

                        case "ln":
                            temp = Result + Math.log(numberOne);
                            sCalculator += "ln(" + number_one;
                            break;

                        case "sin":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.sin(numberOne);
                            sCalculator += "sin(" + number_one;
                            break;

                        case "sin_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.asin(numberOne);
                            sCalculator += sin_inv + "(" + number_one;
                            break;

                        case "cos":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.cos(numberOne);
                            sCalculator += "cos(" + number_one;
                            break;
                        case "cos_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.acos(numberOne);
                            sCalculator += cos_inv + "(" + number_one;
                            break;

                        case "tan":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.tan(numberOne);
                            sCalculator += "tan(" + number_one;
                            break;

                        case "tan_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result + Math.atan(numberOne);
                            sCalculator += tan_inv + "(" + number_one;
                            break;
                    }
                    sAnswer = temp.toString();
                    updateCalculator();
                    break;
                case "-":
                    removeuntilchar(sCalculator, ' ');
                    switch (function) {
                        case "log":
                            temp = Result - Math.log10(numberOne);
                            sCalculator += "log(" + number_one;
                            break;

                        case "ln":
                            temp = Result - Math.log(numberOne);
                            sCalculator += "ln(" + number_one;
                            break;

                        case "sin":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.sin(numberOne);
                            sCalculator += "sin(" + number_one;
                            break;

                        case "sin_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.asin(numberOne);
                            sCalculator += sin_inv + "(" + number_one;
                            break;

                        case "cos":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.cos(numberOne);
                            sCalculator += "cos(" + number_one;
                            break;
                        case "cos_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.acos(numberOne);
                            sCalculator += cos_inv + "(" + number_one;
                            break;

                        case "tan":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.tan(numberOne);
                            sCalculator += "tan(" + number_one;
                            break;

                        case "tan_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result - Math.atan(numberOne);
                            sCalculator += tan_inv + "(" + number_one;
                            break;
                    }
                    sAnswer = temp.toString();
                    updateCalculator();
                    break;
                case "x":
                    removeuntilchar(sCalculator, ' ');
                    switch (function) {
                        case "log":
                            temp = Result * Math.log10(numberOne);
                            sCalculator += "log(" + number_one;
                            break;

                        case "ln":
                            temp = Result * Math.log(numberOne);
                            sCalculator += "ln(" + number_one;
                            break;

                        case "sin":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.sin(numberOne);
                            sCalculator += "sin(" + number_one;
                            break;

                        case "sin_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.asin(numberOne);
                            sCalculator += sin_inv + "(" + number_one;
                            break;

                        case "cos":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.cos(numberOne);
                            sCalculator += "cos(" + number_one;
                            break;
                        case "cos_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.acos(numberOne);
                            sCalculator += cos_inv + "(" + number_one;
                            break;

                        case "tan":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.tan(numberOne);
                            sCalculator += "tan(" + number_one;
                            break;

                        case "tan_inv":
                            if (RorD.equals("DEG")) {
                                numberOne = Math.toDegrees(numberOne);
                            }
                            temp = Result * Math.atan(numberOne);
                            sCalculator += tan_inv + "(" + number_one;
                            break;
                    }
                    sAnswer = temp.toString();
                    updateCalculator();
                    break;
                case "/":
                    removeuntilchar(sCalculator, ' ');
                    switch (function) {
                        case "log":
                            try {
                                temp = Result / Math.log10(numberOne);
                                sCalculator += "log(" + number_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;

                        case "ln":
                            try {
                                temp = Result / Math.log(numberOne);
                                sCalculator += "ln(" + number_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;

                        case "sin":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.sin(numberOne);
                                sCalculator += "sin(" + number_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                        case "sin_inv":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.asin(numberOne);
                                sCalculator += sin_inv + "(" + number_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;

                        case "cos":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.cos(numberOne);
                                sCalculator += "cos(" + number_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;

                        case "cos_inv":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.acos(numberOne);
                                sCalculator += cos_inv + "(" + number_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                        case "tan":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.tan(numberOne);
                                sCalculator += "tan(" + number_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;

                        case "tan_inv":
                            try {
                                if (RorD.equals("DEG")) {
                                    numberOne = Math.toDegrees(numberOne);
                                }
                                temp = Result / Math.atan(numberOne);
                                sCalculator += tan_inv + "(" + number_one;
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            break;
                    }
                    sAnswer = temp.toString();
                    updateCalculator();
                    break;
            }
        }
    }

    public void onClickDelete(View view) {
        if (function_present) {
            DeleteFunction();
            return;
        }
        if (root_present) {
            removeRoot();
            return;
        }
        if (power_present) {
            removePower();
            return;
        }
        if (sAnswer != "") {
            if (getcharfromLast(sCalculator, 1) != ' ') {
                if (number_one.length() < 2 && current_operator != "") {
                    number_one = "";
                    temp = Result;
                    sAnswer = format.format(Result).toString();
                    sCalculator = removechar(sCalculator, 1);
                    updateCalculator();
                } else {
                    switch (current_operator) {
                        case "":
                            if (value_inverted) {
                                sAnswer = sAnswer.substring(1, sAnswer.length());
                                sCalculator = sCalculator.substring(1, sAnswer.length());
                                updateCalculator();
                            }
                            if (sCalculator.length() < 2) {
                                cleardata();
                            } else {
                                if (getcharfromLast(sCalculator, 1) == '.') {
                                    dot_present = false;
                                }
                                number_one = removechar(number_one, 1);
                                numberOne = Double.parseDouble(number_one);
                                temp = numberOne;
                                sCalculator = number_one;
                                sAnswer = number_one;
                                updateCalculator();
                            }
                            break;
                        case "+":
                            if (value_inverted) {
                                numberOne = numberOne * (-1);
                                number_one = format.format(numberOne).toString();
                                temp = Result + numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculator, ' ');
                                sCalculator += number_one;
                                updateCalculator();
                                value_inverted = value_inverted ? false : true;
                            }
                            if (getcharfromLast(sCalculator, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result + numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculator = removechar(sCalculator, 1);
                            updateCalculator();
                            break;
                        case "-":
                            if (value_inverted) {
                                numberOne = numberOne * (-1);
                                number_one = format.format(numberOne).toString();
                                temp = Result - numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculator, ' ');
                                sCalculator += number_one;
                                updateCalculator();
                                value_inverted = value_inverted ? false : true;
                            }
                            if (getcharfromLast(sCalculator, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result - numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculator = removechar(sCalculator, 1);
                            updateCalculator();
                            break;
                        case "x":
                            if (value_inverted) {
                                numberOne = numberOne * (-1);
                                number_one = format.format(numberOne).toString();
                                temp = Result * numberOne;
                                sAnswer = format.format(temp).toString();
                                removeuntilchar(sCalculator, ' ');
                                sCalculator += number_one;
                                updateCalculator();
                                value_inverted = value_inverted ? false : true;
                            }
                            if (getcharfromLast(sCalculator, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            temp = Result * numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculator = removechar(sCalculator, 1);
                            updateCalculator();
                            break;
                        case "/":
                            try {
                                if (value_inverted) {
                                    numberOne = numberOne * (-1);
                                    number_one = format.format(numberOne).toString();
                                    temp = Result / numberOne;
                                    sAnswer = format.format(temp).toString();
                                    removeuntilchar(sCalculator, ' ');
                                    sCalculator += number_one;
                                    updateCalculator();
                                    value_inverted = value_inverted ? false : true;
                                }
                                if (getcharfromLast(sCalculator, 1) == '.') {
                                    dot_present = false;
                                }
                                number_one = removechar(number_one, 1);
                                if (number_one.length() == 1 && number_one == ".") {
                                    numberOne = Double.parseDouble(number_one);
                                }
                                numberOne = Double.parseDouble(number_one);
                                temp = Result / numberOne;
                                sAnswer = format.format(temp).toString();
                                sCalculator = removechar(sCalculator, 1);
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            updateCalculator();
                            break;
                    }
                }
            }
        }
    }

    public void removePower() {
        if (sAnswer != "" && sCalculator != "") {
            switch (current_operator) {
                case "":
                    if (getcharfromLast(sCalculator, 1) == '^') {
                        sCalculator = removechar(sCalculator, 1);
                        number_one = number_two;
                        numberOne = Double.parseDouble(number_one);
                        number_two = "";
                        numberTwo = 0.0;
                        updateCalculator();
                    } else if (getcharfromLast(sCalculator, 2) == '^') {
                        number_one = "";
                        numberOne = 0.0;
                        temp = numberTwo;
                        sAnswer = format.format(temp).toString();
                        sCalculator = removechar(sCalculator, 1);
                        updateCalculator();
                    } else {
                        if (getcharfromLast(sCalculator, 1) == '.') {
                            dot_present = false;
                        }
                        number_one = removechar(number_one, 1);
                        numberOne = Double.parseDouble(number_one);
                        temp = Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp).toString();
                        sCalculator = removechar(sCalculator, 1);
                        updateCalculator();
                    }
                    break;
                case "+":
                    if (getcharfromLast(sCalculator, 1) == '^') {
                        sCalculator = removechar(sCalculator, 1);
                        number_one = number_two;
                        numberOne = Double.parseDouble(number_one);
                        number_two = "";
                        numberTwo = 0.0;
                        updateCalculator();
                    } else if (getcharfromLast(sCalculator, 2) == '^') {
                        number_one = "";
                        numberOne = 0.0;
                        temp = Result + numberTwo;
                        sAnswer = format.format(temp).toString();
                        sCalculator = removechar(sCalculator, 1);
                        updateCalculator();
                    } else {
                        if (getcharfromLast(sCalculator, 1) == '.') {
                            dot_present = false;
                        }
                        number_one = removechar(number_one, 1);
                        numberOne = Double.parseDouble(number_one);
                        temp = Result + Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp).toString();
                        sCalculator = removechar(sCalculator, 1);
                        updateCalculator();
                    }
                    break;
                case "-":
                    if (getcharfromLast(sCalculator, 1) == '^') {
                        sCalculator = removechar(sCalculator, 1);
                        number_one = number_two;
                        numberOne = Double.parseDouble(number_one);
                        number_two = "";
                        numberTwo = 0.0;
                        updateCalculator();
                    } else if (getcharfromLast(sCalculator, 2) == '^') {
                        number_one = "";
                        numberOne = 0.0;
                        temp = Result - numberTwo;
                        sAnswer = format.format(temp).toString();
                        sCalculator = removechar(sCalculator, 1);
                        updateCalculator();
                    } else {
                        if (getcharfromLast(sCalculator, 1) == '.') {
                            dot_present = false;
                        }
                        number_one = removechar(number_one, 1);
                        numberOne = Double.parseDouble(number_one);
                        temp = Result - Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp).toString();
                        sCalculator = removechar(sCalculator, 1);
                        updateCalculator();
                    }
                    break;
                case "x":
                    if (getcharfromLast(sCalculator, 1) == '^') {
                        sCalculator = removechar(sCalculator, 1);
                        number_one = number_two;
                        numberOne = Double.parseDouble(number_one);
                        number_two = "";
                        numberTwo = 0.0;
                        updateCalculator();
                    } else if (getcharfromLast(sCalculator, 2) == '^') {
                        number_one = "";
                        numberOne = 0.0;
                        temp = Result * numberTwo;
                        sAnswer = format.format(temp).toString();
                        sCalculator = removechar(sCalculator, 1);
                        updateCalculator();
                    } else {
                        if (getcharfromLast(sCalculator, 1) == '.') {
                            dot_present = false;
                        }
                        number_one = removechar(number_one, 1);
                        numberOne = Double.parseDouble(number_one);
                        temp = Result * Math.pow(numberTwo, numberOne);
                        sAnswer = format.format(temp).toString();
                        sCalculator = removechar(sCalculator, 1);
                        updateCalculator();
                    }
                    break;
                case "/":
                    try {
                        if (getcharfromLast(sCalculator, 1) == '^') {
                            sCalculator = removechar(sCalculator, 1);
                            number_one = number_two;
                            numberOne = Double.parseDouble(number_one);
                            number_two = "";
                            numberTwo = 0.0;
                            updateCalculator();
                        } else if (getcharfromLast(sCalculator, 2) == '^') {
                            number_one = "";
                            numberOne = 0.0;
                            temp = Result / numberTwo;
                            sAnswer = format.format(temp).toString();
                            sCalculator = removechar(sCalculator, 1);
                            updateCalculator();
                        } else {
                            if (getcharfromLast(sCalculator, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            numberOne = Double.parseDouble(number_one);
                            temp = Result / Math.pow(numberTwo, numberOne);
                            sAnswer = format.format(temp).toString();
                            sCalculator = removechar(sCalculator, 1);
                            updateCalculator();
                        }
                    } catch (Exception e) {
                        sAnswer = e.getMessage();
                    }
                    updateCalculator();
                    break;
            }
        }
    }

    public void removeRoot() {
        if (getcharfromLast(sCalculator, 1) != ' ') {
            if (String.valueOf(getcharfromLast(sCalculator, 1)).equals("\u221A")) {
                sCalculator = removechar(sCalculator, 1);
                root_present = false;
                invert_allow = true;
                updateCalculator();
            }
            if (sAnswer != "") {
                if (number_one.length() < 2 && current_operator != "") {
                    number_one = "";
                    numberOne = Result;
                    temp = Result;
                    sAnswer = format.format(Result).toString();
                    sCalculator = removechar(sCalculator, 1);
                    updateCalculator();
                } else {
                    switch (current_operator) {
                        case "":
                            if (sCalculator.length() <= 2) {
                                cleardata();
                            } else {
                                if (getcharfromLast(sCalculator, 1) == '.') {
                                    dot_present = false;
                                }
                                number_one = removechar(number_one, 1);
                                numberOne = Double.parseDouble(number_one);
                                numberOne = Math.sqrt(numberOne);
                                temp = numberOne;
                                sAnswer = format.format(temp).toString();
                                sCalculator = "\u221A" + number_one;
                                updateCalculator();
                            }
                            break;
                        case "+":
                            if (getcharfromLast(sCalculator, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            numberOne = Math.sqrt(numberOne);
                            temp = Result + numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculator = removechar(sCalculator, 1);
                            updateCalculator();
                            break;

                        case "-":
                            if (getcharfromLast(sCalculator, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            numberOne = Math.sqrt(numberOne);
                            temp = Result - numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculator = removechar(sCalculator, 1);
                            updateCalculator();
                            break;
                        case "x":
                            if (getcharfromLast(sCalculator, 1) == '.') {
                                dot_present = false;
                            }
                            number_one = removechar(number_one, 1);
                            if (number_one.length() == 1 && number_one == ".") {
                                numberOne = Double.parseDouble(number_one);
                            }
                            numberOne = Double.parseDouble(number_one);
                            numberOne = Math.sqrt(numberOne);
                            temp = Result * numberOne;
                            sAnswer = format.format(temp).toString();
                            sCalculator = removechar(sCalculator, 1);
                            updateCalculator();
                            break;

                        case "/":
                            try {
                                if (getcharfromLast(sCalculator, 1) == '.') {
                                    dot_present = false;
                                }
                                number_one = removechar(number_one, 1);
                                if (number_one.length() == 1 && number_one == ".") {
                                    numberOne = Double.parseDouble(number_one);
                                }
                                numberOne = Double.parseDouble(number_one);
                                numberOne = Math.sqrt(numberOne);
                                temp = Result + numberOne;
                                sAnswer = format.format(temp).toString();
                                sCalculator = removechar(sCalculator, 1);
                            } catch (Exception e) {
                                sAnswer = e.getMessage();
                            }
                            updateCalculator();
                            break;
                    }
                }
            }
        }
    }
    public void DeleteFunction() {
        if (current_operator == "") {
            if (getcharfromLast(sCalculator, 1) == ' ') {
                cleardata();
            } else if (getcharfromLast(sCalculator, 2) == ' ') {
                cleardata();
            } else {
                sCalculator = removechar(sCalculator, 1);
                number_one = removechar(number_one, 1);
                numberOne = Double.parseDouble(number_one);
                calculateFunction(function);
            }
            updateCalculator();
        } else {
            if (getcharfromLast(sCalculator, 1) == '(') {
                removeuntilchar(sCalculator, ' ');
                function_present = false;
            } else if (getcharfromLast(sCalculator, 2) == '(') {
                sCalculator = removechar(sCalculator, 1);
                number_one = "";
                temp = Result;
                sAnswer = format.format(Result).toString();
            } else {
                sCalculator = removechar(sCalculator, 1);
                number_one = removechar(number_one, 1);
                numberOne = Double.parseDouble(number_one);
                calculateFunction(function);
            }
            updateCalculator();
        }

    }
}

