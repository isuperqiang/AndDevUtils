package com.richie.utils.observer;

/**
 * 被观察者接口
 *
 * @author Richie on 2018.03.08
 */
public interface Observable {
    /**
     * 添加观察者
     *
     * @param observer
     */
    void addObserver(Observer observer);

    /**
     * 移除观察者
     *
     * @param observer
     */
    void removeObserver(Observer observer);

    /**
     * 通知观察者
     *
     * @param param
     */
    void notifyObserver(Object param);
}
