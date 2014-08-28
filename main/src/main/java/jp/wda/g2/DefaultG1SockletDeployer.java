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

import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.system.AbstractSockletDeployer;
import jp.wda.g2.system.SockletContainer;
import jp.wda.g2.system.SockletLinkage;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/05/02 20:03:53 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class DefaultG1SockletDeployer extends AbstractSockletDeployer {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public DefaultG1SockletDeployer() {
		super();
	}
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/** {@inheritDoc} */
	public Object select(SocketProcessor client, String command, SockletLinkage linkage) throws GPSSException{
		String[] commands = command.split(":");
		if(commands == null || commands.length == 0){ return null; }
		
		ConnectingConditions condition = new ConnectingConditions();
		if(commands.length > 1 && commands[1].length() > 0){ condition.setUsername(commands[1]); }
		if(commands.length > 2 && commands[2].length() > 0){ condition.setPassword(commands[2]); }
		if(commands.length > 3 && commands[3].length() > 0){ condition.setInitialParameters(commands[3]); }
		
		client.setConnectingConditions(condition);
		
		String encoding = condition.getParameter("encoding");
		if(encoding != null && encoding.length() > 0){ client.setEncoding(encoding); }
		
		SockletContainer container = getChild(commands[0]);
		linkage.setNextSocklet(container, false);
		
		return null;
	}

}
