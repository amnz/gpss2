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

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.wda.g2.GeneralSocklet;
import jp.wda.g2.SockletRequest;
import jp.wda.g2.exception.CommandParseException;
import jp.wda.g2.exception.GPSSException;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/03/02 23:45:07 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public abstract class CommandSocklet extends GeneralSocklet implements ICommandSocklet {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public CommandSocklet() {
		super();
	}
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private boolean execIgnoreCase = true;
	/**
	 * コマンド名の大文字小文字を区別するかどうかを決定します。
	 * 真を返す場合は、大文字小文字を区別しません。
	 * @return 大文字小文字を区別しないなら真
	 */
	public boolean isExecIgnoreCase(){ return execIgnoreCase; }
	/**
	 * XXXを設定します。<BR>
	 * @param s 設定値<BR>
	 */
	public void setExecIgnoreCase(boolean s){ execIgnoreCase = s; }
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/** {@inheritDoc} */
	public Object doCommand(SockletRequest req) throws GPSSException{
		CommandRequest request = new CommandRequestImpl(req);
		preProcess(request);
		
		request = parseCommand((CommandRequestImpl)request);
		if(request.getName() == null || request.getName().length() == 0){
			throw new CommandParseException(request.getCommand());
		}
		
		return doCommandMethod(request);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws GPSSException
	 */
	public Object doCommandMethod(CommandRequest request) throws GPSSException{
		String name = request.getName();
		if(execIgnoreCase){
			name = name.substring(0, 1).toUpperCase() + 
					(name.length() > 1 ? 
					 name.substring(1).toLowerCase() : "");
		}
		
		// メソッド検索
		String methodname = request.toString();
		Method method = methodsCache.get(methodname);
		if(method == null){
			try{
				method = getClass().getMethod("cmd" + name, request.getType());
				methodsCache.put(methodname, method);
			}catch(NoSuchMethodException e){
				return cmdUndefined(request);
			}catch(SecurityException e){
				e.printStackTrace();
				return null;
			}
		}
		
		// コマンドメソッド実行
		try{
			return method.invoke(this, request.getParams());
		}catch(Throwable e){
			syslog.fatalmessage(e);
			throw new CommandParseException(request.getCommand());
		}
	}
	
	private ConcurrentMap<String, Method> methodsCache = new ConcurrentHashMap<String, Method>();
	
	/**
	 * 送られてきたコマンドを解析し、メソッドをリフレクションするための情報を作成します。<BR>
	 * 接続中のクライアントから、コマンドが送信されてきたときに呼ばれます。<BR>
	 * 派生クラスではこのメソッドをオーバーライドし、コマンドの解釈方法を記述してください。<BR>
	 * 
	 * @param request 受信したコマンド情報
	 */
	protected abstract CommandRequest parseCommand(CommandRequestImpl request) throws GPSSException;
	
	/**
	 * コマンド名処理リフレクションメソッド未定義字の処理を行ないます。
	 * 
	 * @param request
	 */
	public Object cmdUndefined(CommandRequest request) throws GPSSException{
		return request.handoverRequest();
	}
	
	/**
	 * コマンド実行前処理を行ないます。<BR>
	 * このメソッドの戻り値は、コマンド処理リフレクションメソッドに引き渡されます。<BR>
	 * 全てのコマンドに共通する処理を記述したい場合は、このメソッドをオーバーライドしてください。<BR>
	 * SockletException例外を投げると、コマンドの解釈を中止し、doCommand内で直ちに偽を返します。
	 * 
	 * @param request 受信したコマンド情報
	 * @throws GPSSException コマンド解釈処理を中止したい場合。
	 */
	public void preProcess(CommandRequest request) throws GPSSException{
	}

}
