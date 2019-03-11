package com.example.hp.siemensefficiencycalculator;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hummeling.if97.IF97;

public class MainActivity extends AppCompatActivity {
    EditText etPressure, etTemp, etPressureO, etTempO;
    TextView tvPressure, tvTemp, tvResult, tvTrends, ResH1, ResH2, ResS, ResEff;
    Button btCalc;
    CardView cvRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCalc = (Button) findViewById(R.id.btCalculate);
        etTemp = (EditText) findViewById(R.id.etTemp);
        etPressure = (EditText) findViewById(R.id.etPressure);
        tvPressure = (TextView) findViewById(R.id.tvPressure);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        ResH1 = (TextView) findViewById(R.id.ResH1);
        ResH2 = (TextView) findViewById(R.id.ResH2);
        ResS = (TextView) findViewById(R.id.ResS);
        ResEff = (TextView) findViewById(R.id.ResEff);
        etPressureO = (EditText) findViewById(R.id.etPressureO);
        etTempO = (EditText) findViewById(R.id.etTempO);
        cvRes = (CardView) findViewById(R.id.cvRes);
        final IF97 if97 = new IF97(IF97.UnitSystem.ENGINEERING);

        btCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPressure.getText().toString().length() == 0) {
                    etPressure.setError("Enter Value");
                } else if (etTemp.getText().toString().length() == 0) {
                    etTemp.setError("Enter Value");
                } else if (etPressureO.getText().toString().length() == 0) {
                    etPressureO.setError("Enter Value");
                } else if (etTempO.getText().toString().length() == 0) {
                    etTempO.setError("Enter Value");
                } else {
                    closeKeyboard();
                    double Pressure = Double.valueOf(Double.parseDouble(etPressure.getText().toString()));
                    double Temprature = Double.valueOf(Double.parseDouble(etTemp.getText().toString()));
                    double PressureO = Double.valueOf(Double.parseDouble(etPressureO.getText().toString()));
                    double TempratureO = Double.valueOf(Double.parseDouble(etTempO.getText().toString()));
                    if (Pressure <= 0) {
                        etPressure.setError("Value should be higher than 0");
                    } else if (Pressure > 400) {
                        etPressure.setError("Value should be lower than 400");
                    } else if (Temprature <= 0) {
                        etTemp.setError("Value should be higher than 0°");
                    } else if (Temprature >= 1000) {
                        etTemp.setError("Value should be lower than 1000°");
                    } else if (PressureO <= 0) {
                        etPressureO.setError("Value should be higher than 0");
                    } else if (PressureO > 400) {
                        etPressureO.setError("Value should be lower than 400");
                    } else if (TempratureO <= 0) {
                        etTempO.setError("Value should be higher than 0°");
                    } else if (TempratureO >= 1000) {
                        etTempO.setError("Value should be lower than 1000°");
                    } else {
                        double Enthalpy1 = if97.specificEnthalpyPT(Pressure, Temprature);
                        double Enthalpy2 = if97.specificEnthalpyPT(PressureO, TempratureO);
                        double Entropy = if97.specificEntropyPT(Pressure, Temprature);
                        double ienthalpy = if97.specificEnthalpyPS(PressureO, Entropy);

                        double Efficiency = ((Enthalpy1 - Enthalpy2) / (Enthalpy1 - ienthalpy)) * 100;

                        ResH1.setText(String.format("%.3f", Enthalpy1) + " kJ/kg");
                        ResH2.setText(String.format("%.3f", Enthalpy2) + " kJ/kg");
                        ResS.setText(String.format("%.3f", Entropy) + " kJ/kgK");
                        ResEff.setText(String.format("%.3f", Efficiency) + " %");
                        cvRes.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
