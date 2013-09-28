package phonetics.english;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class LessonActivity extends Activity {

	private ViewPager mViewPager;
	private SwipeView adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new SwipeView(this);
		adapter.setGrade(getIntent().getIntExtra("currentGrade", -1));
		adapter.setCount(40);
		
		int currentLesson = Integer.valueOf(getIntent().getStringExtra("currentLesson"));
		
		setContentView(R.layout.viewpager);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(currentLesson-1);
	}

}
