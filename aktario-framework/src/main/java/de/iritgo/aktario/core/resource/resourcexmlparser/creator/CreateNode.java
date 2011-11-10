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
import de.iritgo.aktario.core.resource.ResourceService;
import de.iritgo.aktario.core.resource.resourcexmlparser.BaseCreator;
import de.iritgo.aktario.core.resource.resourcexmlparser.ContinueException;
import de.iritgo.aktario.core.resource.resourcexmlparser.ElementContainer;
import de.iritgo.aktario.core.resource.resourcexmlparser.ElementIterator;
import de.iritgo.aktario.core.resource.resourcexmlparser.MethodIterator;
import de.iritgo.aktario.core.resource.resourcexmlparser.NodeContainer;
import org.jdom.Attribute;
import org.jdom.Element;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * DefaultCreator for ResourceNodes
 */
public class CreateNode extends BaseCreator
{
	public CreateNode()
	{
	}

	@Override
	public void work(NodeContainer nodeContainer, ElementIterator i) throws ContinueException
	{
		Object node = (ResourceNode) nodeContainer.getNode();

		Element element = ((ElementContainer) i.current()).getElement();
		String tagName = element.getName();
		List attributeList = element.getAttributes();

		String nodeClass = (String) classMap.get(tagName);

		if (nodeClass != null)
		{
			ResourceService resourceService = new ResourceService((ResourceNode) node);

			try
			{
				Class classObject = Class.forName(nodeClass);
				Object[] params = generateConstructorParams(classObject, tagName, attributeList);
				Class[] classes = generateConstructorClasses(classObject, tagName, attributeList);

				resourceService = new ResourceService((ResourceNode) node);
				node = ((Constructor) classObject.getConstructor(classes)).newInstance(params);
				resourceService.addNode("", (ResourceNode) node);
				nodeContainer.setNode(node);
				Log.logDebug("resource", "[XMLParser] CreateNode.work", "create node '" + tagName + "'-'"
								+ (String) element.getAttribute("treename").getValue() + "'.");
			}
			catch (NoSuchMethodException e)
			{
				Log.logFatal("resource", "[XMLParser] CreateNode.work", "NoSuchMethodException");
			}
			catch (InvocationTargetException e)
			{
				Log.logFatal("resource", "[XMLParser] CreateNode.work", "InvocationTargetException");
			}
			catch (ClassNotFoundException e)
			{
				Log.logFatal("resource", "[XMLParser] CreateNode.work", "ClassNotFoundException");
			}
			catch (IllegalAccessException e)
			{
				Log.logFatal("resource", "[XMLParser] CreateNode.work", "IllegalAccessException");
			}
			catch (InstantiationException e)
			{
				Log.logFatal("resource", "[XMLParser] CreateNode.work", "InstantiationException");
			}

			throw new ContinueException();
		}
	}

	/**
	 * Generate the params object array
	 */
	public Object[] generateConstructorParams(Class classObject, @SuppressWarnings("unused") String methodName,
					List attributeList)
	{
		ArrayList objectParams = new ArrayList();
		MethodIterator mi = new MethodIterator(classObject.getConstructors());

		while (mi.hasNext())
		{
			Constructor constructor = (Constructor) mi.next();

			Class[] params = constructor.getParameterTypes();

			if (params.length != attributeList.size())
			{
				continue;
			}

			for (int i = 0; i < params.length; ++i)
			{
				Object o = getObject(params[i].getName(), (Attribute) attributeList.get(i));

				objectParams.add(o);
			}
		}

		return objectParams.toArray();
	}

	/**
	 * Generate the class object array
	 */
	public Class[] generateConstructorClasses(Class classObject, @SuppressWarnings("unused") String methodName,
					List attributeList)
	{
		Object objectParams = null;

		try
		{
			objectParams = Array.newInstance(Class.forName("java.lang.Class"), attributeList.size());
		}
		catch (ClassNotFoundException e)
		{
			Log.log("resource", "[XMLParser] CreateNode.generateConstructorClasses", "ClassNotFoundException",
							Log.FATAL);
		}

		MethodIterator mi = new MethodIterator(classObject.getConstructors());

		while (mi.hasNext())
		{
			Constructor constructor = (Constructor) mi.next();

			Class[] params = constructor.getParameterTypes();

			if (params.length != attributeList.size())
			{
				continue;
			}

			for (int i = 0; i < params.length; ++i)
			{
				Object o = getObject(params[i].getName(), (Attribute) attributeList.get(i));

				Array.set(objectParams, i, o.getClass());
			}
		}

		return (Class[]) objectParams;
	}
}
