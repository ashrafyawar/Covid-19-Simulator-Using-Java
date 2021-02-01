package com.company;

/**
 * producer interface.
 */
interface Producer{
    public void registerConsumer(Consumer o);
    public void removeConsumer(Consumer o);
    public void notifyConsumers();
}