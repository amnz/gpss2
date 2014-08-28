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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import jp.wda.g2.Socklet;
import jp.wda.g2.exception.SockletNotFoundException;
import jp.wda.gpss.util.Logger;

import org.seasar.framework.container.S2Container;

/**
 *
 *
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 *
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 19:43:09 導入 </dd>
 *
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 *
 * @author		A M O I
 */
public abstract class AbstractSocklet implements Socklet {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 *
	 */
	public AbstractSocklet() {
		super();
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** システムロガー */
	protected final Logger syslog = Logger.getLogger(GPSSConstants.SYSTEMLOG_CATEGORY);

	/** このSockletの子Socklet一覧 */
	private List<SockletContainer> children = new CopyOnWriteArrayList<SockletContainer>();

	/** このSockletの子Socklet(配備名検索用) */
	private ConcurrentMap<String, SockletContainer> childrenMap = new ConcurrentHashMap<String, SockletContainer>();

	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private S2Container container = null;
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setS2Container(S2Container s){ container = s; }
	/**
	 *
	 * @param name
	 * @param clazz
	 * @return
	 */
	protected Object registerComponent(String name, Class<?> clazz){
		container.register(clazz, name);
		container.init();

		return container.getComponent(name);
	}

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @return
	 */
	public List<SockletContainer> getChildren(){
		return children;
	}

	/**
	 *
	 * @param name
	 */
	public void addChild(String name){
		addChild(name, (Socklet)container.getComponent(name));
	}

	/**
	 *
	 * @param name
	 * @param className
	 */
	public Socklet addChild(String name, String className){
		Class<?> clazz = null;
		try{
			clazz = Class.forName(className);
		}catch(ClassNotFoundException ex){
			syslog.errormessage("EGSS00003", new Object[]{ name, className });
			return null;
		}

		return addChild(name, clazz);
	}

	/**
	 *
	 * @param name
	 * @param clazz
	 */
	public Socklet addChild(String name, Class<?> clazz){
		Socklet socklet = (Socklet)registerComponent(name, clazz);
		addChild(name, socklet);

		return socklet;
	}

	/**
	 *
	 * @param name
	 * @param socklet
	 */
	public void addChild(String name, Socklet socklet){
		SockletContainer container = new SockletContainer(name, socklet);
		childrenMap.put(name, container);
		children.add(container);
	}

	/**
	 *
	 * @param name
	 * @return
	 * @throws SockletNotFoundException
	 */
	public SockletContainer getChild(String name) throws SockletNotFoundException{
		SockletContainer container = childrenMap.get(name);
		if(container == null){ throw new SockletNotFoundException(name); }

		return container;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public boolean hasChild(String name){
		return childrenMap.containsKey(name);
	}

	/**
	 *
	 */
	public final void destroyAllSocklets(){
		destroy();
		for(int i = 0;i < children.size(); i++){
			children.get(i).getSocklet().destroyAllSocklets();
		}
	}

}
