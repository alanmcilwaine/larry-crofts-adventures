package nz.ac.wgtn.swen225.lc.recorder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

/**
 * Implements all but 1 of Action's methods, so I can use a lambda to creat actions
 */
public interface RecorderAction extends Action {


    @Override
    default public Object getValue(String key) {
        throw new Error("This Action should not have tried to call this method");
    }

    @Override
    default public void putValue(String key, Object value) {
        throw new Error("This Action should not have tried to call this method");
    }

    @Override
    default public void setEnabled(boolean b) {
        throw new Error("This Action should not have tried to call this method");
    }

    @Override
    default public boolean isEnabled() {
        throw new Error("This Action should not have tried to call this method");
    }

    @Override
    default public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new Error("This Action should not have tried to call this method");
    }

    @Override
    default public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new Error("This Action should not have tried to call this method");
    }

}

