package edu.calpoly.android.lab3;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
	protected ListView m_vwJokeLayout;

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
	protected static final int FILTER = Menu.FIRST;
	protected static final int FILTER_LIKE = SubMenu.FIRST;
	protected static final int FILTER_DISLIKE = SubMenu.FIRST + 1;
	protected static final int FILTER_UNRATED = SubMenu.FIRST + 2;
	protected static final int FILTER_SHOW_ALL = SubMenu.FIRST + 3;

	private android.support.v7.view.ActionMode actionMode;
	private android.support.v7.view.ActionMode.Callback callback;
	private int removePos;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Resources resources = this.getResources();

		m_nDarkColor = resources.getColor(R.color.dark);
		m_nLightColor = resources.getColor(R.color.light);
		m_nTextColor = resources.getColor(R.color.text);
		m_strAuthorName = resources.getString(R.string.author_name);
		m_arrJokeList = new ArrayList<Joke>();
		m_arrFilteredJokeList = new ArrayList<Joke>();
		m_jokeAdapter = new JokeListAdapter(this, m_arrFilteredJokeList);

		initLayout();
		initAddJokeListeners();

		String strArr[] = resources.getStringArray(R.array.jokeList);
		for (String string : strArr) {
			Joke newJoke = new Joke(string, m_strAuthorName);
			addJoke(newJoke);
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		m_vwMenu = menu;
        return true;
    }

	private void populateFilteredArrayList(int rating) {
		for (Joke joke : m_arrJokeList)
			if (joke.getRating() == rating)
				m_arrFilteredJokeList.add(joke);
		m_jokeAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		m_arrFilteredJokeList.clear();

		switch (item.getItemId()) {
			case R.id.submenu_like:
				populateFilteredArrayList(Joke.LIKE);
				break;

			case R.id.submenu_dislike:
				populateFilteredArrayList(Joke.DISLIKE);
				break;

			case R.id.submenu_unrated:
				populateFilteredArrayList(Joke.UNRATED);
				break;

			case R.id.submenu_show_all:
				for (Joke joke : m_arrJokeList)
					m_arrFilteredJokeList.add(joke);
				m_jokeAdapter.notifyDataSetChanged();
				break;

			default:
				break;
		}
		return true;
	}

	/**
	 * Method is used to encapsulate the code that initializes and sets the
	 * Layout for this Activity.
	 */
	protected void initLayout() {
		setContentView(R.layout.advanced);

		m_vwJokeLayout = (ListView) findViewById(R.id.jokeListViewGroup);
		m_vwJokeButton = (Button) findViewById(R.id.addJokeButton);
		m_vwJokeEditText = (EditText) findViewById(R.id.newJokeEditText);
		m_vwJokeLayout.setAdapter(m_jokeAdapter);
		callback = new ActionMode.Callback() {

			// Called when the action mode is created; startActionMode() was called
			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// Inflate a menu resource providing context menu items
				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.actionmenu, menu);
				return true;
			}

			// Called each time the action mode is shown. Always called after onCreateActionMode, but
			// may be called multiple times if the mode is invalidated.
			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				return false; // Return false if nothing is done
			}

			// Called when the user selects a contextual menu item
			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_remove:
						m_arrJokeList.remove(m_arrFilteredJokeList.remove(removePos));
						m_jokeAdapter.notifyDataSetChanged();
						mode.finish(); // Action picked, so close the CAB
						return true;
					default:
						return false;
				}
			}
			// Called when the user exits the action mode

			@Override
			public void onDestroyActionMode (ActionMode mode){
				actionMode = null;
			}
		};
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
				if (event.getAction() == KeyEvent.ACTION_UP) {
					if (keyCode == KeyEvent.KEYCODE_ENTER
							|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
						if (temp != null && !(temp.equals(""))) {
							addJoke(new Joke(temp, m_strAuthorName));
							m_vwJokeEditText.setText("");
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

		m_vwJokeLayout.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				removePos = position;
				actionMode = startSupportActionMode(callback);

				return true;
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
		m_arrFilteredJokeList.add(joke);
		m_jokeAdapter.notifyDataSetChanged();
	}
}