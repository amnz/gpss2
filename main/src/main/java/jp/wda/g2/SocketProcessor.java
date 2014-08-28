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
public interface SocketProcessor {

	// properties /////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * ���̃\�P�b�g���g�p���Ă���N���C�A���g�̃N���C�A���gID���擾���܂��B<BR>
	 * �N���C�A���gID�́A�ڑ����̑S�N���C�A���g�ɑ΂��A��ӂȕ�����ƂȂ�܂��B
	 * 
	 * @return �N���C�A���gID
	 */
	public String getClientID();
	
	/**
	 * ���̃\�P�b�g�̎g�p����G���R�[�f�B���O�����擾���܂��B<BR>
	 * �G���R�[�f�B���O���́A�����ݒ�t�@�C���Ŏw�肳��܂��B
	 * 
	 * @return �G���R�[�f�B���O��
	 */
	public String getEncoding();
	/**
	 * 
	 * @param s
	 */
	public void setEncoding(String s);
	
	/**
	 * ���̃\�P�b�g���g�p���Ă���N���C�A���g��IP�A�h���X���擾���܂��B<BR>
	 * 
	 * @return IP�A�h���X
	 */
	public String getIPAddress();
	
	/**
	 * �^�C���A�E�g����~���b�����擾���܂��B
	 * 
	 * @return �^�C���A�E�g����~���b��
	 */
	public long getTimeout();
	/**
	 * �^�C���A�E�g����~���b����ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	public void setTimeout(long s);
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public SockletLinkage getLinkage(SockletContainer container);
//	/**
//	 * 
//	 * @param socklet
//	 * @return
//	 */
//	public Object getSockletAttributes(SockletContainer container);
//	/**
//	 * 
//	 * @param socklet
//	 * @param attribute
//	 */
//	public void setSockletAttributes(SockletContainer container, Object attribute);
	
	/**
	 * 
	 * @return
	 */
	public Object getAttributes();
	/**
	 * 
	 * @param s
	 */
	public void setAttributes(Object s);
	
	/**
	 * 
	 * @return
	 */
	public ConnectingConditions getConnectingConditions();
	/**
	 * 
	 * @param s
	 */
	public void setConnectingConditions(ConnectingConditions s);
	
	// methods ////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param command
	 */
	public void doCommand(ByteBuffer command);
	
	/**
	 * ���̃\�P�b�g���g�p���Ă���N���C�A���g�Ƀ��b�Z�[�W�𑗂�܂��B<BR>
	 * ���b�Z�[�W��null����"\0"���I�[�Ƃ��܂��B<BR>
	 *
	 * @param message �N���C�A���g�ɑ��郁�b�Z�[�W������
	 * @return ���M�ɐ��������ꍇ�͐^�A���s�����ꍇ�͋U
	 */
	public boolean send(String message);
	
	/**
	 * ���̃\�P�b�g���g�p���Ă���N���C�A���g�������I�ɏI�������܂��B
	 */
	public void terminate();
	/**
	 * ���̃\�P�b�g���g�p���Ă���N���C�A���g�ɏI�����b�Z�[�W���o��A�\�P�b�g�������I�ɏI�������܂��B
	 * @param message �I�����b�Z�[�W
	 */
	public void terminate(String message);
	/**
	 * ���̃\�P�b�g���g�p���Ă���N���C�A���g�����ɏI�����Ă��邩���m�F���܂��B
	 * @return ���ɏI�����Ă���ꍇ�͐^
	 */
	public boolean isTerminated();
	
}
