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

import java.nio.ByteBuffer;

import jp.wda.g2.SocketProcessor;

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
public class CommandHandler implements Runnable {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * 
	 */
	public CommandHandler(SocketProcessor client, ByteBuffer buffer) {
		super();
		
		this.client   = client;
		this.buffer   = buffer;
	}

	// �����t�B�[���h��` ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

//	/** ���K�[ */
//	private final Logger logger = Logger.getLogger(this.getClass());
	/**  */
	private SocketProcessor client;
	
	/**
	 * ���b�Z�[�W�o�b�t�@
	 */
	private ByteBuffer buffer;
	
	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////
	
	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	public void run() {
		if (client.isTerminated()) { return; }
		
		synchronized(client){
			client.doCommand(buffer);
		}
	}
}
