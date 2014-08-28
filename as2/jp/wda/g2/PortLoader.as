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

/**
 * 
 * 
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author A M O I
 */
class jp.wda.g2.PortLoader extends LoadVars{
	public function PortLoader(socketConnectionInfo:SocketConnectionInfo){
		super();
		this.info = socketConnectionInfo;
	}
	
	/**  */
	private var info:SocketConnectionInfo;

	/**  */
	public var port:String = null;
	
	function onLoad(bSuccess:Boolean):Void {
		var p:Number = parseInt(this.port);
		
  		if (!bSuccess || isNaN(p)){
			this.info.setPort(this);
  			return;
  		}

		this.info.setPort(p);
	}
	
	function load(url:String):Boolean{
		this.port = null;
		
		return super.load(url);
	}
}