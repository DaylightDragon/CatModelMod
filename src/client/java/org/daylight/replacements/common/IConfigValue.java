package org.daylight.replacements.common;


public interface IConfigValue<T> {
    T get();
    void set(T value);
    void save();
}
