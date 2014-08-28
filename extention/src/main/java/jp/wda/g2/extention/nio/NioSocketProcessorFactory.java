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
package jp.wda.g2.extention.nio;

import jp.wda.g2.Connection;
import jp.wda.g2.SocketProcessor;
import jp.wda.g2.SocketProcessorFactory;
import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.system.SockletContainer;

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
public class NioSocketProcessorFactory implements SocketProcessorFactory {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public NioSocketProcessorFactory() {
		super();
	}
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private long timeout = 15000;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public long getTimeout(){ return timeout; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setTimeout(long s){ timeout = s; }

	/* ***********************************************************************>> */;
	/**
	 * デフォルトのエンコーディング名<BR>
	 */
	private String defaultEncoding = null;
	/**
	 *	使用するデフォルトのエンコーディング名を取得します。<BR>
	 *	@return デフォルトのエンコーディング名
	 */
	public String getDefaultEncoding(){ return defaultEncoding; }
	/**
	 *	使用するデフォルトのエンコーディング名を設定します。<BR>
	 *	@param s 設定値<BR>
	 */
	public void setDefaultEncoding(String s){ defaultEncoding = s; }

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/** {@inheritDoc} */
	public SocketProcessor createProcessor(Connection connection, SockletContainer container) throws GPSSException{
		NioSocketProcessor processor = new NioSocketProcessor(connection, container);
		
		if(defaultEncoding != null){
			processor.setEncoding(defaultEncoding);
		}
		processor.setTimeout(timeout);
		
		return processor;
	}
}
