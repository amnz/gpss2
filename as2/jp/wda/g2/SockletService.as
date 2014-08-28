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

import jp.wda.g2.SocketConnectionInfo;
import jp.wda.g2.XMLSocketEx;

/**
 * 
 * 
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author A M O I
 */
class jp.wda.g2.SockletService {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 接続情報を指定してオブジェクトを構築します。
	 * 
	 * @param connectionInfo 接続情報
	 */
	public function SockletService(connectionInfo:SocketConnectionInfo){
		if(connectionInfo != undefined){ this.conInfo = connectionInfo; }
		
		this._intervalCounter = 0;
		this.isConnected = false;
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////
	
	public static var NG:Number = -1;
	
	public static var OK:Number = 0;
	
	public static var PLEASE_WAIT:Number = 1;

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////
	
	private var connection:XMLSocketEx = null;
	
	/**  */
	private var conInfo:SocketConnectionInfo = null;
	/**
	 * 
	 * @return 	 */
	public function get connectionInfo():SocketConnectionInfo{ return conInfo; }
	
	/**  */
	private var _intervalCounter:Number = 0;
	
	/**  */
	private var _intervalID:Number = 0;
	
	/**  */
	public var isConnected:Boolean = false;
	
	// メソッド /////////////////////////////////////////////////////////////////////////
	//                                                                         Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Sockletサーバに接続します。
	 * @param sockletname 接続したいSockletの配備名
	 */
	public function getSocklet(sockletname:String):Void{
		if(this._intervalCounter > 0){ clearInterval( this._intervalID ); }
		if(conInfo == null ){ return; }
		if(sockletname != undefined){ conInfo.socklet = sockletname; }
		
		trace("coninfo status : " + conInfo.status);
		var status:Number = conInfo.status;
		if(status == NG){
			this.connectionFailure(32);
			return;
		}else if(status == PLEASE_WAIT){
			if(this._intervalCounter < 60){
				trace("Waiting ...(" + ++this._intervalCounter + ")");
				this._intervalID = setInterval( this, "getSocklet", 500 );
				return;
			}else{
				this.connectionFailure(16);
				return;
			}
		}
		
		this._intervalCounter = 0;
		
		if(this.connection != null){ close(); }
		
		this.connection = new XMLSocketEx(conInfo, this);
		if(!this.connection.connect(conInfo.hostname, conInfo.port)){
			connectionFailure(0);
			close();
		}else{
			trace ("Connect to Socklet=" + conInfo.condition);
		}
	}
	
	public function send(data:Object):Boolean{
		if(connection == null){ return false; }
		if(conInfo.sendBy != "xml"){ return connection.send(data); }
		
		var theXML:XML = new XML();
		theXML.parseXML(data.toString());
		
		return connection.send(theXML);
	}
	
	public function changeHandler(service:SockletService):Void{
		service.conInfo     = this.conInfo;
		service.isConnected = this.isConnected;
		service.connection  = this.connection;
		
		this.connection.handler = service;
	}
	
	public function close():Boolean{
		var result:Boolean = this.connection.close();
		this.connection  = null;
		this.isConnected = false;
		
		return result;
	}
	
	// イベントハンドラ ///////////////////////////////////////////////////////////////
	//                                                                  Event Handlers //
	/////////////////////////////////////////////////////////////////////////////////////

	function onClose():Void{
		trace (connectionInfo.socklet + " is disconnected.");
		close();
	}

	function connectionSuccessfully():Void{
	}
	
	function connectionFailure(status:Number):Void{
	}
	
	function onReceive(receive:Object):Void{
	}

}