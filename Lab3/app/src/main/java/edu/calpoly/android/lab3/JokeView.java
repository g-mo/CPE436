package edu.calpoly.android.lab3;

import android.app.Notification;
import android.content.Context;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class JokeView extends LinearLayout implements RadioGroup.OnCheckedChangeListener {

	/** Radio buttons for liking or disliking a joke. */
	private RadioButton m_vwLikeButton;
	private RadioButton m_vwDislikeButton;
	
	/** The container for the radio buttons. */
	private RadioGroup m_vwLikeGroup;

	/** Displays the joke text. */
	private TextView m_vwJokeText;
	
	/** The data version of this View, containing the joke's information. */
	private Joke m_joke;

	/**
	 * Basic Constructor that takes only an application Context.
	 * 
	 * @param context
	 *            The application Context in which this view is being added. 
	 *            
	 * @param joke
	 * 			  The Joke this view is responsible for displaying.
	 */
	public JokeView(Context context, Joke joke) {
		super(context);
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.joke_view, this, true);

		m_vwLikeButton = (RadioButton) findViewById(R.id.likeButton);
		m_vwDislikeButton = (RadioButton) findViewById(R.id.dislikeButton);
		m_vwLikeGroup = (RadioGroup) findViewById(R.id.ratingRadioGroup);
		m_vwJokeText = (TextView) findViewById(R.id.jokeTextView);

		setJoke(joke);

		m_vwLikeGroup.setOnCheckedChangeListener(this);
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.likeButton)
			m_joke.setRating(Joke.LIKE);
		else if (checkedId == R.id.dislikeButton)
			m_joke.setRating(Joke.DISLIKE);
	}

	/**
	 * Mutator method for changing the Joke object this View displays. This View
	 * will be updated to display the correct contents of the new Joke.
	 * 
	 * @param joke
	 *            The Joke object which this View will display.
	 */
	public void setJoke(Joke joke) {
		int tempRating = joke.getRating();

		m_joke = joke;
		m_vwJokeText.setText(m_joke.getJoke());
		m_vwLikeGroup.clearCheck();
		if (tempRating == Joke.LIKE)
			m_vwLikeButton.setChecked(true);
		if (tempRating == Joke.DISLIKE)
			m_vwDislikeButton.setChecked(true);

		requestLayout();
	}
}
