import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class Savage2GameServer {
	static int msgCounter = 1;

	// current map info
	static short strongholdID = 0;
	static short lairID = 0;

	static byte currentTeam = 0;

	public static void setCurrentTeam(byte t) {
		currentTeam = t;
	}

	public static byte getCurrentTeam() {
		return currentTeam;
	}

	public static short getStrongholdID() {
		return strongholdID;
	}

	public static short getLairID() {
		return lairID;
	}

	public static byte[] getServerMapName(byte[] unknown2bytes) {
		byte[] b = new byte[8];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = unknown2bytes[0];
		b[k++] = unknown2bytes[1];
		b[k] = (byte) 0xC4; // marker
		return b;
	}

	public static byte[] getServerConnect(byte[] unknown2bytes) {
		byte[] b = new byte[8];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = unknown2bytes[0];
		b[k++] = unknown2bytes[1];
		b[k] = (byte) 0xC5; // marker
		return b;
	}

	public static byte[] getGameServerDisconnect(byte[] unknown2bytes) {
		// 9A DE 97 F1 01 22 08 C3
		byte[] b = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1, 1, 0,
				0, (byte) 0xC3 };
		b[5] = unknown2bytes[0];
		b[6] = unknown2bytes[1];
		return b;
	}

	public static byte[] getAllChatMessageCommand(String msg,
			byte[] clientid) {
		byte[] b = new byte[10 + msg.length()];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = clientid[0];
		b[k++] = clientid[1];
		b[k++] = (byte) 0xC8; // marker
		b[k++] = 3; // marker
		for (int i = 0; i < msg.length(); ++i)
			b[k++] = (byte) msg.charAt(i);
		b[k] = 0;
		return b;
	}

	public static byte[] getVoiceCommand(int vc, byte[] clientid) {
		String race = "human";

		byte[] b = new byte[10 + race.length() + 1 + 4];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = clientid[0];
		b[k++] = clientid[1];
		b[k++] = (byte) 0xC8; // marker
		b[k++] = 0x36; // marker
		b[k++] = 3; // marker
		for (int i = 0; i < race.length(); ++i)
			b[k++] = (byte) race.charAt(i);
		b[k++] = 0;
		b[k++] = (byte) vc;
		b[k++] = (byte) (vc >> 8);
		b[k++] = (byte) (vc >> 16);
		b[k++] = (byte) (vc >> 24);
		return b;
	}

	public static final byte JOIN_HUMANS = 1;
	public static final byte JOIN_BEASTS = 2;

	public static byte[] getJoinTeam(byte[] unknown2bytes, byte team) {
		/*
		 * 0D 00 00 00 03 D5 00 C8 02 01 00
		 */
		byte[] b = new byte[11];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = unknown2bytes[0];
		b[k++] = unknown2bytes[1];
		b[k++] = (byte) 0xC8; // marker
		b[k++] = 2;
		b[k++] = team;
		b[k++] = 0;
		return b;
	}

	public static byte[] getJoinSquad(byte[] unknown2bytes, int squadNo) {
		/*
		 * 04 00 00 00 03 28 D3 C8 17 01
		 */
		byte[] b = new byte[10];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = unknown2bytes[0];
		b[k++] = unknown2bytes[1];
		b[k++] = (byte) 0xC8; // marker
		b[k++] = 0x17;
		b[k++] = (byte) squadNo;
		return b;
	}

	static final byte BUILDER = (byte) 0xC1;
	static final byte MARKSMAN = (byte) 0xC4;
	static final byte SAVAGE = (byte) 0xC9;
	static final byte CHAPLAIN = (byte)0xBE;
	static final byte LEGIONNAIRE = (byte) 0xC2;
	static final byte STEAMBUCHET = (byte)0xCC;
	static final byte BATTERINGRAM = (byte)0xBC;

	static final byte CONJURER = (byte)0xC0;
	static final byte SHAPESHIFTER = (byte) 0xCB;
	static final byte HUNTER = (byte) 0xD1;
	static final byte SHAMAN = (byte) 0xCA;
	static final byte PREDATOR = (byte)0xC7;
	static final byte BEHEMOTH = (byte)0xBD;
	static final byte TEMPEST = (byte) 0xCE;
	
	static final byte MALPHAS = (byte) 0xC3;
	static final byte DEVOURER = (byte)0xC6;
	static final byte REVENANT = (byte)0xC8;
	static final byte MALIKEN = (byte) 0xCF;

	public static byte[] getCharacterSelect(byte[] unknown2bytes, byte character) {
		/*
		 * 13 00 00 00 03 D5 00 C8 01 C1 02 // team 1 builder 12 00 00 00 03 02
		 * D5 C8 01 CB 02 // team 2 shapeshifter
		 */
		byte[] b = new byte[11];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = unknown2bytes[0];
		b[k++] = unknown2bytes[1];
		b[k++] = (byte) 0xC8; // marker
		b[k++] = 1; // marker
		b[k++] = character;
		b[k++] = 2;
		return b;
	}
	
	//08 00 00 00 03 56 23 C8 0B
	public static byte[] getAfterCharacterSelect(byte[] unknown2bytes) {
		/*
		 * 13 00 00 00 03 D5 00 C8 01 C1 02 // team 1 builder 12 00 00 00 03 02
		 * D5 C8 01 CB 02 // team 2 shapeshifter
		 */
		byte[] b = new byte[9];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = unknown2bytes[0];
		b[k++] = unknown2bytes[1];
		b[k++] = (byte) 0xC8; // marker
		b[k] = (byte)0xB; // marker
		return b;
	}
	

	public static byte[] getSpawnCommand(byte[] clientid, short spawnID) {
		byte[] b = new byte[13];
		int k = 0;
		b[k++] = (byte) msgCounter;
		b[k++] = (byte) (msgCounter >> 8);
		b[k++] = (byte) (msgCounter >> 16);
		b[k++] = (byte) (msgCounter >> 24);
		b[k++] = 3; // marker
		b[k++] = clientid[0];
		b[k++] = clientid[1];
		b[k++] = (byte) 0xC8; // marker
		b[k++] = 0x16;
		b[k++] = (byte) spawnID;
		b[k++] = (byte) (spawnID >> 8);
		b[k++] = 0;
		b[k++] = 0;
		return b;
	}

	public static byte[] getWhisperCommand(String nickname, String msg) {
		byte magic = (byte) 9;
		byte[] b = new byte[1 + nickname.length() + msg.length() + 2];
		b[0] = magic;
		int k = 0;
		for (int i = 0; i < nickname.length(); ++i)
			b[++k] = (byte) nickname.charAt(i);
		b[++k] = 0;
		for (int i = 0; i < msg.length(); ++i)
			b[++k] = (byte) msg.charAt(i);
		b[++k] = 0;
		return b;
	}

	public static byte[] getStrongholdID1(byte[] pkt) {
		String team1 = "Team 1";
		String human = "Human";
		byte[] id = new byte[3];
		id[0] = 0;
		id[1] = 0;
		id[2] = 0; // bool flag
		// check magic
		byte[] magic = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1 };
		for (int i = 0; i < 4; ++i)
			if (pkt[i] != magic[i])
				return id;
		int p = 0;
		int prevp = 0;
		int j = 0;
		int idIndex = -1;
		while (p < pkt.length) {
			if (pkt[p] == 'T') {
				prevp = p;
				for (j = 1; j < team1.length(); ++j) {
					if (pkt[++p] != (byte) team1.charAt(j))
						break;
				}
				if (j < team1.length()) {
					p = prevp + 1;
					continue;
				}
				System.out.println("getStrongholdID(): found Team 1 string.");
				++p; // skip null char
				for (j = 0; j < human.length(); ++j) {
					if (pkt[++p] != (byte) human.charAt(j))
						break;
				}
				if (j < human.length()) {
					p = prevp + 1;
					continue;
				}
				System.out.println("getStrongholdID(): found Human string.");
				// found string
				if (pkt.length <= 8)
					return id; // weird check but whatever
				idIndex = prevp - 8;
				id[0] = pkt[idIndex];
				id[1] = pkt[idIndex + 1];
				System.out.println("getStrongholdID(): found strongholdID "
						+ id[0] + " " + id[1]);
				id[2] = 1; // found
				break;
			} else {
				++p;
			}
		}
		return id;
	}

	// doesnt work
	public static byte[] getLairID1(byte[] pkt) {
		String team2 = "Team 2";
		String beast = "Beast";
		byte[] id = { 0, 0 };
		// check magic
		byte[] magic = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1 };
		for (int i = 0; i < 4; ++i)
			if (pkt[i] != magic[i])
				return id;
		int p = 0;
		int prevp = 0;
		int j = 0;
		int idIndex = -1;
		while (p < pkt.length) {
			if (pkt[p] == 'T') {
				prevp = p;
				for (j = 1; j < team2.length(); ++j) {
					if (pkt[++p] != (byte) team2.charAt(j))
						break;
				}
				if (j < team2.length()) {
					p = prevp + 1;
					continue;
				}
				++p; // skip null char
				for (j = 0; j < beast.length(); ++j) {
					if (pkt[++p] != (byte) beast.charAt(j))
						break;
				}
				if (j < beast.length()) {
					p = prevp + 1;
					continue;
				}
				// found string
				if (pkt.length <= 8)
					return id; // weird check but whatever
				idIndex = prevp - 8;
				id[0] = pkt[idIndex];
				id[1] = pkt[idIndex + 1];
				break;
			} else {
				++p;
			}
		}
		return id;
	}

	public static void getStrongholdLairIDs(byte[] pkt) {
		// check magic
		byte[] magic = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1 };
		for (int i = 0; i < 4; ++i)
			if (pkt[i] != magic[i])
				return;
		// if(pkt[7] != 0x5D) return; // not packet we are looking for//wrong -
		// this changes
		int p = 0;
		int prevp = 0;
		int j = 0;
		int idIndex = -1;
		int iHuman = -1;
		int iBeast = -1;
		String team = "Team 1";
		String race = "Human";
		String neutral = "Neutral";
		String human = "Human";
		boolean doneHuman = false;
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		while (p < pkt.length - 3) {
			if (pkt[p] == 'N') {
				int tp = p;
				int i;
				for (i = 1; i < neutral.length(); ++i)
					if (pkt[++tp] != (byte) neutral.charAt(i))
						break;
				if (i == neutral.length()) {
					++tp; // skip null char
					for (i = 0; i < neutral.length(); ++i)
						if (pkt[++tp] != (byte) neutral.charAt(i))
							break;
					if (i == neutral.length()) {
						System.out.println("Found Neutral.Neutral string");
						while (pkt[tp] != 1 || pkt[tp + 1] != 0
								|| pkt[tp + 2] != 0 || pkt[tp + 3] != 0) {
							++tp;
							if (tp >= pkt.length - 3) {
								// fail
								strongholdID = 0;
								lairID = 0;
								return;
							}
						}
						System.out.println("Found 01 00 00 00 at "+tp);
						tp += 4;
						iHuman = tp;
						System.out.println("iHuman="+iHuman);
						bb.put(pkt[tp]);
						bb.put(pkt[tp + 1]);
						strongholdID = bb.getShort(0);
						bb.rewind();
						p = tp;
						doneHuman = true;
					}
				}
			} else if (pkt[p] == 'H' && doneHuman) {
				int tp = p;
				int i;
				for (i = 1; i < human.length(); ++i)
					if (pkt[++tp] != (byte) human.charAt(i))
						break;
				if (i == human.length()) {
					System.out.println("Found Human string");
					while (pkt[tp] != 2 || pkt[tp + 1] != 0 || pkt[tp + 2] != 0
							|| pkt[tp + 3] != 0) {
						++tp;
						if (tp >= pkt.length - 3) {
							// fail
							strongholdID = 0;
							lairID = 0;
							return;
						}
					}
					System.out.println("Found 02 00 00 00 at "+tp);
					tp += 4;
					iBeast = tp;
					System.out.println("iBeast="+iBeast);
					bb.put(pkt[tp]);
					bb.put(pkt[tp + 1]);
					lairID = bb.getShort(0);
					bb.rewind();
					// sanity checks
					if (Math.abs(lairID - strongholdID) == 1)
						return;
					
					// try the bytes after 0x1 and 0x2..
					// try different combos of before/after
					iHuman -= 6;
					iBeast -= 6;
					bb.rewind();
					bb.put(pkt[iHuman]);
					bb.put(pkt[iHuman + 1]);
					strongholdID = bb.getShort(0);
					bb.rewind();
					bb.put(pkt[iBeast]);
					bb.put(pkt[iBeast + 1]);
					lairID = bb.getShort(0);
					if (Math.abs(strongholdID - lairID) == 1)
						return;

					iBeast += 6;
					bb.rewind();
					bb.put(pkt[iHuman]);
					bb.put(pkt[iHuman + 1]);
					strongholdID = bb.getShort(0);
					bb.rewind();
					bb.put(pkt[iBeast]);
					bb.put(pkt[iBeast + 1]);
					lairID = bb.getShort(0);
					if (Math.abs(strongholdID - lairID) == 1)
						return;

					iHuman += 6;
					iBeast -= 6;
					bb.rewind();
					bb.put(pkt[iHuman]);
					bb.put(pkt[iHuman + 1]);
					strongholdID = bb.getShort(0);
					bb.rewind();
					bb.put(pkt[iBeast]);
					bb.put(pkt[iBeast + 1]);
					lairID = bb.getShort(0);
					if (Math.abs(strongholdID - lairID) == 1)
						return;

					System.out.println("Final checkfailed  at iHuman=" + iHuman
							+ " and iBeast=" + iBeast);
					System.out.println("StrongholdID=" + strongholdID
							+ " at index " + iHuman);
					System.out.println("lairID=" + lairID + " at index "
							+ iBeast);

					// fail
					strongholdID = 0;
					lairID = 0;
					return;

				}
			}
			++p;
		}
	}

	// fail; the FF FF FB .. changes
	public static void getStrongholdLairIDs2(byte[] pkt) {
		// check magic
		byte[] magic = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1 };
		for (int i = 0; i < 4; ++i)
			if (pkt[i] != magic[i])
				return;
		// if(pkt[7] != 0x5D) return; // not packet we are looking for//wrong -
		// this changes
		int p = 0;
		int prevp = 0;
		int j = 0;
		int idIndex = -1;
		int iHuman = -1;
		int iBeast = -1;
		String team = "Team 1";
		String race = "Human";
		boolean doBefore = true;
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		while (p < pkt.length - 3) {
			// FF FF X X ... 01 00 00 00 X X - Human
			// FF FF X X ... 02 00 00 00 X X - Beast
			if (pkt[p] == (byte) 0xFF && pkt[p + 1] == (byte) 0xFF
					&& pkt[p + 2] == (byte) 0xFE && pkt[p + 3] == (byte) 0x1B) {
				while (pkt[p] != 1 && pkt[p + 1] != 0 && pkt[p + 2] != 0
						&& pkt[p + 3] != 0) {
					++p;
					if (p >= pkt.length - 3) { // fail
						strongholdID = 0;
						lairID = 0;
						return;
					}
				}
				p += 4;
				bb.put(pkt[p]);
				bb.put(pkt[p + 1]);
				strongholdID = bb.getShort(0);
				bb.rewind();
			}
			if (pkt[p] == (byte) 0xFF && pkt[p + 1] == (byte) 0xFF
					&& pkt[p + 2] == (byte) 0xFF && pkt[p + 3] == (byte) 0x1B) {
				while (pkt[p] != 2 && pkt[p + 1] != 0 && pkt[p + 2] != 0
						&& pkt[p + 3] != 0) {
					++p;
					if (p >= pkt.length - 3) { // fail
						strongholdID = 0;
						lairID = 0;
						return;
					}
				}
				p += 4;
				bb.put(pkt[p]);
				bb.put(pkt[p + 1]);
				lairID = bb.getShort(0);
				bb.rewind();
				return;
			}
			++p;
		}
	}

	// didnt work due to multiple 0x00000001 etc.
	public static void getStrongholdLairIDs1(byte[] pkt) {
		// check magic
		byte[] magic = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1 };
		for (int i = 0; i < 4; ++i)
			if (pkt[i] != magic[i])
				return;
		// if(pkt[7] != 0x5D) return; // not packet we are looking for//wrong -
		// this changes
		int p = 0;
		int prevp = 0;
		int j = 0;
		int idIndex = -1;
		int iHuman = -1;
		int iBeast = -1;
		String team = "Team 1";
		String race = "Human";
		boolean doBefore = true;
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		while (p < pkt.length) {
			if (pkt[p] == 'T') {
				prevp = p;
				for (j = 1; j < team.length(); ++j) {
					if (pkt[++p] != (byte) team.charAt(j))
						break;
				}
				if (j < team.length()) {
					p = prevp + 1;
					continue;
				}
				System.out
						.println("getStrongholdLairIDs(): found Team string: "
								+ team);
				++p; // skip null char
				for (j = 0; j < race.length(); ++j) {
					if (pkt[++p] != (byte) race.charAt(j))
						break;
				}
				if (j < race.length()) {
					p = prevp + 1;
					continue;
				}
				System.out
						.println("getStrongholdaLairIDs(): found Race string: "
								+ race);
				// found string
				if (pkt.length <= 8)
					return; // weird check but whatever
				// now backtrack until we find 0x0000000(1|2); id is right
				// after(now before :/) it
				idIndex = prevp - 4;
				byte b = race.contentEquals("Human") ? (byte) 1 : (byte) 2;
				while (idIndex > 0
						&& !(pkt[idIndex] == b && pkt[idIndex + 1] == 0
								&& pkt[idIndex + 2] == 0 && pkt[idIndex + 3] == 0)) {
					--idIndex;
				}
				idIndex -= 2;
				// if(doBefore) idIndex -= 2;
				// else idIndex += 4;
				if (team.contentEquals("Team 1")) {
					strongholdID = (short) ((short) (pkt[idIndex] & 0x00FF) | (short) (pkt[idIndex + 1]) << 8);
					iHuman = idIndex;
					bb.rewind();
					bb.put(pkt[iHuman]);
					bb.put(pkt[iHuman + 1]);
					strongholdID = bb.getShort(0);
					// now search for lair ID
					team = "Team 2";
					race = "Beast";
					++p;
				} else if (team.contentEquals("Team 2")) {
					lairID = (short) ((short) (pkt[idIndex] & 0x00FF) | (short) (pkt[idIndex + 1]) << 8);
					iBeast = idIndex;
					bb.rewind();
					bb.put(pkt[iBeast]);
					bb.put(pkt[iBeast + 1]);
					lairID = bb.getShort(0);
					// seems like lairID = strongholdID - 1, use as check..
					// ID seem to be sometimes before 0x0000001/2 sometimes
					// after
					if (Math.abs(lairID - strongholdID) != 1) {
						// try the bytes after 0x1 and 0x2..
						// try different combos of before/after
						iHuman += 6;
						iBeast += 6;
						// strongholdID = (short) ((short)(pkt[iHuman] &
						// (short)0x00FF) |
						// (short)(pkt[iHuman + 1]) << 8);
						bb.rewind();
						bb.put(pkt[iHuman]);
						bb.put(pkt[iHuman + 1]);
						strongholdID = bb.getShort(0);

						// lairID = (short) ((short)(pkt[iBeast] &
						// (short)0x00FF) |
						// (short)(pkt[iBeast + 1]) << 8);
						bb.rewind();
						bb.put(pkt[iBeast]);
						bb.put(pkt[iBeast + 1]);
						lairID = bb.getShort(0);

						if (Math.abs(strongholdID - lairID) == 1)
							return;

						iHuman -= 6;
						bb.rewind();
						bb.put(pkt[iHuman]);
						bb.put(pkt[iHuman + 1]);
						strongholdID = bb.getShort(0);
						if (Math.abs(strongholdID - lairID) == 1)
							return;

						iHuman += 6;
						iBeast -= 6;
						bb.rewind();
						bb.put(pkt[iHuman]);
						bb.put(pkt[iHuman + 1]);
						strongholdID = bb.getShort(0);
						bb.rewind();
						bb.put(pkt[iBeast]);
						bb.put(pkt[iBeast + 1]);
						lairID = bb.getShort(0);
						// if(Math.abs(strongholdID) - lairID) == 1) return;

						System.out.println("Final check at iHuman=" + iHuman
								+ " and iBeast=" + iBeast);
						System.out.println("StrongholdID=" + strongholdID
								+ " at index " + iHuman);
						System.out.println("lairID=" + lairID + " at index "
								+ iBeast);
						if (Math.abs(lairID - strongholdID) != 1) { // fail
							strongholdID = 0;
							lairID = 0;
						}
						return;
						// doBefore = false;
						// p = 0;
					} else { // we're done,reset vars for next use
						team = "Team 1";
						race = "Human";
						doBefore = true;
						p = 0;
						break;
					}
				}
			} else {
				++p;
			}
		}
	}

	public static byte[] getStrongholdIDBytes() {
		byte[] b = new byte[2];
		b[0] = (byte) strongholdID;
		b[1] = (byte) (strongholdID >> 8);
		return b;
	}

	public static byte[] getLairIDBytes() {
		byte[] b = new byte[2];
		b[0] = (byte) lairID;
		b[1] = (byte) (lairID >> 8);
		return b;
	}

	static byte orientation = 0;
	public static void setOrientationByte(String ori) {
		orientation = Integer.decode(ori).byteValue();
	}
	public static void setOrientationByte(byte ori) {
		orientation = ori;
	}
	
	public static void setTestPacketCounter(int c) {
		testPacketCounter = c;
	}
	
	// userAction flag values
	static final byte QUICK_ATTACK = 0x01; //[1,2] [3,4] quick attack sequence
	static final byte BLOCK = 0x04; //6
	static final byte MOVE_FORWARD = 0x08;
	static final byte MOVE_BACK = 0x10;
	static final byte MOVE_FORWARD_LEFT = 0x20;
	static final byte MOVE_FORWARD_RIGHT = 0x40;
	static final byte JUMP = (byte)0x80;
	// 0x60,0xe0 doesnt move
	// 0x50,0x70 moveback
	//static final byte XXXX
	static byte testByte = 0;
	static short testShort = 0x40F0;
	static int testInt = 0x400000;
	static int testPacketCounter = 0;
	static int pingPacketCounter = 0;
	
	public static byte[] getTestPacket(byte[] unknown2bytes, byte testAction) {
		byte moveDistWhenHit = (byte)100;//(byte)0x7f;//100; // nope // 0 makes character stay in place
		// 7f and ff updates but slowly
		byte ability = 0;//(byte)(testAction < 0x10 ? 1 : 0); //(byte) 0xFF; // starts from 0
		//byte affectsHitDist4 = 15; // nope
		//byte animSwitchGun = 1;
		Random r = new Random();
		byte userAction =  (byte)( (new Random().nextInt() % 2 == 0 ? (QUICK_ATTACK + testAction % 2) : BLOCK) | MOVE_FORWARD);//(byte)((QUICK_ATTACK + testAction % 2) );//(byte)(1 + testAction % 2);//(byte)((testAction < 0x02? 0x00: 0x01) | MOVE_FORWARD);
		// 5,7 -> 1 quick attack followed by continuous block
		// 8 continuous run
		// 9 -> 1 quick attack followed by continuous run
		//(byte)(3 + testAction % 2);//RUN;
		//if(userAction == 4 || userAction == 8) ++userAction;
		//(r.nextInt() % 10 > 1) ? RUN : (r.nextInt() % 10 > 1) ? QUICK_ATTACK : BLOCK; // quickattack = 1, 3, 5,
														// FF...4, 6 = block
		byte affectsHitDist2 = 0;//15; // alternates between walking and sprinting
		byte dodge = (byte)(0);//[0,1]toggle//100; // alternates between sprinting and taunting
		//byte doesNothing = (byte) 0xFF;
		//byte taunt = (byte) 0x0; // 2 = taunt, 3 = auto taunt
		byte[] b = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1,
				01,
				00,
				00,

				(byte) 0xC7, // marker
				(byte) testPacketCounter,
				(byte) (testPacketCounter >> 8),
				(byte) (testPacketCounter >> 16),
				(byte) (testPacketCounter >> 24),
				// (byte)0xDC, 0x11, 0, 0, //<---this looks like a counter

				(byte) 0xF6, // this seems to affect speed/rate of update
								// (higher=smoother)
				(byte)0x0E,//(byte) 0x7C, 
				03, 
				moveDistWhenHit, ability, userAction,
				affectsHitDist2, dodge, (byte) 0xFF, 05,

				// affect direction of movement

				0, //(byte) (0xda), // 0x40
				0, //(byte) (0xc7), // 0xBA
				0, //(byte) (0xd2), // 0x1B
				0, //(byte)(0x3f),//(byte) (0), // 0xC2 // 6F, 7F makes character not move

				0, //(byte) 0xE1, // 0x67
				0, //(byte) (0xB6), // 0x61
				orientation,//(byte) (testByte), // 0xA6 <-- this determines
									// orientation/direction of movement
				(byte) (0x43), // 0x43 //0x77,0x70,0x68,0x64,0x63,0x62 no spawn
								// <-- this seems like a sensitivity param
		// 0x00,0x10,0x20,0x30,0x40,0x80,0x90,0xA0,0xB0,0xC0 head north
		// 0x50 head west with orientation facing north
		// 0x51 head north north east
		// 0x52,0x54 head east north east ori north
		// 0x53 west north west ori n
		// 0x55 NNW ori N
		// 0x60,0xD0 head east with orientation facing north
		// 0x61 head north east
		// 0xE0,0xF0 CLIENT FROZE - HAD TO END TASK

		// 0x10 SW 0x20 NE 0x30 S 0x40 W 0x50 SE 0x60 W 0x70 N 0x80 SSW 0x90 ENE
		// 0x60
		/*
		 * (byte)0xC7, (byte)0xA3, (byte)0xC4, 0, 0, (byte)0x7C, (byte)0xC7, 01,
		 * 0, 0, 0,0, 0, (byte)0xFF, 05, (byte)0xAD,(byte)0x9A,(byte)0xAC,
		 * (byte)0x3F,(byte)0xA9,(byte)0x3D,(byte)0x8C,(byte)0x41
		 */
		// maxvals
		// FF FF DF 61 FF FF DF 61

		/*
		 * this made character teleport backwards (byte)0xC7, // marker
		 * (byte)testPacketCounter, (byte)(testPacketCounter >> 8),
		 * (byte)(testPacketCounter >> 16), (byte)(testPacketCounter >> 24),
		 * 0x45, (byte)0xC0, 1, 0, 0, 0, 0, 0, (byte)0xFF, 0,
		 * 
		 * (byte)0xC7, // marker (byte)testPacketCounter,
		 * (byte)(testPacketCounter >> 8), (byte)(testPacketCounter >> 16),
		 * (byte)(testPacketCounter >> 24), 0x56, (byte)0xC0, 01, 00, 00, 00,
		 * 00, 00, (byte)0xFF, 00,
		 */

		/*
		 * // this places a sentry bat (byte)0xC7, // marker
		 * (byte)testPacketCounter, (byte)(testPacketCounter >> 8),
		 * (byte)(testPacketCounter >> 16), (byte)(testPacketCounter >> 24),
		 * 0x52, 0x14, 05, 00, 06, // 00, 00, 00, (byte) 0xFF, 05,(byte) 0xF9
		 * ,(byte)0xED, 0x19, (byte)0xC2, 0x38, 0x19, 0x0A, 0x42,
		 */

		/*
		 * 
		 * (byte)0xC7, (byte)0xDC, 0x11, 0, 0, (byte)0x05, (byte)0x7D, 03,
		 * affectsHitDist4, animSwitchGun, quickAttack, doesNothing, taunt,
		 * (byte)0xFF, 05, 0x40, (byte)0xBA, 0x1B, (byte)0xC2, 0x67, 0x61,
		 * (byte)0xA6, 0x43,
		 * 
		 * //block
		 * 
		 * (byte)0xC7, 07, 0x12, 00, 00, (byte)0x84, (byte)0x85, 03, (byte)0xFF,
		 * (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 05, 0x40,
		 * (byte)0xBA, 0x1B, (byte)0xC2, 0x67, 0x61, (byte)0xA6, 0x43,
		 * 
		 * (byte)0xC7, 07, 0x12, 00, 00, (byte)0x92, (byte)0x85, 03, (byte)0xFF,
		 * (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, 05, 0x40,
		 * (byte)0xBA, 0x1B, (byte)0xC2, 0x67, 0x61, (byte)0xA6, 0x43,
		 * 
		 * //
		 * 
		 * (byte)0xC7, (byte)0x09, 0x12, 00, 00, (byte)0xE7, (byte)0x85, 03,
		 * (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
		 * (byte)0xFF, 05, 0x40, (byte)0xBA, 0x1B, (byte)0xC2, 0x67, 0x61,
		 * (byte)0xA6, 0x43, (byte)0xC7, (byte)0x09, 0x12, 00, 00, (byte)0xF3,
		 * (byte)0x85, 03, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
		 * (byte)0xFF, (byte)0xFF, 05, 0x40, (byte)0xBA, 0x1B, (byte)0xC2, 0x67,
		 * 0x61, (byte)0xA6, 0x43, (byte)0xC7, 0x0A, 0x12, 00, 00, 04,
		 * (byte)0x86, 03, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
		 * (byte)0xFF, (byte)0xFF, 05, 0x40, (byte)0xBA, 0x1B, (byte)0xC2, 0x67,
		 * 0x61, (byte)0xA6, 0x43
		 */
		//
		/*
		 * (byte)0xC7, (byte)0x4C, 0x01, 00, 00, (byte)0x01, (byte)0x41, 00, 0,
		 * 0, (byte)0x08, 0, 0, (byte)0xFF, 05, (byte)0x96, (byte)0xF0, 0x10,
		 * (byte)0xC2, 0x24, (byte)0xA9, (byte)0x74, 0x43,
		 */

		/*
		 * (byte)0xC7, (byte)0x0, 0x00, 00, 00, (byte)0x00, (byte)0x00, 00, 0,
		 * 2, // range weapon (#2) out (byte)0x06, // walking animation 0x8 0,
		 * 0, (byte)0xFF, 05, (byte)0xAA, // no apparent effect (byte)0x99, //
		 * no apparent effect (byte)0x77, // no apparent effect (byte)0x44, //
		 * no apparent effect (byte)0xA0, // no apparent effect (byte)0x70, //
		 * no apparent effect (byte)0x30, // no apparent effect 0x16, // affects
		 * orientation
		 */

		/*
		 * (byte)0xC7, (byte)0x4E, (byte)0x01, 0x00, 00, (byte)0x43, (byte)0x41,
		 * 0, 0, 0, 8, 0, 0, (byte)0xFF, 05, (byte)0x96, (byte)0xF0, 0x10,
		 * (byte)0xC2, 0x24, (byte)0xA9, (byte)0x74, 0x43,
		 * 
		 * (byte)0xC7, (byte)0x4E, (byte)0x01, 0x00, 00, (byte)0x51, (byte)0x41,
		 * 0, 0, 0, 8, 0, 0, (byte)0xFF, 05, (byte)0x96, (byte)0xF0, 0x10,
		 * (byte)0xC2, 0x24, (byte)0xA9, (byte)0x74, 0x43,
		 * 
		 * (byte)0xC7, (byte)0x0C, 1, 0, 0, 0x66, 0x34, 0, 0, 0, 0, 0, 0,
		 * (byte)0xFF, 5, (byte)0xC8, (byte)0x86, (byte)0xF4, (byte)0xC0,
		 * (byte)0xF4, (byte)0xE4, (byte)0x8C, 0x43
		 */
		};
		b[5] = unknown2bytes[0];
		b[6] = unknown2bytes[1];
		// System.out.printf("%s\n",
		// Integer.toHexString((char) (0xFF & testByte)));
		testByte -= 2 * ( (new Random()).nextInt() % 20);
		// System.out.printf("%s\n",
		// Integer.toHexString((short) (0x0FFFF & testShort)));
		++testShort;
		testInt += 0x7F00;
		if (testInt >= 0x61DFFFFF)
			testInt = 0;
		if (testShort >= 0x61DF)
			testShort = 0;

		++testPacketCounter;
		return b;
	}

	
	
	public static byte[] getActionPacket(byte[] clientid, byte ability, byte userAction, int movement) {
		byte moveDistWhenHit = (byte)100;
		byte affectsHitDist2 = 0;
		byte dodge = (byte)(0);
		int movement2 = movement + 0x7F;
		movement = 0x00100000; // camrose moving backwairds orientation working?
		byte[] b = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1,
				01,
				00,
				00,

				(byte) 0xC7, // marker
				(byte) testPacketCounter,
				(byte) (testPacketCounter >> 8),
				(byte) (testPacketCounter >> 16),
				(byte) (testPacketCounter >> 24),
				(byte) movement,
				(byte) (movement >> 8), 
				(byte) (movement >> 16), 
				(byte) (movement >> 24),  
				ability, userAction,
				affectsHitDist2, dodge, (byte) 0xFF, 05,
				0x38,
				(byte)0x5D,
				(byte)0x96,
				(byte)0xBF,

				(byte)0xC9,
				(byte)0xC1,
				orientation,//(byte) (testByte), // 0xA6 <-- this determines
									// orientation/direction of movement
				(byte) (0x43), // 0x43 //0x77,0x70,0x68,0x64,0x63,0x62 no spawn
/*				
				(byte) 0xC7, // marker
				(byte) testPacketCounter,
				(byte) (testPacketCounter >> 8),
				(byte) (testPacketCounter >> 16),
				(byte) (testPacketCounter >> 24),
				(byte) movement2,
				(byte) (movement2 >> 8), 
				(byte) (movement2 >> 16), 
				(byte) (movement2 >> 24), 
				ability, userAction,
				affectsHitDist2, dodge, (byte) 0xFF, 05,
				0x38,
				(byte)0x5D,
				(byte)0x96,
				(byte)0xBF,

				(byte)0xC9,
				(byte)0xC1,
				orientation,//(byte) (testByte), // 0xA6 <-- this determines
									// orientation/direction of movement
				(byte) (0x43), // 0x43 //0x77,0x70,0x68,0x64,0x63,0x62 no spawn
				*/
		};
		b[5] = clientid[0];
		b[6] = clientid[1];
		++testPacketCounter;
		return b;
	}
	
	
	
	
	
	
	
	
	public static byte[] getActionPacket1(byte[] unknown2bytes, byte action) {
		byte moveDistWhenHit = 100; // nope
		byte animSwitchHammer = (byte) 0xFF; // nope
		byte affectsHitDist2 = 15; // no sentry bat
		byte affectsHitDist3 = 100; // nope
		byte affectsHitDist4 = 15; // nope
		byte animSwitchGun = 1;
		Random r = new Random();
		byte userAction = action;
		byte doesNothing = (byte) 0xFF;
		byte taunt = (byte) 0x0; // 2 = taunt, 3 = auto taunt
		byte[] b = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1,
				01,
				00,
				00,

				(byte) 0xC7, // marker
				(byte) testPacketCounter,
				(byte) (testPacketCounter >> 8),
				(byte) (testPacketCounter >> 16),
				(byte) (testPacketCounter >> 24),
				// (byte)0xDC, 0x11, 0, 0, //<---this looks like a counter

				(byte) 0xF6, // this seems to affect speed/rate of update
								// (higher=smoother)
				(byte) 0x7C, 03, moveDistWhenHit, animSwitchHammer, userAction,
				affectsHitDist2, affectsHitDist3, (byte) 0xFF, 05,

				// affect direction of movement

				(byte) (0), // 0x40
				(byte) (0), // 0xBA
				(byte) (0), // 0x1B
				(byte) (0), // 0xC2 // 6F, 7F makes character not move

				(byte) 0, // 0x67
				(byte) (0), // 0x61
				(byte) (testByte), // 0xA6 <-- this determines
									// orientation/direction of movement
				(byte) (0x43), // 0x43 //0x77,0x70,0x68,0x64,0x63,0x62 no spawn
								// <-- this seems like a sensitivity param

		};
		b[5] = unknown2bytes[0];
		b[6] = unknown2bytes[1];
		++testPacketCounter;
		return b;
	}
	
	public static byte[] getPingMinimapRequest(byte[] unknown2bytes, float x, float y) {
		/*
		 834  0.0.0.0:55265  :0  64  SendTo  
0000  9A DE 97 F1 01 06 89 C7 04 B2 43 00 21 61 03 00    ..........C.!a.. <--- ping request
0010  00 01 00 00 FF 05 C1 2D 8D BE EF 54 A8 43 C8 1F    .......-...T.C..
0020  32 24 08 3F 4A C6 1C 3E FF C7 04 B2 43 00 31 61    2$.?J..>....C.1a
0030  03 00 00 00 00 00 FF 05 C1 2D 8D BE EF 54 A8 43    .........-...T.C
		 */
		byte[] b = { (byte) 0x9A, (byte) 0xDE, (byte) 0x97, (byte) 0xF1,
				01,
				00,
				00,
				(byte) 0xC7, // marker
				(byte) testPacketCounter,
				(byte) (testPacketCounter >> 8),
				(byte) (testPacketCounter >> 16),
				(byte) (testPacketCounter >> 24),
				(byte) pingPacketCounter,
				(byte) (pingPacketCounter >> 8),
				(byte) (pingPacketCounter >> 16),
				(byte) (pingPacketCounter >> 24),
				0, 1, 0, 0, (byte) 0xFF, 05,
				(byte)0xC1, 0x2D, (byte)0x8D, (byte)0xBE, (byte)0xEF, 0x54, (byte)0xA8, 0x43, 
				(byte)0xC8, 0x1F,
				(byte)x, (byte)((int)x >> 8), (byte)((int)x >> 16), (byte)((int)x >> 24), // xCoord  
				(byte)y, (byte)((int)y >> 8), (byte)((int)y >> 16), (byte)((int)y >> 24), // yCoord
				(byte)0xFF, 
				(byte)0xC7,
				(byte) testPacketCounter,
				(byte) (testPacketCounter >> 8),
				(byte) (testPacketCounter >> 16),
				(byte) (testPacketCounter >> 24),
				(byte) (pingPacketCounter + 16),
				(byte) ((pingPacketCounter + 16)>> 8),
				(byte) ((pingPacketCounter + 16) >> 16),
				(byte) ((pingPacketCounter + 16) >> 24), 
				00, 00, 00, 00, (byte)0xFF, 05, 
				(byte)0xC1, 0x2D, (byte)0x8D, (byte)0xBE, (byte)0xEF, 0x54, (byte)0xA8, 0x43
		};
		b[5] = unknown2bytes[0];
		b[6] = unknown2bytes[1];
		++testPacketCounter;
		return b;
	}

	public static void next() {
		++msgCounter;
	}
	public static int getMsgCounter() {
		return msgCounter;
	}

	
	
	
	/*
	294  :0  0.0.0.0:55439  39  RecvFrom  
	0000  43 00 00 00 03 4C 82 60 31 31 10 50 08 03 00 49    C....L.`11.P...I
	0010  11 51 08 03 00 FF FF FF FF FF FF FF FF FF FF FF    .Q..............
	0020  FF FF FF FF FF FF FF 
	*/
	public static boolean parsePkt3(byte[] b, int bLen) {
		System.out.println("ISSSISSSS SUSPECTED 3..FFFF  PACKET len=" + bLen);
		byte[] magic = {(byte)0x9A, (byte)0xDE, (byte)0x97, (byte)0xF1};
		byte pktmagic = (byte)0x3;
		int i = 0;
		if(bLen != 0x27) return false;
		System.out.println("CECKING SUSPECTED 3..FFFF  PACKET");
		for(;i < magic.length; ++i) {
			if(magic[i] != b[i]) break;
		}
		if(i == magic.length) return false;
		System.out.println("FOUND SUSPECTED FFFF  PACKET");
		if(b[4] != pktmagic) return false;
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.put(b[0x23]);
		bb.put(b[0x24]);
		bb.put(b[0x25]);
		bb.put(b[0x26]);
		System.out.println("FOUND SUSPECTED FFFF  PACKET: COMAPRING LAST 4 BYTES");
		if(0xFFFFFFFF != bb.getInt(0)) return false;
		return true;
	}
	
	
	/*
902  :0  0.0.0.0:51861  84  RecvFrom  
0000  9A DE 97 F1 01 62 84 5B 48 00 00 00 C6 2D 02 00    .....b.[H....-..
0010  C1 2D 02 00 24 F6 08 00 21 F5 08 00 03 00 EE 72    .-..$...!......r
0020  21 2B 9D C1 46 F8 0F 7D 45 EE 7A A4 CC 3E 75 2D    !+..F..}E.z..>u-
0030  39 01 9A CE 24 46 DA CA 11 46 7B EA 19 42 58 F8    9...$F...F{..BX.
0040  60 C3 C1 B7 5C 40 30 F0 94 C2 21 F5 08 00 74 75    `...\@0...!...tu
0050  15 00 C8 00 

904  :0  0.0.0.0:51861  99  RecvFrom  
0000  9A DE 97 F1 01 62 84 5B 57 00 00 00 C7 2D 02 00    .....b.[W....-..
0010  C2 2D 02 00 56 F6 08 00 41 F5 08 00 03 01 02 02    .-..V...A.......
0020  D0 3A 9A 00 EE 72 21 2B 9D C1 46 F8 0F 7D 45 1A    .:...r!+..F..}E.
0030  82 A4 CC 3E 75 2D 39 01 CE B1 24 46 4A CB 11 46    ...>u-9...$FJ..F
0040  40 66 11 42 20 E3 64 C3 D1 68 6F 40 04 5E 6D C2    @f.B .d..ho@.^m.
0050  41 F5 08 00 74 75 75 12 AE 00 C8 00 A1 75 E9 04    A...tuu......u..
0060  02 BA 3A 

 1714  :0  0.0.0.0:59519  63  RecvFrom  
0000  9A DE 97 F1 01 0A 87 5B 33 00 00 00 25 1F 01 00    .......[3...%...
0010  24 1F 01 00 F0 37 05 00 B4 37 05 00 07 00 AA 2D    $....7...7.....-
0020  2D 39 01 9C 8B 00 45 86 D3 32 45 62 DA BA 42 1F    -9....E..2Eb..B.
0030  91 1C C3 FC 34 1A C3 9D 81 B1 C2 B4 37 05 00       ....4.......7..

contentLen apparently always 3 bytes extra (1 byte short) of being divisible by 4
	 */
	static final byte PK_TYPE_XX = (byte)0x5B;
	static byte[] prev = null;
	static boolean isFirst5BPkt = true;
	
	static float initialX = 0;
	static float initialY = 0;
	static boolean firstCoordPkt = true;
	
	public static boolean parsePkt5b(byte[] serverconnid, byte[] b, int bLen) {
		byte[] magic = {(byte)0x9A, (byte)0xDE, (byte)0x97, (byte)0xF1};
		byte magic2 = 1;
		byte pktmagic = (byte)0x5B;
		int i = 0;
		if(bLen < 0xC) return false;
		if(bLen < 0x1E) return true; // i think 0x12 is the smallest possible 5b pkt content size
		for(;i < magic.length; ++i) {
			if(magic[i] != b[i]) break;
		}
		if(i < magic.length) return false;
		if(b[i] != magic2) return false;
		//System.out.println("Checking pkt5b magic: " + String.format("%x", b[7]));
		if(b[7] != pktmagic) return false;
		// NOTE: length bytes are excluded from length value
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		int len = Utility.getInt(b, 8);
		if(len < 4) return true;
		byte[] content = new byte[len];
		for(i = 12; i < bLen; ++i)
			content[i - 12] = b[i];
		parseAll5BTypes(content, len);
		if(firstCoordPkt == false) return true;
		
		//Utility.dumpBytes(b,  bLen);
		if(bLen < 54) return true;//for now - proper parsing shouldnt require this
		// get next test packet counter
		testPacketCounter = Utility.getInt(b, 16);//Utility.getInt(b, 12);
		pingPacketCounter = Utility.getInt(b, 16) - 0x1b;
		// 20-23 dunno
		// 24-27 dunno
		
		// 2 bytes
		
		
		
		int p = 0;
		/*
		if(b[28] == 0) {
			switch(b[33]) {
			case (byte)0xA2:
				p = 35;
				break;
			case (byte)0x5A:
				p = 36;
				break;
			default:
				return true; // not handled
			}
			p = 35;
		} else {
		// 28-31 dunno
		// 32-35 counter
		if(b[36] != (byte)0xDF) return true; //no coords here

		switch(b[37]) {
		case (byte)0x5B:
			// 4 bytes before coords
			p = 41; // points to last byte before coords
			break;
		case (byte)0xBF:
			// 5 bytes before coords
			p = 42; // points to last byte before coords
			break;
			default:
				return true; //not handled yet
		}
		}
		*/
		
		// implementing crude search for now
		while((b[p] != 0x46 /*|| b[p] != 0x45*/) /*&& b[p+4] != 0x46*/ && p+4 < bLen) ++p;
		if(p + 4 == bLen) return true;
		p -=4;
		
		bb.rewind();
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		float xCoord = bb.getFloat(0);
		bb.rewind();
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		float yCoord = bb.getFloat(0);
		bb.rewind();
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		float zCoord = bb.getFloat(0);
		if(firstCoordPkt) {
			initialX = xCoord;
			initialY = yCoord;
			firstCoordPkt = false;
		} else {
			lookTowardsXY(xCoord, yCoord);
		}
		
		/*
		int p = 0x20;//15;
		if(isFirst5BPkt) {
			p = 0x23;
			isFirst5BPkt = false;
		}

		//else {
		bb.rewind();
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		float xCoord = bb.getFloat(0);
		//int xCoord = bb.getInt(0);
		bb.rewind();
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		float yCoord = bb.getFloat(0);
		//int yCoord = bb.getInt(0);
		bb.rewind();
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		bb.put(b[++p]);
		float zCoord = bb.getFloat(0);
		//int zCoord = bb.getInt(0);
		//System.out.println("x = " + xCoord);
		//System.out.println("y = " + yCoord);
		//System.out.println("z = " + zCoord);
		//Utility.dumpBytes(b,  bLen);
		//}
		*/
		
		
		/*
		if(prev == null) {
			prev = new byte[len]; // just the contents
			// copy into prev for comparison
			for(i = 0; i < len; ++i)
				prev[i] = b[0xc + i];
		} else {
			if(prev.length != len) {
				prev = new byte[len]; // just the contents
				// copy into prev for comparison
				for(i = 0; i < len; ++i)
					prev[i] = b[0xc + i];
				return true;
			}
			// diff compare with prev pkt
			for(i = 0; i < len; ++i) {
				if(prev[i] != b[0xc + i]) {
					System.out.print("[" + String.format("%02X", i) + "] " + String.format("%02X", b[0xc + i]) + " ");
				}
			}
			System.out.println();
		}
		*/
	
		/*
		bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		for(int z = 32; z + 3 < bLen; ++z) {
		if(bLen < z + 3) return true;
		bb.rewind();
		bb.put(b[z]);
		bb.put(b[z+1]);
		bb.put(b[z+2]);
		bb.put(b[z+3]);
		//nope; remained constant as i was moving around		
		//0x47 - 0x4A
		float val = bb.getFloat(0);
		System.out.println("Pkt5B val at index " + z + ": " + val);
		}
		*/
		
		/*
		for(i = 0; i < 16; ++i)
			System.out.print(String.format("%02x ", b[0xc + 0x38 + i]));
		System.out.println();
		*/
		
		/*
		 0x43 - 0x47 is orientation of marksman
		 */
		
		/*
		//////little fun something just fiddling around
		byte t = 0x41;//0x3e;//0x45; //0x46
		// 0x40 doesn't change when standing still..increases when speed increases
		if(bLen > t + 1) {
			System.out.println("Setting orientation to 0x" + String.format("%02X", b[0x38]));
			setOrientationByte(b[t]);
			return true;
		}
		
		////
		*/
		
		/*
		if(len > bLen - 0xc) return false; // this shouldn't happen?!
		for(i = 0; i < len; ++i) {
			System.out.print(String.format("%02X ", b[0xc + i]));
			if(i % 16 == 0) 
				System.out.print(" [" + String.format("%04X", 0xc + i) +  "] ");
		}
		System.out.println();
		*/
		
		/*
		if(len < 0x4c + 8) return true;
		bb = ByteBuffer.allocate(8);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		int x = 0x4C - 1;
		bb.put(b[x + 1]);
		bb.put(b[x + 2]);
		bb.put(b[x + 3]);
		bb.put(b[x + 4]);
		bb.put(b[x + 5]);
		bb.put(b[x + 6]);
		bb.put(b[x + 7]);
		bb.put(b[x + 8]);
		double val = bb.getDouble(0);
		System.out.println("Pkt5B val: " + val);
		*/
		
		return true;
	}
	
	//0000  9A DE 97 F1 01 4B 8B 5D 0A E5 19 00 00 0A E5 19    .....K.]........
	//0010  00 FF FF FF FF A4 FD 0D 00
	//it is the first occurrence of this pkt type that we get mainIDs
	//we also need values used by client later on to get pkt5b
	public static int pkt5dval1 = 0;
	public static int pkt5dval2 = 0;
	public static boolean parsePkt5d(byte[] unknown2bytes, byte[] b) {
		byte[] magic = {(byte)0x9A, (byte)0xDE, (byte)0x97, (byte)0xF1};
		byte magic2 = 1;
		byte[] clientBytes = unknown2bytes;
		byte pktmagic = (byte)0x5D;
		int i = 0;
		if(b.length < magic.length + 4) return false;
		for(;i < magic.length; ++i) {
			if(magic[i] != b[i]) break;
		}
		if(i < magic.length) return false;
		if(b[i] != magic2) return false;
		if(b[7] != pktmagic) return false;
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(b[8]);
		bb.put(b[9]);
		bb.put(b[10]);
		bb.put(b[11]);
		pkt5dval1 = bb.getInt(0);
		bb.rewind();
		bb.put(b[0x15]);
		bb.put(b[0x16]);
		bb.put(b[0x17]);
		bb.put(b[0x18]);
		pkt5dval2 = bb.getInt(0);
		return true;
	}
	
	//9A DE 97 F1 01 4B 8B 5E
	public static int pkt5eval1 = 0;
	public static boolean parsePkt5e(byte[] unknown2bytes, byte[] b) {
		byte[] magic = {(byte)0x9A, (byte)0xDE, (byte)0x97, (byte)0xF1};
		byte magic2 = 1;
		byte[] clientBytes = unknown2bytes;
		byte pktmagic = (byte)0x5E;
		int i = 0;
		if(b.length < magic.length + 4) return false;
		for(;i < magic.length; ++i) {
			if(magic[i] != b[i]) break;
		}
		if(i < magic.length) return false;
		if(b[i] != magic2) return false;
		if(b[7] != pktmagic) return false;
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(b[8]);
		bb.put(b[9]);
		bb.put(b[10]);
		bb.put(b[11]);
		pkt5eval1 = bb.getInt(0);
		return true;
	}
	
	
	
	/*
	 assuming the following format
	 0C 02 ..4 bytes.. ..4 bytes.. 00 29 00 ..4 bytes.. [data]
	*/
	public static void parse0x12D09DF(byte[] b, int len) {
		int marker1 = 0x12D09DF;//DF 09 2D 01
		int p = 0;
		/*
		while(!(b[p] == 0xC && b[p + 1] == 2) && p + 17 < len)
			++p;
		if(p + 17 == len) return;
		*/
		int m = 0;
		for(;m != marker1 && p + 16 < len; ++p) {
			m = Utility.getInt(b, p);
		}
		if(p + 16 == len) return;
		p += 3;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		System.out.println("parse0x12D09DF():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);
		lookTowardsXY(x, y);
	}

	public static void parse0x82D(byte[] b, int len) {
		short marker1 = 0x82D;//2D 08
		int p = 0;
		short m = 0;
		for(;m != marker1 && p + 14 < len; ++p) {
			m = Utility.getShort(b, p);
		}
		if(p + 14 == len) return;
		p += 1;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		System.out.println("parse0x82D():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);		
		lookTowardsXY(x, y);
	}
	
	public static void parse0x0099(byte[] b, int len) {
		short marker1 = 0x99;//99 00
		int p = 0;
		short m = 0;
		for(;m != marker1 && p + 14 < len; ++p) {
			m = Utility.getShort(b, p);
		}
		if(p + 14 == len) return;
		p += 1;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		System.out.println("parse0x0099():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);		
		lookTowardsXY(x, y);
	}
	
	public static void parse0x0044(byte[] b, int len) {
		short marker1 = 0x44;//44 00
		int p = 0;
		short m = 0;
		for(;m != marker1 && p + 14 < len; ++p) {
			m = Utility.getShort(b, p);
		}
		if(p + 14 == len) return;
		p += 1;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		System.out.println("parse0x0044():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);
		lookTowardsXY(x, y);
	}	
	
	public static void parse0x03A70EEF(byte[] b, int len) {
		int marker1 = 0x03A70EEF;//EF 0E A7 03
		int p = 0;
		int m = 0;
		for(;m != marker1 && p + 16 < len; ++p) {
			m = Utility.getInt(b, p);
		}
		if(p + 16 == len) return;
		p += 3;
		float x, y, z;
		x = Utility.getInt(b, p);
		y = Utility.getInt(b, p + 4);
		z = Utility.getInt(b, p + 8);
		System.out.println("parse0x03A70EEF():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);
		lookTowardsXY(x, y);
	}
	
	//9B A9 2C 21
	public static void parse0x212C(byte[] b, int len) {
		short marker1 = 0x212C;//2C 21
		int p = 0;
		short m = 0;
		for(;m != marker1 && p + 14 < len; ++p) {
			m = Utility.getShort(b, p);
		}
		if(p + 14 == len) return;
		p += 1;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		System.out.println("parse0x212C():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);	
		lookTowardsXY(x, y);
	}
	
	public static void parse0x026F(byte[] b, int len) {
		short marker1 = 0x26F;//6F 02
		int p = 0;
		short m = 0;
		for(;m != marker1 && p + 14 < len; ++p) {
			m = Utility.getShort(b, p);
		}
		if(p + 14 == len) return;
		p += 1;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		System.out.println("parse0x026F():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);	
		lookTowardsXY(x, y);
	}
	
	public static void parse0x01D0(byte[] b, int len) {
		short marker1 = 0x01D0;//D0 01
		int p = 0;
		short m = 0;
		for(;m != marker1 && p + 14 < len; ++p) {
			m = Utility.getShort(b, p);
		}
		if(p + 14 == len) return;
		p += 1;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		System.out.println("parse0x01D0():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);	
		lookTowardsXY(x, y);
	}
	
	public static void parse0x05A16FCF(byte[] b, int len) {
		int marker1 = 0x05A16FCF; //CF 6F A1 05
		int p = 0;
		int m = 0;
		for(;m != marker1 && p + 16 < len; ++p) {
			m = Utility.getInt(b, p);
		}
		if(p + 16 == len) return;
		p += 3;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		System.out.println("parse0x05A16FCF():");
		System.out.println("x = " + x);
		System.out.println("y = " + y);
		System.out.println("z = " + z);	
		lookTowardsXY(x, y);
	}
	
	public static void parseDirty(byte[] b, int len) {
		int p = 0;
		int m = 0;
		for(;p + 12 < len; ++p) {
			m = Utility.getInt(b, p);
			if(m > 0x45000000 && m < 0x47000000) {
				int n = Utility.getInt(b, p+4);
				if(n > 0x45000000 && n < 0x47000000)
					break;
			}
		}
		if(p + 16 == len) return;
		float x, y, z;
		x = Utility.getFloat(b, p);
		y = Utility.getFloat(b, p + 4);
		z = Utility.getFloat(b, p + 8);
		//System.out.println("parseDirty():");
		//System.out.println("x = " + x);
		//System.out.println("y = " + y);
		//System.out.println("z = " + z);	
		lookTowardsXY(x, y);
	}
	
	public static void parseAll5BTypes(byte[] content, int len) {
		//parse0x12D09DF(content, len);
		//parse0x0044(content, len); // explore further cleaner parsing
		//parse0x0099(content, len); // explore further cleaner parsing
		//parse0x03A70EEF(content, len); // fail - not found/hardly found
		//parse0x212C(content, len); // fail - not found/hardly found
		//parse0x82D(content, len); // fail - prints own location apparently
		//parse0x026F(content, len); // fail - prints own location apparently
		//parse0x01D0(content, len); // fail - not found/hardly found
		parseDirty(content, len);
		
		// spawn portal stuff
		parsePortalMarker(content, len);
		parsePortalId(content, len);
	}
	
	// 86 00 00 00 03 B4 4A 60 21 01 4D 0F 00 00
	public static void parse602101(byte[] b, int bLen) {
		if(bLen != 14) return;
		if(b[4] != 3 || b[7] != 0x60 || b[8] != 0x21 || b[9] != 1) return;
		short n = Utility.getShort(b, 10);
		System.out.println("parse602101(): " + n);
		System.exit(1);
	}
	
	
	static int portalmarker;
	static boolean portalmarkerfound = false;
	public static void parsePortalMarker(byte[] b, int bLen) {
		//System.out.println("Checking for portal marker in content of length " + String.format("%04X", bLen));
		//Utility.dumpBytes(b, bLen);
		byte[] markermagic = {05, 0x2C, 01, 01, 02, 0x33, 
				0x33, (byte)0xD3, 0x40, 01, 00};
		int p = 0;
		while(p + markermagic.length + 4 <= bLen) {
			int i;
			for(i = p; i < p + markermagic.length; ++i)
				if(b[i] != markermagic[i - p])
					break;
			if(i != p + markermagic.length) {
				++p;
			} else {
				// found marker magic...fetch portal id marker value
				System.out.println("<<<<<<<FOUND PORTAL MARKER >>>>>>>>>>>>>>>>>>>>>>>>");
				portalmarker = Utility.getInt(b, i);
				portalmarkerfound = true;
				return;
			}
		}
	}
	
	static short portalId = 0;
	public static void parsePortalId(byte[] b, int bLen) {
		if(!portalmarkerfound) return;
		int val = Utility.getInt(b, 8);
		if(val != portalmarker) return;
		portalId = (short) (Utility.getShort(b, bLen - 11) / 2);
		System.out.println("<<<< PORTAL ID >>>>> " + String.format("%04X", portalId));
	}
	
	public static short getPortalId() {
		return portalId;
	}
	
	
	public static void lookTowardsXY(float xCoord, float yCoord) {
		float dY = yCoord - initialY;
		float dX = xCoord - initialX;
		double angle = Math.atan(dY / dX);
		byte ori = 0;
		//if(dX > 0 && dY < 0)
		//	angle += Math.PI / 2;
		if(dX > 0)
			ori = (byte) ((byte)0x7F + angle * (0xB0 - 0x7F) / (Math.PI / 2));
		else
			ori = (byte) ((byte)0xD0 + angle * (0xB0 - 0x7F) / (Math.PI / 2));			
		System.out.println("dX=" + dX);
		System.out.println("dY=" + dY);
		//System.out.println("Setting orientation to " + String.format("%02X", ori));
		
		setOrientationByte(ori);
	}
	
	

	public static final int SERVER_IDLE_TIMEOUT = 1;
	public static int parseServerMessage(byte[] b, int len) {
		if(len < 20) return 0;
		if(b[4] != 3) return 0;
		if(b[7] != 0x60 || b[8] != 6) return 0;
		String timeout = "Server idle timeout reached";
		String str = new String(b, 9, timeout.length());
		if(str.contentEquals(timeout))
			return SERVER_IDLE_TIMEOUT;
		return 0;
	}
	
	class AllChatMessage {
		AllChatMessage() {}
		int playerid;
		String msg;
	}
	public static AllChatMessage parseAllChat(byte[] b, int bLen) {
		if(bLen < 13) return null;
		if(b[4] != 3 || b[7] != 0x60 || b[8] != 3) return null;
		AllChatMessage res = (new Savage2GameServer()).new AllChatMessage();
		res.playerid = Utility.getInt(b,  9);
		res.msg = new String(b, 13, bLen - 1 - 13);
		return res;
	}
	
	public static boolean parseAllChatCamrose(byte[] b, int bLen) {
		if(bLen < 13) return false;
		if(b[4] != 3 || b[7] != 0x60 || b[8] != 3) return false;
		String str = new String(b, 13, 7);
		if(str.contentEquals("camrose")) return true;
		return false;
	}
	
	/*
	 * v74 [.........]....[...]..........[ human ]....[.........] 0B 00 00 00 03
	 * B4 E0 C8 36 03 68 75 6D 61 6E 00 78 00 00 00
	 */

	/*
	 * AllChat message received:hello 4E 00 00 00 03 F4 AA 60 03 1E 00 00 00 68
	 * 65 6C 6C 6F 00
	 */
}
