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

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.SockletRequest;
import jp.wda.g2.exception.CommandNotFoundException;
import jp.wda.g2.exception.GPSSException;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/04/21 23:53:27 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class SockletRequestImpl implements SockletRequest, Serializable {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public SockletRequestImpl(SocketProcessor client, ByteBuffer rawCommand, SockletLinkage linkage) {
		super();
		
		this.client     = client;
		this.rawCommand = rawCommand;
		this.linkage    = linkage;
		
		if(rawCommand != null){
			rawCommand.rewind();
			this.command = Charset.forName(client.getEncoding()).decode(rawCommand).toString();
		}
	}

	/**
	 * 
	 * 
	 * @param request
	 */
	public SockletRequestImpl(SockletRequest request) {
		this(request.getClient(), request.getRawCommand(), request.getLinkage());
		setAttribute(request.getAttribute());
	}
	
	/**  */
	private static final long serialVersionUID = -3465530253660016083L;
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private ByteBuffer rawCommand = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public ByteBuffer getRawCommand(){ return rawCommand; }

	/* ***********************************************************************>> */
	/** XXX */
	private String command = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public String getCommand(){ return command; }

	/* ***********************************************************************>> */
	/** XXX */
	private SocketProcessor client = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SocketProcessor getClient(){ return client; }
	/**
	 * 
	 * @return
	 */
	public String getClientID(){ return client.getClientID(); }

	/* ***********************************************************************>> */
	/** XXX */
	private SockletLinkage linkage = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SockletLinkage getLinkage(){ return linkage; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	protected void setLinkage(SockletLinkage s){ linkage = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private Object attribute = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public Object getAttribute(){ return attribute; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setAttribute(Object s){ attribute = s; }
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/** {@inheritDoc} */
	public void setNextSocklet(SockletContainer container, boolean removeChildren) throws GPSSException{
		getLinkage().setNextSocklet(container, removeChildren);
	}
	
	/** {@inheritDoc} */
	public Object handoverRequest() throws GPSSException{
		if(!getLinkage().hasNext()){
			throw new CommandNotFoundException(command);
		}
		
		return getLinkage().getNext().doCommand(rawCommand);
	}

	/** {@inheritDoc} */
	public SocketProcessor[] getAllClients(){
		return getLinkage().getContainer().getAllClients();
	}
	
	/** {@inheritDoc} */
	public void sendToAllClients(String message){
		linkage.sendToAllClients(message);
	}

	/** {@inheritDoc} */
	public void send(String message){
		client.send(message);
	}

}
