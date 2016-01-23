/*
 * $Id: ServerActionTest.java,v 1.1 2005/11/22 13:31:14 kleopatra Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.action;

import junit.framework.TestCase;

public class ServerActionTest extends TestCase {

    /**
     * Issue #206-swingx: NPE in addHeader.
     *
     */
    public void testNPEAddHeader() {
       ServerAction action = new ServerAction();
       action.addHeader("key", "value");
    }
}
