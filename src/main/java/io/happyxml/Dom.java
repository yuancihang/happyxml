package io.happyxml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Dom {
	
	private Element e;
	
	private Dom(Element element){
		this.e = element;
	}
	
	public void print(){
		NodeList nodeList = e.getChildNodes();
		for(int i=0;i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			System.out.println("nodeName: " + node.getNodeName() + ", nodeValue: " + node.getNodeValue() + ", nodeType: " + node.getNodeType());
		}
	}
	
	public Element getDomElement(){
		return e;
	}
	
	public Document getDocument(){
		return e.getOwnerDocument();
	}
	
	public static Dom newDom(String rootName){
		Document doc = null;
		try {
			DocumentBuilder dombuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = dombuilder.newDocument();
			doc.setXmlStandalone(true);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} 
		Element root = doc.createElement(rootName);
		doc.appendChild(root);
		return new Dom(root);
	}
	
	public static Dom parse(InputStream is){
		Document doc = null;
		try {
			DocumentBuilder dombuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = dombuilder.parse(is);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} 
		Element root = doc.getDocumentElement();
		return new Dom(root);
	}
	
	public static Dom parse(Path xmlFile){
		InputStream is = null;
		try {
			is = Files.newInputStream(xmlFile);
			Dom root = parse(is);
			return root;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}
	
	public static Dom parse(String xml){
		return parse(xml.getBytes(StandardCharsets.UTF_8));
	}
	
	public static Dom parse(byte[] xmlData) {
		return parse(new ByteArrayInputStream(xmlData));
	}
	
	public boolean existChild(String elementName){
		NodeList nodeList = e.getElementsByTagName(elementName);
		if((nodeList == null) || (nodeList.getLength() < 1)){
			return false;
		}
		return true;
	}
	
	public Dom findChild(String elementName){
		NodeList nodeList = e.getElementsByTagName(elementName);
		if((nodeList == null) || (nodeList.getLength() < 1)){
			return null;
		}
		Element element = (Element)nodeList.item(0);
		return new Dom(element);
	}
	
	public List<Dom> findChildren(String elementName){
		List<Dom> eList = new ArrayList<Dom>();
		NodeList nodeList = e.getElementsByTagName(elementName);
		if(nodeList == null){
			return eList;
		}
		for(int i=0;i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element)node;
				eList.add(new Dom(element));
			}
		}
		return eList;
	}
	public List<Dom> findAllChildren(){
		List<Dom> eList = new ArrayList<Dom>();
		NodeList nodeList = e.getChildNodes();
		if(nodeList == null){
			return eList;
		}
		for(int i=0;i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element)node;
				eList.add(new Dom(element));
			}
		}
		return eList;
	}
	
	public String name(){
		return e.getTagName();
	}
	
	public Dom addChild(String name){
		Document document = e.getOwnerDocument();
		Element element = document.createElement(name);
		e.appendChild(element);
		return new Dom(element);
	}
	
	public Dom addChild(String name, String value){
		Document document = e.getOwnerDocument();
		Element element = document.createElement(name);
		e.appendChild(element);
		Text text = document.createTextNode(value);
		element.appendChild(text);
		return new Dom(element);
	}
	
	public void removeChild(Dom subDom){
		e.removeChild(subDom.getDomElement());
	}
	
	public void removeChild(String name){
		NodeList nodeList = e.getElementsByTagName(name);
		if(nodeList == null){
			return ;
		}
		for(int i=0;i<nodeList.getLength();i++){
			e.removeChild(nodeList.item(i));
		}
	}
	
	public void removeAttr(String name){
		e.removeAttribute(name);
	}
	
	/**
	 * 获取子元素文本值
	 * @param elementName
	 * @return
	 */
	public String childText(String elementName){
		Element element = (Element)e.getElementsByTagName(elementName).item(0);
		Node textNode =  element.getFirstChild();
		if(textNode == null){
			return "";
		}
		return textNode.getNodeValue();
	}
	
	/**
	 * 修改子元素文本值
	 * @param name
	 * @param value
	 * @return
	 */
	public Dom childText(String name, String value){
		Element element = (Element)e.getElementsByTagName(name).item(0);
		Node textNode =  element.getFirstChild();
		textNode.setNodeValue(value);
		return new Dom(element);
	}
	
	/**
	 * 获取元素属性值
	 * @param attributeName
	 * @return
	 */
	public String attr(String attributeName){
		return e.getAttribute(attributeName);
	}
	
	/*
	 * 修改元素属性值
	 * @param name
	 * @param value
	 * @return
	 */
	public Dom attr(String name, String value){
		e.setAttribute(name, value);
		return this;
	}
	
	/**
	 * 修改元素文本值
	 * @param value
	 * @return
	 */
	public Dom text(String value){
		Node textNode =  e.getFirstChild();
		if(textNode == null){
			Document document = e.getOwnerDocument();
			Text text = document.createTextNode(value);
			e.appendChild(text);
			return this;
		}
		textNode.setNodeValue(value);
		return this;
	}
	
	/**
	 * 获取元素文本值
	 * @return
	 */
	public String text(){
		Node textNode =  e.getFirstChild();
		if(textNode == null){
			return "";
		}
		return textNode.getNodeValue();
	}
	
	public boolean hasChildren(){
		return e.hasChildNodes();
	}
	
	public void write(OutputStream os){
		write(os, StandardCharsets.UTF_8);
	}
	public void write(OutputStream os, Charset charset){
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
//			tFactory.setAttribute("indent-number", 2);
			Transformer transformer = tFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, charset.name());
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes"); 
			transformer.transform(new DOMSource(e.getOwnerDocument()), new StreamResult(new OutputStreamWriter(os)));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	public void write(Path xmlFile) {
		write(xmlFile, StandardCharsets.UTF_8);
	}
	public void write(Path xmlFile, Charset charset) {
		OutputStream os = null;
		try {
			os = Files.newOutputStream(xmlFile);
			write(os, charset);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}
	public String toString(Charset charset) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		write(baos, charset);
		try {
			return baos.toString(charset.name());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	public String toString(){
		return toString(StandardCharsets.UTF_8);
	}

	//去除文档中的空行
	protected Document trim(){
		Document document = e.getOwnerDocument();
		Element root = document.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		for(int i=0;i<nodeList.getLength();i++){
			Node node = nodeList.item(i);
			if(node.getNodeType() == Node.TEXT_NODE){
				String text = node.getNodeValue();
				node.setNodeValue(text.trim());
			}
		}
		return document;
	}
	
}
