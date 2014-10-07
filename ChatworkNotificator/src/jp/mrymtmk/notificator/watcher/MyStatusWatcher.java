package jp.mrymtmk.notificator.watcher;

import jp.mrymtmk.notificator.bean.ChatworkStatus;
import jp.mrymtmk.notificator.event.ReceiveEvent;
import jp.mrymtmk.notificator.event.listener.ReceiveEventListener;
import jp.mrymtmk.notificator.util.PropertyUtil;

public class MyStatusWatcher extends ChatworkWatcher<ChatworkStatus> {

	/**
	 * コンストラクタ
	 * @param listner イベントリスナ
	 */
	public MyStatusWatcher(ReceiveEventListener<ChatworkStatus> listner) {
		super(listner);
	}

	@Override
	public void run() {
		ReceiveEvent<ChatworkStatus> e = new ReceiveEvent<>(this, this.doGet(), ChatworkStatus.class);

		ChatworkStatus info = e.getInfo();
		if (info.failed()) {
			this.fireErrorevent(e);
			return;
		}

		this.fireReceiveEvent(e);
	}

	@Override
	protected String getURI() {
		return PropertyUtil.getProperty("endpoint.my.status");
	}

}
