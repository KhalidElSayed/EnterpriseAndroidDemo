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

import android.content.ContentValues;


/**
 * Convert virtual columns to real columns
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class ColumnDef {
    public static enum Type {
        BOOLEAN, BYTE, BYTEARRAY, DOUBLE, FLOAT, INTEGER, LONG, SHORT, STRING
    };

    private final String name;
    private final Type type;

    public ColumnDef(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public void copy(String srcCol, ContentValues src, ContentValues dst) {
        switch (type) {
            case BOOLEAN:
                dst.put(name, src.getAsBoolean(srcCol));
                break;
            case BYTE:
                dst.put(name, src.getAsByte(srcCol));
                break;
            case BYTEARRAY:
                dst.put(name, src.getAsByteArray(srcCol));
                break;
            case DOUBLE:
                dst.put(name, src.getAsDouble(srcCol));
                break;
            case FLOAT:
                dst.put(name, src.getAsFloat(srcCol));
                break;
            case INTEGER:
                dst.put(name, src.getAsInteger(srcCol));
                break;
            case LONG:
                dst.put(name, src.getAsLong(srcCol));
                break;
            case SHORT:
                dst.put(name, src.getAsShort(srcCol));
                break;
            case STRING:
                dst.put(name, src.getAsString(srcCol));
                break;
        }
    }
}