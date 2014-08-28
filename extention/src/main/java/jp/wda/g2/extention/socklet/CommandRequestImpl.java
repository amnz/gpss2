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

import java.io.Serializable;
import java.nio.ByteBuffer;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.SockletRequest;
import jp.wda.g2.system.SockletLinkage;
import jp.wda.g2.system.SockletRequestImpl;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/03/03 1:18:28 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class CommandRequestImpl extends SockletRequestImpl implements CommandRequest, Serializable {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * 
	 * @param client
	 * @param rawCommand
	 * @param linkage
	 */
	public CommandRequestImpl(SocketProcessor client, ByteBuffer rawCommand, SockletLinkage linkage) {
		super(client, rawCommand, linkage);
	}

	/**
	 * 
	 * 
	 * @param request
	 */
	public CommandRequestImpl(SockletRequest request) {
		super(request);
	}
	
	/**  */
	private static final long serialVersionUID = 3688308924722347462L;
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private String name = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public String getName(){ return name; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setName(String s){ name = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private Class<?>[] type = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public Class<?>[] getType(){ return type; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setType(Class<?>[] s){ type = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private Object[] params = null;
	/**
	 * XXXを取得します。<BR>
	 * @return XXX
	 */
	public Object[] getParams(){ return params; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setParams(Object[] s){ params = s; }
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	public String toString(){
		if(type == null || type.length == 0){ return name; }
		
		StringBuilder result = new StringBuilder(name);
		result.append("(");
		String cmm = "";
		for(int i = 0; i < type.length; i++){
			result.append(cmm);
			result.append(type[i].getName());
			cmm = ",";
		}
		result.append(")");
		
		return result.toString();
	}
}
