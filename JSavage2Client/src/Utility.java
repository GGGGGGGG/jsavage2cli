import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class Utility {
	public static String http_get_contents(String host, String req) {
		String res = "", sentence;
		try {
			Socket socket = new Socket(host, 80);
			DataOutputStream outToMasterServer = new DataOutputStream(
					socket.getOutputStream());
			BufferedReader inFromMasterServer = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			outToMasterServer.writeBytes(req);
			while ((sentence = inFromMasterServer.readLine()) != null) {
				res += sentence;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static void printBytes(byte[] req) {
		for (int i = 0; i < req.length; ++i) {
			System.out.printf("%s ",
					Integer.toHexString((char) (0x0FF & req[i])));
		}
		System.out.println();
	}

	public static void printBytes(byte[] req, int end) {
		for (int i = 0; i < req.length && i < end; ++i) {
			System.out.printf("%s ",
					Integer.toHexString((char) (0x0FF & req[i])));
		}
		System.out.println();
	}

	public void printASCIIBytes(byte[] req) {
		for (int i = 0; i < req.length; ++i) {
			if (Character.isLetterOrDigit(req[i]))
				System.out.print((char) req[i]);
			else
				System.out.print(".");
		}
		System.out.println();
	}

	public void printASCIIBytes(byte[] req, int end) {
		for (int i = 0; i < req.length && i < end; ++i) {
			if (Character.isLetterOrDigit(req[i]))
				System.out.print((char) req[i]);
			else
				System.out.print(".");
		}
		System.out.println();
	}

	public static void dumpBytes(byte[] req) {
		for (int i = 0; i < req.length; ++i) {
			if (i % 16 == 0)
				System.out.print("\n" + String.format("%04X", i));
			System.out.print(String.format(" %02X", (0x0FF & req[i])));
			//System.out.printf(" %s",
			//		Integer.toHexString((char) (0x0FF & req[i])));
			if ((i + 1) % 16 == 0) {
				System.out.print(" ");
				for (int j = i - 15; j <= i; ++j) {
					if (Character.isLetterOrDigit(req[j]))
						System.out.print((char) req[j]);
					else
						System.out.print(".");
				}
			}
		}
		System.out.println();
	}
	
	public static void dumpBytes(byte[] req, int end) {
		for (int i = 0; i < req.length && i < end; ++i) {
			if (i % 16 == 0)
				System.out.print("\n" + String.format("%04X", i));
			System.out.print(String.format(" %02X", (0x0FF & req[i])));
			//System.out.printf(" %s",
			//		Integer.toHexString((char) (0x0FF & req[i])));
			if ((i + 1) % 16 == 0 || i+1 == end) {
				System.out.print(" ");
				int j;
				j = i - 15;
				if(i - 15 < 0) {
					j = 0;
					i = end - 1;
				}
				for (; j <= i; ++j) {
					if (Character.isLetterOrDigit(req[j]))
						System.out.print((char) req[j]);
					else
						System.out.print(".");
				}
			}
		}
		System.out.println();
	}

	public static void copyIntToByteArray(int x, byte[] b) {
		if (b.length != 4)
			return;
		b[0] = (byte) x;
		b[1] = (byte) (x >> 8);
		b[2] = (byte) (x >> 16);
		b[3] = (byte) (x >> 24);
	}
	
	public static short getShort(byte[] b, int i) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(b[i]);
		bb.put(b[i+1]);
		return bb.getShort(0);
	}
	
	public static int getInt(byte[] b, int i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(b[i]);
		bb.put(b[i+1]);
		bb.put(b[i+2]);
		bb.put(b[i+3]);
		return bb.getInt(0);
	}
	
	public static float getFloat(byte[] b, int i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(b[i]);
		bb.put(b[i+1]);
		bb.put(b[i+2]);
		bb.put(b[i+3]);
		return bb.getFloat(0);
	}
	
	
	
	
	public static void foo(String str, int id) {
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
		
		System.out.println("bindimpulse game P Cmd \"setposition " + id + " "
		+ x + " " + y + " " + z + "\"");
	}
	
	//public static void foo2(String str) {}
}
