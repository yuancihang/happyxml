# happyxml
短小精悍的xml操作封装, 只有几百行代码. 支持注解绑定到对象

    public static void sampleQuery() {
      Dom root = Dom.parse(Paths.get("src/main/resources", "person.xml").toAbsolutePath());

      System.out.println(root.childText("firstname"));

      Dom lastnameDom = root.findChild("lastname");
      System.out.println(lastnameDom.text());

      Dom phoneDom = root.findChild("phone");
      System.out.println(phoneDom.childText("code"));
      System.out.println(phoneDom.childText("number"));

      Dom faxDom = root.findChild("fax");
      System.out.println(faxDom.attr("code"));
      System.out.println(faxDom.attr("number"));

      List<Dom> friendDomList = root.findChild("friends").findChildren("friend");
      for(Dom friendDom : friendDomList){
        System.out.println(friendDom.text());
      }
    }

    public static void sampleUpdate() {
      Dom root = Dom.newDom("person");

      root.addChild("firstname", "Joe");

      Dom lastnameDom = root.addChild("lastname");
      lastnameDom.text("Walnes");

      Dom phoneDom = root.addChild("phone");
      phoneDom.addChild("code", "123");
      phoneDom.addChild("number", "1234-456");

      Dom faxDom = root.addChild("fax");
      faxDom.attr("code", "123");
      faxDom.attr("number", "9999-999");

      Dom friendsDom = root.addChild("friends");
      friendsDom.addChild("friend", "zhang");
      friendsDom.addChild("friend", "wang");

      System.out.println(root.toString());
    }

    public static void sampleBindObject(){
      try {
        String xml = readResources("user_buyer_get_response.xml");
        UserBuyerResponse response = Jaxb.parse(UserBuyerResponse.class, xml);

        response.getUserList().forEach(System.out::println);
      } catch (IOException e) {
        e.printStackTrace();
      }
	}

	private static String readResources(String name) throws IOException{
		Path path = Paths.get("src/main/resources", name).toAbsolutePath();
		
		return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
	}
  
## person.xml

    <?xml version="1.0" encoding="UTF-8"?>
    <person>
      <firstname>Joe</firstname>
      <lastname>Walnes</lastname>
      <phone>
        <code>123</code>
        <number>1234-456</number>
      </phone>
      <fax code="123" number="9999-999" />
      <friends>
        <friend>zhang</friend>
        <friend>wang</friend>
      </friends>
    </person>
    
## user_buyer_get_response.xml

    <user_buyer_get_response>
        <user>
            <nick>hz0799</nick>
            <sex>m</sex>
            <avatar>http://assets.taobaocdn.com/app/sns/img/default/avatar-120.png</avatar>
        </user>
    </user_buyer_get_response>
    
## maven
	<dependency>
	    <groupId>com.github.yuancihang</groupId>
	    <artifactId>happyxml</artifactId>
	    <version>1.1</version>
	</dependency>
## gradle
	compile 'com.github.yuancihang:happyxml:1.1'
