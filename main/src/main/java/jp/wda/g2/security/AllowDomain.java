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
package jp.wda.g2.security;

import java.io.Serializable;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/26 1:35:07 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		Takenori Adachi(TheCoolMuseum)
 */
public class AllowDomain implements Serializable {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public AllowDomain(String domain, String ports) {
		super();
		
		this.domain = domain;
		this.ports  = ports;
	}
	
	/**  */
	private static final long serialVersionUID = -5630287325378464189L;
	
	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** XXX */
	private String domain = null;
	
	/** XXX */
	private String ports = null;
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * @return 
	 */
	public String toString(){
		StringBuilder result = new StringBuilder("<allow-access-from ");
		
		result.append("domain=\"");
		result.append(domain);
		result.append("\" ");
		
		result.append("to-ports=\"");
		result.append(ports);
		result.append("\" ");
		
		result.append("/>");
		
		return result.toString();
	}
}
