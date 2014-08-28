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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 22:57:46 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class ConnectingConditions implements Serializable {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public ConnectingConditions() {
		super();
	}
	/**  */
	private static final long serialVersionUID = 71345725964710985L;
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private String username = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public String getUsername(){ return username; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setUsername(String s){ username = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private String password = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public String getPassword(){ return password; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setPassword(String s){ password = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private String initialParameters = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public String getInitialParameters(){ return initialParameters; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setInitialParameters(String s){
		initialParameters = s;
		
		String[] params = initialParameters.split("&");
		for(int i = 0; i < params.length; i++){ addParameter(params[i]); }
	}

	/* ***********************************************************************>> */
	/** XXX */
	private Map<String, String> params = Collections.synchronizedMap(new HashMap<String, String>());
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public String getParameter(String name){ return params.get(name); }
	/**
	 * 
	 * @param param
	 */
	private void addParameter(String param){
		if(param == null || param.length() == 0){ return; }
		
		int idx = param.indexOf("=");
		if(idx == 0){
			params.put(param, "");
			return;
		}
		
		String value = param.substring(idx + 1);
		params.put(param.substring(0, idx), value);
	}
}
