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
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.util.Log;


/**
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class EnterpriseService extends IntentService {
    public static final String CALLBACK = "CALLBACK";
    public static final String ARG1 = "ARG1";
    public static final String ARGN = "ARGN";
    public static final String RESULT = "RESULT";

    private static final String TAG = "EnterpriseService";

    public EnterpriseService() {
        super(EnterpriseService.class.getCanonicalName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent resp = new Intent();
        resp.putExtra(
            RESULT,
            doSomething(
                intent.getExtras().getString(ARG1),
                // other args...
                // e.g., intent.getParcelableExtra(...)
                intent.getExtras().getString(ARGN)));

        try {
            ((PendingIntent) intent.getParcelableExtra(CALLBACK))
                .send(this, Activity.RESULT_OK, resp);
        }
        catch (CanceledException e) { Log.w(TAG, "Cancelled!", e); }
    }

    // implement the business logic here....
    private String doSomething(String arg1, String argn) {
        return arg1 + "..." + argn;
    }
}
