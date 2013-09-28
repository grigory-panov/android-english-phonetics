package phonetics.english;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import phonetics.english.R;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private int currentGrage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//this.menu = menu;
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void openLessonsView(View view){
		
		Button btn = (Button) view;
		String tag = (String)btn.getTag();
		currentGrage = Integer.valueOf(tag);
		Intent intent = new Intent(this, SelectLessonActivity.class);
		intent.putExtra("currentGrage", currentGrage);
		startActivity(intent);
	}

	
//	public void saveResults(){
//		Editor ed = getPreferences(MODE_PRIVATE).edit();
//		ed.putInt("currentGrade", currentGrage);
//	}

}
