package com.scheduler.bagel.friday;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.support.v4.widget.SimpleCursorAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.speech.tts.TextToSpeech;

public class MainActivity extends Activity {
	
	protected EditText searchText;
	public static EmployeeDb db;
	public static EmployeeDbXml dbxml;
	protected Cursor cursor;
    protected ListAdapter adapter;
    protected ListView employeeList;
	String text;
	EditText et;
	TextToSpeech tts;
	String messageText;
	Button announceButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Add annoucement button listener
		addListenerOnButton();
		
        //Generate ListView from SQLite Database
        displayListView();
       
        CharSequence toastText = messageText;
        Toast.makeText(MainActivity.this, toastText,
			   Toast.LENGTH_LONG).show();
       
        //Generate text to speech
        welcomeMessage();
        ConvertTextToSpeech();
       
	}
	

	public void addListenerOnButton() {
		announceButton = (Button) findViewById(R.id.annoucebutton);
		announceButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View argo0) {
				messageText = "We are low in paper supplies. Please, contribute one dollar to Peter Carro";
				ConvertTextToSpeech();
				
			}
		});
		
	}


	private void welcomeMessage() {

		tts=new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.US);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                    	ConvertTextToSpeech();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
            
   });

}


	@Override
	protected void onPause() {
    
		if(tts != null){

			tts.stop();
			tts.shutdown();
		}
			super.onPause();
	}

	@Override
    public void onBackPressed() {
     this.finish();
    }
	
	
	private void ConvertTextToSpeech() {
		text = messageText;
		if(text==null||"".equals(text))
		{
			text = "Content not available";
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
			tts.setSpeechRate((float) 0.8);
		}else
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
		

	private void displayListView() {
		db = new EmployeeDb(this);
		db.open();

		//Clean all data
		db.deleteAllEmployees();

		//Create table 
		//db.createTable();		

		//Add some data
		db.insertSomeEmployees();
		Cursor cursor = db.fetchAllEmployees();
		//the desired columns to be bound
		String[] columns = new String[] {
		    EmployeeDb.KEY_FIRSTNAME,
		    EmployeeDb.KEY_LASTNAME,
		    EmployeeDb.KEY_SCHEDULE
		   };
		 
		  //the XML defined views which the data will be bound to
		  int[] to = new int[] { 
		    R.id.firstName,
		    R.id.lastName,
		    R.id.scheduledDate
		   };
		 
		  //create the adapter using the cursor pointing to the desired data 
		  //as well as the layout information
		  MyCursorAdapter dataAdapter = new MyCursorAdapter(
		    this, R.layout.employee_list_item, 
		    cursor, 
		    columns, 
		    to,
		    0);
		 
		  ListView listView = (ListView) findViewById(R.id.list);
		  // Assign adapter to ListView
		  listView.setAdapter(dataAdapter);
		
	}

     //Extend the SimpleCursorAdapter to create a custom class where we 
	 //can override the getView to change the row colors
	 private class MyCursorAdapter extends SimpleCursorAdapter{
	 
	  public MyCursorAdapter(Context context, int layout, Cursor c,
	    String[] from, int[] to, int flags) {
	   super(context, layout, c, from, to, flags);
	   	   
	  }
	  
	  @SuppressLint("SimpleDateFormat")
		@Override 
		  public View getView(int position, View convertView, ViewGroup parent) { 
		  
		  	Calendar c = Calendar.getInstance();
			
		  	// Set format for date
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
			
			// Pick current system date
			String formattedDate = dateFormat.format(c.getTime());;
			
			Log.d("Current Date", formattedDate);
			
		   // Get reference to the row
		   View view = super.getView(position, convertView, parent); 
		   
		   // Get scheduled Date
		   TextView scheduledDateView = (TextView) view.findViewById(R.id.scheduledDate);
		   String scheduleDate = scheduledDateView.getText().toString();
		   Log.d("Bagel Date", scheduleDate);
		   
		   // Get last name
		   TextView lastNameView = (TextView) view.findViewById(R.id.lastName);
		   String lname = lastNameView.getText().toString();
		  
		   // Get first name
		   TextView firstNameView = (TextView) view.findViewById(R.id.firstName);
		   String fname = firstNameView.getText().toString();
		   
           SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		  
		   Date cdate = null;
		try {
			 cdate = (Date) formatter.parse(formattedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   Date sdate = null;
		try {
				sdate = (Date) formatter.parse(scheduleDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		   
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(sdate);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(cdate);
		long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis1 - milis2;
        long diffDays = diff / (24 * 60 * 60 * 1000);
		
		//boolean correct = scheduleDate.equals(formattedDate);
		//Check if scheduled date is current date
		if (diffDays==0) {  
			   view.setBackgroundColor(Color.WHITE);
			   firstNameView.setTypeface(null, Typeface.BOLD);
			   lastNameView.setTypeface(null, Typeface.BOLD);
			   scheduledDateView.setTypeface(null, Typeface.BOLD);
			   messageText = "Today " + fname + ' ' + lname +  " will be bringing some delicious bagels !!" ;
			   
				
		 	}
		 else if ((diffDays>0) && (diffDays<8)) {  
			  view.setBackgroundColor(Color.WHITE);
			   firstNameView.setTypeface(null, Typeface.BOLD);
			   lastNameView.setTypeface(null, Typeface.BOLD);
			   scheduledDateView.setTypeface(null, Typeface.BOLD);
			   messageText = "This week " + fname + ' ' + lname +  " is scheduled to bring yummy bagels !!" ;
			   
		  	}
		 else if (diffDays<0) {
			  view.setBackgroundColor(Color.WHITE);
			  firstNameView.setPaintFlags(firstNameView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			  lastNameView.setPaintFlags(lastNameView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			  scheduledDateView.setPaintFlags(scheduledDateView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		 	}
		 else {
		    view.setBackgroundColor(Color.WHITE);
		    
		 	}
		   return view;  
		  }  
	  
	 }
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
 
}
