package jp.wda.g2.util;

import java.io.StringReader;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jp.wda.g2.SocketProcessor;
import jp.wda.g2.exception.GPSSException;
import jp.wda.g2.exception.XMLAttributeException;
import jp.wda.g2.exception.XMLParseException;
import jp.wda.g2.system.GPSSConstants;
import jp.wda.gpss.util.Logger;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 * 非常に単純なXML作成用クラス
 *
 * @version	1.00		2003/07/07
 * @since		1.00β007	2003/06/08
 * @author	amoi
 */
public class SimpleXMLCreator {
	// コンストラクタ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 * ノード名を指定してオブジェクトを構築するコンストラクタ
	 * @param name ノード名
	 */
	public SimpleXMLCreator(String name) {
		super();
		setName(name);
		setText("");

		attr = new Hashtable<Object, Object>();
		children = new LinkedList<SimpleXMLCreator>();
	}
	/**
	 * 一つのテキストノードを持ったシンプルなXMLオブジェクトを構築するコンストラクタです。
	 *
	 * @param name ノード名
	 * @param textNode テキストノード
	 */
	public SimpleXMLCreator(String name, String textNode) {
		this(name);
		setText(textNode);
	}

	// 内部フィールド定義 ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 固有属性一覧
	 */
	private Hashtable<Object, Object> attr;
	/**
	 * 子ノード一覧
	 */
	private LinkedList<SimpleXMLCreator> children;
	/**
	 * テキストノード
	 */
	private String text;
	/** XXXX */
	private boolean textIsCDATA = false;

	// プロパティ ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/**
	 *	ノード名<BR>
	 */
	private String name = null;
	/**
	 *	ノード名を取得する<BR>
	 *	@return ノード名
	 */
	public String getName() {
		return name;
	}
	/**
	 *	ノード名を設定する<BR>
	 *	@param s 設定値<BR>
	 */
	public void setName(String s) {
		name = s;
	}

	// インスタンスメソッド /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 * このオブジェクトに、指定された名前の固有属性値を設定します<BR>
	 * @param key 固有属性名<BR>
	 * @param value 固有属性値<BR>
	 */
	public final void setAttribute(Object key, Object value) {
		attr.put(key, value);
	}
	/**
	 * このオブジェクトに設定されている、指定された名前の固有属性値を取得します。<BR>
	 * @param key 固有属性名
	 * @return 固有属性値
	 */
	public final Object getAttribute(Object key) {
		return attr.get(key);
	}
	/**
	 * 固有属性に格納されている全ての属性一覧のSetビューを取得します。<BR>
	 * @return 属性一覧のSetビュー<BR>
	 */
	public Set<Map.Entry<Object,Object>> getAttributes() {
		return attr.entrySet();
	}
	/**
	 * 指定された属性名の固有属性が設定されているかを確認します。<BR>
	 * @param key 固有属性名<BR>
	 * @return 指定された属性名が設定されていれば真<BR>
	 */
	public boolean containsAttributeKey(Object key) {
		return attr.containsKey(key);
	}
	/**
	 * 固有属性に格納されている属性名一覧のSetビューを取得します。<BR>
	 * @return 属性名一覧のSetビュー<BR>
	 */
	public Set<Object> getAttributeKeys() {
		return attr.keySet();
	}
	/**
	 * 指定された属性名の固有属性を削除します。<BR>
	 * @param key 固有属性名<BR>
	 */
	public Object removeAttribute(Object key) {
		return attr.remove(key);
	}

	/**
	 * このオブジェクトに、指定された名前の固有属性値を設定します<BR>
	 * @param key 固有属性名<BR>
	 * @param value 固有属性整数値<BR>
	 */
	public final void setAttribute(Object key, int value) {
		attr.put(key, new Integer(value));
	}
	/**
	 * このオブジェクトに設定されている、指定された名前の固有属性値を整数値として取得します。<BR>
	 * @param key 固有属性名
	 * @return 固有属性整数値
	 */
	public final int getAttributeInt(Object key) throws XMLAttributeException {
		Object ret = attr.get(key);
		if (ret instanceof Integer) {
			return ((Integer) ret).intValue();
		}
		throw new XMLAttributeException("指定された属性(" + key + ")はint型ではありません。");
	}
	/**
	 * このオブジェクトに、指定された名前の固有属性値を長整数値として設定します<BR>
	 * @param key 固有属性名<BR>
	 * @param value 固有属性長整数値<BR>
	 */
	public final void setAttribute(Object key, long value) {
		attr.put(key, new Long(value));
	}
	/**
	 * このオブジェクトに設定されている、指定された名前の固有属性値を長整数値として取得します。<BR>
	 * @param key 固有属性名
	 * @return 固有属性長整数値
	 */
	public final long getAttributeLong(Object key) throws XMLAttributeException {
		Object ret = attr.get(key);
		if (ret instanceof Long) {
			return ((Long) ret).longValue();
		}
		throw new XMLAttributeException("指定された属性(" + key + ")はlong型ではありません。");
	}
	/**
	 * このオブジェクトに、指定された名前の固有属性値を実数値として設定します<BR>
	 * @param key 固有属性名<BR>
	 * @param value 固有属性実数値<BR>
	 */
	public final void setAttribute(Object key, double value) {
		attr.put(key, new Double(value));
	}
	/**
	 * このオブジェクトに設定されている、指定された名前の固有属性値を実数値として取得します。<BR>
	 * @param key 固有属性名
	 * @return 固有属性長実数値
	 */
	public final double getAttributeDouble(Object key)
		throws XMLAttributeException {
		Object ret = attr.get(key);
		if (ret instanceof Double) {
			return ((Double) ret).doubleValue();
		}
		throw new XMLAttributeException("指定された属性(" + key + ")はdouble型ではありません。");
	}
	/**
	 * このオブジェクトに、指定された名前の固有属性値を真偽値として設定します<BR>
	 * @param key 固有属性名<BR>
	 * @param value 固有属性真偽値<BR>
	 */
	public final void setAttribute(Object key, boolean value) {
		attr.put(key, new Boolean(value));
	}
	/**
	 * このオブジェクトに設定されている、指定された名前の固有属性値を真偽値として取得します。<BR>
	 * @param key 固有属性名
	 * @return 固有属性真偽値
	 */
	public final boolean getAttributeBoolean(Object key)
		throws XMLAttributeException {
		Object ret = attr.get(key);
		if (ret instanceof Boolean) {
			return ((Boolean) ret).booleanValue();
		}
		throw new XMLAttributeException("指定された属性(" + key + ")はboolean型ではありません。");
	}

	/* ***********************************************************************>> */;
	/**
	 * このノードに子ノードを追加します
	 * @param name 子ノード名
	 * @return 追加された子ノード
	 */
	public SimpleXMLCreator addChild(String name) {
		SimpleXMLCreator child = new SimpleXMLCreator(name);
		children.add(child);
		return child;
	}
	/**
	 * このノードに子ノードを追加します
	 * @param child 追加する子ノード
	 */
	public void addChild(SimpleXMLCreator child) {
		children.add(child);
	}
	/**
	 * 指定された子ノードを削除します
	 * @param child 削除する子ノード
	 */
	public void removeChild(SimpleXMLCreator child) {
		children.remove(child);
	}
	/**
	 * 指定された名前を持つはじめの子ノードを削除します
	 * @param name 削除する子ノード名
	 */
	public void removeChild(String name) {
		Iterator<SimpleXMLCreator> it = children.iterator();
		while (it.hasNext()) {
			SimpleXMLCreator child = it.next();
			if (child.getName().equals(name)) {
				it.remove();
				return;
			}
		}
	}
	/**
	 * 指定された名前を持つ全ての子ノードを削除します
	 * @param name 削除する子ノード名
	 */
	public void removeChildren(String name) {
		Iterator<SimpleXMLCreator> it = children.iterator();
		while (it.hasNext()) {
			SimpleXMLCreator child = it.next();
			if (child.getName().equals(name)) {
				it.remove();
			}
		}
	}
	/**
	 * 全ての子ノードを削除します
	 */
	public void removeChildren() {
		children = new LinkedList<SimpleXMLCreator>();
	}
	/**
	 * このノードに設定されている、全ての子ノードを取得します。
	 * @return 子ノード一覧
	 */
	public List<SimpleXMLCreator> getChildren() {
		return new LinkedList<SimpleXMLCreator>(children);
	}
	/**
	 * このノードに設定されている、指定した名前をもつすべての子ノードを取得します。
	 * @param name 取得する子ノード名
	 * @return 子ノード一覧
	 */
	public List<SimpleXMLCreator> getChildren(String name) {
		List<SimpleXMLCreator> list = new LinkedList<SimpleXMLCreator>();
		Iterator<SimpleXMLCreator> it = children.iterator();
		while (it.hasNext()) {
			SimpleXMLCreator child = it.next();
			if (child.getName().equals(name)) {
				list.add(child);
			}
		}
		return list;
	}
	/**
	 * このノードに設定されている、子ノード数を取得します。
	 * @return 子ノード数
	 */
	public int sizeOfChildren() {
		return children.size();
	}
	/**
	 * このノードに設定されている、指定された順番の子ノードを取得します。
	 * @param idx 設定順番号 一番初めに設定されたノードは0です。
	 * @return 子ノード
	 */
	public SimpleXMLCreator getChild(int idx) {
		if (idx >= children.size()) {
			return null;
		}
		return (SimpleXMLCreator) children.get(idx);
	}
	/**
	 * このノードに設定されている、指定された名前を持つはじめの子ノードを取得します
	 * @param name 削除する子ノード名
	 * @return 子ノード
	 */
	public SimpleXMLCreator getChild(String name) {
		Iterator<SimpleXMLCreator> it = children.iterator();
		while (it.hasNext()) {
			SimpleXMLCreator child = it.next();
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}
	/**
	 * テキストノードを設定
	 * @param textNode テキストノード文字列
	 */
	public void setText(String textNode) {
		text = textNode;
	}
	/**
	 * テキストノードを取得
	 * @return テキストノード文字列
	 */
	public String getText() {
		return text;
	}
	/**
	 * CDATAノードを設定
	 * @param textNode CDATAノード文字列
	 */
	public void setCData(String textNode) {
		text = textNode;
		textIsCDATA = true;
	}
//
//	/* ***********************************************************************>> */;
//	/**
//	 * コンストラクタで指定されたSockletに所属する全クライアントに、
//	 * このオブジェクトに設定されているXML形式の文字列を送信します。
//	 */
//	public void send() {
//		if (linkage == null) {
//			return;
//		}
//		linkage.sendToAllClients(toString());
//	}
//	/**
//	 * コンストラクタで指定されたSockletに所属するクライアントの内、
//	 * 指定された条件のクライアントにのみ
//	 * このオブジェクトに設定されているXML形式の文字列を送信します。
//	 * @param finder 送信したいクライアント検索用オブジェクト
//	 */
//	public void send(Finder finder) {
//		if (socklet == null) {
//			return;
//		}
//		socklet.sendToClients(toString(), finder);
//	}
//	/**
//	 * コンストラクタで指定されたSockletに所属するクライアントの内、
//	 * 指定された条件のクライアントにのみ
//	 * このオブジェクトに設定されているXML形式の文字列を送信します。
//	 * @param condition 送信したいクライアントの検索条件文
//	 */
//	public void send(String condition) {
//		if (socklet == null) {
//			return;
//		}
//		socklet.sendToClients(toString(), condition);
//	}
	/**
	 * 指定されたクライアントに
	 * このオブジェクトに設定されているXML形式の文字列を送信します。
	 * @param client 送信したいクライアント
	 */
	public void send(SocketProcessor client) {
		if (client == null) {
			return;
		}
		client.send(toString());
	}

	/* ***********************************************************************>> */;
	/**
	 * このオブジェクトのXML形式で表現された文字列を返します。
	 *
	 * @return このオブジェクトのXML形式で表現された文字列
	 */
	public String toString() {
		StringBuffer ret = new StringBuffer("<");
		ret.append(name);

		Set<Map.Entry<Object,Object>> attributes = getAttributes();
		Iterator<Map.Entry<Object,Object>> it = attributes.iterator();
		while (it.hasNext()) {
			Map.Entry<Object,Object> entry = it.next();

			Object value = entry.getValue();
			value = convertEntity(value);

			ret.append(' ');
			ret.append(entry.getKey());
			ret.append("=\"");
			ret.append(value);
			ret.append("\"");
		}
		if (children.size() == 0 && text == "") {
			ret.append(" />");
			return ret.toString();
		}

		ret.append('>');
		if(textIsCDATA) {
			ret.append("<![CDATA[");
			ret.append(text);
			ret.append("]]>");
		} else {
			ret.append(convertEntity(text));
		}
		for (int i = 0; i < children.size(); i++) {
			ret.append(children.get(i).toString());
		}
		ret.append("</");
		ret.append(name);
		ret.append('>');

		return ret.toString();
	}
	/**
	 * XMLエンティティ参照への変換
	 * @param text 変換前のテキスト
	 * @return 変換後のテキスト
	 */
	private Object convertEntity(Object text) {
		String str;
		StringBuffer buf;
		char c;

		if (text != null && text instanceof String) {
			str = (String) text;
			buf = new StringBuffer(str.length() + 20);
		} else {
			return text;
		}

		for (int i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			switch (c) {
				case '<' :
					buf.append("&lt;");
					break;
				case '>' :
					buf.append("&gt;");
					break;
				case '\'' :
					buf.append("&apos;");
					break;
				case '\"' :
					buf.append("&quot;");
					break;
				case '&' :
					buf.append("&amp;");
					break;
				default :
					buf.append(c);
					break;
			}
		}
		return buf.toString();
	}

	// クラスメソッド ///////////////////////////////////////////////////////////////////
	//                                                                   Class Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 指定されたXML文字列を解析し、新たなSimpleXMLCreatorオブジェクトを構築します。
	 *
	 * @param xml 解析するXML文字列
	 * @return 解析されたSimpleXMLCreatorオブジェクト
	 */
	public static SimpleXMLCreator parse(String xml) throws GPSSException {
		xml = weedout(xml);

		Logger syslog = Logger.getLogger(GPSSConstants.SYSTEMLOG_CATEGORY);
		InputSource src = new InputSource(new StringReader(xml));
		Document doc = null;

		try{
			DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbfactory.newDocumentBuilder();
			doc = builder.parse(src);
		}catch(Throwable ex){
			syslog.error("command:" + xml);
			syslog.fatalmessage(ex);
			throw new XMLParseException(ex.getMessage());
		}

		if(doc == null){ throw new XMLParseException("doc is null."); }

		// XMLオブジェクト作成
		Element root = doc.getDocumentElement();
		SimpleXMLCreator ret = new SimpleXMLCreator(root.getTagName());
		setXMLElements(ret, root);
		return ret;
	}

	private static void setXMLElements(SimpleXMLCreator xml, Element element) {
		NamedNodeMap attrs = element.getAttributes();

		for (int i = 0; i < attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			xml.setAttribute(attr.getNodeName(), attr.getNodeValue());
		}

		NodeList children = element.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if(node instanceof CDATASection) {
				xml.setText(((CDATASection)node).getWholeText());
				xml.textIsCDATA = true;
				continue;
			}
			if(node instanceof Text){
				xml.setText(((Text)node).getWholeText());
				continue;
			}
			if(!(node instanceof Element)){ continue; }

			Element child = (Element)node;
			SimpleXMLCreator childxml = xml.addChild(child.getTagName());
			setXMLElements(childxml, child);
		}
	}

	/**
	 * 不正なXMLを防止するためDOCTYPE宣言などを削除する。
	 *
	 * @param xml
	 * @return
	 */
	public static String weedout(String xml) {
		if(xml == null || xml.length() == 0) { return ""; }
		if(xml.startsWith("<?")) { xml = xml.substring(xml.indexOf("?>") + 2); }

		int index = xml.indexOf("<!");
		while(index >= 0) {
			if(xml.length() >= index + 9 && "<![CDATA[".equals(xml.substring(index, index + 9))) {
				index = xml.indexOf("<!", index + 1);
			} else {
				break;
			}
		}
		if(index < 0) { return xml; }
		int counter = 0, endIndex = xml.length();
		for(int i = index + 1; i < xml.length(); i++) {
			if(xml.charAt(i) == '<') { counter++; }
			if(xml.charAt(i) == '>') { counter--; }
			if(counter < 0) { endIndex = i + 1; break; }
		}
		xml = xml.substring(0, index) + xml.substring(endIndex);

		return weedout(xml.trim());
	}

	public static void main(String[] args) {
		SimpleXMLCreator node = new SimpleXMLCreator("node");
		node.setAttribute("attr", "<Test>=\"value\"");
		System.out.println(node);

		String xml = "<TagTest name=\"name dayo\" value=\"value dayo\"><test><![CDATA[1111]]></test><test><![CDATA[2222]]></test></TagTest><!DOCTYPE 例 [<!ENTITY copy \"@\">]>";
		try{
			SimpleXMLCreator creator = SimpleXMLCreator.parse(xml);
			System.out.println(creator.getName());
		}catch(Throwable ex){
			ex.printStackTrace();
		}
	}
}

