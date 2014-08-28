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
public interface SocketProcessor {

	// properties /////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * このソケットを使用しているクライアントのクライアントIDを取得します。<BR>
	 * クライアントIDは、接続中の全クライアントに対し、一意な文字列となります。
	 * 
	 * @return クライアントID
	 */
	public String getClientID();
	
	/**
	 * このソケットの使用するエンコーディング名を取得します。<BR>
	 * エンコーディング名は、初期設定ファイルで指定されます。
	 * 
	 * @return エンコーディング名
	 */
	public String getEncoding();
	/**
	 * 
	 * @param s
	 */
	public void setEncoding(String s);
	
	/**
	 * このソケットを使用しているクライアントのIPアドレスを取得します。<BR>
	 * 
	 * @return IPアドレス
	 */
	public String getIPAddress();
	
	/**
	 * タイムアウトするミリ秒数を取得します。
	 * 
	 * @return タイムアウトするミリ秒数
	 */
	public long getTimeout();
	/**
	 * タイムアウトするミリ秒数を設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setTimeout(long s);
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public SockletLinkage getLinkage(SockletContainer container);
//	/**
//	 * 
//	 * @param socklet
//	 * @return
//	 */
//	public Object getSockletAttributes(SockletContainer container);
//	/**
//	 * 
//	 * @param socklet
//	 * @param attribute
//	 */
//	public void setSockletAttributes(SockletContainer container, Object attribute);
	
	/**
	 * 
	 * @return
	 */
	public Object getAttributes();
	/**
	 * 
	 * @param s
	 */
	public void setAttributes(Object s);
	
	/**
	 * 
	 * @return
	 */
	public ConnectingConditions getConnectingConditions();
	/**
	 * 
	 * @param s
	 */
	public void setConnectingConditions(ConnectingConditions s);
	
	// methods ////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param command
	 */
	public void doCommand(ByteBuffer command);
	
	/**
	 * このソケットを使用しているクライアントにメッセージを送ります。<BR>
	 * メッセージはnull文字"\0"を終端とします。<BR>
	 *
	 * @param message クライアントに送るメッセージ文字列
	 * @return 送信に成功した場合は真、失敗した場合は偽
	 */
	public boolean send(String message);
	
	/**
	 * このソケットを使用しているクライアントを強制的に終了させます。
	 */
	public void terminate();
	/**
	 * このソケットを使用しているクライアントに終了メッセージ送出後、ソケットを強制的に終了させます。
	 * @param message 終了メッセージ
	 */
	public void terminate(String message);
	/**
	 * このソケットを使用しているクライアントが既に終了しているかを確認します。
	 * @return 既に終了している場合は真
	 */
	public boolean isTerminated();
	
}
