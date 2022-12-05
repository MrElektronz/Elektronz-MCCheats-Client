package net.fabricmc.example.listeners;

import java.util.ArrayList;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public abstract class Event<T extends Listener> {
    public abstract void fire(ArrayList<T> listeners);
    public abstract Class<T> getListenerType();
}
