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
package jp.wda.g2.extention.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.Socklet;
import jp.wda.g2.exception.AccessDeniedException;
import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.extention.nio.handler.AcceptHandler;
import jp.wda.g2.system.AbstractSocketReactor;
import jp.wda.g2.system.AccessControledSocklet;
import jp.wda.g2.system.SockletContainer;
import jp.wda.g2.system.SockletLinkage;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.StringUtil;

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
public class NioSocketReactor extends AbstractSocketReactor{
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �f�t�H���g�̐ݒ��p���ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 *
	 */
	public NioSocketReactor() {
		super();
		this.terminated = false;
	}

	// �����t�B�[���h��` ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �T�[�o�[�`�����l��
	 */
	private ServerSocketChannel serverChannel;
	/**
	 * ����Z���N�^�[
	 */
	private Selector selector;

	/**
	 * �T�[�o�I���X�C�b�`
	 */
	private boolean terminator = false;

	/**
	 * �T�[�o�I���σX�C�b�`
	 */
	private boolean terminated = false;

	/* ***********************************************************************>> */
	/** XXX */
	private ExecutorService executor = Executors.newCachedThreadPool();
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public ExecutorService getPool(){ return executor; }
	/**
	 *
	 * @param r
	 */
	public void execute(Runnable r){
		executor.execute(r);
	}

	/* ***********************************************************************>> */

	/**
	 *
	 * @param container
	 * @param client
	 */
	public void notifyAcceptance(SockletContainer container, SocketProcessor client){
		super.notifyAcceptance(container, client);

	}

	/**
	 *
	 * @param container
	 * @param client
	 */
	public void notifyDesertion(SockletContainer container, SocketProcessor client){
		super.notifyDesertion(container, client);

	}

	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private S2Container container = null;
	/**
	 * XXX��ݒ肵�܂��B<BR>
	 * @param s �ݒ�l<BR>
	 */
	public void setContainer(S2Container s){ container = s; }

	/* ***********************************************************************>> */
	/** �^�C���A�E�g�����N���C�A���g���������鏄��Ԋu�i�~���b�j */
	private long sweeperDelay = 10000;
	/**
	 * �^�C���A�E�g�����N���C�A���g���������鏄��Ԋu���~���b�Őݒ肵�܂��B<BR>
	 * �f�t�H���g��10�b�ł��B
	 *
	 * @param s �ݒ�l<BR>
	 */
	public void setSweeperDelay(long s){ sweeperDelay = s; }

	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 */
	public void accept(){
		try{
			openChannel();
		}catch(IOException ex){
			syslog.errormessage("EGSS00001", ex);
			return;
		}

		syslog.infomessage("IGSS00001");

		ScheduledExecutorService sweeperExecutor = Executors.newSingleThreadScheduledExecutor();
		sweeperExecutor.scheduleWithFixedDelay(
				new SleeperSweeper(selector), sweeperDelay, sweeperDelay, TimeUnit.MILLISECONDS);

		try{
			while (true){
				if(terminator){ break; }

				try{
					select();
				} catch (ClosedSelectorException cse) {
					syslog.errormessage("EGSS00002", cse);
					break;
				} catch (IOException ioe) {
					syslog.errormessage("EGSS90001", ioe);
					break;
				} catch (RuntimeException re) {
					syslog.errormessage("EGSS90002", re);
				} catch (Throwable e) {
					syslog.fatalmessage(e);
					break;
				}
			}
		}finally{
			try {
				if (serverChannel != null){ serverChannel.close(); }
				if (selector != null)     { selector.close();      }
			} catch (Exception e) {
				;
			}

			if (sweeperExecutor != null) { sweeperExecutor.shutdown(); }
			if (executor != null)        { executor.shutdownNow();     }

			syslog.infomessage("IGSS00002");

			container.destroy();
			this.terminator = false;
			this.terminated = true;
		}
	}

	private void openChannel() throws IOException{
		this.serverChannel = ServerSocketChannel.open();
		this.serverChannel.configureBlocking(false);

		ServerSocket serverSocket = serverChannel.socket();
		serverSocket.bind(new InetSocketAddress(getPort()));

		this.selector = Selector.open();
		SelectionKey acceptKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		acceptKey.attach(new AcceptHandler(acceptKey, this));
	}

	private void select() throws IOException{
		int n = selector.select(1000);
		if(n == 0){ return; }

		Iterator<SelectionKey> it = selector.selectedKeys().iterator();
		while(it.hasNext()){
			SelectionKey key = it.next();
			it.remove();

			dispatch(key);
		}
	}

	/**
	 * �Z���N�V�����L�[�ɃA�^�b�`���ꂽ�I�u�W�F�N�g��run()���\�b�h�����s���܂��B
	 * @param key
	 */
	private void dispatch(SelectionKey key) {
		Runnable handler = (Runnable)(key.attachment());
		if (handler == null) { return; }

		try {
			handler.run();
		} catch (Throwable e) {
			// �G���[���O�o��
			syslog.error("", e);
		}
	}

	/**
	 *
	 */
	public synchronized void shutdown(){
		if(terminator){ return; }

		getDefaultSocklet().destroyAllSocklets();

		SocketProcessor[] allclients = getDefaultSockletContainer().getAllClients();
		for(int i = 0; i < allclients.length; i++){
			allclients[i].terminate();
		}

		terminator = true;
		selector.wakeup();
	}

	// �����N���X ///////////////////////////////////////////////////////////////////////
	//                                                                     Inner Class //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/** XXX */
	private SystemCommandSocklet systemcommand = new SystemCommandSocklet();
	/**
	 * XXX���擾���܂��B<BR>
	 * @return XXX
	 */
	public Socklet getSystemCommandSocklet(){ return systemcommand; }
	/**
	 *
	 * @param ipaddr
	 */
	public void systemCommandAcceptFrom(String ipaddr){
		systemcommand.acceptFrom(ipaddr);
	}
	/**
	 *
	 * @param ipaddr
	 */
	public void systemCommandRejectFrom(String ipaddr){
		systemcommand.rejectFrom(ipaddr);
	}

	/**
	 *
	 *
	 *
	 */
	private class SystemCommandSocklet extends AccessControledSocklet{

//		/** {@inheritDoc} */
//		public boolean checkConnection(SocketProcessor client, SockletHolder socklets){
//			boolean allow = super.checkConnection(client, socklets);
//
//			client.setAttributes(new Boolean(allow));
//			client.send(allow ? "+OK" : "-Your connection is not allowed.");
//
//			return allow;
//		}

		/** {@inheritDoc} */
		public boolean accept(SocketProcessor client, SockletLinkage linkage){
			client.setAttributes(new Boolean(true));
			client.send("+OK");
			return true;
		}

		/** {@inheritDoc} */
		public boolean denied(SocketProcessor client, SockletLinkage linkage){
			client.setAttributes(new Boolean(false));
			client.send("-Your connection is not allowed.");
			return false;
		}

		/** {@inheritDoc} */
		public void desert(SocketProcessor client, SockletLinkage linkage){
		}

		/** {@inheritDoc} */
		public void destroy(){

		}

		private boolean getAllowFlag(SocketProcessor client){
			Boolean result = (Boolean)client.getAttributes();
			return result == null ? false : result.booleanValue();
		}

		/** {@inheritDoc} */
		public Object doCommand(SocketProcessor client, ByteBuffer command, SockletLinkage linkage) throws GPSSException{
			if(!getAllowFlag(client)){ throw new AccessDeniedException();}

			String cmd = Charset.forName(client.getEncoding()).decode(command).toString();

			if(StringUtil.isEmpty(cmd)){
				client.terminate("+Good bye...");
				return null;
			}

			if(cmd.equals(SHUTDOWN)){
				client.terminate();
				shutdown();

				return null;
			}

			if(cmd.equals(RESTART)){
				String config = container.getPath();

				client.terminate();
				shutdown();
				while(!terminated){ ; }

				startServer(config);

				return null;
			}

			client.send("-" + cmd + " is not found.");

			return null;
		}

		public static final String RESTART  = "restart";
		public static final String SHUTDOWN = "shutdown";
		@SuppressWarnings("unused") public static final String RELOAD   = "reload";
		@SuppressWarnings("unused") public static final String GETLIST  = "getSockletsList";

	}
}
