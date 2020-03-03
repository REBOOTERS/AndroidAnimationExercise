package com.snatik.matches.events.ui;

import com.snatik.matches.events.AbstractEvent;
import com.snatik.matches.events.EventObserver;
import com.snatik.matches.themes.Theme;

public class ThemeSelectedEvent extends AbstractEvent {

	public static final String TYPE = ThemeSelectedEvent.class.getName();
	public final Theme theme;

	public ThemeSelectedEvent(Theme theme) {
		this.theme = theme;
	}

	@Override
	protected void fire(EventObserver eventObserver) {
		eventObserver.onEvent(this);
	}

	@Override
	public String getType() {
		return TYPE;
	}

}
