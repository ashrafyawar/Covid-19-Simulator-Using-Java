package com.company;

/**
 * consumer interface
 */
public interface Consumer {
    void update();
    void addProducer(Producer s);
}