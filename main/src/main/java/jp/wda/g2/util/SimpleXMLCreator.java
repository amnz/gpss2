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
 * ���ɒP����XML�쐬�p�N���X
 *
 * @version	1.00		2003/07/07
 * @since		1.00��007	2003/06/08
 * @author	amoi
 */
public class SimpleXMLCreator {
	// �R���X�g���N�^ ///////////////////////////////////////////////////////////////////
	//                                                                    Constructors //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 * �m�[�h�����w�肵�ăI�u�W�F�N�g���\�z����R���X�g���N�^
	 * @param name �m�[�h��
	 */
	public SimpleXMLCreator(String name) {
		super();
		setName(name);
		setText("");

		attr = new Hashtable<Object, Object>();
		children = new LinkedList<SimpleXMLCreator>();
	}
	/**
	 * ��̃e�L�X�g�m�[�h���������V���v����XML�I�u�W�F�N�g���\�z����R���X�g���N�^�ł��B
	 *
	 * @param name �m�[�h��
	 * @param textNode �e�L�X�g�m�[�h
	 */
	public SimpleXMLCreator(String name, String textNode) {
		this(name);
		setText(textNode);
	}

	// �����t�B�[���h��` ///////////////////////////////////////////////////////////////
	//                                                                          Fields //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �ŗL�����ꗗ
	 */
	private Hashtable<Object, Object> attr;
	/**
	 * �q�m�[�h�ꗗ
	 */
	private LinkedList<SimpleXMLCreator> children;
	/**
	 * �e�L�X�g�m�[�h
	 */
	private String text;
	/** XXXX */
	private boolean textIsCDATA = false;

	// �v���p�e�B ///////////////////////////////////////////////////////////////////////
	//                                                                      Properties //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */
	/**
	 *	�m�[�h��<BR>
	 */
	private String name = null;
	/**
	 *	�m�[�h�����擾����<BR>
	 *	@return �m�[�h��
	 */
	public String getName() {
		return name;
	}
	/**
	 *	�m�[�h����ݒ肷��<BR>
	 *	@param s �ݒ�l<BR>
	 */
	public void setName(String s) {
		name = s;
	}

	// �C���X�^���X���\�b�h /////////////////////////////////////////////////////////////
	//                                                                Instance Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/* ***********************************************************************>> */;
	/**
	 * ���̃I�u�W�F�N�g�ɁA�w�肳�ꂽ���O�̌ŗL�����l��ݒ肵�܂�<BR>
	 * @param key �ŗL������<BR>
	 * @param value �ŗL�����l<BR>
	 */
	public final void setAttribute(Object key, Object value) {
		attr.put(key, value);
	}
	/**
	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���A�w�肳�ꂽ���O�̌ŗL�����l���擾���܂��B<BR>
	 * @param key �ŗL������
	 * @return �ŗL�����l
	 */
	public final Object getAttribute(Object key) {
		return attr.get(key);
	}
	/**
	 * �ŗL�����Ɋi�[����Ă���S�Ă̑����ꗗ��Set�r���[���擾���܂��B<BR>
	 * @return �����ꗗ��Set�r���[<BR>
	 */
	public Set<Map.Entry<Object,Object>> getAttributes() {
		return attr.entrySet();
	}
	/**
	 * �w�肳�ꂽ�������̌ŗL�������ݒ肳��Ă��邩���m�F���܂��B<BR>
	 * @param key �ŗL������<BR>
	 * @return �w�肳�ꂽ���������ݒ肳��Ă���ΐ^<BR>
	 */
	public boolean containsAttributeKey(Object key) {
		return attr.containsKey(key);
	}
	/**
	 * �ŗL�����Ɋi�[����Ă��鑮�����ꗗ��Set�r���[���擾���܂��B<BR>
	 * @return �������ꗗ��Set�r���[<BR>
	 */
	public Set<Object> getAttributeKeys() {
		return attr.keySet();
	}
	/**
	 * �w�肳�ꂽ�������̌ŗL�������폜���܂��B<BR>
	 * @param key �ŗL������<BR>
	 */
	public Object removeAttribute(Object key) {
		return attr.remove(key);
	}

	/**
	 * ���̃I�u�W�F�N�g�ɁA�w�肳�ꂽ���O�̌ŗL�����l��ݒ肵�܂�<BR>
	 * @param key �ŗL������<BR>
	 * @param value �ŗL���������l<BR>
	 */
	public final void setAttribute(Object key, int value) {
		attr.put(key, new Integer(value));
	}
	/**
	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���A�w�肳�ꂽ���O�̌ŗL�����l�𐮐��l�Ƃ��Ď擾���܂��B<BR>
	 * @param key �ŗL������
	 * @return �ŗL���������l
	 */
	public final int getAttributeInt(Object key) throws XMLAttributeException {
		Object ret = attr.get(key);
		if (ret instanceof Integer) {
			return ((Integer) ret).intValue();
		}
		throw new XMLAttributeException("�w�肳�ꂽ����(" + key + ")��int�^�ł͂���܂���B");
	}
	/**
	 * ���̃I�u�W�F�N�g�ɁA�w�肳�ꂽ���O�̌ŗL�����l�𒷐����l�Ƃ��Đݒ肵�܂�<BR>
	 * @param key �ŗL������<BR>
	 * @param value �ŗL�����������l<BR>
	 */
	public final void setAttribute(Object key, long value) {
		attr.put(key, new Long(value));
	}
	/**
	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���A�w�肳�ꂽ���O�̌ŗL�����l�𒷐����l�Ƃ��Ď擾���܂��B<BR>
	 * @param key �ŗL������
	 * @return �ŗL�����������l
	 */
	public final long getAttributeLong(Object key) throws XMLAttributeException {
		Object ret = attr.get(key);
		if (ret instanceof Long) {
			return ((Long) ret).longValue();
		}
		throw new XMLAttributeException("�w�肳�ꂽ����(" + key + ")��long�^�ł͂���܂���B");
	}
	/**
	 * ���̃I�u�W�F�N�g�ɁA�w�肳�ꂽ���O�̌ŗL�����l�������l�Ƃ��Đݒ肵�܂�<BR>
	 * @param key �ŗL������<BR>
	 * @param value �ŗL���������l<BR>
	 */
	public final void setAttribute(Object key, double value) {
		attr.put(key, new Double(value));
	}
	/**
	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���A�w�肳�ꂽ���O�̌ŗL�����l�������l�Ƃ��Ď擾���܂��B<BR>
	 * @param key �ŗL������
	 * @return �ŗL�����������l
	 */
	public final double getAttributeDouble(Object key)
		throws XMLAttributeException {
		Object ret = attr.get(key);
		if (ret instanceof Double) {
			return ((Double) ret).doubleValue();
		}
		throw new XMLAttributeException("�w�肳�ꂽ����(" + key + ")��double�^�ł͂���܂���B");
	}
	/**
	 * ���̃I�u�W�F�N�g�ɁA�w�肳�ꂽ���O�̌ŗL�����l��^�U�l�Ƃ��Đݒ肵�܂�<BR>
	 * @param key �ŗL������<BR>
	 * @param value �ŗL�����^�U�l<BR>
	 */
	public final void setAttribute(Object key, boolean value) {
		attr.put(key, new Boolean(value));
	}
	/**
	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���A�w�肳�ꂽ���O�̌ŗL�����l��^�U�l�Ƃ��Ď擾���܂��B<BR>
	 * @param key �ŗL������
	 * @return �ŗL�����^�U�l
	 */
	public final boolean getAttributeBoolean(Object key)
		throws XMLAttributeException {
		Object ret = attr.get(key);
		if (ret instanceof Boolean) {
			return ((Boolean) ret).booleanValue();
		}
		throw new XMLAttributeException("�w�肳�ꂽ����(" + key + ")��boolean�^�ł͂���܂���B");
	}

	/* ***********************************************************************>> */;
	/**
	 * ���̃m�[�h�Ɏq�m�[�h��ǉ����܂�
	 * @param name �q�m�[�h��
	 * @return �ǉ����ꂽ�q�m�[�h
	 */
	public SimpleXMLCreator addChild(String name) {
		SimpleXMLCreator child = new SimpleXMLCreator(name);
		children.add(child);
		return child;
	}
	/**
	 * ���̃m�[�h�Ɏq�m�[�h��ǉ����܂�
	 * @param child �ǉ�����q�m�[�h
	 */
	public void addChild(SimpleXMLCreator child) {
		children.add(child);
	}
	/**
	 * �w�肳�ꂽ�q�m�[�h���폜���܂�
	 * @param child �폜����q�m�[�h
	 */
	public void removeChild(SimpleXMLCreator child) {
		children.remove(child);
	}
	/**
	 * �w�肳�ꂽ���O�����͂��߂̎q�m�[�h���폜���܂�
	 * @param name �폜����q�m�[�h��
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
	 * �w�肳�ꂽ���O�����S�Ă̎q�m�[�h���폜���܂�
	 * @param name �폜����q�m�[�h��
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
	 * �S�Ă̎q�m�[�h���폜���܂�
	 */
	public void removeChildren() {
		children = new LinkedList<SimpleXMLCreator>();
	}
	/**
	 * ���̃m�[�h�ɐݒ肳��Ă���A�S�Ă̎q�m�[�h���擾���܂��B
	 * @return �q�m�[�h�ꗗ
	 */
	public List<SimpleXMLCreator> getChildren() {
		return new LinkedList<SimpleXMLCreator>(children);
	}
	/**
	 * ���̃m�[�h�ɐݒ肳��Ă���A�w�肵�����O�������ׂĂ̎q�m�[�h���擾���܂��B
	 * @param name �擾����q�m�[�h��
	 * @return �q�m�[�h�ꗗ
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
	 * ���̃m�[�h�ɐݒ肳��Ă���A�q�m�[�h�����擾���܂��B
	 * @return �q�m�[�h��
	 */
	public int sizeOfChildren() {
		return children.size();
	}
	/**
	 * ���̃m�[�h�ɐݒ肳��Ă���A�w�肳�ꂽ���Ԃ̎q�m�[�h���擾���܂��B
	 * @param idx �ݒ菇�ԍ� ��ԏ��߂ɐݒ肳�ꂽ�m�[�h��0�ł��B
	 * @return �q�m�[�h
	 */
	public SimpleXMLCreator getChild(int idx) {
		if (idx >= children.size()) {
			return null;
		}
		return (SimpleXMLCreator) children.get(idx);
	}
	/**
	 * ���̃m�[�h�ɐݒ肳��Ă���A�w�肳�ꂽ���O�����͂��߂̎q�m�[�h���擾���܂�
	 * @param name �폜����q�m�[�h��
	 * @return �q�m�[�h
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
	 * �e�L�X�g�m�[�h��ݒ�
	 * @param textNode �e�L�X�g�m�[�h������
	 */
	public void setText(String textNode) {
		text = textNode;
	}
	/**
	 * �e�L�X�g�m�[�h���擾
	 * @return �e�L�X�g�m�[�h������
	 */
	public String getText() {
		return text;
	}
	/**
	 * CDATA�m�[�h��ݒ�
	 * @param textNode CDATA�m�[�h������
	 */
	public void setCData(String textNode) {
		text = textNode;
		textIsCDATA = true;
	}
//
//	/* ***********************************************************************>> */;
//	/**
//	 * �R���X�g���N�^�Ŏw�肳�ꂽSocklet�ɏ�������S�N���C�A���g�ɁA
//	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���XML�`���̕�����𑗐M���܂��B
//	 */
//	public void send() {
//		if (linkage == null) {
//			return;
//		}
//		linkage.sendToAllClients(toString());
//	}
//	/**
//	 * �R���X�g���N�^�Ŏw�肳�ꂽSocklet�ɏ�������N���C�A���g�̓��A
//	 * �w�肳�ꂽ�����̃N���C�A���g�ɂ̂�
//	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���XML�`���̕�����𑗐M���܂��B
//	 * @param finder ���M�������N���C�A���g�����p�I�u�W�F�N�g
//	 */
//	public void send(Finder finder) {
//		if (socklet == null) {
//			return;
//		}
//		socklet.sendToClients(toString(), finder);
//	}
//	/**
//	 * �R���X�g���N�^�Ŏw�肳�ꂽSocklet�ɏ�������N���C�A���g�̓��A
//	 * �w�肳�ꂽ�����̃N���C�A���g�ɂ̂�
//	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���XML�`���̕�����𑗐M���܂��B
//	 * @param condition ���M�������N���C�A���g�̌���������
//	 */
//	public void send(String condition) {
//		if (socklet == null) {
//			return;
//		}
//		socklet.sendToClients(toString(), condition);
//	}
	/**
	 * �w�肳�ꂽ�N���C�A���g��
	 * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���XML�`���̕�����𑗐M���܂��B
	 * @param client ���M�������N���C�A���g
	 */
	public void send(SocketProcessor client) {
		if (client == null) {
			return;
		}
		client.send(toString());
	}

	/* ***********************************************************************>> */;
	/**
	 * ���̃I�u�W�F�N�g��XML�`���ŕ\�����ꂽ�������Ԃ��܂��B
	 *
	 * @return ���̃I�u�W�F�N�g��XML�`���ŕ\�����ꂽ������
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
	 * XML�G���e�B�e�B�Q�Ƃւ̕ϊ�
	 * @param text �ϊ��O�̃e�L�X�g
	 * @return �ϊ���̃e�L�X�g
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

	// �N���X���\�b�h ///////////////////////////////////////////////////////////////////
	//                                                                   Class Methods //
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * �w�肳�ꂽXML���������͂��A�V����SimpleXMLCreator�I�u�W�F�N�g���\�z���܂��B
	 *
	 * @param xml ��͂���XML������
	 * @return ��͂��ꂽSimpleXMLCreator�I�u�W�F�N�g
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

		// XML�I�u�W�F�N�g�쐬
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
	 * �s����XML��h�~���邽��DOCTYPE�錾�Ȃǂ��폜����B
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

		String xml = "<TagTest name=\"name dayo\" value=\"value dayo\"><test><![CDATA[1111]]></test><test><![CDATA[2222]]></test></TagTest><!DOCTYPE �� [<!ENTITY copy \"@\">]>";
		try{
			SimpleXMLCreator creator = SimpleXMLCreator.parse(xml);
			System.out.println(creator.getName());
		}catch(Throwable ex){
			ex.printStackTrace();
		}
	}
}

