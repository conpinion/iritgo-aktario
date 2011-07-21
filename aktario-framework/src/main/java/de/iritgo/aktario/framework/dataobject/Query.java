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


import de.iritgo.aktario.core.iobject.IObject;
import de.iritgo.aktario.core.iobject.IObjectList;
import de.iritgo.aktario.framework.base.DataObject;
import java.util.Iterator;


/**
 *
 */
public interface Query
{
	public boolean workingWithThisDataObjectTypeId (String dataObjectTypeId);

	public void doQuery ();

	public void doCreatedDataObjectQuery (DataObject dataObject);

	public void doDeletedDataObjectQuery (DataObject dataObject);

	public long getUserUniqueId ();

	public void createdUpdateOwner ();

	public void removedUpdateOwner (IObject iObject);

	public Iterator getResults ();

	public void refresh ();

	public IObjectList getIObjectListResults ();
}
