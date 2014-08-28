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

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.Socklet;
import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.exception.SockletNotFoundException;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 15:01:44 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class SockletLinkage {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public SockletLinkage(SocketProcessor client, SockletContainer container) throws GPSSException {
		super();
		
		this.client = client;
		setContainer(container);
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** XXX */
	private SocketProcessor client = null;

	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private SockletContainer container = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SockletContainer getContainer(){ return container; }
	/**
	 * XXXを設定します。<BR>
	 * @param container 設定値<BR>
	 */
	private void setContainer(SockletContainer container) throws GPSSException{
		if(container == null || container.getSocklet() == null){ throw new SockletNotFoundException(""); }
		
		this.container = container;
		container.accept(client, this);
	}
	public Socklet getSocklet(){
		if(container == null){ return null; }
		return container.getSocklet();
	}

	/* ***********************************************************************>> */
	/** XXX */
	private SockletLinkage next = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public SockletLinkage getNext(){ return next; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	private void setNext(SockletLinkage s){
		this.next = s;
	}
	
	/**
	 * XXXを設定します。<BR>
	 * @param container 設定値<BR>
	 * @param removeChildren 
	 */
	public void setNextSocklet(SockletContainer container, boolean removeChildren) throws GPSSException{
		if(container.getMainReactor() == null){
			container.setMainReactor(this.container.getMainReactor());
		}
		SockletLinkage next = new SockletLinkage(client, container);
		
		SockletLinkage old = this.next;
		this.next = next;
		
		if(old == null){ return; }
		
		old.removeClient(removeChildren);
		if(old.hasNext() && !removeChildren){
			next.setNext(old.getNext());
		}
	}
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public boolean hasNext(){ return next != null; }

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
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param command 
	 */
	public Object doCommand(ByteBuffer command) throws GPSSException{
		if(this.container == null){ return null; }
		
		return container.doCommand(client, command, this);
	}
	
	/**
	 * 
	 * @param removeChildren 
	 */
	void removeClient(boolean removeChildren){
		container.desert(client, this);
		
		if(removeChildren && hasNext()){ next.removeClient(removeChildren); }
	}

	// メッセージ送信メソッド ///////////////////////////////////////////////////////////
	//                                                    Methods for Sending messages //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 指定されたクライアント一覧に向けて、メッセージを送信します。<BR>
	 * 
	 * @param sender メッセージを送信してきたクライアント
	 * @param clients 送信するクライアント一覧
	 * @param message 送信するメッセージ
	 */
	private void sendToClients(SocketProcessor[] clients, String message) {
		for(int i = 0; i < clients.length; i++){
			if (!clients[i].send(message)) {
				clients[i].terminate();
				continue;
			}
		}
	}

	/**
	 * 現在このSockletに接続中の全てのクライアントに向けて、メッセージを送信します。<BR>
	 * このメソッドは、指定されたStringを特に加工することなく送信しますが、
	 * Flash XMLSocketの規定に則り、文字列末尾は必ず\0として送信しますので、
	 * Socklet制作者は特にそれを意識する必要はありません。
	 * 
	 * @param message 送信するメッセージ
	 */
	public final void sendToAllClients(String message) {
		sendToClients(container.getAllClients(), message);
	}

}
