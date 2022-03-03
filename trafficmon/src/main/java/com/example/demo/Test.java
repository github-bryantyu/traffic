package com.example.demo;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.security.cert.X509Certificate;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Security.TrustStrategy;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Test {
	
	private static final String uri = "https://resource.data.one.gov.hk/td/speedmap.xml";

	private static Document convertStringToXMLDocument(String xmlString)  {
	    //Parser that produces DOM object trees from XML content
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	     
	    //API to obtain DOM Document instance
	    DocumentBuilder builder = null;
	    try {
	      //Create DocumentBuilder with default configuration
	      builder = factory.newDocumentBuilder();
	       
	      //Parse the content to Document object
	      Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
	      return doc;
	    } 
	    catch (Exception e)  {
	      e.printStackTrace();
	    }
	    return null;
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
//		ResponseEntity<TSMRecordSearchResults> response = restTemplate.exchange(uri, HttpMethod.GET, null, TSMRecordSearchResults.class);
		


		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
		Document doc = convertStringToXMLDocument( response.getBody() );
	    
	    XPath xPath = XPathFactory.newInstance().newXPath();
	    NodeList nl2 = (NodeList) xPath.compile("/jtis_speedlist/jtis_speedmap").evaluate(doc, XPathConstants.NODESET);
	    System.out.println("NL2 length: " + nl2.getLength());
	    ArrayList<TSMRow> tsmRecords = new ArrayList<TSMRow>();
	    
	    for (int i = 0; i < nl2.getLength(); i++) {
	    	TSMRow tsmRecord = new TSMRow();
	    	Node n = nl2.item(i);
	    	NodeList children = n.getChildNodes();
	    	for (int a = 0; a < children.getLength(); a++) {
	    		Node child = children.item(a);
	    		if (child.getNodeType() == Node.ELEMENT_NODE) {
	    			String nodeName = child.getNodeName();
	    			String nodeValue = child.getFirstChild().getNodeValue();
	    			if (nodeName == "LINK_ID")
	    				tsmRecord.setLinkId(nodeValue);
	    			if (nodeName == "REGION")
	    				tsmRecord.setRegion(nodeValue);
	    			if (nodeName == "ROAD_TYPE")
	    				tsmRecord.setRoadType(nodeValue);
	    			if (nodeName == "ROAD_SATURATION_LEVEL")
	    				tsmRecord.setRoadSaturationLevel(nodeValue);
	    			if (nodeName == "TRAFFIC_SPEED")
	    				tsmRecord.setTrafficSpeed(Integer.parseInt(nodeValue));

	    		}	
	    	}
	    	tsmRecords.add(tsmRecord);
	    }
	} 

}
