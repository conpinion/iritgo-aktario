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

package de.iritgo.aktario.core.tools;


import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * A NullIterator is an empty iterator that returns no items.
 */
public class NullIterator implements Iterator
{
	/**
	 * Create a new null iterator.
	 */
	public NullIterator ()
	{
	}

	/**
	 * A null iterator has no items.
	 *
	 * @return False.
	 */
	public boolean hasNext ()
	{
		return false;
	}

	/**
	 * The null iterator returns no values.
	 */
	public Object next ()
	{
		throw new NoSuchElementException ();
	}

	/**
	 * Nothing to remove.
	 */
	public void remove ()
	{
		throw new UnsupportedOperationException ();
	}
}
