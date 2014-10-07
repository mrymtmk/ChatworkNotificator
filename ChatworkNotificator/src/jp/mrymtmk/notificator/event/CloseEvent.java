package jp.mrymtmk.notificator.event;

public class CloseEvent {

	private Object source;

	/**
	 * クローズイベント
	 * @param source 発生元
	 */
	public CloseEvent(Object source) {
		this.source = source;
	}

	/**
	 * @return source
	 */
	public Object getSource() {
		return source;
	}
}
