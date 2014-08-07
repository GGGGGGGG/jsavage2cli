import java.util.ArrayList;


public class World {
	enum SpawnPointType {
		MAIN, BASE, PORTAL
	}
	public class SpawnPoint {
		short id;
		SpawnPointType type;
	}
	static ArrayList<Entity> worldEntities = new ArrayList<Entity>();
	private static ArrayList<PlayerEntity> playerEntities = new ArrayList<PlayerEntity>();
	static ArrayList<PlayerEntity> humanTeam = new ArrayList<PlayerEntity>();
	static ArrayList<PlayerEntity> beastTeam = new ArrayList<PlayerEntity>();
	static ArrayList<SpawnPoint> spawnPoints = new ArrayList<SpawnPoint>();
	public static void addWorldEntity(Entity e) {
		worldEntities.add(e);
	}
	public static void addPlayerEntity(PlayerEntity e) {
		playerEntities.add(e);
		if(e.team == S2Factory.HUMAN_TEAM)
			humanTeam.add(e);
		if(e.team == S2Factory.BEAST_TEAM)
			beastTeam.add(e);
	}
	public static ArrayList<PlayerEntity> getPlayerEntities() {
		ArrayList<PlayerEntity> pe = new ArrayList<PlayerEntity>();
		for(int i = 0; i < playerEntities.size(); ++i) {
			PlayerEntity e = playerEntities.get(i).clone();
			pe.add(e);
		}
		return pe;
	}
}
