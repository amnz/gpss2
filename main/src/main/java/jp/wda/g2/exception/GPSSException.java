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
package jp.wda.g2.exception;

import org.seasar.framework.message.MessageFormatter;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 15:29:53 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class GPSSException extends Exception {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public GPSSException() {
		super();
	}

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 * @param messageCode
	 */
	public GPSSException(String messageCode) {
		this(messageCode, null, null);
	}

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 * @param messageCode
	 * @param args
	 */
	public GPSSException(String messageCode, Object[] args) {
		this(messageCode, args, null);
	}

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 * @param messageCode
	 * @param cause
	 */
	public GPSSException(String messageCode, Throwable cause) {
		this(messageCode, null, cause);
	}

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 * @param messageCode 
	 * @param args 
	 * @param cause 
	 */
	public GPSSException(String messageCode, Object[] args, Throwable cause) {
		super(cause);
		
		this.messageCode = messageCode;
		this.args = args;
		
		this.simpleMessage = MessageFormatter.getSimpleMessage(messageCode, args);
		this.message       = "[" + this.messageCode + "]" + simpleMessage;
	}

	/**  */
	private static final long serialVersionUID = -2483350199291956737L;

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private String messageCode = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public final String getMessageCode(){ return messageCode; }
	
	/** XXX */
	private String simpleMessage;
	/**
	 * 
	 * @return
	 */
	public final String getSimpleMessage(){ return simpleMessage; }

	/* ***********************************************************************>> */
	/** XXX */
	private Object[] args = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public Object[] getArgs(){ return args; }

	/* ***********************************************************************>> */
	/** XXX */
	private String message = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public String getMessage(){ return message; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setMessage(String s){ message = s; }

}
