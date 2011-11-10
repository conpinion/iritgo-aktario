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


import de.iritgo.aktario.core.logger.Log;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import java.util.HashMap;


/**
 * BaseCreator for ResourceNodes. Helps by the Class-Defs.
 */
public class BaseCreator implements NodeCreator
{
	protected static HashMap classMap = new HashMap();

	public BaseCreator()
	{
	}

	public void work(NodeContainer node, ElementIterator i) throws ContinueException
	{
		Element element = ((ElementContainer) i.current()).getElement();

		String tagName = element.getName();
		String body = element.getTextTrim();

		if (tagName.equals("classdef"))
		{
			classMap.put(element.getAttribute("class").getValue(), body);
			Log.logDebug("resource", "[XMLParser] BaseCreater.work", "adding classdef '"
							+ element.getAttribute("class").getValue() + "'.");
			throw new ContinueException();
		}
	}

	/**
	 * Return a IntegerObject from a XML-Attribute
	 */
	public Object getObject(String classType, Attribute attribute)
	{
		try
		{
			if (classType.equals("java.lang.Boolean"))
			{
				return new Boolean(attribute.getBooleanValue());
			}

			if (classType.equals("java.lang.Integer"))
			{
				return new Integer(attribute.getIntValue());
			}

			if (classType.equals("java.lang.Float"))
			{
				return new Float(attribute.getFloatValue());
			}

			if (classType.equals("java.lang.Double"))
			{
				return new Double(attribute.getDoubleValue());
			}

			if (classType.equals("java.lang.String"))
			{
				return new String(attribute.getValue());
			}

			if (classType.equals("java.lang.Object"))
			{
				return new String(attribute.getValue());
			}
		}
		catch (DataConversionException e)
		{
		}

		return null;
	}
}
