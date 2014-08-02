import java.util.ArrayList;


public class World {
	static ArrayList<Entity> worldEntities = new ArrayList<Entity>();
	static ArrayList<PlayerEntity> playerEntities = new ArrayList<PlayerEntity>();
	public static void addWorldEntity(Entity e) {
		worldEntities.add(e);
	}
	public static void addPlayerEntity(PlayerEntity e) {
		playerEntities.add(e);
		System.out.println("nickname=" + e.nickname);
		System.out.println("clientnum=" + e.clientnum);
		System.out.println("team=" + e.team);
		System.out.println("squad=" + e.squad);
		System.out.println("u2b1=" + e.index);
		System.out.println("entity index=" + e.entityIndex);
	}
}
