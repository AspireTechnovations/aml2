package in.aspiretechnovations.aml.activities;

import androidx.appcompat.app.AppCompatActivity;
import in.aspiretechnovations.aml.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LocationSelectionActivity extends AppCompatActivity {

    private Button btn_location;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        context  =this;

        btn_location = findViewById(R.id.btn_location);

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, HomeActivity.class));
            }
        });
    }
}
