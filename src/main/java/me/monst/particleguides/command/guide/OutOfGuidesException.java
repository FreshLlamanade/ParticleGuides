package me.monst.particleguides.command.guide;

import me.monst.pluginutil.command.exception.CommandExecutionException;

public class OutOfGuidesException extends CommandExecutionException {
    
    public OutOfGuidesException() {
        super("You have reached the maximum number of guides you can have at once.");
    }
    
}
