package net.fabricmc.example.listeners;


import net.fabricmc.example.ElektronzMod;

import java.util.ArrayList;

/**
 * @author Kevin Becker (kevin.becker@stud.th-owl.de)
 */
public interface LeftClickListener extends Listener{

    public void onLeftClick(LeftClickEvent event);

    public static class LeftClickEvent extends CancellableEvent<LeftClickListener> {

        public static final LeftClickEvent INSTANCE = new LeftClickEvent();

        @Override
        public void fire(ArrayList<LeftClickListener> listeners) {
            for(LeftClickListener listener : listeners){
                listener.onLeftClick(this);
                if(isCancelled())
                   break;
            }
        }

        @Override
        public Class<LeftClickListener> getListenerType() {
            return LeftClickListener.class;
        }
    }
}
