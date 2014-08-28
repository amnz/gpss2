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

import jp.wda.g2.SockletService;
import jp.wda.g2.PortLoader;

/**
 * 
 * 
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author A M O I
 */
class jp.wda.g2.SocketConnectionInfo {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * ホストアドレスとポート番号を指定してオブジェクトを構築します。
	 * 
	 * @param hostaddr 接続先ホストアドレス
	 * @param portnum  ポート番号
	 * @param receive  データ受信方法
	 * @param send     データ送信方法
	 */
	public function SocketConnectionInfo(host:String, portnum:Object, receive:String, send:String){
		this.portLoader = new PortLoader(this);
		
		this.hostname  = host;
		this.receiveBy = receive;
		this.sendBy    = send;
		
		this.setPort(portnum);
	}
	
	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/**  */
	private var _hostname:String = "";
	/**
	 * 
	 * @return 
	 */
	public function get hostname():String{
		return _hostname;
	}
	/**
	 * 
	 * @param param 
	 */
	public function set hostname(param:String):Void{
		this._hostname = null;
		if(param == null){ return; }
		
		if(param == "auto"){
			var svrurl:String = _root._url;
			if (svrurl.substring(0, 4) != "file") {
				svrurl = svrurl.split("/")[2];
				svrurl = svrurl.split(":")[0];
			} else {
				svrurl = "localhost";
			}
			this._hostname = svrurl;
		}else{
			this._hostname = param;
		}
	}

	/* ***********************************************************************>> */
	/**  */
	private var _port:Number = null;
	/**
	 * 
	 * @return 
	 */
	public function get port():Number{
		return _port;
	}
	/**
	 * 
	 * @param param 
	 */
	public function setPort(param:Object):Void{
		this._port = null;
		this._portLoading = false;
		if(param == null){ return; }
		
		if(!isNaN(param)){
			this._port = Number(param);
			return;
		}else if(param instanceof PortLoader){ 
			trace ("Port loading failure... :" + param.port);
			return;
		}
		
		this._portLoading = true;
		this.portLoader.load(param.toString());
	}
	private var portLoader:PortLoader;

	/* ***********************************************************************>> */
	/**  */
	private var _portLoading:Boolean = false;
	/**
	 * 
	 * @return 
	 */
	public function get portLoading():Boolean{
		return _portLoading;
	}

	/* ***********************************************************************>> */
	/**  */
	private var _username:String = "";
	/**
	 * 
	 * @return 
	 */
	public function get username():String{
		return _username;
	}
	/**
	 * 
	 * @param param 
	 */
	public function set username(param:String):Void{
		this._username = param;
	}

	/* ***********************************************************************>> */
	/**  */
	private var _password:String= "";
	/**
	 * 
	 * @return 
	 */
	public function get password():String{
		return _password;
	}
	/**
	 * 
	 * @param param 
	 */
	public function set password(param:String):Void{
		this._password = param;
	}

	/* ***********************************************************************>> */
	/**  */
	private var _sendBy:String;
	/**
	 * 
	 * @return 
	 */
	public function get sendBy():String{
		return _sendBy;
	}
	/**
	 * 
	 * @param param 
	 */
	public function set sendBy(param:String):Void{
		this._sendBy = param;
	}

	/* ***********************************************************************>> */
	/**  */
	private var _receiveBy:String;
	/**
	 * 
	 * @return 
	 */
	public function get receiveBy():String{
		return _receiveBy;
	}
	/**
	 * 
	 * @param param 
	 */
	public function set receiveBy(param:String):Void{
		this._receiveBy = param;
	}

	/* ***********************************************************************>> */
	/**  */
	private var _socklet:String = null;
	/**
	 * 
	 * @return 
	 */
	public function get socklet():String{
		return _socklet;
	}
	/**
	 * 
	 * @param param 
	 */
	public function set socklet(param:String):Void{
		this._socklet = param;
	}
	
	/* ***********************************************************************>> */
	/**  */
	private var params:Array = new Array();
	/**
	 * 初期化パラメータを追加します。
	 * 
	 * @param key   初期化パラメータ名
	 * @param value 初期化パラメータ値
	 */
	public function addInitParam(key:String, value:String){
		this.params.push(key + "=" + value);
	}
	/**
	 * 現在設定されている全ての初期化パラメータを削除します。
	 */
	public function removeAllInitParams(Void):Void{
		this.params = new Array();
	}
	
	// メソッド /////////////////////////////////////////////////////////////////////////
	//                                                                         Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	public function get status():Number{
		if(this._socklet  == null){ return SockletService.NG; }
		if(this._hostname == null){ return SockletService.NG; }
		if(this._port == null){
			if(_portLoading){ return SockletService.PLEASE_WAIT; }
			return SockletService.NG;
		}
		
		return SockletService.OK;
	}
	
	public function get condition():String{
		return this.socklet + ":" + 
				this.username + ":" + 
				this.password + ":" + 
				this.params.join("&");
	}
}