package com.examples.components.stockquotes.models;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.examples.components.stockquotes.MainActivity;
import com.examples.components.stockquotes.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class StockInfoActivity_XML_PullParser extends Activity {
	
	// Used to identify the app in the LogCat, so I
	// can output messages and debug the program
	
	private static final String TAG = "STOCKQUOTE";
	
	// Define the TextViews I use in activity_stock_info.xml
	
	TextView companyNameTextView;
	TextView yearLowTextView;
	TextView yearHighTextView;
	TextView daysLowTextView;
	TextView daysHighTextView;
	TextView lastTradePriceOnlyTextView;
	TextView changeTextView;
	TextView daysRangeTextView;

	//
	// TODO: Can be placed in strings.xml, should be of string.format since must get dynamic values (stock name)
	//

	// Used to make the URL to call for XML data
	String yahooURLFirst = "http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";
	String yahooURLSecond = "%22)&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    private final static String FIRST_ELEMENT_NAME = "query";

	// Holds values pulled from the XML document using XmlPullParser
	String[][] xmlPullParserArray = {
			{"AverageDailyVolume", "0"},
			{"Change", "0"},
			{"DaysLow", "0"},
			{"DaysHigh", "0"},
			{"YearLow", "0"},
			{"YearHigh", "0"},
			{"MarketCapitalization", "0"},
			{"LastTradePriceOnly", "0"},
			{"DaysRange", "0"},
			{"Name", "0"},
			{"Symbol", "0"},
			{"Volume", "0"},
			{"StockExchange", "0"}};

	int parserArrayIncrement = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Creates the window used for the UI
		setContentView(R.layout.activity_stock_info);
		
		// Get the message from the intent that has the stock symbol
		Intent intent = getIntent();
		String stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);
		
		// Initialize TextViews
		companyNameTextView = (TextView) findViewById(R.id.companyNameTextView);
		yearLowTextView = (TextView) findViewById(R.id.yearLowTextView);
		yearHighTextView = (TextView) findViewById(R.id.yearHighTextView);
		daysLowTextView = (TextView) findViewById(R.id.daysLowTextView);
		daysHighTextView = (TextView) findViewById(R.id.daysHighTextView);
		lastTradePriceOnlyTextView = (TextView) findViewById(R.id.lastTradePriceOnlyTextView);
		changeTextView = (TextView) findViewById(R.id.changeTextView);
		daysRangeTextView = (TextView) findViewById(R.id.daysRangeTextView);
		
		// Sends a message to the LogCat
		Log.d(TAG, "Before URL Creation " + stockSymbol);
		
		// Create the YQL query
		final String yqlURL = yahooURLFirst + stockSymbol + yahooURLSecond;
		
		// The Android UI toolkit is not thread safe and must always be 
		// manipulated on the UI thread. This means if I want to perform
		// any network operations like grabbing xml data, I have to do it
		// in its own thread. The problem is that you can't write to the
		// GUI from outside the main activity. AsyncTask solves those problems
		
		new MyAsyncTask().execute(yqlURL);
	}
	
	// Use AsyncTask if you need to perform background tasks, but also need
	// to change components on the GUI. Put the background operations in
	// doInBackground. Put the GUI manipulation code in onPostExecute
	private class MyAsyncTask extends AsyncTask<String, String, String>{

		// String... arg0 is the same as String[] args
		protected String doInBackground(String... args) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser parser = factory.newPullParser();
                InputStream urlData = getUrlData(args[0]);
                if(urlData == null) return "";
                parser.setInput(new InputStreamReader(urlData));
                beginDocument(parser, FIRST_ELEMENT_NAME);
                int eventType;
                do {
                    nextElement(parser);
                    parser.next();
                    eventType = parser.getEventType();
                    if(eventType == XmlPullParser.TEXT) {
                        String valueFromXML = parser.getText();
                        xmlPullParserArray[parserArrayIncrement++][1] = valueFromXML;
                    }
                } while(eventType != XmlPullParser.END_DOCUMENT);

			} catch (XmlPullParserException | IOException e) {
                Log.d(TAG, e.getMessage(), e);
			} finally {

            }

            return null;
		}

        private void nextElement(XmlPullParser parser) throws IOException, XmlPullParserException {
            int type;
            while((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
            }
        }

        private void beginDocument(XmlPullParser parser, String firstElementName) throws IOException, XmlPullParserException {
            int type;
            while((type = parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {

            }

            if(type != XmlPullParser.START_TAG) {
                throw new XmlPullParserException("No Start Tag Found.");
            }

            if(!parser.getName().equals(firstElementName)) {
                throw new XmlPullParserException("Unexpected start tag found" + parser.getName() + ", expected " + firstElementName);
            }
        }

        private InputStream getUrlData(String urStr) throws IOException {
            URL url = new URL(urStr);

            // connection is the communications link between the
            // application and a URL that we will read from.
            URLConnection connection;
            connection = url.openConnection();

            // Used to take advantage of HTTP specific features.
            // Provides tools that tell us if a connection was
            // made, lost and other HTTP Status Codes
            HttpURLConnection httpConnection = (HttpURLConnection)connection;
            int responseCode = httpConnection.getResponseCode();

            // Tests if responseCode == 200 Good Connection
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Reads data from the connection
                return httpConnection.getInputStream();
            }

			return null;
		}

		// Changes the values for a bunch of TextViews on the GUI
		protected void onPostExecute(String result) {
			companyNameTextView.setText(xmlPullParserArray[9][1]);
			yearLowTextView.setText("Year Low: " + xmlPullParserArray[4][1]);
			yearHighTextView.setText("Year High: " + xmlPullParserArray[5][1]);
			daysLowTextView.setText("Days Low: " + xmlPullParserArray[2][1]);
			daysHighTextView.setText("Days High: " + xmlPullParserArray[3][1]);
			lastTradePriceOnlyTextView.setText("Last Price: " + xmlPullParserArray[7][1]);
			changeTextView.setText("Change: " + xmlPullParserArray[1][1]);
			daysRangeTextView.setText("Daily Price Range: " + xmlPullParserArray[8][1]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stock_info, menu);
		return true;
	}
}
