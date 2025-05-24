package com.example.todoapp.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum  TaskStatus {
    NEW,
    STARTED,
    DONE,
    COMPLETED;


    @JsonCreator
    public static TaskStatus fromValue(String value) {
        try {
            return TaskStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + value + ". Allowed values are: NEW, STARTED, DONE, COMPLETED.");
        }
    }


    @JsonValue
    public String toValue() {
        return this.name();
    }
}