import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Savage2Client extends S2Lib {

	final static String MASTER_SERVER = "masterserver.savage2.s2games.com";
	
	final static String US_WEST1_HOST = "162.248.7.113";
	final static int US_WEST1_PORT = 13235;

	final static String OPHELIA_EU1_HOST = "188.40.72.24";
	final static int OPHELIA_EU1_PORT = 11236;

	final static String OPHELIA_EU3_HOST = "188.40.72.24";
	final static int OPHELIA_EU3_PORT = 11236;

	final static String MGF_GAMING_HOST = "78.46.21.137";
	final static int MGF_GAMING_PORT = 11235;

	final static String GG = "192.99.34.197";
	final static int GGport = 11236;

	final static String NA_EAST_HOST = "192.99.34.197";
	final static int NA_EAST_PORT = 11239;
	
	String host =  "188.40.72.24";//"127.0.0.1";
	int port = 11235;//11235;

	public Savage2Client() {
		super();
		(new Thread(new CnCServer(null))).start();
		login("camrose", "123camrose"); // account_id = 845432
		connectToGameServer(host, port);//"162.248.7.113", 13235);
	}

	public static void main(String[] args) {
		//Utility.foo("E8 85 FC 45 7B 81 FD 45 4C 63 23 44", 666);
		new Savage2Client();
	}

	@Override
	public void onReceiveWhisper(char[] recvDataSess, int dataLen) {
		// TODO Auto-generated method stub
		// test command and control
		if (dataLen > 12 && recvDataSess[0] == 9) {
			int i;
			for (i = 1; i < 10; ++i) {
				if (BOTMASTER.charAt(i - 1) != recvDataSess[i])
					break;
			}
			if (i < 10)
				return;
			if (dataLen >= 15) {
				switch (recvDataSess[11]) {
				case 'o': // 0x..
					String ori = new String(recvDataSess, 12, 4);
					S2Factory.setOrientationByte(ori);
				}
			}
			if (dataLen >= 12)
				switch (recvDataSess[11]) {
				case 'z':
					printRecv = false;
					break;
				case 'j':
					byte team;
					int squadNo = 0;
					switch (recvDataSess[12]) {
					case '1':
						team = S2Factory.JOIN_HUMANS;
						break;
					case '2':
						team = S2Factory.JOIN_BEASTS;
						break;
					default:
						team = S2Factory.JOIN_HUMANS;

					}
					S2Factory.setCurrentTeam(team);
					boolean tryAll = false;
					switch (recvDataSess[13]) {
					case '0':
						squadNo = 0;
						break;
					case '1':
						squadNo = 1;
						break;
					case '2':
						squadNo = 2;
						break;
					case '3':
						squadNo = 3;
						break;
					default:
						tryAll = true;

					}
					System.out.println("Sending join team/squad request..");
					sendJoinTeamRequest(team);
					if (tryAll) {
						sendJoinSquadRequest(0);
						sendJoinSquadRequest(1);
						sendJoinSquadRequest(2);
						sendJoinSquadRequest(3);
					} else {
						sendJoinSquadRequest(squadNo);
					}
					break;
				case 'x':
					disconnectFromGameServer();
					System.exit(0);
					break;
				case 's':
					// (re)spawn
					byte character;
					switch (recvDataSess[12]) {
					case 'q':
						character = S2Factory.BUILDER;
						break;
					case 'w':
						character = S2Factory.MARKSMAN;
						break;
					case 'e':
						character = S2Factory.SAVAGE;
						break;
					case 'r':
						character = S2Factory.CHAPLAIN;
						break;
					case 't':
						character = S2Factory.LEGIONNAIRE;
						break;
					case 'y':
						character = S2Factory.STEAMBUCHET;
						break;
					case 'u':
						character = S2Factory.BATTERINGRAM;
						break;
					case 'z':
						character = S2Factory.CONJURER;
						break;
					case 'x':
						character = S2Factory.SHAPESHIFTER;
						break;
					case 'c':
						character = S2Factory.HUNTER;
						break;
					case 'v':
						character = S2Factory.SHAMAN;
						break;
					case 'b':
						character = S2Factory.PREDATOR;
						break;
					case 'n':
						character = S2Factory.BEHEMOTH;
						break;
					case 'm':
						character = S2Factory.TEMPEST;
						break;

					case 'p':
						character = S2Factory.MALPHAS;
						break;
					default:
						character = S2Factory.BUILDER;
					}
					sendSelectCharacterRequest(character);
					short mainID;
					if (S2Factory.getCurrentTeam() == S2Factory.JOIN_HUMANS)
						mainID = S2Factory.getStrongholdID();
					else
						mainID = S2Factory.getLairID();
					sendSpawnRequest(mainID);

					hasSpawned.set(true);
					break;
				default:
				}
		}
	}

	
	short[] actionList = {
			0x010,0x0101,0x0101,0x0101
	};
	int pActionList = 0;
	
	
	byte testAction = 0;
	boolean setupOnField = true;
	
	@Override
	public void onReceiveAllChat(int playerid, String msg) {
		if(msg.contentEquals("camrose")) {
			sendVCCommand(111);
			//if (currentState.get() == State.SPAWNSCREEN) {
				// spawn test
				short spawnID = S2Factory.getPortalId();
				System.out.println("Sending spawn at "
						+ String.format("%04X", spawnID) + " request...");
				sendSpawnRequest(spawnID);
			//}
		}
		if(msg.contentEquals("team0")) {
			// join team test
			System.out.println("Sending join team request...");
			sendJoinTeamRequest((byte)0);
		}
		if(msg.contentEquals("team1")) {
			// join team test
			System.out.println("Sending join team request...");
			sendJoinTeamRequest(S2Factory.JOIN_HUMANS);
		}
		if(msg.contentEquals("team2")) {
			// join team test
			System.out.println("Sending join team request...");
			sendJoinTeamRequest(S2Factory.JOIN_BEASTS);
		}
		if(msg.contentEquals("squad")) {
			sendVCCommand(111);
			//if (currentState.get() == State.SPAWNSCREEN) {
				sendJoinSquadRequest(0);
				sendSelectCharacterRequest(S2Factory.SAVAGE);
			//}
		}
		if(msg.contentEquals("savage")) {
			sendSelectCharacterRequest(S2Factory.SAVAGE);
		}
		if(msg.contentEquals("camdc")) {
			disconnectFromGameServer();
			System.exit(0);
		}
	}

	@Override
	public void onStateChange(State state) {
		if(state != State.ONFIELD) {
			if(state == State.INSPEC) {
				/*
				// join team test
				System.out.println("Sending join team request...");
				sendJoinTeamRequest(Savage2GameServer.JOIN_BEASTS);
				// select character
				System.out.println("Sending select character request...");
				sendSelectCharacterRequest(Savage2GameServer.SAVAGE);
				*/
			}

		} else {
			if(setupOnField) {
				// Savage2GameServer.setOrientationByte("0xB0");
				S2Factory
						.setTestPacketCounter(S2Factory.pkt5dval1 + 0x600);
				System.out.println("Entering test packet request loop...");
				setupOnField = false;
			}
			/*
			testAction = Savage2GameServer.MOVE_FORWARD * 2;
			byte action = (byte)((1 + pActionList) % 10);//(byte)(pActionList % 2);//(byte)actionList[pActionList];
			byte ability = 0;//(byte)(actionList[pActionList] >> 8);
			pActionList = (pActionList + 1) % actionList.length;
			setAction(ability, action);
			*/
		}
	}
	
	@Override
	public void onSendAction(byte ability, byte action) {
		byte ab = 0;
		byte ac = 0;
		ac = (byte)((1 & action + 1) % 2);
		ac = (byte)(S2Factory.MOVE_FORWARD | ac);
		setAction(ability, ac);
	}
	
	@Override
	public void onOfficerPromotion() {
		
	}
	
	@Override
	public void onServerIdleTimeout() {
		disconnectFromGameServer();
		connectToGameServer(host, port);
	}

}
