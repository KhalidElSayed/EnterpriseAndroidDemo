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
package net.callmeike.android.enterprise.svc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;


/**
 * A Service Helper based on PendingIntent
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class ServiceHelper {
    private static final int REQ_ID = 42;

    public static interface OnDoSomethingResultHandler {
        void onFail(int code);
        void onResult(String message);
    }

    private static final ServiceHelper instance = new ServiceHelper();

    public static ServiceHelper getInstance() { return instance; }

    public void doSomething(Activity ctxt, String arg1, String argn) {
        Intent intent = new Intent(ctxt, EnterpriseService.class);
        intent.putExtra(EnterpriseService.ARG1, arg1);
        // add other args
        // e.g., intent.putExtra(complexParcelable);
        intent.putExtra(EnterpriseService.ARGN, argn);
        intent.putExtra(
            EnterpriseService.CALLBACK,
            ctxt.createPendingResult(
                REQ_ID,
                new Intent(), // empty default response
                PendingIntent.FLAG_ONE_SHOT));
        ctxt.startService(intent);
    }

    public boolean onDoSomethingResult(
        int reqCode,
        int resCode,
        Intent resp,
        OnDoSomethingResultHandler hdlr)
    {
        if (REQ_ID != reqCode) { return false; }

        if (Activity.RESULT_OK != resCode) { hdlr.onFail(resCode); }
        else {
            hdlr.onResult(
                // could be something more complex than a String...
                resp.getExtras().getString(EnterpriseService.RESULT));
        }

        return true;
    }
}
