package edu.weblibrary.server.persistence.cayenne;

import edu.weblibrary.server.persistence.cayenne.auto._WeblibraryDomainMap;

public class WeblibraryDomainMap extends _WeblibraryDomainMap {

    private static WeblibraryDomainMap instance;

    private WeblibraryDomainMap() {}

    public static WeblibraryDomainMap getInstance() {
        if(instance == null) {
            instance = new WeblibraryDomainMap();
        }

        return instance;
    }
}
