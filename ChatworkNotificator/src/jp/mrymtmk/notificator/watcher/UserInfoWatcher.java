package jp.mrymtmk.notificator.watcher;

import jp.mrymtmk.notificator.bean.ChatworkUserInfo;
import jp.mrymtmk.notificator.event.ReceiveEvent;
import jp.mrymtmk.notificator.event.listener.ReceiveEventListener;
import jp.mrymtmk.notificator.util.PropertyUtil;

public class UserInfoWatcher extends ChatworkWatcher<ChatworkUserInfo> {

	/**
	 * コンストラクタ
	 * @param listner イベントリスナ
	 */
	public UserInfoWatcher(ReceiveEventListener<ChatworkUserInfo> listner) {
		super(listner);
	}

	@Override
	public void run() {
		ReceiveEvent<ChatworkUserInfo> e = new ReceiveEvent<>(this, this.doGet(), ChatworkUserInfo.class);
		if (e.getInfo().failed()) {
			this.fireErrorevent(e);
			return;
		}
		this.fireReceiveEvent(e);
	}

	@Override
	protected String getURI() {
		return PropertyUtil.getProperty("endpoint.me");
	}

}
