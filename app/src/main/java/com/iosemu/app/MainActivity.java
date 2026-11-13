package com.iosemu.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String BASH = "/data/data/com.termux/files/usr/bin/bash";
    private static final String HOME = "/data/data/com.termux/files/home";

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        int p = (int) (24 * getResources().getDisplayMetrics().density);
        l.setPadding(p, p, p, p);

        TextView t = new TextView(this);
        t.setTextSize(16);
        t.setText("iosemu\n\nWymagania:\n1. Termux zainstalowany (F-Droid)\n"
            + "2. W Termux: allow-external-apps=true w ~/.termux/termux.properties\n"
            + "3. Folder iosemu skopiowany do ~/iosemu\n");
        l.addView(t);

        Button setup = new Button(this);
        setup.setText("1. Instalacja zaleznosci");
        setup.setOnClickListener(v -> run("pkg install -y python curl qemu-system-aarch64-headless"));
        l.addView(setup);

        Button go = new Button(this);
        go.setText("2. Uruchom emulator");
        go.setOnClickListener(v -> run("python ~/iosemu/iosemu.py"));
        l.addView(go);

        setContentView(l);
    }

    private void run(String cmd) {
        try {
            Intent i = new Intent("com.termux.RUN_COMMAND");
            i.setClassName("com.termux", "com.termux.app.RunCommandService");
            i.putExtra("com.termux.RUN_COMMAND_PATH", BASH);
            i.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", new String[]{"-c", cmd});
            i.putExtra("com.termux.RUN_COMMAND_WORKDIR", HOME);
            i.putExtra("com.termux.RUN_COMMAND_BACKGROUND", false);
            i.putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0");
            startService(i);
        } catch (Exception e) {
            Toast.makeText(this, "Blad: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
