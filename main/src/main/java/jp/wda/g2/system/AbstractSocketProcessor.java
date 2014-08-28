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
package jp.wda.g2.system;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.ByteBuffer;

import jp.wda.g2.ConnectingConditions;
import jp.wda.g2.Connection;
import jp.wda.g2.SocketProcessor;
import jp.wda.g2.exception.GPSSException;
import jp.wda.gpss.util.Logger;
import jp.wda.gpss.util.PseudoUUID;


/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/23 16:00:00 ���� </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public abstract class AbstractSocketProcessor implements SocketProcessor {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * 
	 */
	public AbstractSocketProcessor(Connection connection, SockletContainer container) throws GPSSException {
		super();
		
		this.connection = connection;
		if(connection != null){
			this.ipAddress = connection.getSocketChannel().socket().getInetAddress().getHostAddress();
		}
		try{
			this.clientID = new PseudoUUID(ipAddress).toString();
		}catch(IOException ex){ throw new GPSSException(); }
		
		this.linkage = new SockletLinkage(this, container);
		terminated = false;
	}

	// �����t�B�[���h��` ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/** �V�X�e�����K�[ */
	protected final Logger syslog = Logger.getLogger(GPSSConstants.SYSTEMLOG_CATEGORY);

	/** XXX */
	private SockletLinkage linkage = null;
	
	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private Connection connection = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public Connection getConnection(){ return connection; }

	/* ***********************************************************************>> */;
	/**
	 *	�N���C�A���gID<BR>
	 */
	private String clientID;
	/**
	 *	�N���C�A���gID���擾���܂��B<BR>
	 *	@return �N���C�A���gID
	 *	@see jp.wda.g2.SocketProcessor#getClientID()
	 */
	public String getClientID(){ return clientID; }

	/* ***********************************************************************>> */
	/** �N���C�A���g�̃z�X�gIP�A�h���X */
	private String ipAddress = null;
	/**
	 * �N���C�A���g�̃z�X�gIP�A�h���X���擾���܂��B<BR>
	 * @return �N���C�A���g�̃z�X�gIP�A�h���X
	 * @see jp.wda.g2.SocketProcessor#getIPAddress()
	 */
	public String getIPAddress(){ return ipAddress; }

	/* ***********************************************************************>> */;
	/**
	 * �f�t�H���g�̃G���R�[�f�B���O��<BR>
	 */
	private String encoding = "UTF-8";
	/**
	 *	�g�p����f�t�H���g�̃G���R�[�f�B���O�����擾���܂��B<BR>
	 *	@return �f�t�H���g�̃G���R�[�f�B���O��
	 *	@see jp.wda.g2.SocketProcessor#getEncoding()
	 */
	public String getEncoding(){ return encoding; }
	/**
	 *	�g�p����f�t�H���g�̃G���R�[�f�B���O����ݒ肵�܂��B<BR>
	 *	@param s �ݒ�l<BR>
	 */
	public void setEncoding(String s){ encoding = s; }

	/* ***********************************************************************>> */
	/** �^�C���A�E�g����~���b�� */
	private long timeout = 0;
	/**
	 * �^�C���A�E�g����~���b�����擾���܂��B<BR>
	 * @return �^�C���A�E�g����~���b��
	 */
	public long getTimeout(){ return timeout; }
	/**
	 * �^�C���A�E�g����~���b����ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	public void setTimeout(long s){ timeout = s; }

	/* ***********************************************************************>> */
	/** �I���ς݃\�P�b�g�ł��邩�ǂ��� */
	private boolean terminated = false;
	/**
	 * �I���ς݃\�P�b�g�ł��邩�ǂ������擾���܂��B<BR>
	 * @return �I���ς݃\�P�b�g�ł���Ȃ�^
	 */
	public boolean isTerminated(){ return terminated; }

	/* ***********************************************************************>> */
	/** XXX */
	private Object attributes = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public Object getAttributes(){ return attributes; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	public void setAttributes(Object s){ attributes = s; }

	/* ***********************************************************************>> */
	/** XXX */
	private ConnectingConditions connectingConditions = null;
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public ConnectingConditions getConnectingConditions(){ return connectingConditions; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	public void setConnectingConditions(ConnectingConditions s){
		if(connectingConditions != null){ return; }
		connectingConditions = s;
	}
	
	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 
	 * @param command 
	 */
	public void doCommand(ByteBuffer command){
		try{
			linkage.doCommand(command);
		}catch(GPSSException ex){
			syslog.errormessage(ex);
			terminate();
		}catch(UndeclaredThrowableException ex){
			Throwable th = ex.getUndeclaredThrowable();
			if(th instanceof InvocationTargetException){
				th = ((InvocationTargetException)th).getTargetException();
				if(th instanceof GPSSException){
					syslog.errormessage((GPSSException)th);
					terminate();
					return;
				}
			}
			syslog.errormessage("EGSS50001", new Object[]{ getClientID(), command }, ex);
		}catch(Throwable ex){
			syslog.errormessage("EGSS50001", new Object[]{ getClientID(), command }, ex);
		}
	}
	
	/**
	 * 
	 */
	public void terminate(){
		synchronized (this) {
			if (this.terminated) {
				return;
			}
			this.terminated = true;
		}
		
		linkage.removeClient(true);
		getConnection().closeConnection();
	}

}
