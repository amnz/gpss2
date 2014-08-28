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
package jp.wda.gpss.util;

import java.io.IOException;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ホストアドレス又は指定されたクライアントアドレスから、
 * 擬似UUIDを生成します。
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 1.00α </dt><dd> 2003/04/29 導入 </dd>
 * 
 * </dl>
 * @version	1.00α
 * @since		1.00α
 * 
 * @author		A M O I
 */
public class PseudoUUID {
	// 静的コンストラクタ ///////////////////////////////////////////////////////////////
	//                                                             Static Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 *	静的コンストラクタ
	 */
	static{
		hostUnique = (new Object()).hashCode();
		mutex      = new Object();
		lastTime   = System.currentTimeMillis();
		DELAY      = 10;
		try{
			md = MessageDigest.getInstance("MD5");
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			md = null;
		}
		try {
			String s    = InetAddress.getLocalHost().getHostAddress();
			hostaddress = MD5(s);
		} catch (Throwable ex) {
			hostaddress = generateNoNetworkID();
		}
	}

	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 *	デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 */
	public PseudoUUID() throws IOException{
		this(null);
	}

	/* ***********************************************************************>> */;
	/**
	 *	デフォルトの設定を用いてオブジェクトを構築するコンストラクタ
	 */
	public PseudoUUID(String clientaddr) throws IOException{
		super();
		
		if(md == null){
			throw new IOException("MD5アルゴリズムがインストールされていません。");
		}
		
		synchronized (mutex) {
			set1st();
			set2nd();
			set3rd(clientaddr);
		}
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 * 
	 */
	private static String hostaddress;
	
	/**
	 * 
	 */
	private static int hostUnique;
	
	/**
	 * 
	 */
	private static Object mutex;
	
	/**
	 * 
	 */
	private static long lastTime;
	
	/**
	 * 
	 */
	private static long DELAY;
	
	/**
	 * 
	 */
	private static MessageDigest md;

	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 *	XXX<BR>
	 */
	private int unique;
	/**
	 *	XXXを取得する<BR>
	 *	@return XXX
	 */
	public String get1st(){ return Integer.toHexString(unique); }
	/**
	 * 
	 */
	private void set1st(){
		this.unique = hostUnique;
	}

	/* ***********************************************************************>> */;
	/**
	 *	XXX<BR>
	 */
	private long time;
	/**
	 *	XXXを取得する<BR>
	 *	@return XXX
	 */
	public String get2nd(){ return Long.toHexString(time); }
	/**
	 * 
	 */
	private void set2nd(){
		while (true) {
			this.time = System.currentTimeMillis();
			
			if (this.time < lastTime + DELAY) {
				try{
					Thread.sleep(DELAY);
				}catch(InterruptedException e){ ; }
					
				continue;
			}
			
			lastTime = time;
			break;
		}
	}

	/* ***********************************************************************>> */;
	/**
	 *	XXX<BR>
	 */
	private String address;
	/**
	 *	XXXを取得する<BR>
	 *	@return XXX
	 */
	public String get3rd(){ return address; }
	/**
	 * 
	 * @param addr
	 */
	public void set3rd(String addr){
		address = (addr == null ? hostaddress : MD5(addr));
	}

	// オーヴァーライド /////////////////////////////////////////////////////////////////
	//                                                               Over Ride Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	public String toString() {
		return get1st() + "-" + get2nd() + "-" + get3rd();
	}

	public boolean equals(Object obj) {
		if((obj != null) && (obj instanceof PseudoUUID)){
			PseudoUUID uuid = (PseudoUUID)obj;
			return (unique == uuid.unique &&
					 time   == uuid.time   &&
					 address.equals(uuid.address));
		}else{
			return false;
		}
	}

	// 内部メソッド /////////////////////////////////////////////////////////////////////
	//                                                                 Private Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	public static String MD5(String val){
		if(val == null) return null;
		if(md  == null) return null;
		
		byte[] digest = md.digest(val.getBytes());
		
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			s.append(Integer.toHexString((digest[i] & 0xf0) >> 4));
			s.append(Integer.toHexString(digest[i] & 0x0f));
		}
		
		return s.toString();
	}
	
	/* ***********************************************************************>> */;
	private static String generateNoNetworkID() {
//		Thread current = Thread.currentThread();
		String nid = Thread.activeCount()+
					System.getProperty("os.version")+
					System.getProperty("user.name")+
					System.getProperty("java.version");
		System.out.println(nid);
		
		return MD5(nid);
	}
	
	/* ***********************************************************************>> */;
    
	public static void main(String args[]) {
		try{
			System.out.println(InetAddress.getLocalHost().getHostAddress());
			System.out.println(new PseudoUUID());
			System.out.println(new PseudoUUID());
			System.out.println(new PseudoUUID());
			System.out.println(new PseudoUUID());
		}catch(Throwable ex){
			ex.printStackTrace();
		}
	}
}
