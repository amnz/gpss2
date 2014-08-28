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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import jp.wda.g2.Connection;
import jp.wda.g2.SocketProcessor;
import jp.wda.g2.SocketProcessorFactory;
import jp.wda.g2.SocketReactor;
import jp.wda.g2.Socklet;
import jp.wda.g2.SockletDeployer;
import jp.wda.g2.exception.GPSSException;
import jp.wda.gpss.util.Logger;


/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/23 16:00:00 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public abstract class AbstractSocketReactor extends Thread implements SocketReactor {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public AbstractSocketReactor() {
		super("GPSS2 Main");
	}

	/**
	 * 
	 * 
	 * @param threadName 
	 */
	public AbstractSocketReactor(String threadName) {
		super(threadName);
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** システムロガー */
	protected final Logger syslog = Logger.getLogger(GPSSConstants.SYSTEMLOG_CATEGORY);
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private SocketProcessorFactory socketProcessorFactory = null;
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setSocketProcessorFactory(SocketProcessorFactory s){ socketProcessorFactory = s; }
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private int port = 9090;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public int getPort(){ return port; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setPort(int s){ port = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private SockletDeployer defaultSocklet = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SockletDeployer getDefaultSocklet(){ return defaultSocklet; }
	/**
	 * XXXを設定します。<BR>
	 * @param socklet 設定値<BR>
	 */
	public void setDefaultSocklet(SockletDeployer socklet){
		defaultSocklet = socklet;
		defaultSocklet.setPort(port);
		defaultSocklet.addChild(
				GPSSConstants.SYSTEM_COMMAND_SOCKLET, 
				getSystemCommandSocklet());
		
		defaultSockletContainer = new SockletContainer(null, defaultSocklet);
		defaultSockletContainer.setMainReactor(this);
	}
	/**
	 * 
	 * @return
	 */
	public abstract Socklet getSystemCommandSocklet();

	/* ***********************************************************************>> */
	/** XXX */
	private SockletContainer defaultSockletContainer = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SockletContainer getDefaultSockletContainer(){ return defaultSockletContainer; }
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	public void run(){
		accept();
	}
	
	protected abstract void accept();
	
	/**
	 * 
	 * @param connection
	 * @return 
	 */
	public SocketProcessor accept(Connection connection) throws GPSSException{
		SocketProcessor client = socketProcessorFactory.createProcessor(connection, defaultSockletContainer);
		
		return client;
	}
	
	/** このSockletの子Socklet(配備名検索用) */
	private Map<SockletContainer, List<SocketProcessor>> sockletClients = new HashMap<SockletContainer, List<SocketProcessor>>();
	/**
	 * 
	 * @param container
	 * @param client
	 */
	public synchronized void notifyAcceptance(SockletContainer container, SocketProcessor client){
		synchronized(sockletClients){
			List<SocketProcessor> clients = sockletClients.get(container);
			if(clients == null){
				clients = Collections.synchronizedList(new ArrayList<SocketProcessor>());
				sockletClients.put(container, clients);
			}
			clients.add(client);
		}
	}
	/**
	 * 
	 * @param container
	 * @param client
	 */
	public void notifyDesertion(SockletContainer container, SocketProcessor client){
		synchronized(sockletClients){
			List<SocketProcessor> clients = sockletClients.get(container);
			if(clients == null){ return; }
			
			clients.remove(client);
		}
	}
	
	/**
	 * 
	 * @param config
	 */
	public static final void startServer(String config){
		S2Container container = S2ContainerFactory.create(config);
		System.out.println("hotswapMode:" + container.isHotswapMode());
		container.init();
	}

}
