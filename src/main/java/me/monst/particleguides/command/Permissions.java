package me.monst.particleguides.command;

public final class Permissions {
    
    public static final PermissionLimit GUIDE = permissionLimit("guide");
    public static final Permission GUIDE_HERE = permission("guide.here");
    public static final Permission GUIDE_COORDS = permission("guide.coords");
    public static final Permission GUIDE_DEATH = permission("guide.death");
    public static final Permission GUIDE_PLAYER = permission("guide.player");
    public static final Permission GUIDE_HOME = permission("guide.home");
    public static final PermissionLimit BREADCRUMBS = permissionLimit("breadcrumbs");
    public static final Permission ADMIN = permission("admin");
    
    private Permissions() {}
    
    private static Permission permission(String permission) {
        return Permission.of("particleguides." + permission);
    }
    
    private static PermissionLimit permissionLimit(String permission) {
        return PermissionLimit.of("particleguides." + permission);
    }
    
}
