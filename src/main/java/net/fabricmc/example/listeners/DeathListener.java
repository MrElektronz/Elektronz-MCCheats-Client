package net.fabricmc.example.listeners;


import java.util.ArrayList;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public interface DeathListener extends Listener{

    void onDeath();

    class DeathEvent extends Event<DeathListener> {

        public static final DeathEvent INSTANCE = new DeathEvent();

        @Override
        public void fire(ArrayList<DeathListener> listeners) {
            for(DeathListener listener : listeners){
                listener.onDeath();
            }
        }

        @Override
        public Class<DeathListener> getListenerType() {
            return DeathListener.class;
        }
    }
}
