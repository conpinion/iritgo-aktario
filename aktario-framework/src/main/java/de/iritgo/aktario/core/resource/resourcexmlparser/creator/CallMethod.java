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

package de.iritgo.aktario.core.resource.resourcexmlparser.creator;


import de.iritgo.aktario.core.logger.Log;
import de.iritgo.aktario.core.resource.ResourceNode;
import de.iritgo.aktario.core.resource.resourcexmlparser.BaseCreator;
import de.iritgo.aktario.core.resource.resourcexmlparser.ContinueException;
import de.iritgo.aktario.core.resource.resourcexmlparser.ElementContainer;
import de.iritgo.aktario.core.resource.resourcexmlparser.ElementIterator;
import de.iritgo.aktario.core.resource.resourcexmlparser.MethodIterator;
import de.iritgo.aktario.core.resource.resourcexmlparser.NodeContainer;
import org.jdom.Attribute;
import org.jdom.Element;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * DefaultCreator for ResourceNodes
 */
public class CallMethod extends BaseCreator
{
	public CallMethod()
	{
	}

	@Override
	public void work(NodeContainer nodeContainer, ElementIterator i) throws ContinueException
	{
		Object node = (ResourceNode) nodeContainer.getNode();

		Element element = ((ElementContainer) i.current()).getElement();

		List attributeList = element.getAttributes();
		String tagName = element.getName();

		if (! tagName.equals("node"))
		{
			Object[] params;

			if (generateMethodParams(node, tagName, "add", attributeList) != null)
			{
				params = generateMethodParams(node, tagName, "add", attributeList);
				invokeMethod(node, tagName, "add", params);
				Log.log("resource", "[XMLParser] CallMethode.run", "call add '" + tagName + "' method", Log.INFO);
			}
			else
			{
				params = generateMethodParams(node, tagName, "set", attributeList);
				invokeMethod(node, tagName, "set", params);
				Log.log("resource", "[XMLParser] CallMethode.run", "call add '" + tagName + "' method", Log.INFO);
			}

			throw new ContinueException();
		}

		if (getObjectFromResourceNode(node, tagName) != null)
		{
			node = getObjectFromResourceNode(node, tagName);
			throw new ContinueException();
		}
	}

	/**
	 * Generate the params object array
	 */
	public Object[] generateMethodParams(Object node, String methodName, String prefix, List attributeList)
	{
		ArrayList objectParams = new ArrayList();
		MethodIterator mi = new MethodIterator(node.getClass().getMethods());

		while (mi.hasNext())
		{
			Method method = (Method) mi.next();

			Class[] params = method.getParameterTypes();

			if ((params.length != attributeList.size()) && (! method.getName().equalsIgnoreCase(prefix + methodName)))
			{
				continue;
			}

			for (int i = 0; i < params.length; ++i)
			{
				Object o = getObject(params[i].getName(), (Attribute) attributeList.get(i));

				objectParams.add(o);
			}

			return objectParams.toArray();
		}

		return null;
	}

	/**
	 * Call the speziefed methode
	 */
	public void invokeMethod(Object object, String methodName, String prefix, Object[] methodParams)
	{
		try
		{
			MethodIterator mi = new MethodIterator(object.getClass().getMethods());

			while (mi.hasNext())
			{
				Method method = (Method) mi.next();

				Class[] params = method.getParameterTypes();

				if ((params.length != methodParams.length)
								&& (! method.getName().equalsIgnoreCase(prefix + methodName)))
				{
					continue;
				}

				method.invoke(object.getClass(), methodParams);
			}
		}

		catch (IllegalAccessException e)
		{
			Log.log("resource", "[XMLParser] CallMethode.invokeMethod", "IllegalAccessException", Log.FATAL);
		}
		catch (InvocationTargetException e)
		{
			Log.log("resource", "[XMLParser] CallMethode.invokeMethod", "InvocationTargetException", Log.FATAL);
		}
	}

	/**
	 * Call the speziefed methode and get the new object
	 */
	public Object getObjectFromResourceNode(Object object, String methodName)
	{
		Class objectClass = object.getClass();
		Method[] methList = objectClass.getMethods();
		Object returnObject = null;

		for (int i = 0; i < methList.length; i++)
		{
			Method m = methList[i];

			if (m.getName().equalsIgnoreCase("get" + methodName))
			{
				try
				{
					returnObject = m.invoke(objectClass, (Object[]) null);

					break;
				}
				catch (IllegalAccessException e)
				{
					Log.log("resource", "[XMLParser] CallMethode.getObjectFromResourceNode", "IllegalAccessException",
									Log.FATAL);
				}
				catch (InvocationTargetException e)
				{
					Log.log("resource", "[XMLParser] CallMethode.getObjectFromResourceNode",
									"InvocationTargetException", Log.FATAL);
				}
			}
		}

		return returnObject;
	}
}
