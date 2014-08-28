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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.wda.g2.SocketProcessor;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/26 2:12:26 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		Takenori Adachi(TheCoolMuseum)
 */
public class AccessController {
	/**
	 * チェック条件をチェック順に格納するリスト
	 */
	private List<ClientChecker> checkerChain;
	
	/**
	 * コンストラクタ
	 *
	 */
	public AccessController(){
		checkerChain = new ArrayList<ClientChecker>();
	}
	
	/**
	 * チェック条件を追加します
	 * @param checker
	 * @return
	 */
	public synchronized AccessController register(ClientChecker checker){
		checkerChain.add(checker);
		return this;
	}
	
	/**
	 * チェックを順に実行します
	 * @param client
	 * @return
	 */
	public synchronized boolean enter(SocketProcessor client){
		Iterator<ClientChecker> itr = checkerChain.iterator();
		
		while(itr.hasNext()){
			ClientChecker checker = itr.next();
			int result = checker.enter(client);
			if(result==ClientChecker.ACCEPT){
				return true;
			}
			if(result==ClientChecker.REJECT){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 開放処理を順に実行します
	 * @param client
	 */
	public synchronized void exit(SocketProcessor client){
		Iterator<ClientChecker> itr = checkerChain.iterator();
		
		while(itr.hasNext()){
			ClientChecker checker = itr.next();
			checker.exit(client);
		}
	}
}
