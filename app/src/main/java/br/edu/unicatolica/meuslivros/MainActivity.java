package br.edu.unicatolica.meuslivros;

import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;

import com.facebook.FacebookSdk;

/**
 * Created by Gang of Three on 15/06/2016.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

    }

}
