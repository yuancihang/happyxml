package io.happyxml;

import java.nio.file.Paths;
import java.util.List;

public class XmlExample {

	public static void main(String[] args) {
		sampleQuery();
	}
	
	public static void sampleQuery() {
		Dom root = Dom.parse(Paths.get("t.xml"));

		Dom studentElement = root.findChild("student");
		System.out.println(studentElement.attr("name"));
		System.out.println(studentElement.text());
		//System.out.println(studentElement.childText(""));
		
		List<Dom> friendElementList = root.findChildren("friends");
		for(Dom friendElement : friendElementList){
			System.out.println(friendElement.text());
		}
	}
	
	public static void sampleUpdate() {
		// write sub element
		// write attr, element text
		Dom root = Dom.newDom("student");
		Dom d = root.addChild("");
//
//		List<Dom> pList = root.elements("property");
//		Dom d = pList.get(0);
//		Dom nameDom = d.element("name");
//		nameDom.setAttribute("a", "2");
//		nameDom.setAttribute("b", "3");
//		nameDom.updateElementText("测试呵呵");
//		d.updateElementText("description", "按当地");
//		root.remove(pList.get(pList.size() - 1));
//		OutputStream os = new FileOutputStream("d:/test/t.xml");
//		root.write(os);
	}

}
