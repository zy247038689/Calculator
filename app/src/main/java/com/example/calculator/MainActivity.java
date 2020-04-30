package com.example.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.math.BigDecimal;
import java.util.Stack;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements View.OnClickListener {

    EditText txt_result;
    EditText txt_edit;
    boolean isOperateDown = false;
    boolean isDotDown = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt_edit = (EditText)findViewById(R.id.et_input);
        txt_result = (EditText)findViewById(R.id.et_input);
        String strEdit = txt_edit.getText().toString();

        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_multiply).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.btn_point).setOnClickListener(this);
        findViewById(R.id.btn_offload).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_0:
                num_down("0");break;
            case R.id.btn_1:
                num_down("1");break;
            case R.id.btn_2:
                num_down("2");break;
            case R.id.btn_3:
                num_down("3");break;
            case R.id.btn_4:
                num_down("4");break;
            case R.id.btn_5:
                num_down("5");break;
            case R.id.btn_6:
                num_down("6");break;
            case R.id.btn_7:
                num_down("7");break;
            case R.id.btn_8:
                num_down("8");break;
            case R.id.btn_9:
                num_down("9");break;
            case R.id.btn_plus:
                operator_down("+");break;
            case R.id.btn_minus:
                operator_down("-");break;
            case R.id.btn_divide:
                operator_down("÷");break;
            case R.id.btn_multiply:
                operator_down("×");break;
            case R.id.btn_clear:
                isDotDown = false;
                isOperateDown = false;
                txt_edit.setText("0");
                txt_result.setText("");
                break;

                case R.id.btn_point: {
                String strEdit = txt_edit.getText().toString();
                if (!isDotDown) {
                    isDotDown = true;
                    if(Pattern.matches("^=[0-9].*", strEdit))
                        strEdit = "0";
                    txt_edit.setText(strEdit + ".");
                }
                break;
            }
            case R.id.btn_offload: {
                String strEdit = txt_edit.getText().toString();
                double temp = Double.parseDouble(strEdit);

                if (temp > 0) {
                    strEdit = "-" + strEdit;
                } else {
                    strEdit = String.valueOf(temp * (-1));
                }
                txt_edit.setText(strEdit);
            }
            break;
            case R.id.btn_equal:
                equal();break;
        }
    }


    private void num_down(String num) {
        String strEdit = txt_edit.getText().toString();
        isOperateDown = false;

        if (strEdit.equals("0") || Pattern.matches("^=[0-9].*", strEdit)) {
            txt_edit.setText(num);
            txt_result.setText("");
        } else {
            txt_edit.setText(strEdit + num);
        }
    }


    private void operator_down(String operator) {
        if(!isOperateDown) {
            String strEdit = txt_edit.getText().toString();
            isOperateDown = true;
            isDotDown = false;
            if(Pattern.matches("^=[0-9].*", strEdit))
                strEdit = strEdit.substring(1, strEdit.length());
            txt_edit.setText(strEdit + operator);
        }
    }

    private void equal() {
        String strEdit = txt_edit.getText().toString();
        int length = strEdit.length();
        if(!Pattern.matches("^=[0-9].*", strEdit))
        {
            txt_result.setText(strEdit);
            if(Pattern.matches(".*[\\+\\-\\×\\÷\\.]$", strEdit)) {
                strEdit = strEdit.substring(0, length - 1);
            }
            String postfixExp = getPostfixExp(strEdit);
            txt_edit.setText("" + calPostfix(postfixExp));
        }
    }



    private String getPostfixExp(String str) {
        String postfix = "";
        String numString = "";
        Stack numStack = new Stack();
        Stack opStack = new Stack();
        for(int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if(Character.isDigit(ch) || ch == '.') {
                numString += String.valueOf(ch);
            } else {
                if(numString.length() > 0) {
                    numStack.push(numString);
                    numString = "";
                }
                opPush(opStack, numStack, ch);
            }
        }


        if(numString.length() > 0)
            numStack.push(numString);

        while(!opStack.empty()) {
            numStack.push(opStack.pop());
        }

        while(!numStack.empty()) {
            opStack.push(numStack.pop());
        }
        while(!opStack.empty()) {
            postfix = postfix + String.valueOf(opStack.pop()) + " ";
        }
        return postfix;
    }


    private String calPostfix(String str) {
        String result = "";
        Stack numStack = new Stack();
        for(int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if(ch == ' ') {

                if(result.length() > 0 && (result.equals("+") || result.equals("-") || result.equals("×") || result.equals("÷")))
                {
                    double num = 0;
                    double secondNum = Double.parseDouble(String.valueOf(numStack.pop()));
                    double firstNum = Double.parseDouble(String.valueOf(numStack.pop()));
                    switch (result) {
                        case "+":
                            num = firstNum + secondNum;break;
                        case "-":
                            num = firstNum - secondNum;break;
                        case "×":
                            num = firstNum * secondNum;break;
                        case "÷":
                            num = firstNum / secondNum;break;
                    }
                    numStack.push(num);
                }
                else if(result.length() > 0) {
                    numStack.push(result);
                }
                result = "";
            } else {
                result += String.valueOf(ch);
            }
        }
        return BigDecimal.valueOf(Double.valueOf(String.valueOf(numStack.pop()))).stripTrailingZeros().toPlainString();
    }

    private int getOpWeight(char ch) {

        if(ch == '+' || ch == '-') return 1;
        if(ch == '×' || ch == '÷') return 4;
        return -1;
    }


    private void opPush(Stack opStack, Stack numStack, char ch) {
        if(canOpPush(opStack, ch)) {
            opStack.push(ch);
        } else {
            numStack.push(String.valueOf(opStack.pop()));
            opPush(opStack, numStack, ch);
        }
    }


    private Boolean canOpPush(Stack opStack, char ch) {

        if(opStack.empty() || (getOpWeight(ch) > getOpWeight(String.valueOf(opStack.peek()).charAt(0))))
            return true;

        return false;
    }
}

