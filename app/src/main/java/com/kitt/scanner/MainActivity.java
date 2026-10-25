
package com.kitt.scanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(Color.rgb(15, 15, 20));

        TextView title = new TextView(this);
        title.setText("KITT SCANNER");
        title.setTextColor(Color.RED);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER);

        TextView description = new TextView(this);
        description.setText(
            "Borde luminoso rojo y blanco\nSin micrófono ambiental"
        );
        description.setTextColor(Color.WHITE);
        description.setTextSize(17);
        description.setGravity(Gravity.CENTER);

        Button activate = new Button(this);
        activate.setText("ACTIVAR BORDE");

        Button deactivate = new Button(this);
        deactivate.setText("DESACTIVAR BORDE");

        layout.addView(title);
        layout.addView(description);
        layout.addView(activate);
        layout.addView(deactivate);

        setContentView(layout);

        activate.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Intent permission = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
                );
                startActivity(permission);
                Toast.makeText(
                    this,
                    "Autoriza mostrar sobre otras aplicaciones",
                    Toast.LENGTH_LONG
                ).show();
                return;
            }

            startService(new Intent(this, ScannerService.class));
        });

        deactivate.setOnClickListener(v -> {
            stopService(new Intent(this, ScannerService.class));
        });
    }
}
