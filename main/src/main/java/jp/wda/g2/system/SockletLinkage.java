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
package jp.wda.g2.system;

import java.nio.ByteBuffer;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.Socklet;
import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.exception.SockletNotFoundException;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 15:01:44 ���� </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class SockletLinkage {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * 
	 */
	public SockletLinkage(SocketProcessor client, SockletContainer container) throws GPSSException {
		super();
		
		this.client = client;
		setContainer(container);
	}

	// �����t�B�[���h��` ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** XXX */
	private SocketProcessor client = null;

	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private SockletContainer container = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public SockletContainer getContainer(){ return container; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param container �ݒ�l<BR>
	 */
	private void setContainer(SockletContainer container) throws GPSSException{
		if(container == null || container.getSocklet() == null){ throw new SockletNotFoundException(""); }
		
		this.container = container;
		container.accept(client, this);
	}
	public Socklet getSocklet(){
		if(container == null){ return null; }
		return container.getSocklet();
	}

	/* ***********************************************************************>> */
	/** XXX */
	private SockletLinkage next = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public SockletLinkage getNext(){ return next; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	private void setNext(SockletLinkage s){
		this.next = s;
	}
	
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param container �ݒ�l<BR>
	 * @param removeChildren 
	 */
	public void setNextSocklet(SockletContainer container, boolean removeChildren) throws GPSSException{
		if(container.getMainReactor() == null){
			container.setMainReactor(this.container.getMainReactor());
		}
		SockletLinkage next = new SockletLinkage(client, container);
		
		SockletLinkage old = this.next;
		this.next = next;
		
		if(old == null){ return; }
		
		old.removeClient(removeChildren);
		if(old.hasNext() && !removeChildren){
			next.setNext(old.getNext());
		}
	}
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public boolean hasNext(){ return next != null; }

	/* ***********************************************************************>> */
	/** XXX */
	private Object attributes = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public Object getAttributes(){ return attributes; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	public void setAttributes(Object s){ attributes = s; }
	
	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param command 
	 */
	public Object doCommand(ByteBuffer command) throws GPSSException{
		if(this.container == null){ return null; }
		
		return container.doCommand(client, command, this);
	}
	
	/**
	 * 
	 * @param removeChildren 
	 */
	void removeClient(boolean removeChildren){
		container.desert(client, this);
		
		if(removeChildren && hasNext()){ next.removeClient(removeChildren); }
	}

	// ���b�Z�[�W���M���\�b�h ///////////////////////////////////////////////////////////
	//                                                    Methods for Sending messages //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * �w�肳�ꂽ�N���C�A���g�ꗗ�Ɍ����āA���b�Z�[�W�𑗐M���܂��B<BR>
	 * 
	 * @param sender ���b�Z�[�W�𑗐M���Ă����N���C�A���g
	 * @param clients ���M����N���C�A���g�ꗗ
	 * @param message ���M���郁�b�Z�[�W
	 */
	private void sendToClients(SocketProcessor[] clients, String message) {
		for(int i = 0; i < clients.length; i++){
			if (!clients[i].send(message)) {
				clients[i].terminate();
				continue;
			}
		}
	}

	/**
	 * ���݂���Socklet�ɐڑ����̑S�ẴN���C�A���g�Ɍ����āA���b�Z�[�W�𑗐M���܂��B<BR>
	 * ���̃��\�b�h�́A�w�肳�ꂽString����ɉ��H���邱�ƂȂ����M���܂����A
	 * Flash XMLSocket�̋K��ɑ���A�����񖖔��͕K��\0�Ƃ��đ��M���܂��̂ŁA
	 * Socklet����҂͓��ɂ�����ӎ�����K�v�͂���܂���B
	 * 
	 * @param message ���M���郁�b�Z�[�W
	 */
	public final void sendToAllClients(String message) {
		sendToClients(container.getAllClients(), message);
	}

}
