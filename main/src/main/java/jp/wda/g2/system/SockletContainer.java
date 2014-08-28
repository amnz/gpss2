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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.SocketReactor;
import jp.wda.g2.Socklet;
import jp.wda.g2.exception.AccessDeniedException;
import jp.wda.g2.exception.GPSSException;

/**
 *
 *
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 *
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/27 3:12:47 ���� </dd>
 *
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 *
 * @author		A M O I
 */
public class SockletContainer {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 *
	 */
	public SockletContainer(String name, Socklet socklet) {
		super();

		this.name    = name;
		this.socklet = socklet;
	}

	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private String name = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public String getName(){ return name; }

	/* ***********************************************************************>> */
	/** XXX */
	private Socklet socklet = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public Socklet getSocklet(){ return socklet; }

	/* ***********************************************************************>> */
	/** XXX */
	private SocketReactor mainReactor = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	SocketReactor getMainReactor(){ return mainReactor; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	void setMainReactor(SocketReactor s){ mainReactor = s; }

	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**  */
	private List<SocketProcessor> clients = Collections.synchronizedList(new ArrayList<SocketProcessor>());

	/**
	 *
	 * @param client
	 * @param linkage
	 * @throws GPSSException
	 */
	synchronized void accept(SocketProcessor client, SockletLinkage linkage) throws GPSSException{
		if(!socklet.checkConnection(client, linkage)){
			throw new AccessDeniedException();
		}
		clients.add(client);

		mainReactor.notifyAcceptance(this, client);
	}

	/**
	 *
	 * @param client
	 * @param linkage
	 */
	synchronized void desert(SocketProcessor client, SockletLinkage linkage){
		socklet.preRemoveClient(client, linkage);
		clients.remove(client);

		mainReactor.notifyDesertion(this, client);
	}

	/**
	 *
	 * @return
	 */
	public SocketProcessor[] getAllClients(){
		return clients.toArray(new SocketProcessor[0]);
	}

	/**
	 *
	 * @param client
	 * @param command
	 * @param linkage
	 * @return
	 * @throws GPSSException
	 */
	Object doCommand(SocketProcessor client, ByteBuffer command, SockletLinkage linkage) throws GPSSException{
		return socklet.doCommand(client, command, linkage);
	}

}
