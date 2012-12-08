/* $Id: $
   Copyright 2012, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package net.callmeike.android.enterprise.db;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;


/**
 * A simple cursor wrapper.
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public abstract class CursorAccess {
    private final Cursor cursor;
    private final Map<String, Integer> colMap;

    public CursorAccess(Cursor c, String... cols) {
        cursor = c;
        Map<String, Integer> m = new HashMap<String, Integer>();
        for (String col: cols) {
            int idx = c.getColumnIndex(col);
            if (0 <= idx) { m.put(col, Integer.valueOf(idx)); }
        }
        colMap = Collections.unmodifiableMap(m);
    }

    protected final Cursor getCursor() { return cursor; }

    protected final int getColIdx(String col) {
        Integer idx = colMap.get(col);
        if (null == idx) {
            throw new IllegalArgumentException(
                "Cursor does not contain column: " + col);
        }
        return idx.intValue();
    }
 }
