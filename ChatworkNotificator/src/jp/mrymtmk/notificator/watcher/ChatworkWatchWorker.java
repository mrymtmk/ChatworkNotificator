package jp.mrymtmk.notificator.watcher;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jp.mrymtmk.notificator.util.PropertyUtil;

public class ChatworkWatchWorker extends Thread {

	private final List<ChatworkWatcher<?>> watchers = new LinkedList<>();
	private volatile boolean stop = false;

	public ChatworkWatchWorker() {
		this.setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (!this.stop) {

				// 都度最新を取得
				Thread.sleep(PropertyUtil.getPropertyInt("sleep.interval.ms"));

				Iterator<ChatworkWatcher<?>> ite = watchers.iterator();
				while (ite.hasNext()) {
					ChatworkWatcher<?> runner = ite.next();
					runner.run();
				}
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public int count() {
		synchronized (watchers) {
			return watchers.size();
		}
	}

	public void entryWatcher(ChatworkWatcher<?> watcher) {
		synchronized (watchers) {
			watchers.add(watcher);
			watchers.notifyAll();
		}
	}

	public void tryStop() {
		this.stop = true;
		synchronized (watchers) {
			watchers.clear();
			watchers.notifyAll();
		}
		this.interrupt();
	}

	public boolean isTryStop() {
		return this.stop;
	}
}
