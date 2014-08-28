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
import java.io.InterruptedIOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.SocketReactor;
import jp.wda.g2.exception.GPSSException;
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
public class AcceptHandler implements Runnable {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * 
	 */
	public AcceptHandler(SelectionKey key, SocketReactor reactor) {
		super();
		
		this.key     = key;
		this.reactor = reactor;
	}

	// �����t�B�[���h��` ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** ���K�[ */
	private final Logger logger = Logger.getLogger(GPSSConstants.SYSTEMLOG_CATEGORY);
	
	/**  */
	private SelectionKey key;
	/**  */
	private SocketReactor reactor;
	
	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �N���C�A���g�����I�u�W�F�N�g���쐬���܂��B
	 */
	public void run() {
		SocketChannel channel = null;
		Throwable     th      = null;
		try{
			ServerSocketChannel server = (ServerSocketChannel)key.channel();
			channel = server.accept();
			if (channel == null) { throw new IOException("channel is null."); }
			
			channel.configureBlocking(false);
			createReadHandler(channel);
		} catch (GPSSException e) {
			logger.errormessage(e);
			th = e;
			return;
		} catch (InterruptedIOException e) {
			logger.error("�^�C���A�E�g�ݒ�Ɏ��s���܂����B", e);
			th = e;
			return;
		} catch (IOException e) {
			logger.error("�ڑ����ɗ�O���������܂����B", e);
			th = e;
			return;
		} catch (Exception e) {
			logger.error("�ڑ����ɗ�O���������܂����B", e);
			th = e;
			return;
		} finally {
			if(th != null){
				if(channel != null){ try{ channel.close(); }catch(IOException ex){ ; } }
			}
		}
	}
	
	private void createReadHandler(SocketChannel channel) throws IOException, GPSSException{
		ReadHandler     connection = new ReadHandler(channel, reactor);
		SocketProcessor client     = reactor.accept(connection);
		
		connection.setClient(client);
		connection.register(key.selector());
		
		logger.info("�ڑ�����܂����B(ID:" + client.getClientID() + ")");
	}

}
