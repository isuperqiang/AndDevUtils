package com.richie.utils.observer;

/**
 * @author Richie on 2018.03.08
 * 观察者接口
 */
public interface Observer {

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable
     * @param data
     */
    void update(Observable observable, Object data);
}
