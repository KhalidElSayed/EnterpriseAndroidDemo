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

import android.database.Cursor;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class EACursorAccess extends CursorAccess {
    public EACursorAccess(Cursor c) {
        super(c,
            EAContract.Columns.ID,
            EAContract.Columns.COL1,
            // other columns...
            EAContract.Columns.COLN);
    }

    public int getId() {
        return getCursor().getInt(getColIdx(EAContract.Columns.ID));
    }

    public String getCol1() {
        return getCursor().getString(getColIdx(EAContract.Columns.COL1));
    }

    // Other getters...

    public String getColN() {
        return getCursor().getString(getColIdx(EAContract.Columns.COLN));
    }
}
