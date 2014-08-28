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
package jp.wda.g2.extention.nio;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

import jp.wda.g2.extention.nio.handler.ReadHandler;
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
public class SleeperSweeper implements Runnable {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * 
	 */
	public SleeperSweeper(Selector selector) {
		super();
		
		this.selector = selector;
	}

	// �����t�B�[���h��` ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** ���K�[ */
	private final Logger logger = Logger.getLogger(this.getClass());
	
	/**  */
	private Selector selector;
	
	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * ����I�ɃN���C�A���g�̃^�C���A�E�g�����o���ؒf����
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run(){
		Iterator<SelectionKey> keys;
		try {
			keys = selector.keys().iterator();
		}catch (Throwable e){
			logger.error("SweeperError create list", e);
			return;
		}
		
		while(keys.hasNext()){
			Object obj = keys.next().attachment();
			if (obj == null){
				logger.debug("SweeperError key has no attachment");
				continue;
			}
			if (!(obj instanceof ReadHandler)){ continue; }
			
			try{
				((ReadHandler)obj).terminateIfInactive();
			} catch (Throwable e) {
				logger.error("SweeperError sweep clients", e);
			}
		}
		keys = null;
	}

}
