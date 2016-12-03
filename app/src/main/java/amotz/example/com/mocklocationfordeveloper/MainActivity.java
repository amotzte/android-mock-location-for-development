package amotz.example.com.mocklocationfordeveloper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (!isMockSettingsON(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("In order to use this app you must enable mock location do you want to enable it now?").setTitle("Mock location is not enable");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i =new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private boolean isMockSettingsON(Context context) {
        String st=Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ALLOW_MOCK_LOCATION);
        return st.equals("1");
    }
}
