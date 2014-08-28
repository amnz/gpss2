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

import jp.wda.g2.SocketProcessor;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/26 2:09:20 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		Takenori Adachi(TheCoolMuseum)
 */
public abstract class ClientChecker {
	/**
	 * 接続が許可された場合の値
	 */
	public static int ACCEPT = 1;
	
	/**
	 * 判断が保留し、次の条件へ連鎖させる場合の値
	 */
	public static int CHAIN = 0;
	
	/**
	 * 接続が拒否された場合の値
	 */
	public static int REJECT = -1;
	
	/**
	 * クライアントを検査し接続を許可するかどうか判定します。<br>
	 * 結果は許可(ACCEPT), 保留(CHAIN), 拒否(REJECT)のいずれかを返してください。
	 * @param client 検査を行うクライアント
	 * @return 検査の結果(ACCEPT|CHAIN|REJECT)
	 */
	public abstract int enter(SocketProcessor client);
	
	/**
	 * クライアント接続時に呼び出されます。<br>
	 * リソースの開放などが必要な場合はこのメソッドをオーバーライドします。
	 * @param client 
	 */
	public void exit(SocketProcessor client){
	}
}
