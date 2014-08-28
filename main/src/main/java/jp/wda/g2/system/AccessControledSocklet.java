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

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.security.AccessController;
import jp.wda.g2.security.ClientChecker;
import jp.wda.g2.security.IPAddressChecker;

/**
 *
 *
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 *
 * <dt> 2.0.0-a1 </dt><dd> 2006/04/01 13:39:59 導入 </dd>
 *
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 *
 * @author		A M O I
 */
public abstract class AccessControledSocklet extends AbstractSocklet {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 *
	 */
	public AccessControledSocklet() {
		super();
	}

	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/** システムアクセスコントローラー */
	private AccessController securityController = new AccessController();

	/**
	 *
	 * @param checker
	 */
	public void registerClientChecker(ClientChecker checker){
		securityController.register(checker);
	}

	/**
	 *
	 * @param ipaddr
	 */
	public void acceptFrom(String ipaddr){
		if(ipaddr == null || ipaddr.length() == 0){ return; }
		securityController.register(
				new IPAddressChecker(
						ipaddr,
						ClientChecker.ACCEPT,
						ClientChecker.CHAIN));
	}

	/**
	 *
	 * @param ipaddr
	 */
	public void rejectFrom(String ipaddr){
		if(ipaddr == null || ipaddr.length() == 0){ return; }
		securityController.register(
				new IPAddressChecker(
						ipaddr,
						ClientChecker.REJECT,
						ClientChecker.CHAIN));
	}

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/** {@inheritDoc} */
	public final boolean checkConnection(SocketProcessor client, SockletLinkage linkage){
		if (!securityController.enter(client)) {
			syslog.error("Connection Rejected..." + client.getIPAddress());
			securityController.exit(client);
			return denied(client, linkage);
		}
		return accept(client, linkage);
	}

	/** {@inheritDoc} */
	public final void preRemoveClient(SocketProcessor client, SockletLinkage linkage){
		securityController.exit(client);
		desert(client, linkage);
	}

	/**
	 *
	 * @param client
	 * @param linkage
	 * @return
	 */
	public abstract boolean accept(SocketProcessor client, SockletLinkage linkage);

	/**
	 *
	 * @param client
	 * @param linkage
	 * @return
	 */
	public abstract boolean denied(SocketProcessor client, SockletLinkage linkage);

	/**
	 *
	 * @param client
	 * @param linkage
	 */
	public abstract void desert(SocketProcessor client, SockletLinkage linkage);

}
