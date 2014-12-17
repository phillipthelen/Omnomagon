/*
Copyright (c) 2012, Phillip Thelen
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package net.pherth.omnomagon.data;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;


public class RSSHandler extends DefaultHandler {

	List<String> descList = new ArrayList<String>();
	String city;
	
	//Current characters being accumulated
	StringBuffer chars = new StringBuffer();

	
	/* 
	 * This method is called everytime a start element is found (an opening XML marker)
	 * here we always reset the characters StringBuffer as we are only currently interested
	 * in the the text values stored at leaf nodes
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String qName, Attributes atts) {
		chars = new StringBuffer();
	}



	/* 
	 * This method is called everytime an end element is found (a closing XML marker)
	 * here we check what element is being closed, if it is a relevant leaf node that we are
	 * checking, such as Title, then we get the characters we have accumulated in the StringBuffer
	 * and set the current Meal's title to the value
	 * 
	 * If this is closing the "Item", it means it is the end of the meal, so we add that to the list
	 * and then reset our Meal object for the next one on the stream
	 * 
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (localName.equalsIgnoreCase("description"))
		{
			descList.add(chars.toString());

		}
	}
	
	



	/* 
	 * This method is called when characters are found in between XML markers, however, there is no
	 * guarante that this will be called at the end of the node, or that it will be called only once
	 * , so we just accumulate these and then deal with them in endElement() to be sure we have all the
	 * text
	 * 
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char ch[], int start, int length) {
		chars.append(new String(ch, start, length));
	}





	/**
	 * This is the entry point to the parser and creates the feed to be parsed
	 * 
	 * @param feedUrl
	 * @return
	 **/
	public String getHTML(String feedUrl, String city) {
		URL url = null;
		this.city = city;
		try {
			if (city.equals("beList")) {
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser sp = spf.newSAXParser();
				XMLReader xr = sp.getXMLReader();

				url = new URL(feedUrl);
			
				xr.setContentHandler(this);
				xr.parse(new InputSource(url.openStream()));
				return descList.get(1);
			} else if (city.equals("ulm")){
				url = new URL(feedUrl);
				return new InputSource(url.openStream()).toString();
			}
				
		} catch (IOException e) {
			Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
		} catch (SAXException e) {
			Log.e("RSS Handler SAX", e.toString());
		} catch (ParserConfigurationException e) {
			Log.e("RSS Handler Parser Config", e.toString());
		}
		
		return null;
	}

}

