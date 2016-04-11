package edu.calpoly.android.lab2;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SimpleJokeList extends Activity {

	/** Contains the list Jokes the Activity will present to the user. */
	protected ArrayList<Joke> m_arrJokeList;

	/** LinearLayout used for maintaining a list of Views that each display Jokes. */
	protected LinearLayout m_vwJokeLayout;

	/** EditText used for entering text for a new Joke to be added to m_arrJokeList. */
	protected EditText m_vwJokeEditText;

	/** Button used for creating and adding a new Joke to m_arrJokeList using the
	 * text entered in m_vwJokeEditText. */
	protected Button m_vwJokeButton;
	
	/** Background Color values used for alternating between light and dark rows
	 * of Jokes. */
	protected int m_nDarkColor;
	protected int m_nLightColor;
	protected int m_nTextColor;
	protected Boolean colorTracker = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		initAddJokeListeners();
		Resources resources = this.getResources();

		m_nDarkColor = resources.getColor(R.color.dark);
		m_nLightColor = resources.getColor(R.color.light);
		m_nTextColor = resources.getColor(R.color.text);

		m_arrJokeList = new ArrayList<Joke>();

		String strArr[] = resources.getStringArray(R.array.jokeList);
		for (String string : strArr) {
			Log.d("lab2gmo", "Adding new joke: \" + strJoke");
			addJoke(string);
		}
	}
	
	/**
	 * Method used to encapsulate the code that initializes and sets the Layout
	 * for this Activity. 
	 */
	protected void initLayout() {
		LinearLayout rootView = new LinearLayout(this);
		rootView.setOrientation(LinearLayout.VERTICAL);

		LinearLayout jokeButtonView = new LinearLayout(this);
		m_vwJokeButton = new Button(this);
		m_vwJokeButton.setText("Add Joke");
		jokeButtonView.addView(m_vwJokeButton);

		m_vwJokeEditText = new EditText(this);

		m_vwJokeEditText.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		m_vwJokeEditText.setInputType(InputType.TYPE_CLASS_TEXT);
		m_vwJokeEditText.setText("");
		jokeButtonView.addView(m_vwJokeEditText);

		m_vwJokeLayout = new LinearLayout(this);
		m_vwJokeLayout.setOrientation(LinearLayout.VERTICAL);

		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(m_vwJokeLayout);

		rootView.addView(jokeButtonView);
		rootView.addView(scrollView);

		setContentView(rootView);
	}
	
	/**
	 * Method used to encapsulate the code that initializes and sets the Event
	 * Listeners which will respond to requests to "Add" a new Joke to the 
	 * list. 
	 */
	protected void initAddJokeListeners() {
		m_vwJokeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String temp = m_vwJokeEditText.getText().toString();
				m_vwJokeEditText.setText("");
				if (temp != null && !(temp.equals(""))) {
					addJoke(temp);
					InputMethodManager imm = (InputMethodManager)
							getSystemService(Context.INPUT_METHOD_SERVICE);

					imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);
				}

			}
		});

		m_vwJokeEditText.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				String temp = m_vwJokeEditText.getText().toString();
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (keyCode == KeyEvent.KEYCODE_ENTER
							|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
						if (temp != null && !(temp.equals(""))) {
							m_vwJokeEditText.setText("");
							addJoke(temp);
							InputMethodManager imm = (InputMethodManager)
									getSystemService(Context.INPUT_METHOD_SERVICE);

							imm.hideSoftInputFromWindow(
									m_vwJokeEditText.getWindowToken(), 0);
						}
					}
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Method used for encapsulating the logic necessary to properly initialize
	 * a new joke, add it to m_arrJokeList, and display it on screen.
	 * 
	 * @param strJoke
	 *            A string containing the text of the Joke to add.
	 */
	protected void addJoke(String strJoke) {
		Joke newJoke = new Joke(strJoke);
		m_arrJokeList.add(newJoke);
		TextView tv = new TextView(this);
		tv.setText(strJoke);
		tv.setTextSize(16);
		tv.setBackgroundColor(colorTracker ? m_nDarkColor : m_nLightColor);
		tv.setTextColor(m_nTextColor);
		colorTracker = !colorTracker;
		m_vwJokeLayout.addView(tv);
	}
}