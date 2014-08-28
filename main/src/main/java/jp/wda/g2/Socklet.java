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
 * Socklet�C���^�[�t�F�[�X<BR>
 * <BR>
 * �S�Ă�Socklet�́A���̃C���^�[�t�F�[�X����������K�v������܂��B<BR>
 * �ʏ�̏ꍇ�́Ajp.wda.g2.GeneralSocklet���p����������y�`���ł��B<BR>
 *
 * 
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [�ύX����]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 * 
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/25 14:40:03 ���� </dd>
 * 
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 * 
 * @author		A M O I
 */
public interface Socklet {

	/**
	 * �ڑ��p�����R�}���h��M��A�N���C�A���g�̌������s�����߂̃��\�b�h�B<BR>
	 * ���̃��\�b�h�́A�T�[�o���V�����N���C�A���g����̐ڑ����m�F�������_�ŁA
	 * ���̃N���C�A���g���������邽�߂ɌĂ΂�܂��B<BR>
	 * �ڑ��N���C�A���g�ɑ΂���ԏ��߂Ɉ�񂾂��Ă΂�܂��B<BR>
	 * SocketProcessor�N���X�̃��\�b�h���g�p���āA���̃N���X���K�؂ł��邩�������A
	 * �܂��N���C�A���g�ɕK�v�ȏ���������^���Ă��������B<BR>
	 * <BR>
	 * ���̃N���C�A���g���K�؂łȂ��Ɣ��f���ꂽ�ꍇ�́A�Ufalse��Ԃ��Ă��������B
	 * �����ɃN���C�A���g�\�P�b�g���I�����܂��B<BR>
	 * ���̑��̏ꍇ�́A�^��Ԃ��Ă��������B�^��Ԃ��ƁA�N���C�A���g�\�P�b�g���p�����܂��B<BR>
	 * 
	 * @param client   �ڑ������݂Ă���N���C�A���g
	 * @param linkage 
	 * @return ������A���I������ꍇ�͋U�B
	 */
	public boolean checkConnection(SocketProcessor client, SockletLinkage linkage);
	
	/**
	 * �N���C�A���g�폜�O�ɌĂяo����郁�\�b�h�B<BR>
	 * �ڑ����̃N���C�A���g���ؒf����A���̃A�v���P�[�V�������珜�O����钼�O�ɌĂ΂�܂��B<BR>
	 * �ڑ��N���C�A���g�ɑ΂��A�I���O�Ɉ�񂾂��Ă΂�܂��B<BR>
	 * ���̐ڑ����N���C�A���g�ցA���̃N���C�A���g���ؒf���ꂽ���Ƃ�ʒm����悤�ȏꍇ�́A
	 * ���̃��\�b�h���ŋL�q���Ă��������B<BR>
	 *
	 * @param client ���ꂩ��폜�����N���C�A���g
	 * @param linkage 
	 */
	public void preRemoveClient(SocketProcessor client, SockletLinkage linkage);
	
	/**
	 * �R�}���h���������邽�߂̃��\�b�h�B<BR>
	 * �ڑ����̃N���C�A���g����A�R�}���h�����M����Ă����Ƃ��ɌĂ΂�܂��B
	 * �T�u�N���X�͂��̃��b�\�h���ŁA�N���C�A���g���瑗���Ă����R�}���h���������Ă��������B<BR>
	 * �N���C�A���g�̐ڑ����ɉ��x�ł��Ă΂�܂��B<BR>
	 * <BR>
	 * �R�}���h�𑗂��Ă����\�P�b�g�ɑ΂��āA���b�Z�[�W�𑗐M�������ꍇ�́A�ȉ��̃��\�b�h���g�p���Ă��������B<BR>
	 * <BR>
	 * client.send(<I>���M���郁�b�Z�[�W</I>);<BR>
	 * <BR>
	 * �ڑ����̑S�N���C�A���g�փ��b�Z�[�W�𑗐M�������ꍇ�́A�ȉ��̃��\�b�h���g�p���Ă��������B<BR>
	 * <BR>
	 * sendToAllClients(<I>���M���郁�b�Z�[�W</I>); <BR>
	 * <BR>
	 * �܂��A����̃N���C�A���g�ւ̂݃��b�Z�[�W�𑗂肽���ꍇ�́A
	 * SocketProcessorFinder�h���N���X��p���Ĉȉ��̃��\�b�h���g�p���Ă��������B<BR>
	 * <BR>
	 * sendToClients(<I>���M���郁�b�Z�[�W</I>, <I>SocketProcessorFinder</I>);<BR>
	 * �܂��́A<BR>
	 * sendToClients(<I>���M���郁�b�Z�[�W</I>, <I>���M�������N���C�A���g�̌���������</I>);<BR>
	 * <BR>
	 * SocketProcessorFinder�ɂ́A���[�U�[���ł̌����p�N���X��p�����^�l�ł̌����p�N���X�Ȃǂ��p�ӂ���Ă��܂��B<BR>
	 * 
	 * @param client �R�}���h�𑗐M���Ă����N���C�A���g�\�P�b�g
	 * @param command ��M�����R�}���h
	 * @param linkage 
	 * @return �R�}���h��������
	 * @throws GPSSException �R�}���h��������GPSSException�𓊂���ƁA�����ɃN���C�A���g�\�P�b�g���I�����܂��B
	 */
	public Object doCommand(SocketProcessor client, ByteBuffer command, SockletLinkage linkage) throws GPSSException;
	
	/**
	 * Socklet���T�[�r�X���~����ۂɁA�Ҏ󂯃��C���T�[�o�ɂ���ČĂяo����܂��B<BR>
	 * Socklet�̏I���������[�`�����L�q���A���̃��\�b�h���������Ă��������B
	 */
	public void destroy();
	
	/**
	 * 
	 *
	 */
	public void destroyAllSocklets();

}
