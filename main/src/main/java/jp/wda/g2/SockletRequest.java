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

import java.nio.ByteBuffer;

import jp.wda.g2.exception.CommandNotFoundException;
import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.system.SockletContainer;
import jp.wda.g2.system.SockletLinkage;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/04/21 23:22:46 ���� </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public interface SockletRequest {

	/**
	 * 
	 * @return
	 */
	public ByteBuffer getRawCommand();
	
	/**
	 * 
	 * @return
	 */
	public String getCommand();
	
	/**
	 * 
	 * @return
	 */
	public SocketProcessor getClient();
	/**
	 * 
	 * @return
	 */
	public String getClientID();
	
	/**
	 * 
	 * @return
	 */
	public SockletLinkage getLinkage();
	
	/**
	 * 
	 * @return
	 */
	public Object getAttribute();
	
	/**
	 * 
	 * @param container
	 * @param removeChildren
	 * @throws GPSSException
	 */
	public void setNextSocklet(SockletContainer container, boolean removeChildren) throws GPSSException;
	
	/**
	 * 
	 * @return
	 * @throws CommandNotFoundException
	 * @throws GPSSException
	 */
	public Object handoverRequest() throws GPSSException;
	
	/**
	 * 
	 * @return
	 */
	public SocketProcessor[] getAllClients();
	
	/**
	 * 
	 * @param message
	 */
	public void send(String message);
	
	/**
	 * ���݂���Socklet�ɐڑ����̑S�ẴN���C�A���g�Ɍ����āA���b�Z�[�W�𑗐M���܂��B<BR>
	 * ���̃��\�b�h�́A�w�肳�ꂽString����ɉ��H���邱�ƂȂ����M���܂����A
	 * Flash XMLSocket�̋K��ɑ���A�����񖖔��͕K��\0�Ƃ��đ��M���܂��̂ŁA
	 * Socklet����҂͓��ɂ�����ӎ�����K�v�͂���܂���B
	 * 
	 * @param message ���M���郁�b�Z�[�W
	 */
	public void sendToAllClients(String message);

}
