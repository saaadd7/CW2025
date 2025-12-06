package com.comp2042.core.logic.command;

/**
 * Represents a command that can be executed.
 * This is a functional interface that can be used to implement the command pattern.
 */
public interface Command {
    /**
     * Executes the command.
     */
    void execute();
}
