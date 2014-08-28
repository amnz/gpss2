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
package jp.wda.g2.examples;

import java.io.Serializable;

import jp.wda.g2.GeneralSocklet;
import jp.wda.g2.SockletRequest;
import jp.wda.g2.exception.GPSSException;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 23:50:40 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class SimpleEcho extends GeneralSocklet implements Serializable {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public SimpleEcho() {
		super();
	}
	/**  */
	private static final long serialVersionUID = -2160154785940117178L;
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/** {@inheritDoc} */
	public boolean accept(SockletRequest request){
		request.send("Welcome!");
		return true;
	}

	/** {@inheritDoc} */
	public boolean denied(SockletRequest request){
		request.send("Good bye...");
		return false;
	}
	
	/** {@inheritDoc} */
	public void desert(SockletRequest request){
		request.send("Good bye...\r\n");
	}
	
	/** {@inheritDoc} */
	public void destroy(){
		
	}
	
	/** {@inheritDoc} */
	public Object doCommand(SockletRequest request) throws GPSSException{
		request.sendToAllClients(request.getCommand());
		
		syslog.debug("Echo!:" + request.getCommand());// + " ( id= " + client.getClientID());
		return null;
	}

}
