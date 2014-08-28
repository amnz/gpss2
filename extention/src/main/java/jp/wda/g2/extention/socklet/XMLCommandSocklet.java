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
package jp.wda.g2.extention.socklet;

import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.util.SimpleXMLCreator;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/03/03 0:20:57 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public abstract class XMLCommandSocklet extends CommandSocklet implements IXmlCommandSocklet {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public XMLCommandSocklet() {
		super();
	}
	
	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 * コマンドリフレクションメソッドの引数タイプ
	 */
	public static final Class<?>[] clazz = new Class[]{ XMLCommandRequest.class, SimpleXMLCreator.class };
	
	// オーバーライド ///////////////////////////////////////////////////////////////////
	//                                                                        Override //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 送られてきたコマンドを解析し、メソッドをリフレクションするための情報を作成します。<BR>
	 * 接続中のクライアントから、コマンドが送信されてきたときに呼ばれます。<BR><BR>
	 * 
	 * @param req 受信したコマンド情報
	 */
	protected final CommandRequest parseCommand(CommandRequestImpl req) throws GPSSException{
		XMLCommandRequestImpl request = new XMLCommandRequestImpl(req);
		request.setType(clazz);
		
		// コマンド分解
		SimpleXMLCreator xml = SimpleXMLCreator.parse(request.getCommand());
		if(xml == null){ return request; }
		
		request.setName(xml.getName());
		request.setXml(xml);
		
		return request;
	}

}