package phonetics.english;

import phonetics.english.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectLessonActivity extends Activity {
	
	private int currentGrade;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lessons_grid);
		currentGrade = getIntent().getIntExtra("currentGrage", -1);
	}
	
	@Override
	public void onPause(){
		//TODO: save state
		super.onPause();
	}
	
	public void runLesson(View view){
		Button btn = (Button) view;
		String text = btn.getText().toString();
		Intent intent = new Intent(this, LessonActivity.class);
		intent.putExtra("currentLesson", text);
		intent.putExtra("currentGrade", currentGrade);
		startActivity(intent);

	}
	

}
