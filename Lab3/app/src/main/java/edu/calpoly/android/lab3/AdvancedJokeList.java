package edu.calpoly.android.lab3;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdvancedJokeList extends AppCompatActivity {

	/** Contains the name of the Author for the jokes. */
	protected String m_strAuthorName;

	/** Contains the list of Jokes the Activity will present to the user. */
	protected ArrayList<Joke> m_arrJokeList;
	
	/** Contains the list of filtered Jokes the Activity will present to the user. */
	protected ArrayList<Joke> m_arrFilteredJokeList;

	/** Adapter used to bind an AdapterView to List of Jokes. */
	protected JokeListAdapter m_jokeAdapter;

	/** ViewGroup used for maintaining a list of Views that each display Jokes. */
	protected LinearLayout m_vwJokeLayout;

	/** EditText used for entering text for a new Joke to be added to m_arrJokeList. */
	protected EditText m_vwJokeEditText;

	/** Button used for creating and adding a new Joke to m_arrJokeList using the
	 *  text entered in m_vwJokeEditText. */
	protected Button m_vwJokeButton;
	
	/** Menu used for filtering Jokes. */
	protected Menu m_vwMenu;

	/** Background Color values used for alternating between light and dark rows
	 *  of Jokes. Add a third for text color if necessary. */
	protected int m_nDarkColor;
	protected int m_nLightColor;
	protected int m_nTextColor;
	protected Boolean colorTracker = false;
		
	/**
	 * Context-Menu MenuItem IDs.
	 * IMPORTANT: You must use these when creating your MenuItems or the tests
	 * used to grade your submission will fail. These are commented out for now.
	 */
	//protected static final int FILTER = Menu.FIRST;
	//protected static final int FILTER_LIKE = SubMenu.FIRST;
	//protected static final int FILTER_DISLIKE = SubMenu.FIRST + 1;
	//protected static final int FILTER_UNRATED = SubMenu.FIRST + 2;
	//protected static final int FILTER_SHOW_ALL = SubMenu.FIRST + 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLayout();
		initAddJokeListeners();
		Resources resources = this.getResources();

		m_nDarkColor = resources.getColor(R.color.dark);
		m_nLightColor = resources.getColor(R.color.light);
		m_nTextColor = resources.getColor(R.color.text);
		m_strAuthorName = resources.getString(R.string.author_name);
		m_arrJokeList = new ArrayList<Joke>();

		String strArr[] = resources.getStringArray(R.array.jokeList);
		for (String string : strArr) {
			Joke newJoke = new Joke(string, m_strAuthorName);
			addJoke(newJoke);
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO
        return true;
    }

	/**
	 * Method is used to encapsulate the code that initializes and sets the
	 * Layout for this Activity.
	 */
	protected void initLayout() {
		/*setContentView(R.layout.advanced);

		m_vwJokeLayout = (LinearLayout) findViewById(R.id.jokeListViewGroup);
		m_vwJokeButton = (Button) findViewById(R.id.addJokeButton);
		m_vwJokeEditText = (EditText) findViewById(R.id.newJokeEditText);
		*/
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
	 * Method is used to encapsulate the code that initializes and sets the
	 * Event Listeners which will respond to requests to "Add" a new Joke to the
	 * list.
	 */
	protected void initAddJokeListeners() {
		m_vwJokeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String temp = m_vwJokeEditText.getText().toString();
				m_vwJokeEditText.setText("");
				if (temp != null && !(temp.equals(""))) {
					addJoke(new Joke(temp, m_strAuthorName));
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
							addJoke(new Joke(temp, m_strAuthorName));
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
	 * Method used for encapsulating the logic necessary to properly add a new
	 * Joke to m_arrJokeList, and display it on screen.
	 * 
	 * @param joke
	 *            The Joke to add to list of Jokes.
	 */
	protected void addJoke(Joke joke) {

		m_arrJokeList.add(joke);
		TextView tv = new TextView(this);
		tv.setText(joke.getJoke());
		tv.setTextSize(16);
		tv.setBackgroundColor(colorTracker ? m_nDarkColor : m_nLightColor);
		tv.setTextColor(m_nTextColor);
		colorTracker = !colorTracker;
		m_vwJokeLayout.addView(tv);
	}
}