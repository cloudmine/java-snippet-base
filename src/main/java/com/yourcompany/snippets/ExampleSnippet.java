package com.yourcompany.snippets;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.SimpleCMObject;
import com.cloudmine.api.rest.CMWebService;
import com.cloudmine.api.rest.callbacks.ObjectModificationResponseCallback;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.cloudmine.coderunner.SnippetArguments;
import com.cloudmine.coderunner.SnippetContainer;

/**
 * <br>
 * Copyright CloudMine LLC. All rights reserved<br>
 * See LICENSE file included with SDK for details.
 */
public class ExampleSnippet implements SnippetContainer {
    private static final String APP_ID = "from your developer console";
    private static final String API_KEY = "from your developer console";
    static {
        CMApiCredentials.initialize(APP_ID, API_KEY);
    }

    @Override
    /**
     * This is what the snippet will be named, and what you provide when you call it, IE
     * http://api.cloudmine.com/v1/app/:appid/text?f=snippetName
     */
    public String getSnippetName() {
        return "snippetName";
    }

    @Override
    /**
     * This is what is called when you run the snippet. The arguments contain the original response, the original
     * request, a session token if the request was made with a valid session token provided. The returned object
     * is converted to JSON
     */
    public Object runSnippet(SnippetArguments arguments) {
        CMWebService service = CMWebService.getService();
        service.setSync(true); //Run our async methods synchronously, so we can use them inside this snippet
        final SimpleCMObject yesToken = new SimpleCMObject();
        yesToken.add("ran", true);
        yesToken.add("inserted", false);
        service.asyncInsert(yesToken, new ObjectModificationResponseCallback() {
            public void onCompletion(ObjectModificationResponse response) {
                yesToken.add("inserted", true);
            }
        });
        //yesToken.inserted will = true in the response, since we set our service to be sync
        return yesToken;
    }

}
