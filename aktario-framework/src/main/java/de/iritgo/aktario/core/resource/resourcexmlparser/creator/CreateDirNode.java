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
import de.iritgo.aktario.core.resource.resourcexmlparser.NodeContainer;
import org.jdom.Element;


/**
 * DefaultCreator for ResourceNodes
 */
public class CreateDirNode extends BaseCreator
{
	public CreateDirNode()
	{
	}

	@Override
	public void work(NodeContainer nodeContainer, ElementIterator i) throws ContinueException
	{
		ResourceNode node = (ResourceNode) nodeContainer.getNode();

		Element element = ((ElementContainer) i.current()).getElement();
		String tagName = element.getName();

		if (tagName.equals("node"))
		{
			ResourceService resourceService = new ResourceService((ResourceNode) node);

			node = new ResourceNode("directory", element.getAttribute("treename").getValue());
			resourceService.addNode("", (ResourceNode) node);
			Log.logDebug("resource", "[XMLParser] CreateDirNode.work", "create directory node '"
							+ element.getAttribute("treename").getValue() + "'.");
			nodeContainer.setNode(node);

			throw new ContinueException();
		}
	}
}
