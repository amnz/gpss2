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
package jp.wda.g2;

import java.nio.ByteBuffer;

import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.system.SockletLinkage;

/**
 * Sockletインターフェース<BR>
 * <BR>
 * 全てのSockletは、このインターフェースを実装する必要があります。<BR>
 * 通常の場合は、jp.wda.g2.GeneralSockletを継承する方が楽チンです。<BR>
 *
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 14:40:03 導入 </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public interface Socklet {

	/**
	 * 接続用初期コマンド受信後、クライアントの検査を行うためのメソッド。<BR>
	 * このメソッドは、サーバが新しいクライアントからの接続を確認した時点で、
	 * そのクライアントを検査するために呼ばれます。<BR>
	 * 接続クライアントに対し一番初めに一回だけ呼ばれます。<BR>
	 * SocketProcessorクラスのメソッドを使用して、このクラスが適切であるか検査し、
	 * またクライアントに必要な初期化情報を与えてください。<BR>
	 * <BR>
	 * このクライアントが適切でないと判断された場合は、偽falseを返してください。
	 * 直ちにクライアントソケットを終了します。<BR>
	 * その他の場合は、真を返してください。真を返すと、クライアントソケットを継続します。<BR>
	 * 
	 * @param client   接続を試みているクライアント
	 * @param linkage 
	 * @return 検査後、即終了する場合は偽。
	 */
	public boolean checkConnection(SocketProcessor client, SockletLinkage linkage);
	
	/**
	 * クライアント削除前に呼び出されるメソッド。<BR>
	 * 接続中のクライアントが切断され、このアプリケーションから除外される直前に呼ばれます。<BR>
	 * 接続クライアントに対し、終了前に一回だけ呼ばれます。<BR>
	 * 他の接続中クライアントへ、このクライアントが切断されたことを通知するような場合は、
	 * このメソッド中で記述してください。<BR>
	 *
	 * @param client これから削除されるクライアント
	 * @param linkage 
	 */
	public void preRemoveClient(SocketProcessor client, SockletLinkage linkage);
	
	/**
	 * コマンドを処理するためのメソッド。<BR>
	 * 接続中のクライアントから、コマンドが送信されてきたときに呼ばれます。
	 * サブクラスはこのメッソド中で、クライアントから送られてきたコマンドを処理してください。<BR>
	 * クライアントの接続中に何度でも呼ばれます。<BR>
	 * <BR>
	 * コマンドを送ってきたソケットに対して、メッセージを送信したい場合は、以下のメソッドを使用してください。<BR>
	 * <BR>
	 * client.send(<I>送信するメッセージ</I>);<BR>
	 * <BR>
	 * 接続中の全クライアントへメッセージを送信したい場合は、以下のメソッドを使用してください。<BR>
	 * <BR>
	 * sendToAllClients(<I>送信するメッセージ</I>); <BR>
	 * <BR>
	 * また、特定のクライアントへのみメッセージを送りたい場合は、
	 * SocketProcessorFinder派生クラスを用いて以下のメソッドを使用してください。<BR>
	 * <BR>
	 * sendToClients(<I>送信するメッセージ</I>, <I>SocketProcessorFinder</I>);<BR>
	 * または、<BR>
	 * sendToClients(<I>送信するメッセージ</I>, <I>送信したいクライアントの検索条件文</I>);<BR>
	 * <BR>
	 * SocketProcessorFinderには、ユーザー名での検索用クラスやパラメタ値での検索用クラスなどが用意されています。<BR>
	 * 
	 * @param client コマンドを送信してきたクライアントソケット
	 * @param command 受信したコマンド
	 * @param linkage 
	 * @return コマンド処理結果
	 * @throws GPSSException コマンド処理中にGPSSExceptionを投げると、直ちにクライアントソケットを終了します。
	 */
	public Object doCommand(SocketProcessor client, ByteBuffer command, SockletLinkage linkage) throws GPSSException;
	
	/**
	 * Sockletがサービスを停止する際に、待受けメインサーバによって呼び出されます。<BR>
	 * Sockletの終了処理ルーチンを記述し、このメソッドを実装してください。
	 */
	public void destroy();
	
	/**
	 * 
	 *
	 */
	public void destroyAllSocklets();

}
