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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import jp.wda.g2.exception.GPSSException;

import org.seasar.framework.message.MessageFormatter;

/**
 *
 *
 * <div style="font-weight:bold; font-size:10.5pt;">
 * [変更履歴]
 * </div><dl style="margin:0px; border:1px solid #eee; padding:10px; font-size:10pt;">
 *
 * <dt> 2.0.0-a1 </dt><dd> 2006/02/23 16:00:00 導入 </dd>
 *
 * </dl>
 * @version	2.0.0-a1
 * @since		2.0.0-a1
 *
 * @author		A M O I
 */
public final class Logger {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @param
	 */
	private Logger(String name) {
		log = org.slf4j.LoggerFactory.getLogger(name);
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	private static Map<String, Logger> loggerMap  = Collections.synchronizedMap(new HashMap<String, Logger>());

	private org.slf4j.Logger log;

	// クラスメソッド ///////////////////////////////////////////////////////////////////
	//                                                                   Class Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @param clazz
	 * @return
	 */
	public static final Logger getLogger(Class<?> clazz) {
		return getLogger(clazz.getName());
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	public static final Logger getLogger(String name) {
		Logger logger = (Logger) loggerMap.get(name);
		if (logger == null) {
			logger = new Logger(name);
			loggerMap.put(name, logger);
		}
		return logger;
	}

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 *
	 * @return
	 */
	public final boolean isDebugEnabled() { return log.isDebugEnabled(); }
	/**
	 *
	 * @return
	 */
	public final boolean isInfoEnabled()  { return log.isInfoEnabled(); }

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	public final void debug(Object message, Throwable throwable) {
		if (isDebugEnabled()) {
			log.debug(String.valueOf(message), throwable);
		}
	}

	public final void debug(Object message) {
		if (isDebugEnabled()) {
			log.debug(String.valueOf(message));
		}
	}

	public final void info(Object message, Throwable throwable) {
		if (isInfoEnabled()) {
			log.info(String.valueOf(message), throwable);
		}
	}

	public final void info(Object message) {
		if (isInfoEnabled()) {
			log.info(String.valueOf(message));
		}
	}

	public final void warn(Object message, Throwable throwable) {
		log.warn(String.valueOf(message), throwable);
	}

	public final void warn(Object message) {
		log.warn(String.valueOf(message));
	}

	public final void error(Object message, Throwable throwable) {
		log.error(String.valueOf(message), throwable);
	}

	public final void error(Object message) {
		log.error(String.valueOf(message));
	}

	public final void fatal(Object message, Throwable throwable) {
		log.error(String.valueOf(message), throwable);
	}

	public final void fatal(Object message) {
		log.error(String.valueOf(message));
	}

	public final void log(Throwable throwable) {
		error(throwable.getMessage(), throwable);
	}

	public final void log(String messageCode, Object[] args) {
		log(messageCode, args, null);
	}

	public final void log(String messageCode, Object[] args, Throwable throwable) {
		char messageType = messageCode.charAt(0);
		if (isEnabledFor(messageType)) {
			String message = MessageFormatter.getSimpleMessage(messageCode,
					args);
			switch (messageType) {
			case 'D':
				log.debug(message, throwable);
				break;
			case 'I':
				log.info(message, throwable);
				break;
			case 'W':
				log.warn(message, throwable);
				break;
			case 'E':
				log.error(message, throwable);
				break;
			case 'F':
				log.error(message, throwable);
				break;
			default:
				throw new IllegalArgumentException(String.valueOf(messageType));
			}
		}
	}

	private boolean isEnabledFor(final char messageType) {
		switch (messageType) {
		case 'D':
			return log.isDebugEnabled();
		case 'I':
			return log.isInfoEnabled();
		case 'W':
			return log.isWarnEnabled();
		case 'E':
			return log.isErrorEnabled();
		case 'F':
			return log.isErrorEnabled();
		default:
			throw new IllegalArgumentException(String.valueOf(messageType));
		}
	}

	public final void infomessage(String messageCode) {
		if (isInfoEnabled()) {
			log.info(MessageFormatter.getSimpleMessage(messageCode, null));
		}
	}

	public final void infomessage(String messageCode, Object[] args) {
		if (isInfoEnabled()) {
			log.info(MessageFormatter.getSimpleMessage(messageCode, args));
		}
	}

	public final void infomessage(String messageCode, Throwable throwable) {
		if (isInfoEnabled()) {
			log.info(MessageFormatter.getSimpleMessage(messageCode, null), throwable);
		}
	}

	public final void infomessage(String messageCode, Object[] args, Throwable throwable) {
		if (isInfoEnabled()) {
			log.info(MessageFormatter.getSimpleMessage(messageCode, args), throwable);
		}
	}

	public final void errormessage(String messageCode) {
		log.error(MessageFormatter.getMessage(messageCode, null));
	}

	public final void errormessage(String messageCode, Object[] args) {
		log.error(MessageFormatter.getMessage(messageCode, args));
	}

	public final void errormessage(String messageCode, Throwable throwable) {
		log.error(MessageFormatter.getMessage(messageCode, null), throwable);
	}

	public final void errormessage(String messageCode, Object[] args, Throwable throwable) {
		log.error(MessageFormatter.getMessage(messageCode, args), throwable);
	}

	public final void errormessage(GPSSException exception) {
		log.error("", exception);
	}

	public final void fatalmessage(Throwable throwable) {
		log.error(MessageFormatter.getMessage("EGSS99999", null), throwable);
	}

}
