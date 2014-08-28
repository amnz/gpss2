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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.SockletDeployer;
import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.security.AllowDomain;

/**
 *
 *
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 *
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 19:43:09 導入 </dd>
 *
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 *
 * @author		A M O I
 */
public abstract class AbstractSockletDeployer extends AccessControledSocklet implements SockletDeployer {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 *
	 */
	public AbstractSockletDeployer() {
		super();
	}

	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private int port = 9090;
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setPort(int s){ port = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private String permittedCDP = "all";
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setPermittedCDP(String s){ permittedCDP = s; }
	/** デフォルトのCross Domain Policy取得用ポート */
	public static final int DEFALUT_CDP_PORT = 843;

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 使用を許可するドメインのリストです。
	 */
	private List<AllowDomain> allowDomains = new ArrayList<AllowDomain>();

	/** {@inheritDoc} */
	public void allowDomain(String domain){
		if(domain == null || domain.length() == 0){ return; }

		int colon = domain.indexOf(':');
		String ports = null;
		if(colon >= 0){
			ports  = domain.substring(colon + 1);
			domain = domain.substring(0, colon);
		}

		if(domain.length() == 0){
			domain = "*";
		}
		if(ports == null || ports.length() == 0){
			ports  = Integer.toString(port);
		}

		allowDomains.add(new AllowDomain(domain, ports));
	}

	/** {@inheritDoc} */
	public String getCrossDomainPolicy() {
		if(allowDomains.size() == 0){
			allowDomains.add(new AllowDomain("*", Integer.toString(port)));
		}

		StringBuilder result = new StringBuilder("<cross-domain-policy>");
		if(port == DEFALUT_CDP_PORT){
			result.append("<site-control permitted-cross-domain-policies=\"");
			result.append(permittedCDP);
			result.append("\"/>");
		}
		for(int i = 0; i < allowDomains.size(); i++){
			result.append(allowDomains.get(i));
		}
		result.append("</cross-domain-policy>");

		return result.toString();
	}

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/** {@inheritDoc} */
	public boolean accept(SocketProcessor client, SockletLinkage linkage){
		return true;
	}

	/** {@inheritDoc} */
	public boolean denied(SocketProcessor client, SockletLinkage linkage){
		return false;
	}

	/** {@inheritDoc} */
	public void desert(SocketProcessor client, SockletLinkage linkage){
	}

	/** {@inheritDoc} */
	public void destroy(){

	}

	/** {@inheritDoc} */
	public Object doCommand(SocketProcessor client, ByteBuffer command, SockletLinkage linkage) throws GPSSException{
		if(linkage.hasNext()){
			return linkage.getNext().doCommand(command);
		}

		String com = Charset.forName(client.getEncoding()).decode(command).toString();
		if(com == null || com.length() == 0){ return null; }

		// cross domein policy
		if(com.charAt(0) == '<'){
			client.terminate(getCrossDomainPolicy());
			return null;
		}

		return select(client, com, linkage);
	}

	/**
	 *
	 * @param client
	 * @param command
	 * @param linkage
	 * @return
	 * @throws GPSSException
	 */
	public abstract Object select(SocketProcessor client, String command, SockletLinkage linkage) throws GPSSException;

}
