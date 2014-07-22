import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Savage2Client {
	
	final static String MASTER_SERVER = "masterserver.savage2.s2games.com";
	
	public static void main(String[] args) {
//foo();
		
		new Savage2Login();
	}
	
	public static void foo() {
		String str = "DF C9 17 46 22 C6 B3 46 06 89 94 41";
		float x,y,z;
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		str = str.replaceAll(" ", "");	
		String xs = str.substring(0, 8);
		String ys = str.substring(8, 16);
		String zs = str.substring(16, 24);
		
		String q = xs;
		for(int i = 0; i < 8; i+=2) {
			int c = Integer.decode("0x" + q.substring(i, i+2));
			bb.put( (byte)c );
		}
		x = bb.getFloat(0);
		bb.rewind();
		q = ys;
		for(int i = 0; i < 8; i+=2) {
			int c = Integer.decode("0x" + q.substring(i, i+2));
			bb.put( (byte)c );
		}
		y = bb.getFloat(0);
		bb.rewind();
		q = zs;
		for(int i = 0; i < 8; i+=2) {
			int c = Integer.decode("0x" + q.substring(i, i+2));
			bb.put( (byte)c );
		}
		z = bb.getFloat(0);
		bb.rewind();
		
		System.out.println("bindimpulse game P Cmd \"setposition 614 " 
		+ x + " " + y + " " + z + "\"");
	}

}
