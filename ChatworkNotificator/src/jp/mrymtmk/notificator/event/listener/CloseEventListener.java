package jp.mrymtmk.notificator.event.listener;

import java.util.EventListener;

import jp.mrymtmk.notificator.event.CloseEvent;

public interface CloseEventListener extends EventListener {

	/**
	 * クローズイベント
	 *
	 * @param e イベント
	 */
	public void onClose(CloseEvent e);
}
