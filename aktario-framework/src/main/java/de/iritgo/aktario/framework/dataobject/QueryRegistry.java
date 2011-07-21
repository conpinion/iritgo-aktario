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

package de.iritgo.aktario.framework.dataobject;


import de.iritgo.aktario.core.base.BaseObject;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class QueryRegistry extends BaseObject
{
	private List queries;

	public QueryRegistry ()
	{
		queries = new LinkedList ();
	}

	public void addQuery (Query query)
	{
		queries.add (query);
	}

	synchronized public Iterator queryIterator ()
	{
		return new LinkedList (queries).iterator ();
	}

	synchronized public Iterator queryIterator (String dataObjectTypeId)
	{
		List iteratorList = new LinkedList ();

		for (Iterator i = queries.iterator (); i.hasNext ();)
		{
			Query query = (Query) i.next ();

			if (query.workingWithThisDataObjectTypeId (dataObjectTypeId))
			{
				iteratorList.add (query);
			}
		}

		return iteratorList.iterator ();
	}

	public void removeQuery (Query query)
	{
		queries.remove (query);
	}
}
