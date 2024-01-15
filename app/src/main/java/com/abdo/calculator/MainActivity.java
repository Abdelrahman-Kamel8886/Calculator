package com.abdo.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.abdo.calculator.databinding.ActivityMainBinding;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String final_result ;
    Float num1 , num2;
    boolean active ;

    String formula = "";
    String tempFormula = "";
    Set<String> myStrings;
    SharedPreferences.Editor editor;
    SharedPreferences settings;
    HistoryRecyclerAdapter adapter = new HistoryRecyclerAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.navView.setVisibility(View.GONE);
        setContentView(binding.getRoot());
        settings = this.getSharedPreferences("YourActivityPreferences", this.MODE_PRIVATE);
        onClicks();
    }

    private void onClicks(){
        binding.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.navView.getVisibility()== View.VISIBLE){
                    binding.navView.setVisibility(View.GONE);
                }
            }
        });
        binding.history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.navView.setVisibility(View.VISIBLE);

                editor = settings.edit();
                myStrings = settings.getStringSet("history", new HashSet<String>());
                adapter.setList(myStrings);
                binding.recyclerResult.setAdapter(adapter);
            }
        });
        binding.buttonAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.resultTv.setText("");
                binding.finalTv.setText("");
                final_result = null;
            }
        });

        binding.button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.resultTv.getText().equals("")){
                   onNumClicks("0");
                }
            }
        });
        binding.button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.resultTv.getText().equals("")){
                    onNumClicks("00");
                }
            }
        });
        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("1");
            }
        });
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("2");
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("3");
            }
        });
        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("4");
            }
        });
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("5");
            }
        });
        binding.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("6");
            }
        });
        binding.button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("7");
            }
        });
        binding.button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("8");
            }
        });
        binding.button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNumClicks("9");

            }
        });

        binding.buttonPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperatorClicks("^");
            }
        });
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperatorClicks("+");
            }
        });
        binding.buttonMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperatorClicks("x");
            }
        });
        binding.buttonDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperatorClicks("/");
            }
        });
        binding.buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.resultTv.getText().equals("")){
                    binding.resultTv.setText(binding.resultTv.getText()+"-");
                }
                else{
                    onOperatorClicks("-");
                }

            }
        });

        binding.buttonPercentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String res = binding.resultTv.getText().toString().trim();
                if (!res.isEmpty()){
                    char e = res.charAt(res.length()-1);

                    if (e=='-'||e=='+'||e=='/'||e=='x'||e=='^'){
                        binding.resultTv.setText(res.substring(0,res.length()-1)+"%");
                        final_result = null;
                    }
                    else{
                        binding.resultTv.setText(binding.resultTv.getText()+"%");
                    }

                    String data = binding.resultTv.getText().toString().trim();
                    data = data.replaceAll("x","*");
                    data = data.replaceAll("%%%","/1000000*");
                    data = data.replaceAll("%%","/10000*");
                    data = data.replaceAll("%","/100*");
                    checkForPowerOf(data);
                    data = tempFormula;
                    tempFormula="";

                    if (data.charAt(data.length()-1)=='*'){
                        data = data.substring(0,data.length()-1);
                    }
                    final_result = getResult(data);


                    if(!final_result.equals("Err")){
                        binding.finalTv.setText("= "+final_result);
                    }
                    final_result = null;
                }
            }
        });
        binding.buttonComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.resultTv.getText().equals("")){
                    String res = binding.resultTv.getText().toString().trim();
                    char e = res.charAt(res.length()-1);

                    if (e=='-'||e=='+'||e=='/'||e=='x'||e=='^'){
                        binding.resultTv.setText(res+"0.");
                        final_result = null;
                    }
                    else if(e!='.'){
                        binding.resultTv.setText(res+".");
                        final_result = null;
                    }
                }
                else {
                    binding.resultTv.setText(binding.resultTv.getText()+"0.");
                }
            }
        });
        binding.buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!binding.resultTv.getText().toString().trim().isEmpty()){
                    String data = binding.resultTv.getText().toString().trim();
                    data = data.replaceAll("x","*");
                    data = data.replaceAll("%%%","/1000000*");
                    data = data.replaceAll("%%","/10000*");
                    data = data.replaceAll("%","/100*");
                    checkForPowerOf(data);
                    data = tempFormula;
                    tempFormula="";
                    if (data.charAt(data.length()-1)=='*'){
                        data = data.substring(0,data.length()-1);
                    }
                    final_result = getResult(data);
                    if(final_result.equals("Err")){
                        binding.finalTv.setText("Expression Error");
                    }
                    else{
                        binding.finalTv.setText("");
                        binding.resultTv.setText(final_result);
                        myStrings.add(final_result);
                        editor.putStringSet("history", myStrings);
                        editor.commit();
                    }
                }


            }
        });
        binding.erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!binding.resultTv.getText().toString().trim().isEmpty()){
                    String res = binding.resultTv.getText().toString().trim();
                    binding.resultTv.setText(res.substring(0,res.length()-1));

                    if (!binding.resultTv.getText().toString().trim().isEmpty()){
                        String data = binding.resultTv.getText().toString().trim();
                        data = data.replaceAll("x","*");
                        data = data.replaceAll("%%%","/1000000*");
                        data = data.replaceAll("%%","/10000*");
                        data = data.replaceAll("%","/100*");
                        checkForPowerOf(data);
                        data = tempFormula;
                        tempFormula="";

                        if (data.charAt(data.length()-1)=='*'){
                            data = data.substring(0,data.length()-1);
                        }
                        final_result = getResult(data);


                        if(!final_result.equals("Err")){
                            binding.finalTv.setText("= "+final_result);
                        }
                    }
                    else{
                        binding.finalTv.setText("");
                    }


                    final_result = null;

                }
            }
        });

    }

    private void onNumClicks(String num){
        if (final_result!=null){
            final_result = null;
            binding.resultTv.setText("");
            binding.resultTv.setText(binding.resultTv.getText()+num);




        }
        else {
            binding.resultTv.setText(binding.resultTv.getText()+num);
            String data = binding.resultTv.getText().toString().trim();
            data = data.replaceAll("x","*");
            data = data.replaceAll("%%%","/1000000*");
            data = data.replaceAll("%%","/10000*");
            data = data.replaceAll("%","/100*");
            checkForPowerOf(data);
            data = tempFormula;
            tempFormula="";
            if (data.charAt(data.length()-1)=='*'){
                data = data.substring(0,data.length()-1);
            }
            final_result = getResult(data);


            if(!final_result.equals("Err")){
                binding.finalTv.setText("= "+final_result);
                final_result=null;
            }
        }
    }

    private void onOperatorClicks(String sign){

        if (!binding.resultTv.getText().equals("")){
            String res = binding.resultTv.getText().toString().trim();
            char e = res.charAt(res.length()-1);
            char a;
            if (res.length()>1){
                a = res.charAt(res.length()-2);
            }
            else {
                a = 'n';
            }


            if (e=='-'&&res.length()==1){
                binding.resultTv.setText(res+"1"+sign);
            }
            else if (e=='-'||e=='+'||e=='/'||e=='x'||e=='^'){

                if (sign.equals("-")&&e!='-'){
                    binding.resultTv.setText(res+sign);
                }
                else {
                    if (a!='n'){
                        if (a=='+'||e=='/'||a=='x'||a=='^'){
                            binding.resultTv.setText(res.substring(0,res.length()-1));
                        }
                        else {
                            binding.resultTv.setText(res.substring(0,res.length()-1)+sign);
                        }
                    }
                    else {
                        binding.resultTv.setText(res.substring(0,res.length()-1)+sign);
                    }


                }
                final_result = null;
            }
            else{
                binding.resultTv.setText(res+sign);
                final_result = null;
            }
        }
    }


    String getResult(String data){
        try{
            Context context  = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult =  context.evaluateString(scriptable,data,"Javascript",1,null).toString();
            if(finalResult.endsWith(".0")){
                finalResult = finalResult.replace(".0","");
            }
            return finalResult;
        }catch (Exception e){
            return "Err";
        }
    }

    private void checkForPowerOf(String workings) {
        ArrayList<Integer> indexOfPowers = new ArrayList<>();
        for(int i = 0; i < workings.length(); i++)
        {
            if (workings.charAt(i) == '^')
                indexOfPowers.add(i);
        }

        formula = workings;
        tempFormula = workings;
        for(Integer index: indexOfPowers)
        {
            changeFormula(workings , index);
        }
        formula = tempFormula;
    }
    private void changeFormula(String workings , Integer index) {
        String numberLeft = "";
        String numberRight = "";

        for(int i = index + 1; i< workings.length(); i++)
        {
            if(isNumeric(workings.charAt(i)))
                numberRight = numberRight + workings.charAt(i);
            else
                break;
        }

        for(int i = index - 1; i >= 0; i--)
        {
            if(isNumeric(workings.charAt(i)))
                numberLeft = numberLeft + workings.charAt(i);
            else
                break;
        }

        String original = numberLeft + "^" + numberRight;
        String changed = "Math.pow("+numberLeft+","+numberRight+")";
        tempFormula = tempFormula.replace(original,changed);
    }
    private boolean isNumeric(char c) {
        if((c <= '9' && c >= '0') || c == '.')
            return true;

        return false;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding  = null;
    }
}