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
package jp.wda.g2.extention.socklet;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.wda.g2.GeneralSocklet;
import jp.wda.g2.SockletRequest;
import jp.wda.g2.exception.CommandParseException;
import jp.wda.g2.exception.GPSSException;

/**
 * 
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/03/02 23:45:07 ���� </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public abstract class CommandSocklet extends GeneralSocklet implements ICommandSocklet {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * 
	 */
	public CommandSocklet() {
		super();
	}
	
	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private boolean execIgnoreCase = true;
	/**
	 * �R�}���h���̑啶������������ʂ��邩�ǂ��������肵�܂��B
	 * �^��Ԃ��ꍇ�́A�啶������������ʂ��܂���B
	 * @return �啶������������ʂ��Ȃ��Ȃ�^
	 */
	public boolean isExecIgnoreCase(){ return execIgnoreCase; }
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	public void setExecIgnoreCase(boolean s){ execIgnoreCase = s; }
	
	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/** {@inheritDoc} */
	public Object doCommand(SockletRequest req) throws GPSSException{
		CommandRequest request = new CommandRequestImpl(req);
		preProcess(request);
		
		request = parseCommand((CommandRequestImpl)request);
		if(request.getName() == null || request.getName().length() == 0){
			throw new CommandParseException(request.getCommand());
		}
		
		return doCommandMethod(request);
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws GPSSException
	 */
	public Object doCommandMethod(CommandRequest request) throws GPSSException{
		String name = request.getName();
		if(execIgnoreCase){
			name = name.substring(0, 1).toUpperCase() + 
					(name.length() > 1 ? 
					 name.substring(1).toLowerCase() : "");
		}
		
		// ���\�b�h����
		String methodname = request.toString();
		Method method = methodsCache.get(methodname);
		if(method == null){
			try{
				method = getClass().getMethod("cmd" + name, request.getType());
				methodsCache.put(methodname, method);
			}catch(NoSuchMethodException e){
				return cmdUndefined(request);
			}catch(SecurityException e){
				e.printStackTrace();
				return null;
			}
		}
		
		// �R�}���h���\�b�h���s
		try{
			return method.invoke(this, request.getParams());
		}catch(Throwable e){
			syslog.fatalmessage(e);
			throw new CommandParseException(request.getCommand());
		}
	}
	
	private ConcurrentMap<String, Method> methodsCache = new ConcurrentHashMap<String, Method>();
	
	/**
	 * �����Ă����R�}���h����͂��A���\�b�h�����t���N�V�������邽�߂̏����쐬���܂��B<BR>
	 * �ڑ����̃N���C�A���g����A�R�}���h�����M����Ă����Ƃ��ɌĂ΂�܂��B<BR>
	 * �h���N���X�ł͂��̃��\�b�h���I�[�o�[���C�h���A�R�}���h�̉��ߕ��@���L�q���Ă��������B<BR>
	 * 
	 * @param request ��M�����R�}���h���
	 */
	protected abstract CommandRequest parseCommand(CommandRequestImpl request) throws GPSSException;
	
	/**
	 * �R�}���h���������t���N�V�������\�b�h����`���̏������s�Ȃ��܂��B
	 * 
	 * @param request
	 */
	public Object cmdUndefined(CommandRequest request) throws GPSSException{
		return request.handoverRequest();
	}
	
	/**
	 * �R�}���h���s�O�������s�Ȃ��܂��B<BR>
	 * ���̃��\�b�h�̖߂�l�́A�R�}���h�������t���N�V�������\�b�h�Ɉ����n����܂��B<BR>
	 * �S�ẴR�}���h�ɋ��ʂ��鏈�����L�q�������ꍇ�́A���̃��\�b�h���I�[�o�[���C�h���Ă��������B<BR>
	 * SockletException��O�𓊂���ƁA�R�}���h�̉��߂𒆎~���AdoCommand���Œ����ɋU��Ԃ��܂��B
	 * 
	 * @param request ��M�����R�}���h���
	 * @throws GPSSException �R�}���h���ߏ����𒆎~�������ꍇ�B
	 */
	public void preProcess(CommandRequest request) throws GPSSException{
	}

}
