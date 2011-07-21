/**
 * This file is part of the Iritgo/Aktario Framework.
 *
 * Copyright (C) 2005-2011 Iritgo Technologies.
 * Copyright (C) 2003-2005 BueroByte GbR.
 *
 * Iritgo licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iritgo.aktario.core.resource.resourcexmlparser;


import de.iritgo.aktario.core.base.BaseObject;
import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ResourceNode;
import de.iritgo.aktario.core.resource.ResourceNotFoundException;
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.resource.resourcexmlparser.creator.DefaultCreator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;


/**
 *
 */
public class XMLParser extends BaseObject
{
	HashMap classMap = new HashMap ();

	/**
	 * Create a new XMLParser
	 *
	 * @param resourceXMLFile The XML-Description file.
	 * @param resourceService The resource generator.
	 */
	public XMLParser (String resourceXMLFile, ResourceService resourceService)
	{
		try
		{
			SAXBuilder builder = new SAXBuilder (false);
			Document doc = builder.build (new File (resourceXMLFile));

			transform (doc, resourceService);
		}
		catch (JDOMException x)
		{
			Log.logFatal ("resource", "XMLParser.XMLParser", "ResourceDescription-File not found");
			x.printStackTrace ();
		}
		catch (IOException x)
		{
			Log.logFatal ("resource", "XMLParser.XMLParser", "ResourceDescription-File not found");
			x.printStackTrace ();
		}
	}

	/**
	 * Create a new XMLParser
	 *
	 * @param resourceXMLURL The XML-Description accessed by URL.
	 * @param resourceService The resource generator.
	 */
	public XMLParser (URL resourceXMLURL, ResourceService resourceService)
	{
		try
		{
			SAXBuilder builder = new SAXBuilder (false);
			Document doc = builder.build (resourceXMLURL);

			transform (doc, resourceService);
		}
		catch (JDOMException x)
		{
			Log.logFatal ("resource", "XMLParser.XMLParser", "ResourceDescription-File not found");
			x.printStackTrace ();
		}
		catch (IOException x)
		{
			Log.logFatal ("resource", "XMLParser.XMLParser", "ResourceDescription-File not found");
			x.printStackTrace ();
		}
	}

	/**
	 * Transform the XML document
	 *
	 * @param doc The XML document.
	 * @param resourceService The resource generator.
	 */
	private void transform (Document doc, ResourceService resourceService)
	{
		Element element = doc.getRootElement ();

		transformToNode (element, resourceService.getBaseNode ());
	}

	/**
	 * Transform the XML-Tree to a ResourceNodetree.
	 */
	public void transformToNode (Element element, ResourceNode node)
	{
		ElementIterator i = new ElementIterator (element);
		ResourceService resourceBase = new ResourceService (node);
		NodeContainer nodeContainer = new NodeContainer ();

		nodeContainer.setNode (node);

		DefaultCreator defaultCreater = new DefaultCreator ();

		for (; i.hasNext (); i.next ())
		{
			ElementContainer elementContainer = (ElementContainer) i.current ();

			try
			{
				try
				{
					if (resourceBase.getNode (elementContainer.getPath ()) != null)
					{
						nodeContainer.setNode (resourceBase.getNode (elementContainer.getPath ()));

						continue;
					}
				}
				catch (ResourceNotFoundException x)
				{
				}

				defaultCreater.work (nodeContainer, i);
			}
			catch (ContinueException x)
			{
			}
		}
	}
}
