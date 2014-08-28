/* *****************************************************************************
 *
 * Copyright(C) The GPSS Project Team and the Others. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 * ***************************************************************************** */
package jp.wda.g2.extention.nio.handler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import jp.wda.g2.Connection;
import jp.wda.g2.SocketProcessor;
import jp.wda.g2.SocketReactor;
import jp.wda.g2.system.GPSSConstants;
import jp.wda.gpss.util.Logger;

/**
 *
 *
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 *
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/23 16:00:00 導入 </dd>
 *
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 *
 * @author		A M O I
 */
public class ReadHandler implements Runnable, Connection {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 *
	 */
	public ReadHandler(SocketChannel channel, SocketReactor reactor) {
		super();

		this.channel = channel;
		this.reactor = reactor;

		this.buffer        = ByteBuffer.allocateDirect(BUFFER_SIZE);
		this.messageBuffer = new ByteBufferList(BUFFER_SIZE);
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** ロガー */
	private final Logger logger = Logger.getLogger(GPSSConstants.SYSTEMLOG_CATEGORY);

	/**  */
	private SelectionKey key;
	/**
	 * XXXを設定します。<BR>
	 * @param channel 設定値<BR>
	 */
	void register(Selector selector) throws IOException{
		this.key = channel.register(selector, SelectionKey.OP_READ);

		recordActive();
		this.key.attach(this);
	}

	/**
	 * メッセージバッファ
	 */
	private ByteBuffer buffer;
	private ByteBufferList messageBuffer;
	private static final int BUFFER_SIZE = 1024;

	/**
	 * 最終活動時間
	 */
	private long lastAct;
	/**
	 * 最終活動時刻を記録
	 */
	private void recordActive() {
		lastAct = System.currentTimeMillis();
	}

	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private SocketReactor reactor = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SocketReactor getReactor(){ return reactor; }

	/* ***********************************************************************>> */
	/** XXX */
	private SocketChannel channel;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SocketChannel getSocketChannel(){ return channel; }

	/* ***********************************************************************>> */
	/** XXX */
	private SocketProcessor client;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SocketProcessor getClient(){ return client; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setClient(SocketProcessor s){ client = s; }

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	public void run() {
		int length = -1;
		Throwable th = null;

		try{
			length = readChannel();
		} catch (ClosedChannelException e) {
			logger.error("ソケットは既にクローズされています。", e);
			th = e;
			return;
		} catch (IOException ioe) {
			logger.error("入力エラーが発生しました。", ioe);
			th = ioe;
			return;
		} catch (Throwable e) {
			logger.error("エラーが発生しました。", e);
			th = e;
			return;
		}finally{
			if((length < 0 || th != null) && !client.isTerminated()){ client.terminate(); }
		}
	}

	private int readChannel() throws IOException{
		int length = -1;

		buffer.clear();
		while (!client.isTerminated() && (length = channel.read(buffer)) > 0) {
			recordActive();
			buffer.flip();

			while (buffer.hasRemaining()) {
				byte b = buffer.get();
				if (b == 0) {
					CommandHandler command = new CommandHandler(client, messageBuffer.toByteBuffer());
					reactor.execute(command);

					messageBuffer.clear();
					continue;
				}

				messageBuffer.put(b);
			}
			buffer.clear();
		}

		return length;
	}

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/** {@inheritDoc} */
	public boolean send(String message){
		if(client.isTerminated()){ return false; }
		if(this.channel == null){
			logger.error("ライタを喪失しました。" + client.getClientID());
			client.terminate();
			return false;
		}

		ByteBuffer buffer = Charset.forName(client.getEncoding()).encode(message + "\0");
		try {
			channel.write(buffer);
			return true;
		} catch (Exception e) {
			logger.error("送信に失敗しました。" + client.getClientID() + "\n" + message, e);
			client.terminate();
			return false;
		}
	}

	/**
	 * チャンネルを閉じる
	 */
	public void closeConnection() {
		if (channel != null) {
			try {
				channel.finishConnect();
				channel.close();
				key.cancel();
			} catch (Throwable e) {
				logger.error("チャンネル終了処理中に例外が発生しました。：" + client.getClientID(), e);
			}
		}
		channel = null;
	}

	/**
	 * タイムアウトしているなら終了
	 */
	public void terminateIfInactive() {
		if (isInactive() && !client.isTerminated()) {
			client.terminate("Connection timeout..");
		}
	}

	/**
	 * タイムアウトしているかどうかチェック
	 * @return
	 */
	public boolean isInactive() {
		if(client.isTerminated()){ return false; }

		long timeout = client.getTimeout();
		if (timeout <= 0) {
			return false;
		}
		return System.currentTimeMillis() > timeout + lastAct;
	}


	// 内部クラス ///////////////////////////////////////////////////////////////////////
	//                                                                     Inner Class //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * メッセージバッファ用の内部クラス
	 */
	private class ByteBufferList {
		private ArrayList<ByteBuffer> list;
		private ByteBuffer current;
		private int bufferLength;

		/**
		 * コンストラクタ
		 * @param length
		 */
		public ByteBufferList(int length) {
			list = new ArrayList<ByteBuffer>();
			bufferLength = length;
			current = null;
		}
		/**
		 * バッファに1バイト追加書き込み
		 * @param b
		 */
		public void put(byte b) {
			if (current == null || current.position() == current.limit()) {
				current = ByteBuffer.allocate(bufferLength);
				list.add(current);
			}
			current.put(b);
		}
		/**
		 * バッファに一括追加書き込み
		 * @param bb
		 */
		@SuppressWarnings("unused")
		public void put(ByteBuffer bb) {
			while (bb.hasRemaining()) {
				put(bb.get());
			}
		}
		/**
		 * 内容をバイトバッファ化
		 * @return
		 */
		public ByteBuffer toByteBuffer() {
			ByteBuffer buf = ByteBuffer.allocate(bufferLength * list.size());
			Iterator<ByteBuffer> itr = list.iterator();
			while (itr.hasNext()) {
				ByteBuffer tmpBuf = itr.next();
				tmpBuf.flip();
				buf.put(tmpBuf);
			}
			buf.flip();
			return buf;
		}
		/**
		 * バッファのクリア
		 */
		public void clear() {
			list.clear();
			current = null;
		}
	}

}
