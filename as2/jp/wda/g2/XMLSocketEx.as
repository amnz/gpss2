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
import jp.wda.g2.SocketConnectionInfo;

/**
 * 
 * 
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author A M O I
 */
class jp.wda.g2.XMLSocketEx extends XMLSocket {
	public function XMLSocketEx(connectionInfo:SocketConnectionInfo, service:SockletService){
		super();
		
		this.conInfo = connectionInfo;
		this.handler = service;
		
		this._onDataDefaultFunc = onData;
		this.onData  = checkStatingMessage;
	}
	
	/**  */
	private var conInfo:SocketConnectionInfo = null;
	
	/**  */
	private var _handler:SockletService = null;
	/**
	 * 
	 * @param service 	 */
	public function set handler(service:SockletService):Void{ this._handler = service; }
	
	/**  */
	private var _onDataDefaultFunc:Function = null;
	
	// イベントハンドラ ///////////////////////////////////////////////////////////////
	//                                                                  Event Handlers //
	/////////////////////////////////////////////////////////////////////////////////////
	
	public function onConnect(bOK:Boolean):Void{
		if(bOK){
			trace ("connect seccessfuly!");
			super.send(conInfo.condition);
		}else{
			_handler.isConnected = false;
			_handler.connectionFailure(0);
		}
	}

	function checkStatingMessage(command:String):Void {
		trace ("Starting command : " + command);
		this.onData = this._onDataDefaultFunc;
		
		var ok = command.charAt(0);
		if(ok == "+"){
			_handler.isConnected = true;
			_handler.connectionSuccessfully();
		}else{
			_handler.isConnected = false;
			_handler.connectionFailure(1);
		}
	}
	
	function onData(src:String):Void{
		if(conInfo.receiveBy != "xml"){ _handler.onReceive(src); }
		super.onData(src);
	}
	
	function onXML(src:XML):Void{
		_handler.onReceive(src);
	}
	
	function onClose():Void{
		_handler.onClose();
	}

}