package me.monst.particleguides.command;

public class NoPermissionException extends CommandExecutionException {
    
    public NoPermissionException(String message) {
        super(message);
    }
    
}
