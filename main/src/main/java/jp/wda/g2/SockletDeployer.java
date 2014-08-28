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

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/05/02 19:16:19 ���� </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public interface SockletDeployer extends Socklet {

	/**
	 * 
	 * @param port
	 */
	public void setPort(int port);
	
	/**
	 * 
	 * @param name
	 * @param socklet
	 */
	public void addChild(String name, Socklet socklet);
	
	/**
	 * �\�P�b�g���󂯓����h���C�����w�肵�܂��B
	 * domain:port�̌`�Ŏw�肵�Ă��������B
	 * ��jgpss.wda.jp:9090
	 * 
	 * �R�����ȍ~���ȗ�����ƁA���̃T�[�o�Ŏg�p���Ă���|�[�g�Ƃ݂Ȃ��܂��B
	 * �܂��A�ǂ̃h���C��������󂯓����ꍇ�́A*�Ƃ��Ă��������B
	 * 
	 * @param domain
	 */
	public void allowDomain(String domain);
	
	/**
	 * �N���X�h���C���|���V�[��XML������ŕԂ��܂�
	 * 
	 * @return �N���X�h���C���|���V�[
	 */
	public String getCrossDomainPolicy();

}
