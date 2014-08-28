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
 * [ïœçXóöó]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/26 2:11:01 ì±ì¸ </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		Takenori Adachi(TheCoolMuseum)
 */
public class IPAddressChecker extends ClientChecker {

	private String[] addresses;

	private int trueOperation;

	private int falseOperation;

	public IPAddressChecker(String addresses, int trueOperation, int falseOperation) {
		this.addresses = addresses.split(",");
		for (int i = 0; i < this.addresses.length; i++) {
			String str = this.addresses[i];
			str = str.trim();
			str = str.replaceAll("\\.", "\\\\.");
			str = str.replaceAll("\\*", ".*");
			this.addresses[i] = str;
		}
		this.trueOperation = trueOperation;
		this.falseOperation = falseOperation;
	}

	public int enter(SocketProcessor client) {
		String ipAddress = client.getIPAddress();
		return check(ipAddress);
	}

	private int check(String ipAddress) {
		for (int i = 0; i < this.addresses.length; i++) {
			if (ipAddress.matches(addresses[i])) {
				return trueOperation;
			}
		}
		return falseOperation;
	}
}
