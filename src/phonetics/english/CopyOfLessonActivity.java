package phonetics.english;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import phonetics.english.R;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PatternMatcher;
import android.renderscript.Font.Style;
import android.text.Layout;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class CopyOfLessonActivity extends Activity {
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        String lesson = getIntent().getStringExtra("currentLesson");  
        int grade = getIntent().getIntExtra("currentGrade", -1);

        int resId = getResourceIdByName("grade" + grade);
		InputStream is = this.getResources().openRawResource(resId);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			display.getSize(size);
	    }else{
	    	size.x = display.getWidth();
	    	size.y = display.getHeight();
	    }
		
		int padding = Math.max(size.x, size.y)/50;
		try {
			String line;
			boolean isComplete = false;
			while( (line = br.readLine()) != null){
				if(line.equals("Lesson " + lesson)){
					// we find our lesson, fill view
					setContentView(R.layout.lesson);
					LinearLayout lessonView = (LinearLayout) findViewById(R.id.lessonContent);
					lessonView.removeAllViews();
					
					//TODO: animation?
					while( (line = br.readLine()) != null){
						if(line == "" || line.startsWith("Lesson")){
							Button ok_btn = new Button(this);
							ok_btn.setText(getString(R.string.ok));
							ok_btn.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									CopyOfLessonActivity.this.finish();
									
								}
							});
							lessonView.addView(ok_btn);
//							lessonView.forceLayout();
							isComplete = true;
							break;
						}else{
							String[] parts = line.split(";");

							// main string layout
							LinearLayout layoutHorizontal = new LinearLayout(this);
                            LinearLayout.LayoutParams layoutHorizontalParams = new LinearLayout.LayoutParams(
                            		LinearLayout.LayoutParams.MATCH_PARENT, 
                            		LinearLayout.LayoutParams.MATCH_PARENT);
                            layoutHorizontal.setLayoutParams(layoutHorizontalParams);
                            layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

                            // text
							TextView label = new TextView(this);
							label.setText(parts[0]);
							label.setPadding(padding/4, 2, 2, padding/4);
							TableLayout.LayoutParams labelLayoutParams = new TableLayout.LayoutParams(
									TableLayout.LayoutParams.WRAP_CONTENT,
									TableLayout.LayoutParams.WRAP_CONTENT);
							labelLayoutParams.weight = 0.6f;
							labelLayoutParams.gravity = Gravity.CENTER_VERTICAL;
							
							label.setLayoutParams(labelLayoutParams);
							label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
							
							
							
							TextView label2 = new TextView(this);
							label2.setText(parts[1]+ " " + parts[2]);
							TableLayout.LayoutParams labelLayoutParams2 = new TableLayout.LayoutParams(
									TableLayout.LayoutParams.WRAP_CONTENT,
									TableLayout.LayoutParams.WRAP_CONTENT);
							labelLayoutParams2.weight = 0.4f;
							labelLayoutParams2.gravity = Gravity.CENTER_VERTICAL;
							label2.setLayoutParams(labelLayoutParams2);
							label2.setPadding(0, 2, padding/4, 2);

							
							

							// buttons
							LinearLayout buttonLayout = new LinearLayout(this);
                            LinearLayout.LayoutParams buttonsLayoutParams = new LinearLayout.LayoutParams(
                            		LinearLayout.LayoutParams.WRAP_CONTENT, 
                            		LinearLayout.LayoutParams.WRAP_CONTENT);
							buttonsLayoutParams.gravity = Gravity.CENTER_VERTICAL;
							//buttonsLayoutParams.weight = 0f;
							
							buttonLayout.setLayoutParams(buttonsLayoutParams);
							buttonLayout.setGravity(Gravity.RIGHT); 
							
							LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT);

							// play buttom
							ImageButton playButton = new ImageButton(this);
							playButton.setLayoutParams(buttonLayoutParams);
							playButton.setImageResource(R.drawable.play);
							playButton.setOnClickListener(new PlaySound());
							playButton.setTag(parts[0]);
							
							playButton.setPadding(padding, padding, padding, padding);
							
							//rec button
							ImageButton recButton = new ImageButton(this);
							recButton.setImageResource(R.drawable.rec);
							recButton.setOnClickListener(new RecordSound());
							recButton.setLayoutParams(buttonLayoutParams);
							recButton.setPadding(padding, padding, padding, padding);

							// play recorder button
							ImageButton playRecorderButton = new ImageButton(this);
							playRecorderButton.setEnabled(false);
							playRecorderButton.setImageResource(R.drawable.play);
							playRecorderButton.setOnClickListener(new PlayRecorderSound());
							playRecorderButton.setLayoutParams(buttonLayoutParams);
							playRecorderButton.setPadding(padding, padding, padding, padding);

							
							buttonLayout.addView(playButton);
							buttonLayout.addView(recButton);
							buttonLayout.addView(playRecorderButton);							
							
							layoutHorizontal.addView(label);
							layoutHorizontal.addView(label2);
							layoutHorizontal.addView(buttonLayout);
							lessonView.addView(layoutHorizontal);
							
						}
					}
				}
				if(isComplete){
					break;
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		LinearLayout lessonView = (LinearLayout) findViewById(R.id.lessonContent);
		if(lessonView == null) return;
		for(int i = 0; i< lessonView.getChildCount(); i++){
			View v = lessonView.getChildAt(i);
			if(v instanceof LinearLayout){
				String s = (String) ((LinearLayout) v).getChildAt(1).getTag();
				if(s!= null){
					new File(s).delete();
				}
			}
		}
		
	}
	
//	private String extractWord(String line) {
//		Pattern p = Pattern.compile("[a-zA-Z]*");
//		Matcher m = p.matcher(line);
//		
//		while(m.find()){
//			if(m.start() != m.end()){
//				return line.substring(m.start(), m.end());
//			} 
//		}
//		return "";
//	}

	private int getResourceIdByName(String resName) {
		Field f;
		try {
			f = R.raw.class.getDeclaredField(resName);
		} catch (NoSuchFieldException e1) {
			try {
				f = R.raw.class.getDeclaredField("_" + resName);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				throw new RuntimeException(e1);
			}
		}
        int resId;
		try {
			resId = f.getInt(new R.raw());
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}	
		return resId;
	}

	private class PlaySound implements OnClickListener{
		@Override
		public void onClick(View v) {
			final ImageButton b = (ImageButton)v;
			b.setImageResource(R.drawable.play_active);
			MediaPlayer mp = MediaPlayer.create(CopyOfLessonActivity.this, getResourceIdByName((String)v.getTag()));
            mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					b.setImageResource(R.drawable.play);
				}
			});
            mp.start();
		}
		
	}
	
	private class PlayRecorderSound implements OnClickListener{
		@Override
		public void onClick(View v) {
			final ImageButton b = (ImageButton)v;
			b.setImageResource(R.drawable.play_active);
			b.setEnabled(false);
			
			View recButton = ((LinearLayout)v.getParent()).getChildAt(1);
			if(recButton.getTag()== null) return;
			
			final MediaPlayer mp = new MediaPlayer();
			try {
				String data = (String) recButton.getTag();
				File f = new File(data);
				mp.setDataSource(data);
				mp.prepareAsync();
			} catch (Exception e) {
				// wrong file, delete it
				e.printStackTrace();
				b.setImageResource(R.drawable.play);
				b.setEnabled(false);
				String data = (String) recButton.getTag();
				File f = new File(data);
				f.delete();
				return;
			}
			mp.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer arg0) {
					try{
						arg0.start();
					}catch(Exception ex){
						b.setImageResource(R.drawable.play);
						b.setEnabled(true);
						mp.release();
					}
					
				}
			});
            mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					b.setImageResource(R.drawable.play);
					b.setEnabled(true);
					mp.release();
				}
			});
		}
		
	}
	
	private class RecordSound implements OnClickListener{
		@Override
		public void onClick(final View v) {
			
			final ImageButton b = (ImageButton)v;
			v.setEnabled(false);
			b.setImageResource(R.drawable.rec_active);
			
			if(v.getTag()!= null){
				new File((String)v.getTag()).delete();
			}
	        
			final MediaRecorder recorder = new MediaRecorder();
	        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        try {
	        	File f = File.createTempFile("sound", null);
	        	v.setTag(f.getAbsolutePath());
		        recorder.setOutputFile(f.getAbsolutePath());
	        	recorder.prepare();
		        recorder.start();
		        v.postDelayed(new Runnable() {
					@Override
					public void run() {
						recorder.stop();
						recorder.release();
						b.setImageResource(R.drawable.rec);
						v.setEnabled(true);
						View playButton = ((LinearLayout)v.getParent()).getChildAt(2);
						playButton.setEnabled(true);
						v.refreshDrawableState();
						
					}
				}, 1500);
	        } catch (Exception e) {
	            e.printStackTrace();
	            if(recorder!= null){
					recorder.release();
	            }
				b.setImageResource(R.drawable.rec);
				v.setEnabled(true);
				View playButton = ((LinearLayout)v.getParent()).getChildAt(2);
				playButton.setEnabled(false);
				v.refreshDrawableState();
	        }

		}
	}
	
}
