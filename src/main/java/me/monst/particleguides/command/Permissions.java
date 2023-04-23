package me.monst.particleguides.command;

import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.PermissionLimit;

public final class Permissions {
    
    public static final Permission GUIDE_HERE = permission("guide.here");
    public static final Permission GUIDE_COORDS = permission("guide.coords");
    public static final Permission GUIDE_DEATH = permission("guide.death");
    public static final Permission GUIDE_PLAYER = permission("guide.player");
    public static final Permission GUIDE_PLAYER_NO_ASK = permission("guide.player.no-ask");
    public static final Permission GUIDE_HOME = permission("guide.home");
    public static final PermissionLimit GUIDE_LIMIT = permissionLimit("guide.limit");
    public static final Permission BREADCRUMBS = permission("breadcrumbs");
    public static final PermissionLimit BREADCRUMBS_LIMIT = permissionLimit("breadcrumbs.limit");
    public static final Permission ADMIN = permission("admin");
    
    private Permissions() {}
    
    private static Permission permission(String permission) {
        return Permission.of("particleguides." + permission);
    }
    
    private static PermissionLimit permissionLimit(String permission) {
        return PermissionLimit.of("particleguides." + permission);
    }
    
}
