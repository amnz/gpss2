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
package jp.wda.g2.extention.socklet;

import java.io.Serializable;
import java.nio.ByteBuffer;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.Socklet;
import jp.wda.g2.SockletRequest;
import jp.wda.g2.exception.CommandNotFoundException;
import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.system.SockletLinkage;
import jp.wda.g2.util.SimpleXMLCreator;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/06/04 18:47:28 ���� </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class XMLCommandRequestImpl extends CommandRequestImpl implements XMLCommandRequest, Serializable {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * 
	 * @param client
	 * @param rawCommand
	 * @param linkage
	 */
	public XMLCommandRequestImpl(SocketProcessor client, ByteBuffer rawCommand, SockletLinkage linkage) {
		super(client, rawCommand, linkage);
	}

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * 
	 * @param request
	 */
	public XMLCommandRequestImpl(SockletRequest request) {
		super(request);
	}

	/**  */
	private static final long serialVersionUID = 744644202132758997L;
	
	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private SimpleXMLCreator xml = null;
	/** {@inheritDoc} */
	public SimpleXMLCreator getXml(){ return xml; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	void setXml(SimpleXMLCreator s){
		xml = s;
		setParams(new Object[]{ this, xml });
	}
	
	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/** {@inheritDoc} */
	public Object handoverRequest() throws GPSSException{
		if(!getLinkage().hasNext()){
			throw new CommandNotFoundException(getName());
		}
		
		SockletLinkage next = getLinkage().getNext();
		
		Socklet socklet = next.getSocklet();
		if(socklet instanceof IXmlCommandSocklet){
			setLinkage(next);
			return ((IXmlCommandSocklet)socklet).doCommandMethod(this);
		}
		
		return next.doCommand(getRawCommand());
	}

}
