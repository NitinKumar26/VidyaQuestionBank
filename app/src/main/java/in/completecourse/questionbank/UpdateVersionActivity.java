package in.completecourse.questionbank;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateVersionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_version);

        Button updateNowButton = findViewById(R.id.update_now_button);
        updateNowButton.setOnClickListener(v -> {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=in.completecourse.questionbank"));
            startActivity(intent);
        });
    }
}
