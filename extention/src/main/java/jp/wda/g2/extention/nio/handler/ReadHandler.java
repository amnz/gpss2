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
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 *
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/23 16:00:00 ���� </dd>
 *
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 *
 * @author		A M O I
 */
public class ReadHandler implements Runnable, Connection {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 *
	 */
	public ReadHandler(SocketChannel channel, SocketReactor reactor) {
		super();

		this.channel = channel;
		this.reactor = reactor;

		this.buffer        = ByteBuffer.allocateDirect(BUFFER_SIZE);
		this.messageBuffer = new ByteBufferList(BUFFER_SIZE);
	}

	// �����t�B�[���h��` ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** ���K�[ */
	private final Logger logger = Logger.getLogger(GPSSConstants.SYSTEMLOG_CATEGORY);

	/**  */
	private SelectionKey key;
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param channel �ݒ�l<BR>
	 */
	void register(Selector selector) throws IOException{
		this.key = channel.register(selector, SelectionKey.OP_READ);

		recordActive();
		this.key.attach(this);
	}

	/**
	 * ���b�Z�[�W�o�b�t�@
	 */
	private ByteBuffer buffer;
	private ByteBufferList messageBuffer;
	private static final int BUFFER_SIZE = 1024;

	/**
	 * �ŏI��������
	 */
	private long lastAct;
	/**
	 * �ŏI�����������L�^
	 */
	private void recordActive() {
		lastAct = System.currentTimeMillis();
	}

	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private SocketReactor reactor = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public SocketReactor getReactor(){ return reactor; }

	/* ***********************************************************************>> */
	/** XXX */
	private SocketChannel channel;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public SocketChannel getSocketChannel(){ return channel; }

	/* ***********************************************************************>> */
	/** XXX */
	private SocketProcessor client;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public SocketProcessor getClient(){ return client; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	public void setClient(SocketProcessor s){ client = s; }

	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	public void run() {
		int length = -1;
		Throwable th = null;

		try{
			length = readChannel();
		} catch (ClosedChannelException e) {
			logger.error("�\�P�b�g�͊��ɃN���[�Y����Ă��܂��B", e);
			th = e;
			return;
		} catch (IOException ioe) {
			logger.error("���̓G���[���������܂����B", ioe);
			th = ioe;
			return;
		} catch (Throwable e) {
			logger.error("�G���[���������܂����B", e);
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

	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/** {@inheritDoc} */
	public boolean send(String message){
		if(client.isTerminated()){ return false; }
		if(this.channel == null){
			logger.error("���C�^��r�����܂����B" + client.getClientID());
			client.terminate();
			return false;
		}

		ByteBuffer buffer = Charset.forName(client.getEncoding()).encode(message + "\0");
		try {
			channel.write(buffer);
			return true;
		} catch (Exception e) {
			logger.error("���M�Ɏ��s���܂����B" + client.getClientID() + "\n" + message, e);
			client.terminate();
			return false;
		}
	}

	/**
	 * �`�����l�������
	 */
	public void closeConnection() {
		if (channel != null) {
			try {
				channel.finishConnect();
				channel.close();
				key.cancel();
			} catch (Throwable e) {
				logger.error("�`�����l���I���������ɗ�O���������܂����B�F" + client.getClientID(), e);
			}
		}
		channel = null;
	}

	/**
	 * �^�C���A�E�g���Ă���Ȃ�I��
	 */
	public void terminateIfInactive() {
		if (isInactive() && !client.isTerminated()) {
			client.terminate("Connection timeout..");
		}
	}

	/**
	 * �^�C���A�E�g���Ă��邩�ǂ����`�F�b�N
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


	// �����N���X ///////////////////////////////////////////////////////////////////////
	//                                                                     Inner Class //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * ���b�Z�[�W�o�b�t�@�p�̓����N���X
	 */
	private class ByteBufferList {
		private ArrayList<ByteBuffer> list;
		private ByteBuffer current;
		private int bufferLength;

		/**
		 * �R���X�g���N�^
		 * @param length
		 */
		public ByteBufferList(int length) {
			list = new ArrayList<ByteBuffer>();
			bufferLength = length;
			current = null;
		}
		/**
		 * �o�b�t�@��1�o�C�g�ǉ���������
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
		 * �o�b�t�@�Ɉꊇ�ǉ���������
		 * @param bb
		 */
		@SuppressWarnings("unused")
		public void put(ByteBuffer bb) {
			while (bb.hasRemaining()) {
				put(bb.get());
			}
		}
		/**
		 * ���e���o�C�g�o�b�t�@��
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
		 * �o�b�t�@�̃N���A
		 */
		public void clear() {
			list.clear();
			current = null;
		}
	}

}
