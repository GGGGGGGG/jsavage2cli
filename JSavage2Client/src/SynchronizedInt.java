
	public class SynchronizedInt {
		private int c = 0;
		boolean updated = false;

		public synchronized void set(int val) {
			c = val;
			updated = true;
		}

		public synchronized int get() {
			updated = false;
			return c;
		}

		public synchronized boolean wasUpdated() {
			return updated;
		}
	}
