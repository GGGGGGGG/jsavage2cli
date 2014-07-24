import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Savage2Client extends Savage2Login {

	final static String MASTER_SERVER = "masterserver.savage2.s2games.com";

	public Savage2Client() {
		super();
		login("camrose", "123camrose");
		connectToGameServer("162.248.7.113", 13235);
	}

	public static void main(String[] args) {
		// Utility.foo("DF C9 17 46 22 C6 B3 46 06 89 94 41");
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
					Savage2GameServer.setOrientationByte(ori);
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
						team = Savage2GameServer.JOIN_HUMANS;
						break;
					case '2':
						team = Savage2GameServer.JOIN_BEASTS;
						break;
					default:
						team = Savage2GameServer.JOIN_HUMANS;

					}
					Savage2GameServer.setCurrentTeam(team);
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
						character = Savage2GameServer.BUILDER;
						break;
					case 'w':
						character = Savage2GameServer.MARKSMAN;
						break;
					case 'e':
						character = Savage2GameServer.SAVAGE;
						break;
					case 'r':
						character = Savage2GameServer.CHAPLAIN;
						break;
					case 't':
						character = Savage2GameServer.LEGIONNAIRE;
						break;
					case 'y':
						character = Savage2GameServer.STEAMBUCHET;
						break;
					case 'u':
						character = Savage2GameServer.BATTERINGRAM;
						break;
					case 'z':
						character = Savage2GameServer.CONJURER;
						break;
					case 'x':
						character = Savage2GameServer.SHAPESHIFTER;
						break;
					case 'c':
						character = Savage2GameServer.HUNTER;
						break;
					case 'v':
						character = Savage2GameServer.SHAMAN;
						break;
					case 'b':
						character = Savage2GameServer.PREDATOR;
						break;
					case 'n':
						character = Savage2GameServer.BEHEMOTH;
						break;
					case 'm':
						character = Savage2GameServer.TEMPEST;
						break;

					case 'p':
						character = Savage2GameServer.MALPHAS;
						break;
					default:
						character = Savage2GameServer.BUILDER;
					}
					sendSelectCharacterRequest(character);
					short mainID;
					if (Savage2GameServer.getCurrentTeam() == Savage2GameServer.JOIN_HUMANS)
						mainID = Savage2GameServer.getStrongholdID();
					else
						mainID = Savage2GameServer.getLairID();
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
		System.out.println("Recevied allchatmessage: " + msg);
		if(msg.contentEquals("camrose")) {
			sendVCCommand(111);
			//if (currentState.get() == State.SPAWNSCREEN) {
				// spawn test
				short spawnID = Savage2GameServer.getPortalId();
				System.out.println("Sending spawn at "
						+ String.format("%04X", spawnID) + " request...");
				sendSpawnRequest(spawnID);
			//}
		}
	}

	@Override
	public void onStateChange(State state) {
		if(state != State.ONFIELD) {
			// just to keep the 5b packets coming - experimental
			//sendAction((byte)0, (byte)0);
			if(state == State.INSPEC) {
				// join team test
				System.out.println("Sending join team request...");
				sendJoinTeamRequest(Savage2GameServer.JOIN_BEASTS);
				// select character
				System.out.println("Sending select character request...");
				sendSelectCharacterRequest(Savage2GameServer.SAVAGE);
			}

		} else {
			if(setupOnField) {
				// Savage2GameServer.setOrientationByte("0xB0");
				Savage2GameServer
						.setTestPacketCounter(Savage2GameServer.pkt5dval1 + 0x600);
				System.out.println("Entering test packet request loop...");
				setupOnField = false;
			}
			testAction = Savage2GameServer.MOVE_FORWARD * 2;
			byte action = (byte)9;//(byte)(pActionList % 2);//(byte)actionList[pActionList];
			byte ability = 0;//(byte)(actionList[pActionList] >> 8);
			pActionList = (pActionList + 1) % actionList.length;
			setAction(ability, action);
		}
	}
	
	//portal spawn did not work on..
	// moonlight ancientcities autumn bunker crossroads desolation duskwood eden hellpeak
	// hiddenvillage kunlunpass losthills mirakar...havent test others

}
