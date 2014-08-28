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

import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.system.AccessControledSocklet;
import jp.wda.g2.system.SockletLinkage;
import jp.wda.g2.system.SockletRequestImpl;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/04/22 0:05:26 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public abstract class GeneralSocklet extends AccessControledSocklet {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public GeneralSocklet() {
		super();
	}
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/** {@inheritDoc} */
	public final boolean accept(SocketProcessor client, SockletLinkage linkage){
		return accept(new SockletRequestImpl(client, null, linkage));
	}
	/**
	 * 
	 * @param request
	 */
	public abstract boolean accept(SockletRequest request);
	
	/** {@inheritDoc} */
	public final boolean denied(SocketProcessor client, SockletLinkage linkage){
		return denied(new SockletRequestImpl(client, null, linkage));
	}
	/**
	 * 
	 * @param request
	 */
	public abstract boolean denied(SockletRequest request);
	
	/** {@inheritDoc} */
	public final void desert(SocketProcessor client, SockletLinkage linkage){
		desert(new SockletRequestImpl(client, null, linkage));
	}
	/**
	 * 
	 * @param request
	 */
	public abstract void desert(SockletRequest request);

	/** {@inheritDoc} */
	public Object doCommand(SocketProcessor client, ByteBuffer commandbuf, SockletLinkage linkage) throws GPSSException{
		SockletRequest request = new SockletRequestImpl(client, commandbuf, linkage);
		return doCommand(request);
	}
	/**
	 * 
	 * @param request
	 * @return
	 * @throws GPSSException
	 */
	public abstract Object doCommand(SockletRequest request) throws GPSSException;

}
