package cn.richie.anddevutil;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.richie.easylog.ILogger;
import com.richie.easylog.LoggerFactory;

public class MainActivity extends AppCompatActivity {
    private final ILogger logger = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logger.debug("onCreate");
    }
}
