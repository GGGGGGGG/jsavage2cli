public class PlayerEntity extends Entity {
	public static final int COMMANDER = 6;
	public static final int OFFICER = 10;
	public static final int SQUADMATE = 0;
	short entityIndex;
	int accountid;
	int clientnum;
	String nickname;
	String clanname;
	int role; // commander = 6, officer = 10, N/A = squad mate
	int team;
	byte squad;

	PlayerEntity() {
	}
	
	public PlayerEntity clone() {
		PlayerEntity e = new PlayerEntity();
		e.entityIndex = entityIndex;
		e.accountid = accountid;
		e.clientnum = clientnum;
		e.nickname = String.copyValueOf(nickname.toCharArray());
		e.clanname = String.copyValueOf(clanname.toCharArray());
		e.role = role;
		e.team = team;
		e.squad = squad;
		return e;
	}
	
	public void print() {
		System.out.println("nickname=" + nickname);
		System.out.println("clientnum=" + clientnum);
		System.out.println("team=" + team);
		System.out.println("squad=" + squad);
		System.out.println("u2b1=" + index);
		System.out.println("entity index=" + entityIndex);
	}

	public static void parsePlayerEntities(byte[] b, int len) {
		for(int i = 0; i < len;)
			i = parsePlayerEntity(b, i, len);
	}
	
	public static int parsePlayerEntity(byte[] b, int iStart, int len) {
		try {
			int i = iStart;
			// look for player entry magic
			while(b[i] != 6 || b[i + 1] != 0)
				++i;
			if(i > len) return len;
			i -= 2;
			short someIndex = Utility.getShort(b, i);
			i += 4;
			// NB: length checks are necessary because un-zeroed out byte arrays could be (re)used!
			if(i + 4 > len) return len;
			int marker1 = Utility.getInt(b, i);
			//System.out.println("parsePlayerEntity(): found marker1=" + String.format("%08X", marker1));
			switch (marker1) {
			case 0xFEBFFFFD:
				if(b[i + 5] == (byte)0xFF && b[i + 12] == (byte)0xFF)
					i += 16;
				else if(b[i + 5] == (byte)0xFA)
					i += 14;
				else
					i += 15;
				// skip to account id
				break;
			case 0xE9F7FBDF:
				i += 15;
				break;
			case 0xEBF7FBDF:
				byte marker2 = b[i + 5];
				byte marker3 = b[i + 12];
				if((marker2 == (byte)0xFE || marker2 == (byte)0xFF) 
						&& marker3 == (byte)0xFF)
					i += 17;//11;
				else 
					i += 16;//10;
				break;
			case 0xFE9FFFFD:
				i += 14;
				break;
			case 0xFD5BDBDF:
				byte m3 = b[i + 10];
				if( (b[i + 4] == 0x56 || b[i + 4] == 0x76) &&
						(m3 == (byte)0xFD || m3 == (byte)0xBD || m3 == (byte)0xF7 || m3 == (byte)0xFF) )
					i += 14;
				else if(b[i + 4] == 0x56)
					i += 13;
				else i += 12;
				break;
			case 0xBFD5FBFD:
				i += 13;
				break;
			case 0xFBFBDBDF:
				if(b[i + 12] == 0)
					i += 15;
				else
					i += 14;
				break;
			case 0x7FBFFBFD:
				i += 13;
				break;
			case 0xFAFBDBDF:
				i += 14;
				break;
			case 0x7D97D3FD:
				i += 9;
				break;
			default:
				return i;
			}
			//get entity index
			short entityIndex = Utility.getShort(b, i - 2);
			int accountid = Utility.getInt(b, i);
			i += 4;
			int clientnum = Utility.getInt(b, i);
			i += 4;
			String nickname = "";
			for (; b[i] != 0; ++i)
				nickname += Character.toString((char)((byte)0x0FF & b[i]));
			String clanname = "";
			++i;
			if (Character.isLetterOrDigit(b[i])) {
				for (; b[i] != 0; ++i)
					clanname += Character.toString((char)((byte)0x0FF & b[i]));
				++i;
			}
			int team = Utility.getInt(b, i);
			int role = 0;
			if (team == 0 || team > 2) {
				role = team;
				i += 4;
				team = Utility.getInt(b, i);
			}
			i += 4;
			byte squad = b[i];
			// TODO parse other unknown bytes
			// TODO parse stats
			PlayerEntity e = new PlayerEntity();
			e.index = someIndex;
			e.entityIndex = entityIndex;
			e.accountid = accountid;
			e.clientnum = clientnum;
			e.nickname = String.copyValueOf(nickname.toCharArray());
			e.clanname = String.copyValueOf(clanname.toCharArray());
			e.role = role;
			e.team = team;
			e.squad = squad;
			World.addPlayerEntity(e);
			return i+1;
		} catch (ArrayIndexOutOfBoundsException e) {
			return len;
		}
	}
}
