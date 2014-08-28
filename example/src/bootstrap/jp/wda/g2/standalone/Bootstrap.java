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
package jp.wda.g2.standalone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

import jp.wda.g2.system.GPSSConstants;

/**
 * 
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/06/10 1:10:52 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public class Bootstrap {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 * 
	 */
	public Bootstrap(String server, String port, String socklet, String command) {
		super();
		
		if(server  != null && server.length()  > 0){ this.server  = server; }
		if(socklet != null && socklet.length() > 0){ this.socklet = socklet; }
		if(command != null && command.length() > 0){ this.command = command; }
		if(port    != null && port.length()    > 0){
			try{
				this.port = Integer.parseInt(port);
			}catch(NumberFormatException ex){
				this.port = 9090;
			}
		}
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	private String server  = "localhost";
	private int    port    = 9090;
	private String socklet = GPSSConstants.SYSTEM_COMMAND_SOCKLET;
	private String command = "shutdown";
	
	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 */
	public void connect(){
		System.out.println("Connect to " + server + ":" + port);
		System.out.println("System:    " + socklet);
		System.out.println("Command:   " + command);
		
		// 受信文字用
		char c[] = new char[1];
		
		Socket socket = null;
		BufferedReader reader = null;
		PrintWriter writer = null;
		try{
			socket = new Socket(server, port);
			
			reader = new BufferedReader( new InputStreamReader(  socket.getInputStream(),  "SJIS"));
			writer = new PrintWriter(    new OutputStreamWriter( socket.getOutputStream(), "SJIS"), true);
			
			writer.print(socklet + ":::" + "\0");
			if(writer.checkError()){ return; }
			
			// サーバから受信
			int counter = 0;
			String nextcommand = null;
			while( reader.read(c, 0, 1) != -1 ){
				StringBuffer sb = new StringBuffer(4096);
				
				while(c[0] != '\0'){
					sb.append(c[0]);
					reader.read(c, 0 ,1);
				}
				
				System.out.println(sb.toString());
				switch(counter){
					case 0: nextcommand = checkInitialMessage(sb.toString(), command); break;
					default : nextcommand = null;
				}
				
				if(nextcommand == null){ return; }
				counter++;
				
				writer.print(nextcommand + "\0");
				if(writer.checkError()){ return; }
			}
		}catch(Throwable e){ ; }finally{
			// PrintWriterを閉じる
			if(writer != null)      { writer.close(); }
			
			//BufferdReaderを閉じる
			try { if(reader != null){ reader.close(); }    }
			catch(IOException e){
			}
			
			// ソケットを閉じる
			try { if(socket != null){ socket.close(); }}
			catch(IOException e){
			}
			
			writer = null;
			reader = null;
			socket = null;
		}
	}
	
	private String checkInitialMessage(String message, String command){
		if(message.charAt(0) == '-'){
			return null;
		}
		return command;
	}
	
	// アプリケーションエントリーポイント ///////////////////////////////////////////////
	//                                                         Application Entry Point //
	/////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args){
		String config  = "gpss.dicon";
		String server  = null;
		String port    = null;
		String socklet = null;
		String command = null;
		
		for(int i = 0; i < args.length; i++){
			if(args[i].charAt(0) != '-'){
				command = args[i];
				continue;
			}
			
			if(args[i].startsWith(SW_SERVER)){
				server  = args[i].substring(SW_SERVER.length());
			}else if(args[i].startsWith(SW_PORT)){
				port    = args[i].substring(SW_PORT.length());
			}else if(args[i].startsWith(SW_SOCKLET)){
				socklet = args[i].substring(SW_SOCKLET.length());
			}else if(args[i].startsWith(SW_CONFIG)){
				config  = args[i].substring(SW_CONFIG.length());
			}
		}
		
		if(command == null){
			S2Container container = S2ContainerFactory.create(config);
			System.out.println("hotswapMode:" + container.isHotswapMode());
			container.init();
			return;
		}
		
		Bootstrap commander = new Bootstrap(server, port, socklet, command);
		commander.connect();
	}
	
	public static final String SW_CONFIG  = "-config:";
	public static final String SW_SERVER  = "-server:";
	public static final String SW_PORT    = "-port:";
	public static final String SW_SOCKLET = "-connect:";

}
