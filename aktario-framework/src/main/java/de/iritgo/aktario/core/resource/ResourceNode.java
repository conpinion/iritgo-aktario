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

package de.iritgo.aktario.core.resource;


import de.iritgo.aktario.core.base.BaseObject;
import java.util.Iterator;
import java.util.TreeMap;


/**
 *
 */
public class ResourceNode extends BaseObject
{
	private String description = "no.description";

	private String name;

	private TreeMap nodeMap;

	/**
	 * Standard constructor
	 *
	 * @param description The description of the resource.
	 * @param nodeName The node Name
	 */
	public ResourceNode (String description, String nodeName)
	{
		this.description = description;
		this.name = nodeName;
	}

	public ResourceNode (String nodeName)
	{
		this.name = nodeName;
	}

	/**
	 * Get the description of the resource.
	 *
	 * @return The description of the resource.
	 */
	public String getDescription ()
	{
		return description;
	}

	/**
	 * Get the tree node name.
	 *
	 * @return The tree node name.
	 */
	public String getName ()
	{
		return name;
	}

	/**
	 * Get the node by node name.
	 *
	 * @param nodeName
	 * @return The node.
	 */
	public ResourceNode getNodeByName (String nodeName)
	{
		if (nodeName.equals (this.name))
		{
			return this;
		}

		return nodeMap != null ? ((ResourceNode) nodeMap.get (nodeName)) : null;
	}

	/**
	 * Return the name of the resource.
	 *
	 * @return The node.
	 */
	public String getValue ()
	{
		return name;
	}

	/**
	 * Add children to this note.
	 */
	public void addNode (ResourceNode node)
	{
		nodeMapOnDemand ();
		nodeMap.put (node.getName (), node);
	}

	/**
	 * NodeMap on demand.
	 *
	 */
	public void nodeMapOnDemand ()
	{
		if (nodeMap == null)
		{
			nodeMap = new TreeMap ();
		}
	}

	/**
	 * Return a keyiterator with all keys of this node
	 *
	 * @return The a Key-Iterator
	 */
	public Iterator getKeyIterator ()
	{
		return nodeMap.keySet ().iterator ();
	}

	/**
	 * Return a node iterator with all chieldren of this node
	 *
	 * @return The a Key-Iterator
	 */
	public Iterator getNodeIterator ()
	{
		return nodeMap.values ().iterator ();
	}
}
