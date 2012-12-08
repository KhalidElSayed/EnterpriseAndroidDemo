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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Just enough code to demonstrate the use of the QueryBuilder
 *
 * EAContentProvider
 */
public class EAContentProvider extends ContentProvider {

    private static final Map<String, String> COL_AS_MAP;
    static {
        Map<String, String> m = new HashMap<String, String>();
        m.put(EAContract.Columns.ID,
            DbHelper.COL_ID + " AS " + EAContract.Columns.ID);
        m.put(EAContract.Columns.COL1,
            DbHelper.COL_A + " AS " + EAContract.Columns.COL1);
        m.put(EAContract.Columns.COLN,
            "CASE WHEN " + DbHelper.COL_Z
                + " NOT NULL THEN " + DbHelper.COL_ID
                + " ELSE NULL END AS " + EAContract.Columns.COLN);
        COL_AS_MAP = Collections.unmodifiableMap(m);
    }

    private static final Map<String, ColumnDef> WRITE_COL_MAP;
    static {
        Map<String, ColumnDef> m = new HashMap<String, ColumnDef>();
        m.put(
            EAContract.Columns.COL1,
            new ColumnDef(DbHelper.COL_A, ColumnDef.Type.STRING));
        m.put(
            EAContract.Columns.COLN,
            new ColumnDef(DbHelper.COL_Z, ColumnDef.Type.STRING));
        WRITE_COL_MAP = Collections.unmodifiableMap(m);
    }

    private static final String PK_CONSTRAINT
        = DbHelper.TABLE + "." + DbHelper.COL_ID + "=";

    private static final int STATUS_DIR = 1;
    private static final int STATUS_ITEM = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(
            EAContract.AUTHORITY,
            EAContract.TABLE,
            STATUS_DIR);
        uriMatcher.addURI(
            EAContract.AUTHORITY,
            EAContract.TABLE + "/#",
            STATUS_ITEM);
    }

    private DbHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DbHelper(getContext());
        return null != helper;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case STATUS_DIR:
                return EAContract.TYPE_ITEM;
            case STATUS_ITEM:
                return EAContract.TYPE_DIR;
            default:
                return null;
        }
    }

    @Override
    public int update(Uri uri, ContentValues cv, String sel, String[] sArgs) {
        throw new UnsupportedOperationException("Update not supported");
    }

    @Override
    public int delete(Uri uri, String sel, String[] sArgs) {
        throw new UnsupportedOperationException("Delete not supported");
    }

    @Override
    public Uri insert(Uri uri, ContentValues vals) {
        long pk;
        switch (uriMatcher.match(uri)) {
            case STATUS_DIR:
                pk = helper.getWritableDatabase()
                    .insert(DbHelper.TABLE, null, translateCols(vals));
                break;

            default:
                throw new UnsupportedOperationException(
                    "Unrecognized URI: " + uri);
        }

        if (0 > pk) { uri = null; }
        else {
            uri = uri.buildUpon().appendPath(String.valueOf(pk)).build();
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return uri;
    }

    @SuppressWarnings("fallthrough")
    @Override
    public Cursor query(
        Uri uri,
        String[] proj,
        String sel,
        String[] selArgs,
        String ord)
    {
        Cursor cur;

        long pk = -1;
        switch (uriMatcher.match(uri)) {
            case STATUS_ITEM:
                pk = ContentUris.parseId(uri);
            case STATUS_DIR:
                cur = doQuery(proj, sel, selArgs, ord, pk);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized URI: " + uri);
        }

        cur.setNotificationUri(getContext().getContentResolver(), uri);

        return cur;
    }

    private Cursor doQuery(
        String[] proj,
        String sel,
        String[] selArgs,
        String ord,
        long pk)
    {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setStrict(true);

        qb.setProjectionMap(COL_AS_MAP);

        qb.setTables(DbHelper.TABLE);

        if (0 <= pk) { qb.appendWhere(PK_CONSTRAINT + pk); }

        return qb.query(
            helper.getWritableDatabase(),
            proj,
            sel,
            selArgs,
            null,
            null,
            ord);
    }

    private ContentValues translateCols(ContentValues vals) {
        ContentValues newVals = new ContentValues();
        for (String colName: vals.keySet()) {
            ColumnDef colDef = WRITE_COL_MAP.get(colName);
            if (null == colDef) {
                throw new IllegalArgumentException(
                    "Unrecognized column: " + colName);
            }
            colDef.copy(colName, vals, newVals);
         }

        return newVals;
    }
}
