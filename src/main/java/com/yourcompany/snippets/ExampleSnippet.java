package com.yourcompany.snippets;

import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.CMSessionToken;
import com.cloudmine.api.SimpleCMObject;
import com.cloudmine.api.rest.CMWebService;
import com.cloudmine.api.rest.UserCMWebService;
import com.cloudmine.api.rest.response.ObjectModificationResponse;
import com.cloudmine.coderunner.SnippetArguments;
import com.cloudmine.coderunner.SnippetContainer;

import java.util.Map;

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
        //If we have a valid sessiontoken, this will take whatever was inserted, add it to the user store,
        //and then delete it from the application store. It will return the result of deleting the objects if
        //the insert was a success, the response to updating the objects if that failed, and a SimpleCMObject with
        //a single "ran"=false value if we did not get a valid token.
        CMSessionToken token = arguments.getSessionToken();
        if(token.isValid()) {
            final UserCMWebService userService = CMWebService.getService().getUserWebService(token);
            final Map<String, CMObject> returnedValues = arguments.getSuccessDataObjects(); //This will only return something valid if this was a GET request
            ObjectModificationResponse response = userService.insert(arguments.getSuccessTransportableRepresentation());
            if(response.wasSuccess()) {
                return CMWebService.getService().delete(returnedValues.keySet());
            }
            return response;
        }
        SimpleCMObject noToken = new SimpleCMObject();
        noToken.add("ran", false);
        return noToken;
    }

}
