
	public class PlayerStats {
		//TODO unknown 4 bytes
		// in-game statistics
		short exp;
		short gold;
		short souls;
		int lvl;
		// TODO variable number of short
		short ping;
		int kills;
		int deaths;
		int assists;
		// END in-game statistics
		int playerRank;
		int playerExp;
		int something;
		int matchRecordWins;
		int matchRecordLosses;
		int totalKills;
		int totalDeaths;
		int totalAssists;
		int soulsSpent;
		int buildingsRazed;
		int playerDamage;
		int buildingDmg;
		int npcKills;
		int hpHealed;
		int resurrections;
		int karmaScore;
		int totalGold;
		int hpRepaired;
		int[] timePlayed; // [3]hrs:mins:sec otherwise [2]mins:sec if hrs = 0
		int killDeathRatioKill;
		int killDeathRatioDeath;
		int SF;
		int something2;
		String clanimgext; // if applicable
		//6A 70 67 00 jpg.
	}
