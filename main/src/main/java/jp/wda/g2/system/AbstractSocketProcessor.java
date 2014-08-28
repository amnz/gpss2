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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;

import jp.wda.g2.ConnectingConditions;
import jp.wda.g2.Connection;
import jp.wda.g2.SocketProcessor;
import jp.wda.g2.exception.GPSSException;
import jp.wda.gpss.util.Logger;
import jp.wda.gpss.util.PseudoUUID;


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
public abstract class AbstractSocketProcessor implements SocketProcessor {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public AbstractSocketProcessor(Connection connection, SockletContainer container) throws GPSSException {
		super();
		
		this.connection = connection;
		if(connection != null){
			this.ipAddress = connection.getSocketChannel().socket().getInetAddress().getHostAddress();
		}
		try{
			this.clientID = new PseudoUUID(ipAddress).toString();
		}catch(IOException ex){ throw new GPSSException(); }
		
		this.linkage = new SockletLinkage(this, container);
		terminated = false;
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** システムロガー */
	protected final Logger syslog = Logger.getLogger(GPSSConstants.SYSTEMLOG_CATEGORY);

	/** XXX */
	private SockletLinkage linkage = null;
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private Connection connection = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public Connection getConnection(){ return connection; }

	/* ***********************************************************************>> */;
	/**
	 *	クライアントID<BR>
	 */
	private String clientID;
	/**
	 *	クライアントIDを取得します。<BR>
	 *	@return クライアントID
	 *	@see jp.wda.g2.SocketProcessor#getClientID()
	 */
	public String getClientID(){ return clientID; }

	/* ***********************************************************************>> */
	/** クライアントのホストIPアドレス */
	private String ipAddress = null;
	/**
	 * クライアントのホストIPアドレスを取得します。<BR>
	 * @return クライアントのホストIPアドレス
	 * @see jp.wda.g2.SocketProcessor#getIPAddress()
	 */
	public String getIPAddress(){ return ipAddress; }

	/* ***********************************************************************>> */;
	/**
	 * デフォルトのエンコーディング名<BR>
	 */
	private String encoding = "UTF-8";
	/**
	 *	使用するデフォルトのエンコーディング名を取得します。<BR>
	 *	@return デフォルトのエンコーディング名
	 *	@see jp.wda.g2.SocketProcessor#getEncoding()
	 */
	public String getEncoding(){ return encoding; }
	/**
	 *	使用するデフォルトのエンコーディング名を設定します。<BR>
	 *	@param s 設定値<BR>
	 */
	public void setEncoding(String s){ encoding = s; }

	/* ***********************************************************************>> */
	/** タイムアウトするミリ秒数 */
	private long timeout = 0;
	/**
	 * タイムアウトするミリ秒数を取得します。<BR>
	 * @return タイムアウトするミリ秒数
	 */
	public long getTimeout(){ return timeout; }
	/**
	 * タイムアウトするミリ秒数を設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setTimeout(long s){ timeout = s; }

	/* ***********************************************************************>> */
	/** 終了済みソケットであるかどうか */
	private boolean terminated = false;
	/**
	 * 終了済みソケットであるかどうかを取得します。<BR>
	 * @return 終了済みソケットであるなら真
	 */
	public boolean isTerminated(){ return terminated; }

	/* ***********************************************************************>> */
	/** XXX */
	private Object attributes = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public Object getAttributes(){ return attributes; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setAttributes(Object s){ attributes = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private ConnectingConditions connectingConditions = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public ConnectingConditions getConnectingConditions(){ return connectingConditions; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setConnectingConditions(ConnectingConditions s){
		if(connectingConditions != null){ return; }
		connectingConditions = s;
	}
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param command 
	 */
	public void doCommand(ByteBuffer command){
		try{
			linkage.doCommand(command);
		}catch(GPSSException ex){
			syslog.errormessage(ex);
			terminate();
		}catch(UndeclaredThrowableException ex){
			Throwable th = ex.getUndeclaredThrowable();
			if(th instanceof InvocationTargetException){
				th = ((InvocationTargetException)th).getTargetException();
				if(th instanceof GPSSException){
					syslog.errormessage((GPSSException)th);
					terminate();
					return;
				}
			}
			syslog.errormessage("EGSS50001", new Object[]{ getClientID(), command }, ex);
		}catch(Throwable ex){
			syslog.errormessage("EGSS50001", new Object[]{ getClientID(), command }, ex);
		}
	}
	
	/**
	 * 
	 */
	public void terminate(){
		synchronized (this) {
			if (this.terminated) {
				return;
			}
			this.terminated = true;
		}
		
		linkage.removeClient(true);
		getConnection().closeConnection();
	}

}
