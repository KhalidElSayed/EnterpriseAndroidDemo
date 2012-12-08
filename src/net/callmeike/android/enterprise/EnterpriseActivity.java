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
package net.callmeike.android.enterprise;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

import net.callmeike.android.enterprise.svc.ServiceHelper;


/**
 * Just enough to demonstrate the ServiceHelper...
 *
 * @version $Revision: $
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 */
public class EnterpriseActivity extends Activity
    implements ServiceHelper.OnDoSomethingResultHandler
{
    private TextView text;

    @Override public void onResult(String message) {
        text.setText("Success: " + message);
    }

    @Override
    public void onFail(int code) {
        text.setText("Failed: " + code);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.text1);

        findViewById(R.id.button1).setOnClickListener(
            new View.OnClickListener() {
                @Override public void onClick(View v) {
                    ServiceHelper.getInstance()
                    .doSomething(EnterpriseActivity.this, "Arg1", "ArgN");
                } });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent resp) {
        Log.d("ACTIVITY" , "Response received");
        if (ServiceHelper.getInstance()
                .onDoSomethingResult(reqCode, resCode, resp, this))
        {
            return;
        }
        Log.d("ACTIVITY", "unhandled request" + reqCode);
    }
}
