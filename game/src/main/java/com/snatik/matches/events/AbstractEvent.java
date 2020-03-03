package com.snatik.matches.events;

public abstract class AbstractEvent implements Event {

	protected abstract void fire(EventObserver eventObserver);

}
