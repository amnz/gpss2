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
package jp.wda.g2;

import java.nio.channels.SocketChannel;

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
public interface Connection {

	/**
	 * 
	 * @return
	 */
	public SocketReactor getReactor();
	
	/**
	 * 
	 * @return
	 */
	public SocketChannel getSocketChannel();
	
	/**
	 * ���̃\�P�b�g���g�p���Ă���N���C�A���g�Ƀ��b�Z�[�W�𑗂�܂��B<BR>
	 * ���b�Z�[�W��null����"\0"���I�[�Ƃ��܂��B<BR>
	 *
	 * @param message �N���C�A���g�ɑ��郁�b�Z�[�W������
	 * @return ���M�ɐ��������ꍇ�͐^�A���s�����ꍇ�͋U
	 */
	public boolean send(String message);
	
	/**
	 * 
	 */
	public void closeConnection();
	
}
