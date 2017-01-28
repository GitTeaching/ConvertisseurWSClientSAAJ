import java.util.Iterator;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;


public class ConvertisseurWSClientSAAJ {

	public static void main(String[] args) {
		
		try {
			
			//1 - Create SOAPConnection
			SOAPConnectionFactory  soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			
			//2 - Create SOAP Request
			SOAPMessage soapRequest = createSOAPRequest();
			
			//3 - Send Request and Get Response
			SOAPMessage soapResponse = soapConnection.call(soapRequest, "http://localhost:1234/convertisseurws?wsdl");
			
			System.out.println("Your SOAP Response : ");
			soapResponse.writeTo(System.out);
			System.out.println("\n");
			
			//4 - Traitement Response
			SOAPBody soapBody = soapResponse.getSOAPBody();
			QName bodyQName = new QName("http://tp3.ws.soa.org/", "getDinarFromEuroResponse", "ns2");
			Iterator iterator = soapBody.getChildElements(bodyQName);
			SOAPBodyElement soapBodyElement = (SOAPBodyElement)iterator.next();
		    String convValue = soapBodyElement.getTextContent();
		    System.out.println(convValue);
		    
		    System.out.println(soapBodyElement.getElementsByTagName("montantDinar").item(0).getTextContent());
		    
		    //5 - Closing Connection
		    soapConnection.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		

	}

	private static SOAPMessage createSOAPRequest() throws Exception {
		
		// 1 -
		MessageFactory msgFactory = MessageFactory.newInstance();
		SOAPMessage soapMsg = msgFactory.createMessage();
		
		// 2 -
		SOAPPart soapPart = soapMsg.getSOAPPart();
		
		// 3 -
		SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
		soapEnvelope.addNamespaceDeclaration("convertisseur", "http://tp3.ws.soa.org/");
		
		// 4 -
		SOAPHeader soapHeader = soapEnvelope.getHeader();
		soapHeader.detachNode();
		
		// 5 -
		SOAPBody soapBody = soapEnvelope.getBody();
		
		// 6 -
		SOAPElement soapElement = soapBody.addChildElement("getDinarFromEuro", "convertisseur");
		SOAPElement soapSubElement = soapElement.addChildElement("montantEuro");
		soapSubElement.addTextNode("100");
		
		// 7 -
		soapMsg.saveChanges();
		
		// 8 -
		System.out.println("Your SOAP Request : ");
		soapMsg.writeTo(System.out);
		System.out.println("\n");
		
		return soapMsg;
	}

}
