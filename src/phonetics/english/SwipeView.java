package phonetics.english;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SwipeView extends PagerAdapter {

	private final Context ctx;
	private int grade;
	private String lesson;
	private int viewId = 1;
	private int viewCount;
	
	
	
	public SwipeView(Context context){
		ctx = context;
	}

	@Override
	public int getCount() {
		return viewCount;
	}
	
	public void setCount(int count){
		this.viewCount = count;
	}

	@Override
    public Object instantiateItem(ViewGroup collection, int position) {
		System.out.println("position = " + position);
		System.out.println("collection length = " + collection.getChildCount());		
		if(collection.getChildCount() <= position){
			System.out.println("add new");
		//}else{
			//System.out.println("return cached");
			//return collection.getChildAt(position);
		}
		int resId = getResourceIdByName("grade" + grade);
		InputStream is = ctx.getResources().openRawResource(resId);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		int padding = 10;
		ScrollView scrollView = new ScrollView(ctx);
        ScrollView.LayoutParams scrollViewParams = new ScrollView.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT, 
        		LinearLayout.LayoutParams.MATCH_PARENT);
        scrollView.setLayoutParams(scrollViewParams);


		try {
			String line;
			boolean isComplete = false;
			while( (line = br.readLine()) != null){
				if(line.equals("Lesson " + (position +1))){
					// we find our lesson, fill view
					
					LinearLayout lessonView = new LinearLayout(ctx);
                    LinearLayout.LayoutParams lessonViewParams = new LinearLayout.LayoutParams(
                    		LinearLayout.LayoutParams.MATCH_PARENT, 
                    		LinearLayout.LayoutParams.WRAP_CONTENT);
                    lessonView.setLayoutParams(lessonViewParams);
                    lessonView.setOrientation(LinearLayout.VERTICAL);

					
					
					//TODO: animation?
					while( (line = br.readLine()) != null){
						if(line == "" || line.startsWith("Lesson")){
							isComplete = true;
							break;
						}else{
							String[] parts = line.split(";");

							// relative layout
							RelativeLayout relLayout = new RelativeLayout(ctx);
							RelativeLayout.LayoutParams layoutHorizontalParams = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.MATCH_PARENT, 
									RelativeLayout.LayoutParams.WRAP_CONTENT);
                            relLayout.setLayoutParams(layoutHorizontalParams);

                            // text
							TextView label = new TextView(ctx);
							label.setText(parts[0]);
							label.setPadding(padding/4, 2, 2, padding/4);
							label.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
							label.setId(viewId++);
							
							//tect 2
							TextView label2 = new TextView(ctx);
							if(parts.length == 3){
								label2.setText(parts[1]+ " " + parts[2]);
							}else{
								label2.setText(parts[1]);
							}
							label2.setId(viewId++);
							label2.setPadding(padding, 0, padding/2, 0);

							// play buttom
							ImageButton playButton = new ImageButton(ctx);
							playButton.setImageResource(R.drawable.play);
							playButton.setOnClickListener(new PlaySound());
							playButton.setTag(parts[0]);
							playButton.setPadding(padding, padding, padding, padding);
							playButton.setId(viewId++);
							
							//rec button
							ImageButton recButton = new ImageButton(ctx);
							recButton.setImageResource(R.drawable.rec);
							recButton.setOnClickListener(new RecordSound());
							recButton.setPadding(padding, padding, padding, padding);
							recButton.setId(viewId++);

							// play recorder button
							ImageButton playRecorderButton = new ImageButton(ctx);
							playRecorderButton.setEnabled(false);
							playRecorderButton.setImageResource(R.drawable.play);
							playRecorderButton.setOnClickListener(new PlayRecorderSound());
							playRecorderButton.setPadding(padding, padding, padding, padding);
							playRecorderButton.setId(viewId++);

							RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT, 
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							p.addRule(RelativeLayout.CENTER_VERTICAL);
							relLayout.addView(label, p);

							p = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT, 
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							p.addRule(RelativeLayout.CENTER_VERTICAL);
							p.addRule(RelativeLayout.RIGHT_OF, label.getId());
							p.addRule(RelativeLayout.LEFT_OF, playButton.getId());
							relLayout.addView(label2, p);
							
							p = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT, 
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							p.addRule(RelativeLayout.CENTER_VERTICAL);
							p.addRule(RelativeLayout.LEFT_OF, recButton.getId());
							relLayout.addView(playButton, p);
							
							p = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT, 
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							p.addRule(RelativeLayout.CENTER_VERTICAL);
							p.addRule(RelativeLayout.LEFT_OF, playRecorderButton.getId());
							relLayout.addView(recButton, p);
							
							p = new RelativeLayout.LayoutParams(
									RelativeLayout.LayoutParams.WRAP_CONTENT, 
									RelativeLayout.LayoutParams.WRAP_CONTENT);
							p.addRule(RelativeLayout.CENTER_VERTICAL);
							p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
							relLayout.addView(playRecorderButton, p);
							lessonView.addView(relLayout);
							
							
						}
					}
					scrollView.addView(lessonView);
					collection.addView(scrollView);
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
        return scrollView;
    }
	
    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
		System.out.println("remove view at position = " + position);
		System.out.println("collection length before = " + collection.getChildCount());		

    	ScrollView scroll = (ScrollView) view;
		LinearLayout lessonView = (LinearLayout) scroll.getChildAt(0);
		if(lessonView == null) return;
		for(int i = 0; i< lessonView.getChildCount(); i++){
			View v = lessonView.getChildAt(i);
			if(v instanceof RelativeLayout){
				String s = (String) ((RelativeLayout) v).getChildAt(3).getTag();
				if(s!= null){
					new File(s).delete();
				}
			}
		}
		scroll.removeAllViews();
        collection.removeView((View) view);
        System.out.println("collection length after = " + collection.getChildCount());
    }

	@Override
	public boolean isViewFromObject(View view, Object object) {
		
		return (view==object);
	}

	public void setGrade(int grage) {
		this.grade = grage;
		
	}

	public void setLesson(String lesson) {
		this.lesson = lesson;
		
	}
	
	private int getResourceIdByName(String resName) {
		resName = resName.replace('\'', '_');
		Field f;
		try {
			f = R.raw.class.getDeclaredField(resName);
		} catch (NoSuchFieldException e1) {
			try {
				f = R.raw.class.getDeclaredField("_" + resName);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				return -1;
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
			int resId = getResourceIdByName((String)v.getTag());
			if(resId != -1){
				MediaPlayer mp = MediaPlayer.create(ctx, resId);
	            mp.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.release();
						b.setImageResource(R.drawable.play);
					}
				});
	            mp.start();
			}else{
				v.setEnabled(false);
			}
		}
		
	}
	
	private class PlayRecorderSound implements OnClickListener{
		@Override
		public void onClick(View v) {
			final ImageButton b = (ImageButton)v;
			b.setImageResource(R.drawable.play_active);
			b.setEnabled(false);
			
			View recButton = ((RelativeLayout)v.getParent()).getChildAt(3);
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
	        	b.setImageResource(R.drawable.rec_active);
	        	b.refreshDrawableState();
	        	recorder.start();
		        v.postDelayed(new Runnable() {
					@Override
					public void run() {
						recorder.stop();
						recorder.release();
						b.setImageResource(R.drawable.rec);
						v.setEnabled(true);
						View playButton = ((RelativeLayout)v.getParent()).getChildAt(4);
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
				View playButton = ((RelativeLayout)v.getParent()).getChildAt(4);
				playButton.setEnabled(false);
				v.refreshDrawableState();
	        }

		}
	}

}
