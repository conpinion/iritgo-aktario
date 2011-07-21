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

package de.iritgo.aktario.core.iobject;


import de.iritgo.aktario.core.Engine;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * IObjectList, kann fuer einfache IObjectList verwendet werden, gibt sofort das gecachete Object zur?ck.
 */
public class IObjectIterator implements Iterator
{
	Iterator iterator;

	public IObjectIterator (IObjectList linkedList)
	{
		iterator = linkedList.getListIterator ();
	}

	/**
	 * Returns <tt>true</tt> if the iteration has more elements. (In other
	 * words, returns <tt>true</tt> if <tt>next</tt> would return an element
	 * rather than throwing an exception.)
	 *
	 * @return <tt>true</tt> if the iterator has more elements.
	 */
	synchronized public boolean hasNext ()
	{
		return iterator.hasNext ();
	}

	/**
	 * Returns the next element in the iteration.
	 *
	 * @return the next element in the iteration.
	 * @exception NoSuchElementException iteration has no more elements.
	 */
	synchronized public Object next ()
	{
		IObjectProxy proxy = (IObjectProxy) iterator.next ();

		return proxy.getRealObject ();
	}

	/**
	 * Returns the next element in the iteration and register a proxy listener for this object.
	 *
	 * @IObjectProxyListener The Proxy listener
	 * @return the next element in the iteration.
	 * @exception NoSuchElementException iteration has no more elements.
	 */
	synchronized public Object next (IObjectProxyListener listener)
	{
		IObjectProxy proxy = (IObjectProxy) iterator.next ();

		Engine.instance ().getProxyEventRegistry ().addEventListener (proxy.getSampleRealObject (), listener);

		return proxy.getRealObject ();
	}

	/**
	 *
	 * Removes from the underlying collection the last element returned by the
	 * iterator (optional operation).  This method can be called only once per
	 * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
	 * the underlying collection is modified while the iteration is in
	 * progress in any way other than by calling this method.
	 *
	 * @exception UnsupportedOperationException if the <tt>remove</tt>
	 *                  operation is not supported by this Iterator.

	 * @exception IllegalStateException if the <tt>next</tt> method has not
	 *                  yet been called, or the <tt>remove</tt> method has already
	 *                  been called after the last call to the <tt>next</tt>
	 *                  method.
	 */
	public void remove ()
	{
		iterator.remove ();
	}
}
